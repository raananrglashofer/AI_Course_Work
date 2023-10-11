import org.junit.Test;
import edu.yu.introtoalgs.BigOMeasurable;
import edu.yu.introtoalgs.BigOIt2;
import edu.yu.introtoalgs.BigOIt2Base;
import edu.yu.introtoalgs.BigOMeasurable;
//import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Assert;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
public class BigOlt2Test {
    public static class Mystery extends BigOMeasurable {
        //protected int n = 1000;
        private int[] a; // = new int[n];

        @Override
        public void setup(final int n) {
            System.out.println("The amount of data is " + n);
            assert n > 0 : "n must be greater than 0";
            //this.n = n;
            a = new int[n];
            int MAX = 1000000;
            int MIN = -1000000;
            for (int i = 0; i < n; i++) {
                a[i] = ThreadLocalRandom.current().nextInt(MIN, MAX + 1);
            }
        }

        @Override
        public void execute() {
            // Count triples that sum to 0.
            int N = a.length;
            int cnt = 0;
            for (int i = 0; i < N; i++) {
                for (int j = i + 1; j < N; j++) {
                    for (int k = j + 1; k < N; k++) {
                        if (a[i] + a[j] + a[k] == 0) {
                            cnt++;
                        }
                    }
                }
            }
            System.out.println("The count is " + cnt);
        }

        public static void main(String[] args) {
            BigOIt2Base it = new BigOIt2();

            //String bigOMeasurableClassName = "Mystery"; // Replace with the actual class name you want to test
            Mystery mystery = new Mystery();
            long timeOutInMs = 100000; // Set the timeout value in milliseconds

            double ratio = it.doublingRatio(mystery.getClass().getName(), timeOutInMs);
            System.out.println("Doubling Ratio: " + ratio);
        }
    }
}

/*    @Test
    public void testMysteryAlgorithm() {
        class MysteryTest {

            // Define the test class for your algorithm
            static class Mystery extends BigOMeasurable {
                private int[] a; // Data for the ThreeSumAlgorithm
                private int cnt; // Count for the ThreeSumAlgorithm

                public Mystery() {
                    // Constructor initializes data and count
                    this.a = new int[0]; // Initialize with an empty array
                    this.cnt = 0;
                }

                @Override
                public void setup(final int n) {
                    // Generate or receive data as a function of "n"
                    a = generateData(n);
                }

                @Override
                public void execute() {
                    // Perform the ThreeSumAlgorithm on the data
                    int N = a.length;
                    cnt = 0; // Reset count before execution

                    for (int i = 0; i < N; i++) {
                        for (int j = i + 1; j < N; j++) {
                            for (int k = j + 1; k < N; k++) {
                                if (a[i] + a[j] + a[k] == 0) {
                                    cnt++;
                                }
                            }
                        }
                    }
                }

                // Getter method to retrieve the count after execution
                public int getCount() {
                    return cnt;
                }

                // Example data generation method (you can modify this based on your requirements)
                private int[] generateData(int n) {
                    // Generate or receive data here based on "n"
                    // Example: Generate random integers between -100 and 100
                    int[] data = new int[n];
                    for (int i = 0; i < n; i++) {
                        data[i] = (int) (Math.random() * 201) - 100;
                    }
                    return data;
                }
            }

            // Define the delta for double comparisons
            private static final double DELTA = 0.001;


            public void runTest() {
                final BigOIt2Base it = new BigOIt2();
                final String bigOMeasurable = Mystery.class.getName();
                final double actual = it.doublingRatio(bigOMeasurable, 40000);
                final double expected = 1.0;

                System.out.println("Doubling ratio for class " + bigOMeasurable + " is " + actual);

                assertEquals(expected, actual, DELTA, "Mismatch on expected doubling ratio");
            }
        }
    }
}

*/
