package edu.yu.cs.com1320.project.impl;

import edu.yu.cs.com1320.project.stage1.Document;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DocumentStoreImpl implements DocumentStore { // figure this out - caused by adding enum
    private HashTableImpl table = new HashTableImpl();
    public DocumentStoreImpl(){
    }

    /**
     * @param input the document being put
     * @param uri unique identifier for the document
     * @param format indicates which type of document format is being passed
     * @return if there is no previous doc at the given URI, return 0. - done
     * If there is a previous doc, return the hashCode of the previous doc. - done
     * If InputStream is null, this is a delete, and thus return either the hashCode of the deleted doc or 0 if there is no doc to delete.
     * @throws IOException if there is an issue reading input - done
     * @throws IllegalArgumentException if uri or format are null - done
     */
/*    a. Read the entire contents of the document from the InputStream into a byte[] - done
    b. Create an instance of DocumentImpl with the URI and the String or byte[]that was passed to you. - done
    c. Insert the Document object into the hash table with URI as the key and the Document object as the value - done
    d. Return the hashCode of the previous document that was stored in the hashTable at that URI, or zero if there was
    none - done */
    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        if(uri == null || format == null){
            throw new IllegalArgumentException();
        }
        if(input == null){
            delete(uri);
            return 0; // fix
        }
        byte[] byteArray;
        try {
            byteArray = input.readAllBytes();
        } catch (IOException e){
            throw new IOException();
        }
        DocumentImpl doc = null;
        if(format == DocumentFormat.TXT){
            doc = new DocumentImpl(uri, byteToString(byteArray)); // is this giving me the string i want
        } else if (format == DocumentFormat.BINARY) {
            doc = new DocumentImpl(uri, byteArray);
        }
        DocumentImpl data = (DocumentImpl) this.table.put(uri, doc);
        if(data != null){
            return data.hashCode();
        } else{
            return 0;
        }
    }

    /**
     * @param uri the unique identifier of the document to get
     * @return the given document
     */
    @Override
    public Document get(URI uri) {
        return (Document) this.table.get(uri); // do i need to cast it to a document
    }

    /**
     * @param uri the unique identifier of the document to delete
     * @return true if the document is deleted, - done
     * false if no document exists with that URI - done
     */
    @Override
    public boolean delete(URI uri) {
        if(this.table.get(uri) == null){
            return false;
        }
        this.table.put(uri, null);
        return true;
    }

    private String byteToString(byte [] bytes){
        String str = new String(bytes);
        return str;
    }
}
