import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.ThereAndBackAgain;
import edu.yu.da.ThereAndBackAgainBase;

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
        // doIt() has already been invoked
        taba.doIt();
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
        ThereAndBackAgain taba = new ThereAndBackAgain("A");
        taba.doIt();
        assertNull(taba.goalVertex());
        assertTrue(taba.goalCost() == 0.0); // assertEquals() is being annoying and forcing me to have a delta
        assertTrue(taba.getOneLongestPath().isEmpty());
        assertTrue(taba.getOtherLongestPath().isEmpty());
    }
}
