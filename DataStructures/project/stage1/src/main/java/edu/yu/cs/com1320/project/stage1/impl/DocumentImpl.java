package edu.yu.cs.com1320.project.stage1.impl;

import edu.yu.cs.com1320.project.stage1.Document;
import java.net.URI;
import java.util.Arrays;

public class DocumentImpl implements Document {
    private URI uri;
    private String text;
    private byte[] binaryData;

    public DocumentImpl(URI uri, String txt){
        if(uri == null || uri.toString().isEmpty()){ // is this too much to check if it's blank/empty
            throw new IllegalArgumentException();
        }
        if(txt == null || txt.isEmpty()){
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.text = txt;
    }

    public DocumentImpl(URI uri, byte[] binaryData){
        if(uri == null || uri.toString().isEmpty()){ // is this too much to check if it's blank/empty
            throw new IllegalArgumentException();
        }
        if(binaryData == null || binaryData.length == 0){ // not sure if I have to do both
            throw new IllegalArgumentException();
        }
        this.uri = uri;
        this.binaryData = binaryData;
    }
    @Override
    public String getDocumentTxt() {
        return this.text;
    }

    @Override
    public byte[] getDocumentBinaryData() {
        return this.binaryData;
    }

    @Override
    public URI getKey() {
        return this.uri;
    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(binaryData);
        return result;
    }

    //Two documents are considered equal if they have the same hashCode.
    @Override
    public boolean equals(Object obj){
        Object otherObject = (Object) obj;
        if(this.hashCode() == otherObject.hashCode()){
            return true;
        }
        return false;
    }
}
