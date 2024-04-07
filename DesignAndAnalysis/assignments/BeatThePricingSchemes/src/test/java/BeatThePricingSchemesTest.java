import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.BeatThePricingSchemesBase;
import edu.yu.da.BeatThePricingSchemes;
import org.junit.experimental.theories.suppliers.TestedOn;


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
            for(int i = 1; i < 22; i++){
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

        // optimalDecisions() called before cheapestPrice()
        assertThrows(IllegalStateException.class, () -> {
            BeatThePricingSchemesBase test = new BeatThePricingSchemes(4);
            test.optimalDecisions();
        });
    }

    @Test
    public void simpleLeffTest(){
        final BeatThePricingSchemesBase btps = new BeatThePricingSchemes(2.0);
        btps.addPricingScheme(8.75, 5);
        double cheapest1 = btps.cheapestPrice(4);
        List<Integer> optimalDecisions1 = btps.optimalDecisions();
        List<Integer> expected1 = List.of(0, 0, 0, 0);
        assertTrue(cheapest1 == 8.0);
        assertEquals(optimalDecisions1.size(), 4);
        double cheapest2 = btps.cheapestPrice(6);
        List<Integer> optimalDecisions2 = btps.optimalDecisions();
        List<Integer> expected2 = List.of(0, 1);
        assertTrue(cheapest2 == 10.75);
        assertEquals(optimalDecisions2.size(), 2);
    }

    @Test
    public void moreMatzosIsCheaper(){
        BeatThePricingSchemesBase btps = new BeatThePricingSchemes(5.0);
        btps.addPricingScheme(10.0, 20);
        btps.addPricingScheme(8.75, 3);
        btps.addPricingScheme(14.0, 4);
        btps.addPricingScheme(6.0, 1);
        double cheapest1 = btps.cheapestPrice(4);
        List<Integer> optimalDecisions1 = btps.optimalDecisions();
        List<Integer> expected1 = List.of(1); //  should be the 10 for 20
        System.out.println(cheapest1);
        assertTrue(cheapest1 == 10.0);
        assertEquals(optimalDecisions1, expected1);
    }

    @Test
    public void unitPriceIsTheBest(){
        BeatThePricingSchemesBase btps = new BeatThePricingSchemes(5.0);
        btps.addPricingScheme(1000.0, 20);
        btps.addPricingScheme(8.75, 1);
        btps.addPricingScheme(14.0, 2);
        btps.addPricingScheme(60.0, 2);
        btps.addPricingScheme(11, 2);
        btps.addPricingScheme(95.99, 4);
        double cheapest1 = btps.cheapestPrice(90);
        List<Integer> optimalDecisions1 = btps.optimalDecisions();
        List<Integer> expected1 = new ArrayList<>();
        for(int i = 0; i < 90; i++){
            expected1.add(0);
        }
        assertEquals(optimalDecisions1, expected1);
        assertTrue(cheapest1 == 450.0);
    }

    @Test
    public void needThreeSchemes(){
        BeatThePricingSchemesBase btps = new BeatThePricingSchemes(11.0);
        btps.addPricingScheme(70.0, 7);
        btps.addPricingScheme(30.0, 3);
        double cheapest = btps.cheapestPrice(11);
        assertTrue(cheapest == 111.0);
        List<Integer> optimalDecisions1 = btps.optimalDecisions();
        List<Integer> expected1 = List.of(0, 1, 2);
        assertEquals(optimalDecisions1.size(), 3);
    }

    @Test
    public void wayBetterScheme(){
        BeatThePricingSchemesBase btps = new BeatThePricingSchemes(100.0);
        btps.addPricingScheme(1, 10);
        List<Integer> list = List.of(1);
        for(int i = 1; i < 11; i++){
            double cheapest = btps.cheapestPrice(i);
            assertTrue(cheapest == 1.0);
            List<Integer> optimalDecisions = btps.optimalDecisions();
            assertEquals(list, optimalDecisions);
        }
    }

    @Test
    public void testThresholdExceedsSchemes() {
        BeatThePricingSchemes btps = new BeatThePricingSchemes(0.5);
        btps.addPricingScheme(1.0, 3); // $1 for 3 matzos
        double expectedPrice = 33.5; // 100 matzos at unit price
        double cheapest = btps.cheapestPrice(100);
        assertEquals(expectedPrice, cheapest, 0.001);
        assertEquals(34, btps.optimalDecisions().size()); // All decisions are unit price
    }

    @Test
    public void testBasicFunctionality() {
        BeatThePricingSchemes btps = new BeatThePricingSchemes(1.0); // unit price is $1
        btps.addPricingScheme(5.0, 6); // $5 for 6 matzos
        double expectedPrice = 5.0;
        assertEquals(expectedPrice, btps.cheapestPrice(6), 0.001);
        assertEquals(Arrays.asList(1), btps.optimalDecisions()); // Scheme 1 used
    }
}
