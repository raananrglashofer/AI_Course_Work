import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.BeatThePricingSchemesBase;
import edu.yu.da.BeatThePricingSchemes;

import java.util.*;
import java.util.Collections;

import static org.junit.Assert.*;
public class BeatThePricingSchemesTest {

    @Test
    public void exceptionsTest(){
        // unitPrice isn't greater than 0
        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(-4);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(0);
        });
        // price isn't greater than 0 in addPricingScheme()
        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(4);
            test.addPricingScheme(-4, 6);
        });

        // quantity isn't greater than 0 in addPricingScheme()
        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(4);
            test.addPricingScheme(4, -6);
        });

        // quantity is greater than MAX_MATZOS in addPricingScheme()
        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(4);
            test.addPricingScheme(4, 1000);
        });

        // Adding 21st pricing scheme
        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(4);
            for(int i = 1; i < 21; i++){
                test.addPricingScheme(i, 10);
            }
        });

        // threshold is greater than MAX_MATZOS in cheapestPrice()
        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(4);
            test.cheapestPrice(1000);
        });

        // threshold isn't greater than 0 in cheapestPrice()
        assertThrows(IllegalArgumentException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(4);
            test.cheapestPrice(0);
        });
    }

    @Test
    public void simpleLeffTest(){
        final BeatThePricingSchemesBase btps = new BeatThePricingSchemes(2.0);
        btps.addPricingScheme(8.75, 5);
        double cheapest1 = btps.cheapestPrice(4);
        List<Integer> optimalDecisions1 = btps.optimalDecisions();
        assertNotNull(optimalDecisions1);
        assertEquals(cheapest1, 8.0);
        double cheapest2 = btps.cheapestPrice(6);
        List<Integer> optimalDecisions2 = btps.optimalDecisions();
        assertNotNull(optimalDecisions2);
        assertEquals(cheapest2, 10.75);
    }
}
