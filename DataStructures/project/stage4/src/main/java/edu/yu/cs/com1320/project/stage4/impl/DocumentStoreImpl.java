package edu.yu.cs.com1320.project.stage4.impl;

import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.DocumentStore;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

public class DocumentStoreImpl implements DocumentStore {
    @Override
    public int put(InputStream input, URI uri, DocumentFormat format) throws IOException {
        return 0;
    }

    @Override
    public Document get(URI uri) {
        return null;
    }

    @Override
    public boolean delete(URI uri) {
        return false;
    }

    @Override
    public void undo() throws IllegalStateException {

    }

    @Override
    public void undo(URI uri) throws IllegalStateException {

    }

    @Override
    public List<Document> search(String keyword) {
        return null;
    }

    @Override
    public List<Document> searchByPrefix(String keywordPrefix) {
        return null;
    }

    @Override
    public Set<URI> deleteAll(String keyword) {
        return null;
    }

    @Override
    public Set<URI> deleteAllWithPrefix(String keywordPrefix) {
        return null;
    }

    @Override
    public void setMaxDocumentCount(int limit) {

    }

    @Override
    public void setMaxDocumentBytes(int limit) {

    }
}
