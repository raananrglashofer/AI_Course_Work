package edu.yu.cs.com1320.project.stage3.impl;

import edu.yu.cs.com1320.project.stage3.Document;

import java.net.URI;
import java.util.*;
import java.util.HashMap;

public class DocumentImpl implements Document {
    private URI uri;
    private String text;
    private byte[] binaryData;
    private HashMap<String, Integer> count = new HashMap<String, Integer>(); // do i want map or hashmap
    private Boolean isTxt;

    public DocumentImpl(URI uri, String txt){
        if(uri == null || uri.toString().isEmpty()){
            throw new IllegalArgumentException();
        }
        if(txt == null ){//|| txt.isEmpty()){
            throw new IllegalArgumentException();
        }
        toHashMap(txt); //puts all words into HashMap
        this.uri = uri;
        this.text = txt;
        this.isTxt = true;
    }

    public DocumentImpl(URI uri, byte[] binaryData){
        if(uri == null || uri.toString().isEmpty()){
            throw new IllegalArgumentException();
        }
        if(binaryData == null || binaryData.toString().isEmpty()){ // is this allowed / makes sense?
            throw new IllegalArgumentException();
        }
        toHashMap(binaryData.toString()); // will this work?
        this.uri = uri;
        this.binaryData = binaryData;
        this.isTxt = false;
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

    /**
     * how many times does the given word appear in the document?
     * @param word
     * @return the number of times the given words appears in the document. If it's a binary document, return 0.
     */
    @Override
    public int wordCount(String word) {
        if(this.isTxt == true) {
            if(count.get(word) == null){
                return 0;
            } else{
                return count.get(word);
            }
        }
        else{
            return 0;
            }
    }

    /**
     * @return all the words that appear in the document
     */
    // if called on Byte Array then return an empty set
    @Override
    public Set<String> getWords() {
        Set<String> words = new HashSet<>();
        if(this.isTxt == false){
            return words;
        }
        words = count.keySet(); // does this work?
        return words;
    }

    @Override
    public int hashCode() {
        int result = uri.hashCode();
        result = 31 * result + (text != null ? text.hashCode() : 0);
        result = 31 * result + Arrays.hashCode(binaryData);
        return result;
    }

    //Two documents are considered equal if they have the same hashCode
    @Override
    public boolean equals(Object obj){
        DocumentImpl otherDoc = (DocumentImpl) obj;
        if(this.hashCode() == otherDoc.hashCode()){
            return true;
        }
        return false;
    }
    // set equal to this.count
    private void toHashMap(String txt){
        String noPunctuation = txt.replaceAll("\\p{Punct}", ""); // make sure this runs properly
        String[] split = noPunctuation.split(" ");
        for(int i = 0; i < split.length; i++){
            if(count.get(split[i]) != null){ // should i do is not null or greater than zero
                int val = count.get(split[i]);
                count.replace(split[i], val + 1);
            }
            else{
                count.put(split[i], 1);
            }
        }
    }
}
