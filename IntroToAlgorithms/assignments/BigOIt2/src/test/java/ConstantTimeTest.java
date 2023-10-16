import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.BigOIt2;
import edu.yu.introtoalgs.BigOIt2Base;

import java.util.concurrent.ThreadLocalRandom;

public class ConstantTimeTest {
    public static class Constant extends BigOMeasurable {
        protected int n;
        private int[] a; // = new int[n];

        @Override
        public void setup(final int n) {
            this.n = n;
        }

        @Override
        public void execute() {
            int yalla = this.n;
        }

        public static void main(String[] args) {
            BigOIt2Base it = new BigOIt2();

            Constant constant = new Constant();
            long timeOutInMs = 6000; // Set the timeout value in milliseconds

            double ratio = it.doublingRatio(constant.getClass().getName(), timeOutInMs);
            System.out.println("Doubling Ratio: " + ratio);
        }
    }
}
