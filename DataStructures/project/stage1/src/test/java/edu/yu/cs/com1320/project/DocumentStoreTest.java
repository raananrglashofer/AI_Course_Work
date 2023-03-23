package edu.yu.cs.com1320.project;

import edu.yu.cs.com1320.project.stage1.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage1.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage1.DocumentStore;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class DocumentStoreTest {
    private URI uri1;
    private String txt1;

    //variables to hold possible values for doc2
    private URI uri2;
    private String txt2;

    @Test
    public void putNewDocument() throws URISyntaxException, IOException {
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        DocumentStore store = new DocumentStoreImpl();
        int value = store.put(new ByteArrayInputStream(this.txt1.getBytes()), uri1, DocumentStore.DocumentFormat.BINARY);
        assertTrue(value == 0);
    }

    @Test
    public void putReplaceDocument() throws IOException, URISyntaxException {
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        DocumentStore store = new DocumentStoreImpl();
        int value = store.put(new ByteArrayInputStream(this.txt1.getBytes()), uri1, DocumentStore.DocumentFormat.BINARY);
        assertTrue(value == 0 );
        this.txt2 = "Text for doc2. A plain old String.";
        DocumentImpl document = (DocumentImpl) store.get(uri1);
        int hash = document.hashCode();
        int second = store.put(new ByteArrayInputStream(this.txt2.getBytes()), uri1, DocumentStore.DocumentFormat.BINARY);
        assertTrue(hash == second);
    }

    @Test
    public void putNullValueToDelete() throws URISyntaxException, IOException {
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        DocumentStore store = new DocumentStoreImpl();
        int value = store.put(new ByteArrayInputStream(this.txt1.getBytes()), uri1, DocumentStore.DocumentFormat.BINARY);
        assertTrue(value == 0);
        DocumentImpl document = (DocumentImpl) store.get(uri1);
        int hash = document.hashCode();
        int nothing = store.put(null, uri1, DocumentStore.DocumentFormat.BINARY);
        assertTrue(nothing == hash);
    }

    @Test
    public void nullValueInPut() throws IOException,URISyntaxException {
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        DocumentStore store = new DocumentStoreImpl();
        try {
            store.put(new ByteArrayInputStream(this.txt1.getBytes()), null, DocumentStore.DocumentFormat.TXT);
            fail("null URI should've thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            store.put(new ByteArrayInputStream(this.txt1.getBytes()), this.uri1, null);
            fail("null format should've thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void deleteDoc() throws IOException, URISyntaxException {
        this.uri1 = new URI("http://edu.yu.cs/com1320/project/doc1");
        this.txt1 = "This is the text of doc1, in plain text. No fancy file format - just plain old String";
        DocumentStore store = new DocumentStoreImpl();
        store.put(new ByteArrayInputStream(this.txt1.getBytes()), uri1, DocumentStore.DocumentFormat.BINARY);
        store.delete(uri1);
        assertTrue(store.get(uri1) == null);
    }


}
