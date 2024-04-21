import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.MatzoDistribution;
import edu.yu.da.MatzoDistributionBase;


import java.util.*;
import java.util.Collections;

import static org.junit.Assert.*;

public class MatzoDistributionTests {
    @Test
    public void exceptionsTest(){
        // sourceWarehouse is Blank
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("", 5, "hello");
        });

        // destinationWarehouse is Blank
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("hey", 5, "");
        });

        // sourceConstraint isn't a positive integer
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 0, "B");
        });

        // sourceWarehouse equals destinationWarehouse
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "A");
        });

        // warehouseId is blank in addWarehouse() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.addWarehouse("", 1000);
        });

        // constraint isn't a positive integer in addWarehouse() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.addWarehouse("C", 0);
        });

        // adding a repeat warehouse in addWarehouse() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.addWarehouse("C", 5);
            test.addWarehouse("C", 5);
        });

        // w1 is blank in roadExists() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.roadExists("", "B", 4);
        });

        // w2 is blank in roadExists() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.roadExists("C", "", 4);
        });

        // constraint isn't positive in roadExists() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.roadExists("E", "C", 0);
        });

        // w1 equals w2 in roadExists() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.roadExists("C", "C", 0);
        });

        // w1 doesn't exist in roadExists() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.roadExists("G", "B", 0);
        });

        // w2 doesn't exist in roadExists() method
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.roadExists("A", "F", 0);
        });

        // adding repeat road
        assertThrows(IllegalArgumentException.class, () -> {
            MatzoDistribution test = new MatzoDistribution("A", 4, "B");
            test.addWarehouse("B", 5);
            test.roadExists("A", "B", 6);
            test.roadExists("A", "B", 10);
        });
    }


    @Test
    public void roadsNeverAdded(){
        MatzoDistribution test = new MatzoDistribution("A", 5, "B");
        double flow = test.max();
        assertTrue(flow == 0);
    }

    @Test
    public void roadsNeverConnect(){
        MatzoDistribution test = new MatzoDistribution("A", 5, "B");
        test.addWarehouse("C", 4);
        test.roadExists("A", "C", 4);
        double flow = test.max();
        assertTrue(flow == 0);
    }

    @Test
    public void superSimpleFlow(){
        MatzoDistribution test = new MatzoDistribution("A", 5, "B");
        test.addWarehouse("C", 4);
        test.roadExists("A", "C", 4);
        test.roadExists("C", "B", 4);
        double flow = test.max();
        assertTrue(flow == 4);
    }

    @Test
    public void wareHouseLimits(){
        MatzoDistribution test = new MatzoDistribution("A", 10, "D");
        test.addWarehouse("B", 1);
        test.addWarehouse("C", 10);
        test.roadExists("A", "B", 10);
        test.roadExists("B", "C", 10);
        test.roadExists("C", "D", 10);
        double flow = test.max();
        assertTrue(flow == 1);
    }

    @Test
    public void simpleSplitFlowToGetMax(){
        MatzoDistribution test = new MatzoDistribution("A", 10, "D");
        test.addWarehouse("B", 5);
        test.addWarehouse("C", 5);
        test.roadExists("A", "B", 10);
        test.roadExists("A", "C", 10);
        test.roadExists("B", "D", 10);
        test.roadExists("C", "D", 10);
        double flow = test.max();
        assertTrue(flow == 10);
    }

    @Test
    public void flowIsMax(){
        MatzoDistribution test = new MatzoDistribution("A", Integer.MAX_VALUE, "D");
        test.addWarehouse("B", Integer.MAX_VALUE);
        test.addWarehouse("C", Integer.MAX_VALUE);
        test.addWarehouse("E", Integer.MAX_VALUE);
        test.addWarehouse("F", Integer.MAX_VALUE);
        test.addWarehouse("G", Integer.MAX_VALUE);

        test.roadExists("A", "B", Integer.MAX_VALUE);
        test.roadExists("A", "C", Integer.MAX_VALUE);
        test.roadExists("B", "D", Integer.MAX_VALUE);
        test.roadExists("C", "D", Integer.MAX_VALUE);
        test.roadExists("A", "E", Integer.MAX_VALUE);
        test.roadExists("C", "E", Integer.MAX_VALUE);
        test.roadExists("F", "G", Integer.MAX_VALUE);
        test.roadExists("A", "F", Integer.MAX_VALUE);

        double flow = test.max();
        assertTrue(flow == Integer.MAX_VALUE);
    }

    @Test
    public void bottleNeckFlow(){
        MatzoDistribution test = new MatzoDistribution("S", 30, "T");
        test.addWarehouse("A", 30);
        test.addWarehouse("B", 30);
        test.addWarehouse("C", 30);
        test.addWarehouse("D", 30);
        test.addWarehouse("E", 30);
        test.addWarehouse("F", 30);

        test.roadExists("S", "A", 10);
        test.roadExists("A", "D", 9);
        test.roadExists("A", "B", 4);
        test.roadExists("A", "E", 15);
        test.roadExists("S", "B", 5);
        test.roadExists("B", "E", 8);
        test.roadExists("B", "C", 4);
        test.roadExists("S", "C", 15);
        test.roadExists("C", "F", 16);
        test.roadExists("F", "T", 10);
        test.roadExists("F", "B", 6);
        test.roadExists("E", "F", 15);
        test.roadExists("E", "T", 10);
        test.roadExists("D", "T", 10);
        test.roadExists("D", "E", 15);

        double max = test.max();
        assertTrue(max == 28);
    }
}
