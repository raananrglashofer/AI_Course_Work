package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.stage4.Document;

import java.net.URI;
import java.util.Set;

public class DocumentImpl implements Document {
    @Override
    public String getDocumentTxt() {
        return null;
    }

    @Override
    public byte[] getDocumentBinaryData() {
        return new byte[0];
    }

    @Override
    public URI getKey() {
        return null;
    }

    @Override
    public int wordCount(String word) {
        return 0;
    }

    @Override
    public Set<String> getWords() {
        return null;
    }

    @Override
    public long getLastUseTime() {
        return 0;
    }

    @Override
    public void setLastUseTime(long timeInNanoseconds) {

    }

    @Override
    public int compareTo(Document o) {
        return 0;
    }
}
