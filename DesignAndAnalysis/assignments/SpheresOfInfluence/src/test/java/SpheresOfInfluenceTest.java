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
    public void simpleLeffTest(){
        SpheresOfInfluence soi = new SpheresOfInfluence(2, 10);
        soi.addInfluencer("A", 2, 3);
        soi.addInfluencer("B", 6, 5);
        List<String> solution = soi.getMinimalCoverageInfluencers();
        List<String> expected = List.of("A", "B");
        assertEquals(solution, expected);
    }
}
