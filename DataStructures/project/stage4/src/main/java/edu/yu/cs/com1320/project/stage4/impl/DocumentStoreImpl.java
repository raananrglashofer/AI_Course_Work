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
    private boolean wasMaxDocsSet = false;
    private boolean wasMaxBytesSet = false;

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
            if(wasMaxBytesSet == true) {
                if (doc.getDocumentTxt().getBytes().length > this.maxBytesStored) {
                    throw new IllegalArgumentException();
                }
                if ((this.bytesCount + doc.getDocumentTxt().getBytes().length) > maxBytesStored) {
                    removeFromEverything();
                }
            }
            this.bytesCount += doc.getDocumentTxt().getBytes().length;
        } else if (format == DocumentFormat.BINARY) {
            doc = new DocumentImpl(uri, tryReadingInput(input));
            // if doc is bigger than size constraint throw IAE
            if(wasMaxBytesSet == true) {
                if (doc.getDocumentBinaryData().length > this.maxBytesStored) {
                    throw new IllegalArgumentException();
                }
                if ((this.bytesCount + doc.getDocumentBinaryData().length) > maxBytesStored) {
                    removeFromEverything();
                }
            }
            this.bytesCount += doc.getDocumentBinaryData().length;
        }
        DocumentImpl data = (DocumentImpl) this.table.put(uri, doc);
        if(doc != null){
            if(wasMaxDocsSet == true) {
                if (this.docsCount == this.maxDocsStored) {
                    removeFromEverything();
                }
            }
        }
        addToTrie(doc);
        addToHeap(doc);
        Document document = get(uri);
        // double check if lambda is correct with heap - also figure out remove logic when doc isn't lowest value
        Function<URI, Boolean> undo = (URI uri1) -> {
            table.put(uri1, data);
            removeFromTrie(document);
            removeFromHeap(document);
            return true;
        };
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
        if(this.table.get(uri) != null) {
            this.table.get(uri).setLastUseTime(System.nanoTime());
            this.heap.reHeapify(this.table.get(uri));// I believe that setting the time here will work for put, get, and delete
        }
        return (Document) this.table.get(uri);
    }

    @Override
    public boolean delete(URI uri) {
        if(this.table.get(uri) == null){
            return false;
        }
        removeFromTrie(this.table.get(uri)); // removes all values of Document in the trie
        DocumentImpl deletedDoc = (DocumentImpl) this.table.put(uri, null);
        removeFromHeap(deletedDoc);
        if(deletedDoc.getDocumentBinaryData() != null) {
            bytesCount -= deletedDoc.getDocumentBinaryData().length; // double check that deletedDoc works here
        } else{
            bytesCount -= deletedDoc.getDocumentTxt().getBytes().length; // double check that deletedDoc works here
        }
        storedHash = deletedDoc.hashCode();
        Function<URI, Boolean> undo = (URI uri1) -> {
            table.put(uri, deletedDoc);
            addToTrie(deletedDoc);
            addToHeap(deletedDoc);
            deletedDoc.setLastUseTime(System.nanoTime());
            return true;
        };
        GenericCommand command = new GenericCommand(uri, undo);
        commandStack.push(command);
        return true;
    }
    @Override
    public void undo() throws IllegalStateException {
        if(this.commandStack.size() == 0){
            throw new IllegalStateException();
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
                    //genericCommandRemove(temp); // I don't think it matters whether it is a pop or a peek
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
                       //     genericCommandRemove(g); // I don't think it matters whether it is a pop or a peek
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

    @Override
    public List<Document> search(String keyword) {
        Comparator<Document> comparator = (Document doc1, Document doc2) -> (doc2.wordCount(keyword) - doc1.wordCount(keyword));
        List<Document> sorted = this.trie.getAllSorted(keyword, comparator);
        for(Document d : sorted){
            d.setLastUseTime(System.nanoTime());
            this.heap.reHeapify(d);
        }
        return sorted;
    }

    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        Comparator<Document> comparator = (Document doc1, Document doc2) -> (doc2.wordCount(keywordPrefix) - doc1.wordCount(keywordPrefix));
        List<Document> sorted = this.trie.getAllWithPrefixSorted(keywordPrefix, comparator);
        for(Document d : sorted){
            d.setLastUseTime(System.nanoTime());
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
            if(deletedDoc.getDocumentBinaryData() != null) {
                bytesCount -= d.getDocumentBinaryData().length;
            } else{
                bytesCount -= d.getDocumentTxt().getBytes().length;
            }
            removeFromHeap(d); // based off put right before, the doc to delete should be at the top of the heap
            Function<URI, Boolean> undo = (URI uri1) -> {table.put(d.getKey(), deletedDoc); addToTrie(deletedDoc); addToHeap(deletedDoc); return true;};
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
            if(deletedDoc.getDocumentBinaryData() != null) {
                bytesCount -= d.getDocumentBinaryData().length;
            } else{
                bytesCount -= d.getDocumentTxt().getBytes().length;
            }
            removeFromHeap(d); // should i be inserting d or deletedDoc in lambda ?
            Function<URI, Boolean> undo = (URI uri1) -> {table.put(d.getKey(), deletedDoc); addToTrie(deletedDoc); addToHeap(deletedDoc); return true;};
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
        this.wasMaxDocsSet = true;
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
        wasMaxBytesSet = true;
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

    // This method might be redundent because I may be doing it anyways when I undo and it goes to my
    // my put or delete
    // test for this potential error
//    private void genericCommandRemove(GenericCommand g){
//        // no get because it was deleted
//        if(this.get((URI) g.getTarget()).getDocumentBinaryData() != null) {
//            if (wasMaxBytesSet == true) {
//                if ((bytesCount + this.get((URI) g.getTarget()).getDocumentBinaryData().length) > maxBytesStored) {
//                    while (bytesCount > maxBytesStored) {
//                        removeFromEverything();
//                    }
//                }
//            } else {
//                if ((bytesCount + this.get((URI) g.getTarget()).getDocumentTxt().getBytes().length) > maxBytesStored) {
//                    while (bytesCount > maxBytesStored) {
//                        removeFromEverything();
//                    }
//                }
//            }
//        }
//        if(wasMaxDocsSet == true) {
//            if (docsCount == maxDocsStored) {
//                removeFromEverything();
//            }
//        }
//        this.get((URI) g.getTarget()); // does calling get set the time on the doc?
//    }

    private void removeCommand(Document doc){
        boolean goneInWhile = false;
        boolean uriFound = false;
        StackImpl<Undoable> tempStack = new StackImpl<Undoable>();
        while(commandStack.size() > 0){
            goneInWhile = true;
            if(this.commandStack.peek() instanceof GenericCommand) {
                GenericCommand temp = (GenericCommand) this.commandStack.pop();
                if (temp.getTarget().equals(doc.getKey())) {
                    uriFound = true;
                    //this.get((URI) temp.getTarget()); // does calling get set the time on the doc?
                    // should be putting back the stack without the command that was just popped
                    putBackStack(tempStack, this.commandStack);
                    return;
                }
                tempStack.push(temp);
            } else {
                CommandSet tempSet = (CommandSet) this.commandStack.pop();
                if (tempSet.containsTarget(doc.getKey())) {
                    CommandSet updatedSet = new CommandSet();
                    uriFound = true;
                    Set<GenericCommand> values = new HashSet<>(getCommandSetValues(tempSet));
                    for (GenericCommand g : values) {
                        if (g.getTarget().equals(doc.getKey())) {
                            // idea is that this command will not get added back to commandSet - essentially deleting it
                           // this.get((URI) g.getTarget()); // does calling get set the time on the doc?
                        } else {
                            updatedSet.addCommand(g);
                        }
                    }
                    if (tempSet.size() > 0) {
                        tempStack.push(updatedSet);
                    }
                    putBackStack(tempStack, this.commandStack);
                    return;
                }
                tempStack.push(tempSet);
            }
        }
        putBackStack(tempStack, commandStack);
    }

    private void removeFromEverything(){
        Document removedDoc = this.heap.remove(); // remove from heap and save
        removeFromTrie(removedDoc); // remove from trie
        removeCommand(removedDoc); // remove from CommandStack
        this.table.put(removedDoc.getKey(), null); // choosing to call this over delete to remove from hashTable
        docsCount--;
    }

    private void addToHeap(Document doc){
        this.heap.insert(doc);
        doc.setLastUseTime(System.nanoTime());
        this.heap.reHeapify(doc);
        docsCount++;
    }

    private void removeFromHeap(Document doc){
        doc.setLastUseTime(System.nanoTime());
        this.heap.reHeapify(doc);
        this.heap.remove();
        docsCount--;
    }
}
