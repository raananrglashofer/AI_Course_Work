package edu.yu.cs.com1320.project.stage2.impl;

import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.Document;
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import edu.yu.cs.com1320.project.stage2.impl.DocumentImpl;
import edu.yu.cs.com1320.project.Command;
import java.util.function.Function;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreImpl implements DocumentStore {
    private HashTableImpl<URI, DocumentImpl> table = new HashTableImpl<URI, DocumentImpl>(); // this should parameterized with uri, DocumentImpl shouldn't it?
    private StackImpl<Command> commandStack = new StackImpl<Command>();
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
        Function<URI, Boolean> undo = (URI uri1) -> {table.put(uri1, data); return true;};
        Command command = new Command(uri, undo);
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

    @Override
    public boolean delete(URI uri) {
        if(this.table.get(uri) == null){
            return false;
        }
        DocumentImpl deletedDoc = (DocumentImpl) this.table.put(uri, null);
        storedHash = deletedDoc.hashCode();
        Function<URI, Boolean> undo = (URI uri1) -> {table.put(uri, deletedDoc); return true;}; // should i be putting null here instead of deletedDoc
        Command command = new Command(uri, undo);
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
        Command undo = this.commandStack.pop();
        undo.undo();
    }

    /**
     * undo the last put or delete that was done with the given URI as its key
     * @param uri
     * @throws IllegalStateException if there are no actions on the command stack for the given URI
     */

    // use a for loop to check each document that's on the stack for the same uri passed in as a parameter
    // after popping each item off the stack put it on the new tempStack to hold it there
    // once you reach the document with the same uri, then undo it and put back all the documents in the tempStack
    // if no duocument matches the uri then at the end throw IllegalStateException
    @Override
    public void undo(URI uri) throws IllegalStateException {
        boolean goneInWhile = false;
        boolean uriFound = false;
        StackImpl<Command> tempStack = new StackImpl<Command>();
        while(this.commandStack.size() > 0){
            goneInWhile = true;
            Command temp = this.commandStack.pop();
            if(temp.getUri().equals(uri)){
                uriFound = true;
                try {
                    temp.undo();
                } catch(IllegalStateException e){

                }
                putBackStack(tempStack, this.commandStack);
                return;
            }
            tempStack.push(temp);
        }
        if(goneInWhile == false || uriFound == false){
            throw new IllegalStateException();
        }
        putBackStack(tempStack, commandStack);
        //throw new IllegalStateException(); // if the code gets to this point then it means no uri matched, so throw exception
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
            Command recent = (Command) temp.pop();
            current.push(recent);
        }
        return current;
    }
}
