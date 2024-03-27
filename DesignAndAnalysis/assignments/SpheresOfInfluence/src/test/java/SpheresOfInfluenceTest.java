import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.SpheresOfInfluenceBase;
import edu.yu.da.SpheresOfInfluence;

import java.util.*;
import java.util.Collections;

import static org.junit.Assert.*;

public class SpheresOfInfluenceTest {

    @Test
    public void exceptionsTest(){
        // maxRight is less than 1
        assertThrows(IllegalArgumentException.class, () -> {
            SpheresOfInfluence test = new SpheresOfInfluence(2, -4);
        });

        // maxStrength is less than 1
        assertThrows(IllegalArgumentException.class, () -> {
            SpheresOfInfluence test = new SpheresOfInfluence(-2, 4);
        });

        // ID is empty in addInfluencer()
        assertThrows(IllegalArgumentException.class, () -> {
            SpheresOfInfluence test = new SpheresOfInfluence(2, 4);
            String id = "";
            test.addInfluencer(id, 4, 4);
        });

        // xValue is negative in addInfluencer()
        assertThrows(IllegalArgumentException.class, () -> {
            SpheresOfInfluence test = new SpheresOfInfluence(2, 4);
            test.addInfluencer("Hello", -3, 6);
        });

        // radius is less than 1 in addInfluencer()
        assertThrows(IllegalArgumentException.class, () -> {
            SpheresOfInfluence test = new SpheresOfInfluence(2, 4);
            test.addInfluencer("Hello", 3, 0);
        });

        // adding two influencers with the same ID
        assertThrows(IllegalArgumentException.class, () -> {
            SpheresOfInfluence test = new SpheresOfInfluence(2, 4);
            test.addInfluencer("A", 3, 4);
            test.addInfluencer("A", 6, 6);
        });
    }

    @Test
    public void simpleLeffTestPass(){
        SpheresOfInfluence soi = new SpheresOfInfluence(2, 10);
        soi.addInfluencer("A", 2, 3);
        soi.addInfluencer("B", 6, 5);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A", "B");
        assertEquals(solution, expected);
    }

    @Test
    public void simpleLeffTestFailBecauseMaxRight(){
        SpheresOfInfluence soi = new SpheresOfInfluence(2, 12);
        soi.addInfluencer("A", 2, 3);
        soi.addInfluencer("B", 6, 5);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = Collections.emptyList();
        // List should be empty
        assertEquals(solution, expected);
    }
    @Test
    public void simpleLeffTestFailBecauseMaxStrength(){
        SpheresOfInfluence soi = new SpheresOfInfluence(8, 10);
        soi.addInfluencer("A", 2, 3);
        soi.addInfluencer("B", 6, 5);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = Collections.emptyList();
        // List should be empty
        assertEquals(solution, expected);
    }

    @Test
    public void leffTimeTest(){
        long startTime = System.nanoTime();
        SpheresOfInfluence soi = new SpheresOfInfluence(2, 12);
        for(int i = 1; i < 8001; i++){
            soi.addInfluencer(String.valueOf(i), i, 2);
        }
        List<String> solution = soi.getMinimalCoverageInfluencers();
        long endTime = System.nanoTime();
        long time = (long) ((endTime - startTime) / 1000000000.0);
        System.out.println("Algorithm time: " + time + " seconds");
        assertTrue(time <= 0.45);
    }

    @Test
    // question from Piazza - 99% covered is still not all covered
    public void almostAllCovered(){
        SpheresOfInfluence soi = new SpheresOfInfluence(2, 5);
        soi.addInfluencer("A", 2, 3);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = Collections.emptyList();
        // List should be empty
        assertEquals(solution, expected);
    }

    @Test
    public void manyInfluencers(){
        SpheresOfInfluence soi = new SpheresOfInfluence(8, 10);
        soi.addInfluencer("A", 2, 3);
        soi.addInfluencer("B", 7, 5);
        soi.addInfluencer("C", 3, 5);
        soi.addInfluencer("D", 1, 3);
        soi.addInfluencer("E", 2, 4);
        soi.addInfluencer("F", 2, 1);
        soi.addInfluencer("G", 2, 3);
        soi.addInfluencer("H", 3, 3);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("B", "C");
        assertEquals(solution, expected);
    }

    @Test
    public void initialSortTurnsOutBad(){
        SpheresOfInfluence soi = new SpheresOfInfluence(8, 10);
        soi.addInfluencer("A", 2, 5);
        soi.addInfluencer("B", 3, 5);
        soi.addInfluencer("C", 7, 5);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected1 = List.of("A", "C"); // but could also be B + C
        List<String> expected2 = List.of("B", "C");
        assertTrue(solution.equals(expected1) || solution.equals(expected2) );
    }

    @Test
    public void needAllThree(){
        SpheresOfInfluence soi = new SpheresOfInfluence(8, 10);
        soi.addInfluencer("A", 0, 5);
        soi.addInfluencer("B", 10, 5);
        soi.addInfluencer("C", 4, 5);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A", "B", "C");
        assertEquals(solution, expected);
    }

    @Test
    public void oddMaxStrength(){
        SpheresOfInfluence soi = new SpheresOfInfluence(5, 12);
        soi.addInfluencer("A", 3, 4);
        soi.addInfluencer("B", 9, 4);
        soi.addInfluencer("C", 10, 3);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A", "B");
        assertEquals(solution, expected);
    }

    @Test
    public void soSoSoClose(){
        SpheresOfInfluence soi = new SpheresOfInfluence(8, 4);
        soi.addInfluencer("A", 0, 4);
        soi.addInfluencer("B", 1, 4);
        soi.addInfluencer("C", 2, 4);
        soi.addInfluencer("D", 3, 4);
        soi.addInfluencer("E", 4, 4);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = Collections.emptyList();
        // List should be empty
        assertEquals(solution, expected);
    }

    @Test
    public void edgeInfluencersBarelyMeet() {
        SpheresOfInfluence soi = new SpheresOfInfluence(8, 20);
        soi.addInfluencer("A", 5, 8); // Covers from left to the middle
        soi.addInfluencer("B", 15, 8); // Covers from the middle to right
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A", "B");
        assertEquals(expected, solution);
    }

    @Test
    public void singleInfluencerCoversEntireRange() {
        SpheresOfInfluence soi = new SpheresOfInfluence(2, 10);
        soi.addInfluencer("A", 5, 15); // Single influencer covers more than the entire x-axis
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A");
        assertEquals(expected, solution);
    }

    @Test
    public void randomlyPositionedInfluencers() {
        SpheresOfInfluence soi = new SpheresOfInfluence(5, 50);
        Random rand = new Random();
        // Adding 20 influencers with random positions and radii
        for (int i = 0; i < 20; i++) {
            int xValue = rand.nextInt(50) + 1; // Ensure xValue is within the range
            int radius = rand.nextInt(25) + 1; // Ensure radius is positive
            soi.addInfluencer("R" + i, xValue, radius);
        }
        // This test checks if the algorithm can handle random input and still find a solution
        List<String> solution = soi.getMinimalCoverageInfluencers();
        assertFalse(solution.isEmpty()); // Expecting some solution, though specific influencers are unpredictable
    }

    @Test
    public void veryLargeValuesTest() {
        SpheresOfInfluence soi = new SpheresOfInfluence(1000, 100000);
        soi.addInfluencer("A", 50000, 60000); // One influencer covers the entire range
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A");
        assertEquals(expected, solution);
    }

    @Test
    public void influencersOverlapSignificantly() {
        SpheresOfInfluence soi = new SpheresOfInfluence(4, 20);
        soi.addInfluencer("A", 5, 10);
        soi.addInfluencer("B", 7, 10);
        soi.addInfluencer("C", 10, 10);
        soi.addInfluencer("D", 20, 10);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("B", "D"); // Assuming "C" is unnecessary due to overlap
        assertEquals(expected, solution);
    }

    @Test
    public void nonCoveringInfluencersPresent() {
        SpheresOfInfluence soi = new SpheresOfInfluence(4, 30);
        soi.addInfluencer("A", 5, 7);
        soi.addInfluencer("B", 15, 7);
        soi.addInfluencer("NonCovering", 25, 2); // This influencer doesn't help in covering the x-axis fully
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = Collections.emptyList(); // Expecting no solution as full coverage isn't possible
        assertEquals(expected, solution);
    }
    @Test
    public void maxRightAtTheEdgeOfInfluencersRange() {
        SpheresOfInfluence soi = new SpheresOfInfluence(6, 15);
        soi.addInfluencer("A", 3, 5);
        soi.addInfluencer("B", 11, 5); // B's edge just covers maxRight
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A", "B");
        assertEquals(expected, solution);
    }

    @Test
    public void allInfluencersInsufficientRadiusForYAxisCoverage() {
        SpheresOfInfluence soi = new SpheresOfInfluence(10, 10);
        soi.addInfluencer("A", 2, 4); // Insufficient radius for Y-axis
        soi.addInfluencer("B", 5, 3); // Insufficient radius for Y-axis
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = Collections.emptyList();
        assertEquals(expected, solution);
    }

    @Test
    public void coveragePossibleWithNonSequentialSelection() {
        SpheresOfInfluence soi = new SpheresOfInfluence(4, 20);
        soi.addInfluencer("A", 3, 4);
        soi.addInfluencer("B", 10, 6); // Must choose B before A for optimal coverage
        soi.addInfluencer("C", 16, 5);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A", "B", "C");
        assertEquals(expected, solution);
    }



//    // create test where largest influencer is not in minimal set
//    // 3 influencers and 2 smallest make min set spanning whole region (i.e. biggest is extreneous)
//    // if biggest is included then need all 3 - biggest needs 2 others
//    @Test
//    public void tooGreedyTest(){
//
//    }

}
