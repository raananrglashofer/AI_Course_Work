import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.BigOIt2;
import edu.yu.introtoalgs.BigOIt2Base;

import java.util.concurrent.ThreadLocalRandom;

public class LinearTest {
    public static class Linear extends BigOMeasurable {
        //protected int n = 1000;
        private int[] a; // = new int[n];

        @Override
        public void setup(final int n) {
            //System.out.println("The amount of data is " + n);
            assert n > 0 : "n must be greater than 0";
            this.a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = i;
            }
        }

        @Override
        public void execute() {
            // Count triples that sum to 0.
            int cnt = 0;
            for(int i = 0; i < a.length; i++) {
                if (a[i] == 9) {
                    cnt++;
                }
            }
            //System.out.println("The count is " + cnt);
        }

        public static void main(String[] args) {
            BigOIt2Base it = new BigOIt2();

            Linear linear = new Linear();
            long timeOutInMs = 6000; // Set the timeout value in milliseconds

            double ratio = it.doublingRatio(linear.getClass().getName(), timeOutInMs);
            System.out.println("Doubling Ratio: " + ratio);
        }
    }
}
