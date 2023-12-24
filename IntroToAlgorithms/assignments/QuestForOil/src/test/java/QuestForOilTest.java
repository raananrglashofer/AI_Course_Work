import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import org.junit.Test;
import org.junit.Assert;
import edu.yu.introtoalgs.QuestForOil;
public class QuestForOilTest {
    @Test
    // should throw IAE when map is null
    public void checkIfMapNull(){
        char[][] map = null;
        assertThrows(IllegalArgumentException.class, () -> {
            QuestForOil quest = new QuestForOil(map);
        });
    }

    @Test
    // simple 3x3 map which should return 8
    // also checks multiple nContiguous() calls on same map
    public void simpleMap(){
        char[][] map = {
                {'S', 'S', 'S'},
                {'S', 'U', 'S'},
                {'S', 'S', 'S'}
        };
        QuestForOil quest = new QuestForOil(map);
        assertEquals(8, quest.nContiguous(0,0));
        assertEquals(8, quest.nContiguous(1,0));
        assertEquals(8, quest.nContiguous(0,1));
    }

    @Test
    // simple 3x3 map which should return 0 because starting at unsafe position
    public void startAtUnsafeSpot(){
        char[][] map = {
                {'S', 'S', 'S'},
                {'S', 'U', 'S'},
                {'S', 'S', 'S'}
        };
        QuestForOil quest = new QuestForOil(map);
        assertEquals(0, quest.nContiguous(1,1));
    }

    @Test
    // a little more complicated map which has a sae spot that is not contiguous
    public void aLittleCrazierMap(){
        char[][] map = {
                {'S', 'S', 'S', 'U', 'S', 'U'},
                {'S', 'U', 'S', 'S', 'U', 'U'},
                {'S', 'S', 'S', 'U', 'U', 'S'},
        };
        QuestForOil quest = new QuestForOil(map);
        assertEquals(10, quest.nContiguous(0,0));
        assertEquals(1, quest.nContiguous(2,5));
    }

    @Test
    // simple 3x3 map which should return 8
    public void evenCrazierMap(){
        char[][] map = {
                {'U', 'S', 'U', 'U', 'S', 'S'},
                {'S', 'U', 'U', 'U', 'U', 'S'},
                {'S', 'U', 'S', 'S', 'S', 'S'},
                {'S', 'S', 'U', 'S', 'U', 'U'},
                {'U', 'S', 'S', 'S', 'S', 'U'},
                {'S', 'S', 'U', 'S', 'U', 'U'}
        };
        QuestForOil quest = new QuestForOil(map);
        assertEquals(20, quest.nContiguous(0,1));
    }
}
