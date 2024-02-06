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
        assertEquals(taba.getOneLongestPath(), firstList);
        assertEquals(taba.getOtherLongestPath(), secondList);
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
        // My intuition thought that a,d,g would be the first path, not a,e,g
        assertEquals(taba.getOneLongestPath(), firstList);
        assertEquals(taba.getOtherLongestPath(), secondList);
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

//    @Test
//    public void reallyBigRandomGraphGPT() {
//        int numVertices = 15000;
//        String startVertex = "a";
//        ThereAndBackAgain taba = new ThereAndBackAgain(startVertex);
//
//        // Adding 10,000 vertices
//        for (int i = 1; i < numVertices; i++) {
//            char vertexLabel = (char) ('a' + i);
//            String vertex = String.valueOf(vertexLabel);
//            taba.addEdge(startVertex, vertex, Math.random() * 10 + 1); // Random weight between 1 and 10
//        }
//
//        // Adding random edges between all vertices
//        for (int j = 0; j < 100000; j++) {
//            Random random = new Random();
//            String firstVertex = taba.graph.getVertices().stream().toList().get(random.nextInt(taba.graph.getVertices().size()));
//            String secondVertex = taba.graph.getVertices().stream().toList().get(random.nextInt(taba.graph.getVertices().size()));
//
//            if (!firstVertex.equals(secondVertex)) {
//                    taba.addEdge(firstVertex, secondVertex, Math.random() * 10 + 1);
//                }
//            }
//
//        taba.doIt();
//
//        // Print or assert the result as needed
//        System.out.println("Goal Vertex: " + taba.goalVertex());
//        System.out.println("Goal Cost: " + taba.goalCost());
//        System.out.println("One Longest Path: " + taba.getOneLongestPath());
//        System.out.println("Other Longest Path: " + taba.getOtherLongestPath());
//    }
}
