
import edu.yu.cs.com1320.project.impl.TrieImpl;
import edu.yu.cs.com1320.project.impl.HashTableImpl;
import edu.yu.cs.com1320.project.impl.StackImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentImpl;
import edu.yu.cs.com1320.project.stage3.impl.DocumentStoreImpl;
import edu.yu.cs.com1320.project.Undoable;
import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.CommandSet;
import edu.yu.cs.com1320.project.GenericCommand;
import edu.yu.cs.com1320.project.stage3.Document;
import edu.yu.cs.com1320.project.stage3.DocumentStore;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Comparator;

public class DocumentStoreTests {

    @Test
    public void putInTriePlusGetAllSorted(){
        Trie<Integer> trie = new TrieImpl<Integer>();
        trie.put("raanan", 5);
        trie.put("glashofer", 24);
        trie.put("raanan", 25);
        List<Integer> compare = new ArrayList<>();
        compare.add(25);
        compare.add(5);
        Comparator c = Collections.reverseOrder();
        List<Integer> list = trie.getAllSorted("raanan", c);
        assertEquals(compare, list);
    }

    @Test
    public void deleteAllFromTrie(){
        Trie<Integer> trie = new TrieImpl<Integer>();
        trie.put("raanan", 5);
        trie.put("glashofer", 24);
        trie.put("raanan", 25);
        Set<Integer> compare = new HashSet<>();
        compare.add(5);
        compare.add(25);
        Set<Integer> deletedInts = trie.deleteAll("raanan");
        assertEquals(deletedInts, compare);
        assertTrue(trie.getAllSorted("raanan", Collections.reverseOrder()).isEmpty());
    }

    @Test
    public void deleteFromTrie(){
        Trie<Integer> trie = new TrieImpl<Integer>();
        trie.put("raanan", 5);
        trie.put("glashofer", 24);
        trie.put("raanan", 25);
        Integer deleted = trie.delete("raanan", 25);
        List<Integer> compare = new ArrayList<>();
        compare.add(5);
        assertEquals(deleted, 25);
        assertEquals(trie.getAllSorted("raanan", Collections.reverseOrder()),compare);
    }

    @Test
    public void deletePrefixAndGetAllSortedPrefix(){
        Trie<Integer> trie = new TrieImpl<Integer>();
        trie.put("raanan", 5);
        trie.put("rain", 24);
        trie.put("raanan", 25);
        trie.put("glashofer", 17);
        List<Integer> compare = new ArrayList<>();
        compare.add(25);
        compare.add(24);
        compare.add(5);
        List<Integer> sorted = trie.getAllWithPrefixSorted("ra", Collections.reverseOrder());
        assertEquals(sorted, compare);
        Set<Integer> deleted = trie.deleteAllWithPrefix("ra");
        assertTrue(trie.getAllWithPrefixSorted("ra", Collections.reverseOrder()).isEmpty());
    }

    @Test
    public void undoPut() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "raanan went down the road";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "raanan did not go down the road. Instead, he went up the road";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        DocumentImpl doc = new DocumentImpl(uri1, txt1);
        assertTrue(store.get(uri1).equals(doc));
        assertEquals(store.search("road").size(), 2);
        assertEquals(store.search("up").size(), 1);
        store.undo();
        assertEquals(store.search("road").size(), 1);
        assertTrue(store.search("up").isEmpty());
        assertNull(store.get(uri2));
    }

    @Test
    public void undoDelete() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "Raanan went down the road, but all raanan found was more road.";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "The kid walked down all day long. All he found was a road.";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        URI uri3 = URI.create("rachamim.com");
        String txt3 = "Raanan went down the road, but all he could find was the bathroom.";
        store.put(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);
        DocumentImpl doc = new DocumentImpl(uri1, txt1);
        assertTrue(store.get(uri1).equals(doc));
        assertEquals(store.search("road").size(), 3);
        assertEquals(store.search("Raanan").size(), 2);
        assertEquals(store.search("raanan").size(), 1);
        assertEquals(store.search("The").size(), 1);
        store.delete(uri1);
        assertEquals(store.search("road").size(), 2);
        assertEquals(store.search("Raanan").size(), 1);
        assertTrue(store.search("raanan").isEmpty());
        store.undo();
        assertEquals(store.search("road").size(), 3);
        assertEquals(store.search("Raanan").size(), 2);
        assertEquals(store.search("raanan").size(), 1);
        assertNotNull(store.get(uri1));
    }

    @Test
    public void UndoDeleteAll() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "Raanan went down the road, but all raanan found was more road.";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "The kid walked down all day long. All he found was a road.";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        URI uri3 = URI.create("rachamim.com");
        String txt3 = "Raanan went down the road, but all he could find was the bathroom.";
        store.put(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);
        URI uri4 = URI.create("lesgo.com");
        String txt4 = "She sells sea shells by the sea shore";
        store.put(new ByteArrayInputStream(txt4.getBytes()), uri4, DocumentStore.DocumentFormat.TXT);
        DocumentImpl doc = new DocumentImpl(uri1, txt1);
        assertTrue(store.get(uri1).equals(doc));
        assertEquals(store.search("road").size(), 3);
        assertEquals(store.search("Raanan").size(), 2);
        assertEquals(store.search("raanan").size(), 1);
        assertEquals(store.search("The").size(), 1);
        assertEquals(store.search("sells").size(), 1);
        assertEquals(store.search("the").size(), 3);
        store.deleteAll("road");
        assertNull(store.get(uri1));
        assertNull(store.get(uri2));
        assertNull(store.get(uri3));
        assertTrue(store.search("road").isEmpty());
        assertTrue(store.search("The").isEmpty());
        assertEquals(store.search("sells").size(), 1);
        assertEquals(store.search("the").size(), 1);
        store.undo();
        assertNotNull(store.get(uri1));
        assertNotNull(store.get(uri2));
        assertNotNull(store.get(uri3));
        assertEquals(store.search("road").size(), 3);
        assertEquals(store.search("Raanan").size(), 2);
        assertEquals(store.search("raanan").size(), 1);
        assertEquals(store.search("The").size(), 1);
        assertEquals(store.search("sells").size(), 1);
        assertEquals(store.search("the").size(), 3);
    }

    @Test
    public void undoUndo() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "Raanan went down the road, but all raanan found was more road.";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "The kid walked down all day long. All he found was a road.";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        DocumentImpl doc = new DocumentImpl(uri1, txt1);
        assertTrue(store.get(uri1).equals(doc));
        store.undo(uri1);
        assertNull(store.get(uri1));
        assertThrows(IllegalStateException.class, () -> { store.undo(uri1);});
    }

    @Test
    public void removePartialCommandSet() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "Raanan2 went down the road, but all raanan found was more road.";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "The kid walked down all day long. All he found was a road.";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        assertEquals(store.search("down").size(), 2);
        store.deleteAll("down");
        assertEquals(store.search("down").size(), 0);
        assertNull(store.get(uri2));
        store.undo(uri2);
        assertNotNull(store.get(uri2));
        assertNull(store.get(uri1));
        store.undo(uri1);
        assertNotNull(store.get(uri1));
    }

    @Test
    public void tooManyUndos() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "Raanan went down the road, but all raanan found was more road.";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "The kid walked down all day long. All he found was a road.";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        URI uri3 = URI.create("rachamim.com");
        String txt3 = "Raanan went down the road, but all he could find was the bathroom.";
        store.put(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);
        URI uri4 = URI.create("lesgo.com");
        String txt4 = "She sells sea shells by the sea shore";
        store.put(new ByteArrayInputStream(txt4.getBytes()), uri4, DocumentStore.DocumentFormat.TXT);
        store.deleteAll("down");
        for(int i = 0; i < 5; i++){
            store.undo();
        }
        assertThrows(IllegalStateException.class, () -> { store.undo();});
    }

    @Test
    public void checkIfSorting() throws IOException {
        DocumentStore store = new DocumentStoreImpl();
        URI uri1 = URI.create("raanan.com");
        String txt1 = "Raanan went down the road, but all raanan found was more road.";
        store.put(new ByteArrayInputStream(txt1.getBytes()), uri1, DocumentStore.DocumentFormat.TXT);
        URI uri2 = URI.create("glashofer.com");
        String txt2 = "The kid walked down all day long. All he found was a road. down down";
        store.put(new ByteArrayInputStream(txt2.getBytes()), uri2, DocumentStore.DocumentFormat.TXT);
        URI uri3 = URI.create("rachamim.com");
        String txt3 = "Raanan went down the road, but all he could find was the bathroom. down";
        store.put(new ByteArrayInputStream(txt3.getBytes()), uri3, DocumentStore.DocumentFormat.TXT);
        URI uri4 = URI.create("lesgo.com");
        String txt4 = "She sells sea shells by the sea shore";
        store.put(new ByteArrayInputStream(txt4.getBytes()), uri4, DocumentStore.DocumentFormat.TXT);
        List<Document> returned = store.search("down");
        assertEquals(returned.size(), 3);
        List<Document> checker = new ArrayList<>();
        checker.add(store.get(uri2));
        checker.add(store.get(uri3));
        checker.add(store.get(uri1));
        for(int i = 0; i < returned.size(); i++){
            assertEquals(returned.get(i), checker.get(i));
        }
    }
}
