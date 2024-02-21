import org.junit.Test;
import org.junit.Assert;
import edu.yu.da.PickAYeshivaBase;
import edu.yu.da.PickAYeshiva;

import java.util.*;
import java.util.Collections;

import static org.junit.Assert.*;
public class PickAYeshivaTests {

    @Test
    public void arraysAreNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            double[] cooking = new double[1];
            cooking[0] = 4;
            PickAYeshiva test = new PickAYeshiva(null, cooking);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            double[] faculty = new double[1];
            faculty[0] = 4;
            PickAYeshiva test = new PickAYeshiva(faculty, null);
        });
    }

    @Test
    public void arraysAreNotSameLength(){
        double[] cooking = new double[1];
        cooking[0] = 4;
        double[] faculty = new double[2];
        faculty[0] = 4;
        faculty[1] = 3;
        assertThrows(IllegalArgumentException.class, () -> {
            PickAYeshiva test = new PickAYeshiva(faculty, cooking);
        });
    }

    @Test
    public void simpleLeffTest(){
        final double [ ] facultyRatioRankings = {0 , 1 , 2 } ;
        final double [ ] cookingRankings = {3 , 2 , 1 } ;
        final PickAYeshivaBase pay =
                new PickAYeshiva ( facultyRatioRankings , cookingRankings ) ;
        assertEquals(facultyRatioRankings, pay.getFacultyRatioRankings());
        assertEquals(cookingRankings, pay.getCookingRankings());
    }
}
