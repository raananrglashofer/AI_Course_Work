import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.ThereAndBackAgain;
import edu.yu.da.ThereAndBackAgainBase;

import java.util.*;
import java.util.Collections;

import static org.junit.Assert.*;

public class ThereAndBackAgainTests {
    @Test
    public void exceptionTests(){
        // Length of start vertex is < 1
        assertThrows(IllegalArgumentException.class, () -> {
            ThereAndBackAgain test = new ThereAndBackAgain("");
        });
        // Length of first vertex is < 1
        ThereAndBackAgain taba = new ThereAndBackAgain("A");
        assertThrows(IllegalArgumentException.class, () -> {
            taba.addEdge("", "A", 2);
        });
        // Length of second vertex is < 1
        assertThrows(IllegalArgumentException.class, () -> {
            taba.addEdge("A", "", 2);
        });
        // Weight is < 1
        assertThrows(IllegalArgumentException.class, () -> {
            taba.addEdge("A", "B", -1);
        });
        taba.doIt();
        // doIt() has already been invoked
        assertThrows(IllegalStateException.class, () -> {
            taba.addEdge("A", "", 2);
        });
        // doIt() has already been invoked
        assertThrows(IllegalStateException.class, () -> {
            taba.doIt();
        });
    }
    @Test
    public void nothingHappened(){
        ThereAndBackAgain taba = new ThereAndBackAgain("a");
        taba.doIt();
        assertNull(taba.goalVertex());
        assertTrue(taba.goalCost() == 0.0); // assertEquals() is being annoying and forcing me to have a delta
        assertTrue(taba.getOneLongestPath().isEmpty());
        assertTrue(taba.getOtherLongestPath().isEmpty());
    }

    @Test
    public void simpleLeffTest(){
        String startVertex = "a";
        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 1.0);
        taba.addEdge("b", "c", 2.0);
        taba.doIt();
        assertNull(taba.goalVertex());
        assertTrue(taba.goalCost() == 0);
        assertEquals(taba.getOneLongestPath(), Collections.<String>emptyList());
        assertEquals(taba.getOtherLongestPath(), Collections.<String>emptyList());
    }

    @Test
    public void testToSeeIfCodeWorks(){
        String startVertex = "a";
        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 2.0);
        taba.addEdge(startVertex, "c", 2.0);
        taba.addEdge("b", "d", 3.0);
        taba.addEdge("c", "d", 3.0);
        taba.doIt();
        assertEquals(taba.goalVertex(), "d");
        assertTrue(taba.goalCost() == 5.0);
        List<String> firstList = new ArrayList<>();
        firstList.add("a");
        firstList.add("c");
        firstList.add("d");
        List<String> secondList = new ArrayList<>();
        secondList.add("a");
        secondList.add("b");
        secondList.add("d");
        assertEquals(taba.getOneLongestPath(), secondList);
        assertEquals(taba.getOtherLongestPath(), firstList);
    }

    @Test
    public void notBalancedPaths(){
        String startVertex = "a";
        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 4.0);
        taba.addEdge(startVertex, "c", 4.0);
        taba.addEdge("b", "d", 5.0);
        taba.addEdge("d", "e", 3.0);
        taba.addEdge("e", "f", 1.0);
        taba.addEdge("d", "f", 4.0);
        taba.doIt();
        assertNull(taba.goalVertex());
        assertTrue(taba.goalCost() == 0);
        assertEquals(taba.getOneLongestPath(), Collections.<String>emptyList());
        assertEquals(taba.getOtherLongestPath(), Collections.<String>emptyList());
    }

    @Test
    public void equalTriangle(){
        String startVertex = "a";
        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 1.0);
        taba.addEdge(startVertex, "c", 1.0);
        taba.addEdge("b", "c", 1.0);
        taba.doIt();
        assertNull(taba.goalVertex());
        assertTrue(taba.goalCost() == 0);
        assertEquals(taba.getOneLongestPath(), Collections.<String>emptyList());
        assertEquals(taba.getOtherLongestPath(), Collections.<String>emptyList());
    }

    @Test
    public void twoEdges(){
        String startVertex = "a";
        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 1.0);
        taba.addEdge(startVertex, "c", 1.0);
        taba.doIt();
        assertNull(taba.goalVertex());
        assertTrue(taba.goalCost() == 0);
        assertEquals(taba.getOneLongestPath(), Collections.<String>emptyList());
        assertEquals(taba.getOtherLongestPath(), Collections.<String>emptyList());
    }

    @Test
    public void twoValidPaths(){
        String startVertex = "a";
        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 1.0);
        taba.addEdge(startVertex, "c", 1.0);
        taba.addEdge(startVertex, "d", 1.0);
        taba.addEdge(startVertex, "e", 1.0);
        taba.addEdge("b", "f", 2.0);
        taba.addEdge("c", "f", 2.0);
        taba.addEdge("d", "g", 3.0);
        taba.addEdge("e", "g", 3.0);
        taba.doIt();
        assertEquals(taba.goalVertex(), "g");
        assertTrue(taba.goalCost() == 4.0);
        List<String> firstList = new ArrayList<>();
        firstList.add("a");
        firstList.add("e");
        firstList.add("g");
        List<String> secondList = new ArrayList<>();
        secondList.add("a");
        secondList.add("d");
        secondList.add("g");
        assertEquals(taba.getOneLongestPath(), secondList);
        assertEquals(taba.getOtherLongestPath(), firstList);
    }

    @Test
    public void reallyBigRandomGraph(){
        String startVertex = "a";
        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
        taba.addEdge(startVertex, "b", 1.0);
        taba.addEdge(startVertex, "c", 1.0);
        taba.addEdge(startVertex, "d", 1.0);
        taba.addEdge("b", "f", 2.0);
        taba.addEdge("c", "f", 4.0);
        taba.addEdge("b", "c", 11.0);
        taba.addEdge("d", "e", 5.0);
        taba.addEdge("a", "e", 10.0);
        taba.addEdge("e", "f", 9.0);
        taba.addEdge("e", "g", 3.0);
        taba.addEdge("g", "h", 7.0);
        taba.addEdge("g", "i", 4.0);
        taba.addEdge("f", "h", 19.0);
        taba.addEdge("f", "j", 8.0);
        taba.addEdge("j", "h", 8.0);
        taba.addEdge("e", "i", 1.0);
        taba.addEdge("c", "e", 5.0);
        taba.doIt();
        System.out.println(taba.goalVertex());
        System.out.println(taba.goalCost());
        System.out.println(taba.getOneLongestPath());
        System.out.println(taba.getOtherLongestPath());
    }

    @Test
    public void microGoalVertexExists3(){
        String startVertex = "A";
        ThereAndBackAgain TABA = new ThereAndBackAgain(startVertex);
        TABA.addEdge("S", "A", 6.0);
        TABA.addEdge("S", "B", 5.0);
        TABA.addEdge("S", "G", 1.0);
        TABA.addEdge("A", "B", 2.0);
        TABA.addEdge("A", "D", 7.0);
        TABA.addEdge("B", "C", 7.0);
        TABA.addEdge("B", "D", 8.0);
        TABA.addEdge("B", "G", 2.0);
        TABA.addEdge("C", "E", 5.0);
        TABA.addEdge("E", "F", 3.0);
        TABA.addEdge("E", "G", 4.0);
        TABA.addEdge("F", "G", 7.0);
        TABA.doIt();
        System.out.println(TABA.goalVertex());
        System.out.println(TABA.goalCost());
        System.out.println(TABA.getOneLongestPath());
        System.out.println(TABA.getOtherLongestPath());
    }

    @Test
    public void nanoGoalVertexExists(){
        String startVertex = "A";
        ThereAndBackAgain TABA = new ThereAndBackAgain(startVertex);
        TABA.addEdge("A", "C", 1.0);
        TABA.addEdge("A", "B", 2.0);
        TABA.addEdge("C", "B", 1.0);
        TABA.doIt();
        System.out.println(TABA.goalVertex());
        System.out.println(TABA.goalCost());
        System.out.println(TABA.getOneLongestPath());
        System.out.println(TABA.getOtherLongestPath());
    }

    @Test
    public void startVertexNotConnectedToGraph(){
        String startVertex = "A";
        ThereAndBackAgain TABA = new ThereAndBackAgain(startVertex);
        TABA.addEdge("D", "B", 1.0);
        TABA.addEdge("B", "C", 1.0);
        TABA.doIt();
        System.out.println(TABA.goalVertex());
        System.out.println(TABA.goalCost());
        System.out.println(TABA.getOneLongestPath());
        System.out.println(TABA.getOtherLongestPath());
    }
}








