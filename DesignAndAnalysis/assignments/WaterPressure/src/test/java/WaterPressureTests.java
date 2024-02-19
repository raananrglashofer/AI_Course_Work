import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.WaterPressureBase;
import edu.yu.da.WaterPressure;

import java.util.*;
import java.util.Collections;

import static org.junit.Assert.*;
public class WaterPressureTests {
    @Test
    public void exceptionTests(){
        assertThrows(IllegalArgumentException.class, () -> {
            WaterPressure test = new WaterPressure("");
        });
        // Length of first vertex is < 1
        WaterPressure wp = new WaterPressure("A");
        assertThrows(IllegalArgumentException.class, () -> {
            wp.addBlockage("", "A", 2);
        });
        // Length of second vertex is < 1
        assertThrows(IllegalArgumentException.class, () -> {
            wp.addBlockage("A", "", 2);
        });
        // Weight is < 1
        assertThrows(IllegalArgumentException.class, () -> {
            wp.addBlockage("A", "B", -1);
        });
        // call method after calling minAmount() without adding a second pump
        wp.addBlockage("B", "A", 2.0);
        wp.addBlockage("B", "C", 2.0);
        wp.minAmount();
        assertThrows(IllegalStateException.class, () -> {
            wp.addBlockage("A", "", 2);
        });
        // add secondPump that is equal to initialInputPump
        assertThrows(IllegalArgumentException.class, () -> {
            wp.addSecondInputPump("A");
        });
        // add secondPump that isn't in set of vertices
        assertThrows(IllegalArgumentException.class, () -> {
            wp.addSecondInputPump("D");
        });
        // try to add multiple second pumps
        wp.addSecondInputPump("B");
        assertThrows(IllegalStateException.class, () -> {
            wp.addBlockage("B", "D", 2.0);
        });
        assertThrows(IllegalStateException.class, () -> {
            wp.addSecondInputPump("C");
        });
    }

    @Test
    public void nothingHappened(){
        WaterPressure wp = new WaterPressure("A");
        double minAmount = wp.minAmount();
        assertTrue(minAmount == 0.0); // assertEquals() is being annoying and forcing me to have a delta
    }

    @Test
    public void startVertexNotConnectedToGraph(){ // should return -1
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("B", "D", 2.0);
        wp.addBlockage("B", "C", 1.0);
        wp.addBlockage("C", "D", 2.0);
        assertTrue(wp.minAmount() == -1.0);
    }

    @Test
    public void simpleLeffTest(){
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("A", "B", 1.0);
        wp.addBlockage("B", "C", 2.0);
        double firstMinAmount = wp.minAmount();
        assertTrue(firstMinAmount == 2.0);
        wp.addSecondInputPump("C");
        double secondMinAmount = wp.minAmount();
        System.out.println(secondMinAmount);
        assertTrue(secondMinAmount == 1.0);
    }

    @Test
    public void simpleMinAmount(){
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("A", "B", 2.0);
        wp.addBlockage("A", "C", 3.0);
        wp.addBlockage("B", "G", 5.0);
        wp.addBlockage("C", "D", 4.0);
        wp.addBlockage("D", "B", 3.0);
        wp.addBlockage("D", "H", 6.0);
        wp.addBlockage("D", "F", 7.0);
        wp.addBlockage("D", "E", 8.0);
        wp.addBlockage("G", "D", 1.0);
        wp.addBlockage("E", "F", 8.0);
        wp.addBlockage("H", "F", 1.0);
        double min1 = wp.minAmount();
        System.out.println(min1);
        assertTrue(min1 == 8.0);
        wp.addSecondInputPump("E");
        double min2 = wp.minAmount();
        System.out.println(min2);
        assertTrue(min2 == 6.0);
    }
    @Test
    public void twoDisjointParts(){
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("A", "B", 2.0);
        wp.addBlockage("B", "C", 4.0);
        wp.addBlockage("D", "E", 3.0);
        wp.addBlockage("E", "F", 5.0);

        double min1 = wp.minAmount();
        // not all pumps can be reached
        assertTrue(min1 == -1.0);

        wp.addSecondInputPump("D");
        double min2 = wp.minAmount();
        // with second input pump all pumps can be reached
        assertTrue(min2 == 5.0);
    }

    @Test
    public void worseAfterSecondPump(){
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("A", "B", 3.0);
        wp.addBlockage("B", "C", 2.0);
        wp.addBlockage("A", "C", 1.0);
        wp.addBlockage("C", "D", 5.0);
        wp.addBlockage("B", "E", 2.0);
        wp.addBlockage("E", "D", 3.0);
        double min1 = wp.minAmount();
        assertTrue(min1 == 3.0);
        wp.addSecondInputPump("C");
        double min2 = wp.minAmount();
        System.out.println(min2);
        assertTrue(min1 == min2);
    }

    @Test
    public void adaptationOfSimpleLeffTest(){
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("A", "B", 1.0);
        wp.addBlockage("B", "C", 2.0);
        wp.addBlockage("C", "D", 3.0);
        double firstMinAmount = wp.minAmount();
        assertTrue(firstMinAmount == 3.0);
        wp.addSecondInputPump("D");
        double secondMinAmount = wp.minAmount();
        System.out.println(secondMinAmount);
        assertTrue(secondMinAmount == 2.0);
    }

    @Test
    public void secondPumpNoChange(){
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("A", "B", 2.0);
        wp.addBlockage("B", "C", 2.0);
        wp.addBlockage("C", "D", 2.0);
        double firstMinAmount = wp.minAmount();
        assertTrue(firstMinAmount == 2.0);
        wp.addSecondInputPump("D");
        double secondMinAmount = wp.minAmount();
        System.out.println(secondMinAmount);
        assertTrue(secondMinAmount == firstMinAmount);
    }

    @Test
    public void onlyTwoVertices(){
        WaterPressure wp = new WaterPressure("A");
        wp.addBlockage("A", "B", 2.0);
        wp.addBlockage("B", "A", 2.0);
        double min1 = wp.minAmount();
        assertTrue(min1 == 2);
        wp.addSecondInputPump("B");
        double min2 = wp.minAmount();
        assertTrue(min2 == 0.0);
    }

//    @Test
//    public void timeTest(){
//        WaterPressure wp = new WaterPressure("A");
//        for(int i = 0; i < 16777216; i++){
//            wp.addBlockage("A", "B" + i, 1.0);
//        }
//
//        long startTime = System.currentTimeMillis();
//
//        // Call the method you want to test
//        double minAmount = wp.minAmount();
//
//        long endTime = System.currentTimeMillis();
//        long elapsedTime = endTime - startTime;
//
//        System.out.println("Minimum Amount: " + minAmount);
//        System.out.println("Elapsed Time: " + elapsedTime + " milliseconds");
//    }
}
