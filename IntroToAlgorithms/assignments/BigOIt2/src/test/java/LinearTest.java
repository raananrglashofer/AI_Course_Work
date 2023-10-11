import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.BigOIt2;
import edu.yu.introtoalgs.BigOIt2Base;

import java.util.concurrent.ThreadLocalRandom;

public class LinearTest {
    public static class Mystery extends BigOMeasurable {
        //protected int n = 1000;
        private int[] a; // = new int[n];

        @Override
        public void setup(final int n) {
            System.out.println("The amount of data is " + n);
            assert n > 0 : "n must be greater than 0";
            a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = 1;
            }
        }

        @Override
        public void execute() {
            // Count triples that sum to 0.
            int cnt = 0;
            for(int i = 0; i < a.length; i++) {
                if (a[i] == 2) {
                    cnt++;
                }
            }
            System.out.println("The count is " + cnt);
        }

        public static void main(String[] args) {
            BigOIt2Base it = new BigOIt2();

            //String bigOMeasurableClassName = "Mystery"; // Replace with the actual class name you want to test
            BigOlt2Test.Mystery mystery = new BigOlt2Test.Mystery();
            long timeOutInMs = 10000; // Set the timeout value in milliseconds

            double ratio = it.doublingRatio(mystery.getClass().getName(), timeOutInMs);
            System.out.println("Doubling Ratio: " + ratio);
        }
    }
}
