import edu.yu.cs.com1320.project.MinHeap;
import edu.yu.cs.com1320.project.impl.MinHeapImpl;
import edu.yu.cs.com1320.project.Trie;
import edu.yu.cs.com1320.project.stage4.Document;
import edu.yu.cs.com1320.project.stage4.impl.DocumentImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.Assert.*;

public class MinHeapTests {



    @Test
    public void simpleTrieTest() throws URISyntaxException {
        MinHeap heap = new MinHeapImpl();

        DocumentImpl txtDoc = new DocumentImpl(new URI("www.IDontKnowWhatImDoing"),
                "trying to see how to run tests");
        txtDoc.setLastUseTime(1);

        DocumentImpl txtDoc2 = new DocumentImpl(new URI("www.pleasework"),
                "du du du du you cant see him");
        txtDoc2.setLastUseTime(2);

        DocumentImpl txtDoc3 = new DocumentImpl(new URI("www.thisisfun"),
                "John Cena");
        txtDoc3.setLastUseTime(3);

        DocumentImpl txtDoc4 = new DocumentImpl(new URI("www.letsgo"),
                "nope not down");
        txtDoc4.setLastUseTime(4);


        heap.insert(txtDoc);
        heap.insert(txtDoc2);
        heap.insert(txtDoc3);
        heap.insert(txtDoc4);

    }

    @Test
    public void simple() throws URISyntaxException {
        DocumentImpl txtDoc = new DocumentImpl(new URI("www.IDontKnowWhatImDoing"),
                "trying to see how to run tests");
        txtDoc.setLastUseTime(1);

        DocumentImpl txtDoc2 = new DocumentImpl(new URI("www.pleasework"),
                "du du du du you cant see him");
        txtDoc2.setLastUseTime(2);

        assertEquals(-1,txtDoc.compareTo(txtDoc2));
        assertEquals(0,txtDoc.compareTo(txtDoc));
        assertEquals(1,txtDoc2.compareTo(txtDoc));
    }

    @Test
    public void minHeap() throws URISyntaxException {

        MinHeap heap = new MinHeapImpl();
        DocumentImpl txtDoc = new DocumentImpl(new URI("www.IDontKnowWhatImDoing"),
                "trying to see how to run tests");
        txtDoc.setLastUseTime(1);

        DocumentImpl txtDoc2 = new DocumentImpl(new URI("www.pleasework"),
                "du du du du you cant see him");
        txtDoc2.setLastUseTime(2);

        DocumentImpl txtDoc3 = new DocumentImpl(new URI("www.thisisfun"),
                "John Cena");
        txtDoc3.setLastUseTime(3);

        DocumentImpl txtDoc4 = new DocumentImpl(new URI("www.letsgo"),
                "nope not down");
        txtDoc4.setLastUseTime(4);

        heap.insert(txtDoc);
        heap.insert(txtDoc2);
        heap.insert(txtDoc3);
        heap.insert(txtDoc4);

        txtDoc.setLastUseTime(6);
        heap.reHeapify(txtDoc);
        //assertEquals(heap.remove(), txtDoc2);

        txtDoc2.setLastUseTime(9);

        heap.reHeapify(txtDoc2);

        txtDoc2.setLastUseTime(1);
        heap.reHeapify(txtDoc2);

        assertEquals(heap.remove(), txtDoc2);

        assertThrows(NoSuchElementException.class, () -> {
            heap.reHeapify(txtDoc2);
        });
        //assertThrows(new NoSuchElementException(), heap.reHeapify(txtDoc2));
        assertEquals(heap.remove(), txtDoc3);
        assertEquals(heap.remove(), txtDoc4);
        assertEquals(heap.remove(), txtDoc);



    }

//    @Test
//    public void knowClue(){
//        MinHeap heap = new MinHeapImpl();
//        int we = 1;
//        int hello = 2;
//        int nope = 3;
//        int truth = 4;
//        int dare = 5;
//
//        heap.insert(we);
//        heap.insert(hello);
//        heap.insert(nope);
//        heap.insert(truth);
//        heap.insert(dare);
//
//        we = 10;
//
//        heap.reHeapify(we);
//
//    }





    //Document d = new DocumentImpl();


    //MinHeap heap = new MinHeapImpl();
//        heap.insert(txtDoc);
//
//        heap.insert(5);
//        heap.insert(4);
//        heap.insert(3);
//        heap.insert(2);
//        heap.insert(1);
//        System.out.println(heap);
//        System.out.println();
//        heap.put("APPLE123", 1);
//        trie.put("APPLE123", 2);
//        trie.put("APPLE123", 3);
//        trie.put("WORD87", 8);
//        trie.put("WORD87", 7);

//        List<Integer> apple123List = trie.getAllSorted("apple123", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });//this comparator will order integers from lowest to highest
//        List<Integer> word87List = trie.getAllSorted("word87", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//
//        assertEquals(3, apple123List.size());
//        assertEquals(2, word87List.size());
//        assertEquals(java.util.Optional.of(1), java.util.Optional.of(apple123List.get(0)));
//        assertEquals(java.util.Optional.of(2), java.util.Optional.of(apple123List.get(1)));
//        assertEquals(java.util.Optional.of(3), java.util.Optional.of(apple123List.get(2)));
//        assertEquals(java.util.Optional.of(7), java.util.Optional.of(word87List.get(0)));
//        assertEquals(java.util.Optional.of(8), java.util.Optional.of(word87List.get(1)));
//
//        trie.put("app", 12);
//        trie.put("app", 5);
//        trie.put("ap", 4);
//
//        List<Integer> apList = trie.getAllWithPrefixSorted("AP", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//        List<Integer> appList = trie.getAllWithPrefixSorted("APP", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//
//        assertEquals(6, apList.size());
//        assertEquals(5, appList.size());
//
//
//        //assertEquals(java.util.Optional.of(12), apList.get(5));
//        assertEquals(java.util.Optional.of(12), java.util.Optional.of(appList.get(4)));
//
//        Set<Integer> deletedAppPrefix = trie.deleteAllWithPrefix("aPp");
//        assertEquals(5, deletedAppPrefix.size());
//        assertTrue(deletedAppPrefix.contains(3));
//        assertTrue(deletedAppPrefix.contains(5));
//
//        apList = trie.getAllWithPrefixSorted("AP", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//        appList = trie.getAllWithPrefixSorted("APP", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//
//        assertEquals(1, apList.size());
//        assertEquals(0, appList.size());
//
//        trie.put("deleteAll", 100);
//        trie.put("deleteAll", 200);
//        trie.put("deleteAll", 300);
//
//        List<Integer> deleteList = trie.getAllSorted("DELETEALL", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//
//        assertEquals(3, deleteList.size());
//        Set<Integer> thingsActuallyDeleted = trie.deleteAll("DELETEall");
//        assertEquals(3, thingsActuallyDeleted.size());
//        assertTrue(thingsActuallyDeleted.contains(100));
//
//        deleteList = trie.getAllSorted("DELETEALL", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//
//        assertEquals(0, deleteList.size());
//
//        trie.put("deleteSome", 100);
//        trie.put("deleteSome", 200);
//        trie.put("deleteSome", 300);
//
//        List<Integer> deleteList2 = trie.getAllSorted("DELETESOME", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//
//        assertEquals(3, deleteList2.size());
//        Integer twoHundred = (Integer) trie.delete("deleteSome", 200);
//        Integer nullInt = (Integer) trie.delete("deleteSome", 500);
//
//        assertEquals(java.util.Optional.of(200), java.util.Optional.of(twoHundred));
//        assertNull(nullInt);
//
//        deleteList2 = trie.getAllSorted("DELETESOME", (int1, int2) -> {
//            if ((int) int1 < (int) int2) {
//                return -1;
//            } else if ((int) int2 < (int) int1) {
//                return 1;
//            }
//            return 0;
//        });
//
//        assertEquals(2, deleteList2.size());
//        assertFalse(deleteList2.contains(200));



    //    //public class TrieImplEdgeCaseTest {
//        Trie<Integer> trie = new TrieImpl<>();
//        String string1 = "It was a dark and stormy night";
//        String string2 = "It was the best of times it was the worst of times";
//        String string3 = "It was a bright cold day in April and the clocks were striking thirteen";
//        String string4 = "I am free no matter what rules surround me";
//
//        @BeforeEach
//        public void init() {
//            for (String word : string1.split(" ")) {
//                trie.put(word, string1.indexOf(word));
//            }
//            for (String word : string2.split(" ")) {
//                trie.put(word, string2.indexOf(word));
//            }
//            for (String word : string3.split(" ")) {
//                trie.put(word, string3.indexOf(word));
//            }
//            for (String word : string4.split(" ")) {
//                trie.put(word, string4.indexOf(word));
//            }
//        }
//    @Test
//    public void testPut() {
//        MinHeap heap = new MinHeapImpl();
//
//
//        Trie<Integer> trie = new TrieImpl<Integer>();
//        String string1 = "It was a dark and stormy night";
//        String string2 = "It was the best of times it was the worst of times";
//        String string3 = "It was a bright cold day in April and the clocks were striking thirteen";
//        String string4 = "I am free no matter what rules surround me";
//
//        for (String word : string1.split(" ")) {
//            trie.put(word, string1.indexOf(word));
//        }
//        for (String word : string2.split(" ")) {
//            trie.put(word, string2.indexOf(word));
//        }
//        for (String word : string3.split(" ")) {
//            trie.put(word, string3.indexOf(word));
//        }
//        for (String word : string4.split(" ")) {
//            trie.put(word, string4.indexOf(word));
//        }
//
//        assertThrows(IllegalArgumentException.class, () -> {
//            trie.put(null, 100);
//        });
//
//        System.out.println(trie.getAllSorted("the", Comparator.naturalOrder()).size());
//
//        trie.put("", 100);
//
//        trie.put("the", null);
//
//        assertEquals(trie.getAllSorted("the", Comparator.naturalOrder()).size(), 2);
//    }

}
