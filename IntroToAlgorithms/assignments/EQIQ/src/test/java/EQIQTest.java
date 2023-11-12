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
    public void sampleEQIQ(){
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
    public void threePplEQIQ(){
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

}
