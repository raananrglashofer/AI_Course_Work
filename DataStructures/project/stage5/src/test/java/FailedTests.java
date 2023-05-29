package edu.yu.cs.com1320.project.stage5.impl;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import com.google.gson.JsonSerializer;
import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Comparator;
import edu.yu.cs.com1320.project.stage5.Document;
import edu.yu.cs.com1320.project.stage5.DocumentStore;
import edu.yu.cs.com1320.project.stage5.impl.DocumentStoreImpl;
import java.io.File;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
public class FailedTests {
    private File baseDir = new File(System.getProperty("user.dir"));
    private String URItoFile(URI uri){
        String toFile = uri.toString().replace("http://", "");
        String path = toFile + ".json";
        return path;
    }
    private void checkContents(String errorMsg, String contents, String expected){
        assertNotNull(contents, errorMsg + ": contents were null");
        assertTrue(contents.toLowerCase().indexOf(expected.toLowerCase()) >= 0, errorMsg + ": expected content not found");
    }
    @Test
    public void stage5PushToDiskViaMaxDocCount() throws IOException {
        DocumentStoreImpl store = new DocumentStoreImpl(this.baseDir);
        store.setMaxDocumentCount(2);
        pushAboveMaxViaPutNew(store);
    }

    private void pushAboveMaxViaPutNew(DocumentStoreImpl store) throws IOException {
        try {
            URI uri1 = URI.create("http://edu.yu.cs/com1320/project/doc1");
            String txt1 = "doc1 went down the road, but all raanan found was more road.";
            URI uri2 = URI.create("http://edu.yu.cs/com1320/project/doc2");
            String txt2 = "doc2 walked down all day long. All he found was a road.";
            URI uri3 = URI.create("http://edu.yu.cs/com1320/project/doc3");
            String txt3 = "Raanan went down the road, but all he could find was the bathroom.";
            store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
            store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
            Document doc1 = store.get(uri1);
            Document doc2 = store.get(uri2);
            store.put(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);
            store.get(uri1);
            store.get(uri2);

        } catch (NullPointerException e) {
            e.printStackTrace(System.err);
            throw e;
        }
    }

    @Test
    public void stage5PushToDiskViaMaxDocCountBringBackInViaDeleteAndSearchTest() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "Raanan went down the road, but all raanan found was more road.";
        DocumentImpl doc = new DocumentImpl(uri1, txt1, null);
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "The kid walked down all day long. All he found was a road.";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        URI uri3 = URI.create("rachamim.com");
        String txt3 = "Raanan went down the road, but all he could find was the bathroom.";
        store.put(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);
        int hash = doc.hashCode();
        assertEquals(hash,store.get(uri1).hashCode());
        assertFalse(store.get(uri1) instanceof JsonSerializer<?>);
        store.setMaxDocumentCount(2);
    }

    @Test
    public void stage5PushToDiskViaMaxDocCountViaUndoDeleteTest(){

    }

    @Test
    public void deleteAllWithPrefixTest(){

    }

    @Test
    public void stage3UndoByURIThatImpactsEarlierThanLast(){

    }
}
