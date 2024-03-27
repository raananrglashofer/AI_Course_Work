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
            soi.addInfluencer(String.valueOf(i), 1, i);
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
        System.out.println(solution);
        assertEquals(solution, expected);
    }

    // create test where largest influencer is not in minimal set
    // 3 influencers and 2 smallest make min set spanning whole region (i.e. biggest is extreneous)
    // if biggest is included then need all 3 - biggest needs 2 others
    @Test
    public void tooGreedyTest(){

    }

}
