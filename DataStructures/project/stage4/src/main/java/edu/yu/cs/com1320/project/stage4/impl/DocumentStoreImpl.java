package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;
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
import static java.lang.System.nanoTime;
public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI, DocumentImpl> table = new HashTableImpl<URI, DocumentImpl>(); // this should parameterized with uri, DocumentImpl shouldn't it?
    private StackImpl<Undoable> commandStack = new StackImpl<Undoable>();
    private TrieImpl<DocumentImpl> trie = new TrieImpl<DocumentImpl>();
    private MinHeapImpl<Document> heap = new MinHeapImpl<>();
    private int storedHash = 0;
    private int maxDocsStored;
    private int maxBytesStored;
    private int docsCount;
    private int bytesCount;

    public DocumentStoreImpl(){
    }
    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if(uri == null || format == null){
            throw new IllegalArgumentException();
        }
        if(input == null){
            delete(uri);
            return storedHash;
        }
        DocumentImpl doc = null;
        if(format == DocumentFormat.TXT){
            doc = new DocumentImpl(uri, byteToString(tryReadingInput(input)));
            if(doc.getDocumentTxt().getBytes().length > this.maxBytesStored){
                throw new IllegalArgumentException();
            }
            if((this.bytesCount + doc.getDocumentTxt().getBytes().length) > maxBytesStored){
                removeFromEverything();
            }
            this.bytesCount += doc.getDocumentTxt().getBytes().length;
        } else if (format == DocumentFormat.BINARY) {
            doc = new DocumentImpl(uri, tryReadingInput(input));
            // if doc is bigger than size constraint throw IAE
            if(doc.getDocumentBinaryData().length > this.maxBytesStored){
                throw new IllegalArgumentException();
            }
            if((this.bytesCount + doc.getDocumentBinaryData().length) > maxBytesStored){
                removeFromEverything();
            }
            this.bytesCount += doc.getDocumentBinaryData().length;
        }
        DocumentImpl data = (DocumentImpl) this.table.put(uri, doc);
        if(doc != null){ // is this my only way to keep track of the number of docs
            if(this.docsCount == this.maxDocsStored){
                removeFromEverything();
            }
            docsCount++;
        }
        addToTrie(doc);
        this.heap.insert(doc);
        Document document = get(uri);
        // double check if lambda is correct with heap - also figure out remove logic when doc isn't lowest value
        Function<URI, Boolean> undo = (URI uri1) -> {table.put(uri1, data); removeFromTrie(document); heap.remove(); return true;};
        GenericCommand command = new GenericCommand(uri, undo);
        commandStack.push(command);
        //data.setLastUseTime(nanoTime()); // is this what i need to do to set nanoTime
        if(data != null){
            return data.hashCode();
        } else{
            return 0;
        }
    }

    @Override
    public Document get(URI uri) {
        this.table.get(uri).setLastUseTime(nanoTime()); // I believe that setting the time here will work for put, get, and delete
        this.heap.reHeapify(this.table.get(uri));
        return (Document) this.table.get(uri);
    }

    @Override
    public boolean delete(URI uri) {
        if(this.table.get(uri) == null){
            return false;
        }
        removeFromTrie(this.table.get(uri)); // removes all values of Document in the trie
        DocumentImpl deletedDoc = (DocumentImpl) this.table.put(uri, null);
        docsCount--;
        if(deletedDoc.getDocumentBinaryData() != null) {
            bytesCount -= deletedDoc.getDocumentBinaryData().length; // double check that deletedDoc works here
        } else{
            bytesCount -= deletedDoc.getDocumentTxt().getBytes().length; // double check that deletedDoc works here
        }
        this.heap.remove(); // based off put right before, the doc to delete should be at the top of the heap
        storedHash = deletedDoc.hashCode();
        Function<URI, Boolean> undo = (URI uri1) -> {table.put(uri, deletedDoc); addToTrie(deletedDoc); heap.insert(deletedDoc); return true;};
        GenericCommand command = new GenericCommand(uri, undo);
        commandStack.push(command);
        return true;
    }
    // need to add to both undos the logic for checking if we need to remove everything
    @Override
    public void undo() throws IllegalStateException {
        if(this.commandStack.size() == 0){
            throw new IllegalStateException();
        }
        if(this.commandStack.peek() instanceof GenericCommand) {
            GenericCommand temp = (GenericCommand) this.commandStack.peek();
            genericCommandRemove(temp);
        }
        else{
            CommandSet temp = (CommandSet) this.commandStack.peek();
            Set<GenericCommand> values = new HashSet<>(getCommandSetValues(temp));
            for(GenericCommand g : values){
                genericCommandRemove(g);
            }
        }
        this.commandStack.pop().undo();
    }
// need to be aware if undo is not succesful because then deleted stuff for no reason
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
                    genericCommandRemove(temp); // I don't think it matters whether it is a pop or a peek
                    uriFound = true;
                    try {
                        temp.undo();
                        this.get((URI) temp.getTarget()); // does calling get set the time on the doc?
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
                            genericCommandRemove(g); // I don't think it matters whether it is a pop or a peek
                            try{
                                g.undo();
                                this.get((URI) g.getTarget()); // does calling get set the time on the doc?
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

    @Override
    public List<Document> search(String keyword) {
        Comparator<Document> comparator = (Document doc1, Document doc2) -> (doc2.wordCount(keyword) - doc1.wordCount(keyword));
        List<Document> sorted = this.trie.getAllSorted(keyword, comparator);
        for(Document d : sorted){
            d.setLastUseTime(nanoTime());
            this.heap.reHeapify(d);
        }
        return sorted;
    }

    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        Comparator<Document> comparator = (Document doc1, Document doc2) -> (doc2.wordCount(keywordPrefix) - doc1.wordCount(keywordPrefix));
        List<Document> sorted = this.trie.getAllWithPrefixSorted(keywordPrefix, comparator);
        for(Document d : sorted){
            d.setLastUseTime(nanoTime());
            this.heap.reHeapify(d);
        }
        return sorted;
    }

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
            docsCount--;
            if(deletedDoc.getDocumentBinaryData() != null) {
                bytesCount -= d.getDocumentBinaryData().length;
            } else{
                bytesCount -= d.getDocumentTxt().getBytes().length;
            }
            heap.remove(); // based off put right before, the doc to delete should be at the top of the heap
            Function<URI, Boolean> undo = (URI uri1) -> {table.put(d.getKey(), deletedDoc); addToTrie(deletedDoc); heap.insert(deletedDoc); return true;};
            GenericCommand command = new GenericCommand(d.getKey(), undo);
            commands.addCommand(command);
            deletedUris.add(d.getKey());
            removeFromTrie(d);
        }
        commandStack.push(commands);
        return deletedUris;
    }

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
            docsCount--;
            if(deletedDoc.getDocumentBinaryData() != null) {
                bytesCount -= d.getDocumentBinaryData().length;
            } else{
                bytesCount -= d.getDocumentTxt().getBytes().length;
            }
            heap.remove(); // should i be inserting d or deletedDoc in lambda ?
            Function<URI, Boolean> undo = (URI uri1) -> {table.put(d.getKey(), deletedDoc); addToTrie(deletedDoc); heap.insert(deletedDoc); return true;};
            GenericCommand command = new GenericCommand(d.getKey(), undo);
            commands.addCommand(command);
            deletedUris.add(d.getKey());
            removeFromTrie(d);
        }
        commandStack.push(commands);
        return deletedUris;
    }

    @Override
    public void setMaxDocumentCount(int limit) {
        if(limit < 0){
            throw new IllegalArgumentException();
        }
        this.maxDocsStored = limit;
        while(docsCount > limit) {
            removeFromEverything();
        }
    }

    @Override
    public void setMaxDocumentBytes(int limit) {
        if(limit < 0){
            throw new IllegalArgumentException();
        }
        this.maxBytesStored = limit;
        while(bytesCount > limit) {
            removeFromEverything();
        }
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

    private void genericCommandRemove(GenericCommand g){
        if(this.get((URI) g.getTarget()).getDocumentBinaryData() != null) {
            if((bytesCount + this.get((URI) g.getTarget()).getDocumentBinaryData().length) > maxBytesStored){
                while(bytesCount > maxBytesStored){
                    removeFromEverything();
                }
            }
        } else{
            if((bytesCount + this.get((URI) g.getTarget()).getDocumentTxt().getBytes().length) > maxBytesStored){
                while(bytesCount > maxBytesStored){
                    removeFromEverything();
                }
            }
        }
        if(docsCount == maxDocsStored){
            removeFromEverything();
        }
        this.get((URI) g.getTarget()); // does calling get set the time on the doc?
    }

    private void removeFromEverything(){
       // while(this.docsCount > this.maxDocsStored || this.bytesCount > this.maxBytesStored){ // until under limit
            Document removedDoc = this.heap.remove(); // remove from heap and save
            removeFromTrie(removedDoc); // remove from trie
            this.table.put(removedDoc.getKey(), null); // choosing to call this over delete to remove from hashTable
            for(int i = 0; i < commandStack.size(); i++){
                if(this.commandStack.peek() instanceof GenericCommand) {
                    if(((GenericCommand<?>) this.commandStack.peek()).getTarget().equals(removedDoc.getKey())){
                        this.commandStack.pop(); // should remove it from commandStack
                    }
                } else if(this.commandStack.peek() instanceof CommandSet<?>){
                    Set<GenericCommand> values = getCommandSetValues((CommandSet<GenericCommand>) this.commandStack.peek());
                    for (GenericCommand g : values){
                        if(g.getTarget().equals(removedDoc.getKey())){
                            ((CommandSet<GenericCommand>) this.commandStack.peek()).remove(g);
                            values.remove(g);
                        }
                    }
                    if(values.isEmpty()){
                        this.commandStack.pop(); // if all commands from the set are removed then pop off stack
                    }
                }
            }
       // }
    }
}
