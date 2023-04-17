package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;

import java.util.*;
import java.util.function.Function;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI, DocumentImpl> table = new HashTableImpl<URI, DocumentImpl>(); // this should parameterized with uri, DocumentImpl shouldn't it?
    private StackImpl<Undoable> commandStack = new StackImpl<Undoable>();
    private TrieImpl<DocumentImpl> trie = new TrieImpl<DocumentImpl>();
    private int storedHash = 0;

    public DocumentStoreImpl(){
    }
    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if(uri == null || format == null){
            throw new IllegalArgumentException();
        }
        if(input == null){
            delete(uri);
            return storedHash; // double check that if delete returns false, then storedHash = 0;
        }
        //tryReadingInput(input);
        DocumentImpl doc = null;
        if(format == DocumentFormat.TXT){
            doc = new DocumentImpl(uri, byteToString(tryReadingInput(input)));
        } else if (format == DocumentFormat.BINARY) {
            doc = new DocumentImpl(uri, tryReadingInput(input));
        }
        DocumentImpl data = (DocumentImpl) this.table.put(uri, doc);
        addToTrie(doc);
        Document document = get(uri);
        Function<URI, Boolean> undo = (URI uri1) -> {table.put(uri1, data); removeFromTrie(document); return true;};
        GenericCommand command = new GenericCommand(uri, undo);
        commandStack.push(command);
        if(data != null){
            return data.hashCode();
        } else{
            return 0;
        }
    }

    @Override
    public Document get(URI uri) {
        return (Document) this.table.get(uri);
    }

//    If the Document being removed is the last one at that node in the Trie, you must delete it and all ancestors between it
//    and the closest ancestor that has at least one document in its Value collection.
    @Override
    public boolean delete(URI uri) {
        if(this.table.get(uri) == null){
            return false;
        }
        removeFromTrie(this.table.get(uri)); // removes all values of Document in the trie
        DocumentImpl deletedDoc = (DocumentImpl) this.table.put(uri, null);
        storedHash = deletedDoc.hashCode();
        Function<URI, Boolean> undo = (URI uri1) -> {table.put(uri, deletedDoc); addToTrie(deletedDoc); return true;}; // lambda not adding trie data also
        GenericCommand command = new GenericCommand(uri, undo);
        commandStack.push(command);
        return true;
    }

    /**
     * undo the last put or delete command
     * @throws IllegalStateException if there are no actions to be undone, i.e. the command stack is empty
     */
    @Override
    public void undo() throws IllegalStateException {
        if(this.commandStack.size() == 0){
            throw new IllegalStateException();
        }
        this.commandStack.pop().undo();
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */
    @Override
    public void undo(URI uri) throws IllegalStateException {
        boolean goneInWhile = false;
        boolean uriFound = false;
        StackImpl<Undoable> tempStack = new StackImpl<Undoable>();
        while(this.commandStack.size() > 0){
            goneInWhile = true;
            if(this.commandStack.peek() instanceof GenericCommand) {
                GenericCommand temp = (GenericCommand) this.commandStack.pop();
                if(temp.getTarget().equals(uri)){
                    uriFound = true;
                    try {
                        temp.undo();
                    } catch(IllegalStateException e){}
                    putBackStack(tempStack, this.commandStack);
                    return;
                }
                tempStack.push(temp);
            } else{
                CommandSet tempSet = (CommandSet) this.commandStack.pop();
                if(tempSet.containsTarget(uri)) {
                    CommandSet updatedSet = new CommandSet();
                    uriFound = true;
                    Set<GenericCommand> values = new HashSet<>(getCommandSetValues(tempSet));
                    for(GenericCommand g : values){
                        if(g.getTarget().equals(uri)){
                            try{
                                g.undo();
                            } catch(IllegalStateException e) {
                            }
                        } else{
                            updatedSet.addCommand(g);
                        }
                    }
                    if(tempSet.size() > 0){
                        tempStack.push(updatedSet);
                    }
                    putBackStack(tempStack, this.commandStack);
                    return;
                }
                tempStack.push(tempSet);
                }
            }
        if(goneInWhile == false || uriFound == false){
            throw new IllegalStateException();}
        putBackStack(tempStack, commandStack);
    }

    /**
     * Retrieve all documents whose text contains the given keyword.
     * Documents are returned in sorted, descending order, sorted by the number of times the keyword appears in the document.
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> search(String keyword) {
        Comparator<Document> comparator = (Document doc1, Document doc2) -> (doc2.wordCount(keyword) - doc1.wordCount(keyword));
        return this.trie.getAllSorted(keyword, comparator);
    }

    /**
     * Retrieve all documents whose text starts with the given prefix
     * Documents are returned in sorted, descending order, sorted by the number of times the prefix appears in the document.
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a List of the matches. If there are no matches, return an empty list.
     */
    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        Comparator<Document> comparator = (Document doc1, Document doc2) -> (doc2.wordCount(keywordPrefix) - doc1.wordCount(keywordPrefix));
        return this.trie.getAllWithPrefixSorted(keywordPrefix, comparator);
    }

    /**
     * Completely remove any trace of any document which contains the given keyword
     * Search is CASE SENSITIVE.
     * @param keyword
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAll(String keyword) {
        if(keyword == null){
            return null;
        }
        // delete docs at the node
        Set<Document> deletedDocs = this.trie.deleteAll(keyword);
        Set<URI> deletedUris = new HashSet<>();
        CommandSet<GenericCommand> commands = new CommandSet();
        // add Uris to set, delete doc from hashTable, and delete doc from trie
        for(Document d : deletedDocs){
            DocumentImpl deletedDoc = this.table.put(d.getKey(), null);
            Function<URI, Boolean> undo = (URI uri1) -> {table.put(d.getKey(), deletedDoc); addToTrie(deletedDoc); return true;};
            GenericCommand command = new GenericCommand(d.getKey(), undo);
            commands.addCommand(command);
            deletedUris.add(d.getKey());
            removeFromTrie(d);
        }
        commandStack.push(commands);
        return deletedUris;
    }

    /**
     * Completely remove any trace of any document which contains a word that has the given prefix
     * Search is CASE SENSITIVE.
     * @param keywordPrefix
     * @return a Set of URIs of the documents that were deleted.
     */
    @Override
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        if(keywordPrefix == null){
            return null;
        }
        Set<URI> deletedUris = new HashSet<>();
        Set<Document> deletedDocs = this.trie.deleteAllWithPrefix(keywordPrefix);
        CommandSet<GenericCommand> commands = new CommandSet();
        for(Document d : deletedDocs){
            DocumentImpl deletedDoc = this.table.put(d.getKey(), null);
            Function<URI, Boolean> undo = (URI uri1) -> {table.put(d.getKey(), deletedDoc); addToTrie(deletedDoc); return true;};
            GenericCommand command = new GenericCommand(d.getKey(), undo);
            commands.addCommand(command);
            deletedUris.add(d.getKey());
            removeFromTrie(d);
        }
        commandStack.push(commands);
        return deletedUris;
    }

    private String byteToString(byte [] bytes){
        String str = new String(bytes);
        return str;
    }

    private byte[] tryReadingInput(InputStream input) throws IOException{
        byte[] byteArray;
        try {
            byteArray = input.readAllBytes();
        } catch (IOException e){
            throw new IOException();
        }
        return byteArray;
    }

    private StackImpl putBackStack(StackImpl temp, StackImpl current){
        while(temp.size() > 0){
            Undoable recent = (Undoable) temp.pop();
            current.push(recent);
        }
        return current;
    }

    private void addToTrie(Document doc){
        for(String s: doc.getWords()){
            this.trie.put(s, (DocumentImpl) doc);
        }
    }

    private void removeFromTrie(Document doc){
        for(String s: doc.getWords()){
            this.trie.delete(s, (DocumentImpl) doc);
        }
    }

    private Set<GenericCommand> getCommandSetValues(CommandSet<GenericCommand> set){
        Set<GenericCommand> returnSet = new HashSet<>();
        Iterator itr = set.iterator();
        while(itr.hasNext()){
            GenericCommand gc = (GenericCommand) itr.next();
            returnSet.add(gc);
        }
        return returnSet;
    }
}
