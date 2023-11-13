import edu.yu.introtoalgs.EQIQ;
import org.junit.Test;
import org.junit.Assert;
import java.util.*;
import static org.junit.Assert.assertThrows;
public class EQIQTest {
    @Test
    public void parameterCheck(){
        double[] eq = new double[2];
        double[] iq = new double[2];
        eq[0] = 10;
        eq[1] = 20;
        iq[0] = 10;
        iq[1] = 20;
        assertThrows(IllegalArgumentException.class, () -> {
            EQIQ eqiq = new EQIQ(-2, eq, iq, 1);
        });
        double[] eq2 = new double[3];
        eq2[0] = 10;
        eq2[1] = 20;
        eq2[2] = 30;
        assertThrows(IllegalArgumentException.class, () -> {
            EQIQ eqiq = new EQIQ(-2, eq, iq, 1);
        });
    }

    @Test
    public void sampleEQIQFavorEQ(){
        double[] eq = new double[2];
        double[] iq = new double[2];
        int questions = 2;
        int index = 1;
        eq[0] = 10;
        eq[1] = 20;
        iq[0] = 40;
        iq[1] = 40;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }

    @Test
    public void sampleEQIQFavorIQ(){
        double[] eq = new double[2];
        double[] iq = new double[2];
        int questions = 2;
        int index = 1;
        eq[0] = 40;
        eq[1] = 40;
        iq[0] = 10;
        iq[1] = 20;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }

    @Test
    public void threePplEQIQFavorEQ(){
        double[] eq = {10, 20, 15};
        double[] iq = {40, 35, 35};
        int questions = 2;
        int index = 1;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }
    @Test
    public void threePplEQIQFavorIQ(){
        double[] eq = {40, 35, 35};
        double[] iq = {10, 20, 15};
        int questions = 2;
        int index = 1;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }

    @Test
    public void comboOfQuestions(){
        double[] eq = {10, 5, 20, 30};
        double[] iq = {50, 60, 40, 10};
        int questions = 4;
        int index = 2;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }

    @Test
    public void cantWin(){
        double[] eq = {10, 5, 10, 30};
        double[] iq = {50, 60, 35, 10};
        int questions = 2;
        int index = 2;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }

    @Test
    public void lotsOfStudentsWin(){
        double[] eq = {10, 5, 10, 30, 20, 20, 15, 1, 2};
        double[] iq = {50, 60, 35, 10, 100, 45, 65, 90, 45};
        int questions = 2;
        int index = 4;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }
    // only 5 and 7 can win
    @Test
    public void lotsOfStudentsWinVeryClose(){
        double[] eq = {10, 11, 9, 12, 14, 15, 12, 8, 9};
        double[] iq = {50, 49, 53, 45, 42, 35, 48, 60, 54};
        int questions = 2;
        int index = 5;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }
    @Test
    public void manyQuestions(){
        double[] eq = {10, 5, 10, 30, 20, 20, 15, 1, 2};
        double[] iq = {50, 60, 35, 10, 10, 45, 65, 90, 45};
        int questions = 6;
        int index = 5;
        EQIQ eqiq = new EQIQ(questions, eq, iq, index);
        System.out.println(eqiq.canNepotismSucceed());
        System.out.println(eqiq.getNumberIQQuestions());
        System.out.println(eqiq.getNumberEQQuestions());
        System.out.println(eqiq.getNumberOfSecondsSuccess());
    }
}
