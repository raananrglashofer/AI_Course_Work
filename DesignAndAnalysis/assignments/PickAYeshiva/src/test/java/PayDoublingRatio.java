import edu.yu.da.PickAYeshiva;

import java.util.concurrent.ThreadLocalRandom;


public class PayDoublingRatio {
    public static class Pay extends BigOMeasurable{
        private double[] faculty;
        private double[] cooking;

        @Override
        public void setup(final int n) {
            //System.out.println("The amount of data is " + n);
            assert n > 0 : "n must be greater than 0";
            this.faculty = new double[n];
            this.cooking = new double[n];
            for (int i = 0; i < n; i++) {
                faculty[i] = i;
                cooking[i] = cooking.length - 1;
            }
        }

        @Override
        public void execute() {
            PickAYeshiva pay = new PickAYeshiva(faculty, cooking);
        }

        public static void main(String[] args) {
            BigOIt2Base it = new BigOIt2();

            Pay linear = new Pay();
            long timeOutInMs = 6000; // Set the timeout value in milliseconds

            double ratio = it.doublingRatio(linear.getClass().getName(), timeOutInMs);
            System.out.println("Doubling Ratio: " + ratio);
        }
    }
}
