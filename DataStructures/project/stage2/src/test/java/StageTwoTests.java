package edu.yu.cs.com1320.project.stage2;

import edu.yu.cs.com1320.project.HashTable;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage2.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.stage2.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage2.DocumentStore;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import edu.yu.cs.com1320.project.Stack;
import edu.yu.cs.com1320.project.impl.StackImpl;

import static org.junit.jupiter.api.Assertions.*;

public class StageTwoTests {

    @Test
    public void pushPeekPop() throws URISyntaxException, IOException {
        Stack<Integer> stack = new StackImpl<Integer>();
        stack.push(4);
        assertEquals(stack.peek(), 4);
        stack.pop();
        assertEquals(stack.peek(), null);
    }
    @Test
    public void undoPut() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "raanan";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        DocumentImpl doc = new DocumentImpl(uri1, txt1);
        assertTrue(store.get(uri1).equals(doc));
        store.undo();
        assertNull(store.get(uri1));
    }

    @Test
    public void undoDelete() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "raanan";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        DocumentImpl doc = new DocumentImpl(uri1, txt1);
        assertTrue(store.get(uri1).equals(doc));
        store.delete(uri1);
        assertNull(store.get(uri1));
        store.undo();
        assertEquals(doc, store.get(uri1));
    }

    @Test
    public void undoPutUri() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri;
        String txt;
        for(int i = 0; i < 10; i++){
            uri = URI.create("raanan" + i + ".com");
            txt = "hey " + i;
            assertEquals(0, store.put(new ByteArrayInputStream(txt.getBytes()), uri, DocumentStore.DocumentFormat.TXT));
        }
        Document doc = new DocumentImpl(URI.create("raanan3.com"), "hey 3");
        assertEquals(doc, store.get(URI.create("raanan3.com")));
        store.undo(URI.create("raanan3.com"));
        assertNull(store.get(URI.create("raanan3.com")));
    }

    @Test
    public void undoDeleteUri() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri;
        String txt;
        for(int i = 0; i < 10; i++){
            uri = URI.create("raanan" + i + ".com");
            txt = "hey " + i;
            assertEquals(0, store.put(new ByteArrayInputStream(txt.getBytes()), uri, DocumentStore.DocumentFormat.TXT));
        }
        Document doc = new DocumentImpl(URI.create("raanan3.com"), "hey 3");
        assertEquals(doc, store.get(URI.create("raanan3.com")));
        store.delete(URI.create("raanan3.com"));
        store.undo(URI.create("raanan3.com"));
        assertEquals(doc, store.get(URI.create("raanan3.com")));
    }

    @Test
    public void undoEmptyStack(){
        DocumentStore store = new DocumentStoreImpl();
        assertThrows(IllegalStateException.class, () -> { store.undo();});
    }

    @Test
    public void undoMissingUri() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri;
        String txt;
        for(int i = 0; i < 10; i++){
            uri = URI.create("raanan" + i + ".com");
            txt = "hey " + i;
            assertEquals(0, store.put(new ByteArrayInputStream(txt.getBytes()), uri, DocumentStore.DocumentFormat.TXT));
        }
        assertThrows(IllegalStateException.class, () -> { store.undo(URI.create("raanan24.com"));});
    }

    @Test
    public void undoPutAfterOverWritten() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri;
        String txt;
        for(int i = 0; i < 10; i++){
            uri = URI.create("raanan" + i + ".com");
            txt = "hey " + i;
            assertEquals(0, store.put(new ByteArrayInputStream(txt.getBytes()), uri, DocumentStore.DocumentFormat.TXT));
        }
        Document doc1 = new DocumentImpl(URI.create("raanan1.com"), "hey 1");
        assertEquals(doc1, store.get(URI.create("raanan1.com")));
        store.put(new ByteArrayInputStream("hello".getBytes()), URI.create("raanan1.com"), DocumentStore.DocumentFormat.TXT);
        Document doc2 = new DocumentImpl(URI.create("raanan1.com"), "hello");
        assertEquals(doc2, store.get(URI.create("raanan1.com")));
        store.undo(URI.create("raanan1.com"));
        assertTrue(doc1.equals(store.get(URI.create("raanan1.com"))));
        assertFalse(doc2.equals(store.get(URI.create("raanan1.com"))));
    }
}