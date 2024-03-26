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
    public void simpleLeffTestFail(){
        SpheresOfInfluence soi = new SpheresOfInfluence(2, 12);
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
        long time = endTime - startTime;
        assertTrue(time / 1000000000.0 <= 0.45);
    }

    // create test where largest influencer is not in minimal set
    // 3 influencers and 2 smallest make min set spanning whole region (i.e. biggest is extreneous)
    // if biggest is included then need all 3 - biggest needs 2 others
    @Test
    public void tooGreedyTest(){

    }
}
