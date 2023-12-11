import edu.yu.introtoalgs.Tx;
import edu.yu.introtoalgs.TxBase;
import edu.yu.introtoalgs.TxSortFJ;
import edu.yu.introtoalgs.TxSortFJBase;
import edu.yu.introtoalgs.Account;
import org.junit.Test;
import org.junit.Assert;

import java.time.LocalDateTime;
import java.util.*;
import static org.junit.Assert.assertThrows;
public class SortTest {
    private boolean isSorted(TxBase[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i].compareTo(arr[i + 1]) == -1) {
                return false;
            }
        }
        return true;
    }
    private static class Stopwatch {
        private final long start;
        private Stopwatch() {
            start = System.nanoTime();
        }
        private double elapsedTime() {
            long now = System.nanoTime();
            return (now - start) / 1000000.0;
        }
    }
    @Test
    // threshold right now is 1000 then gets wonky
    public void checkIfSortedSimple(){
        List<TxBase> transactions = new ArrayList<>();
        // create transactions
        Account nullSender = new Account();
        Account nullReceiver = new Account();
        Tx txNull = new Tx(nullSender, nullReceiver, 10);
        txNull.setTimeToNull();
        transactions.add(txNull);
        for(int i = 0; i < 10000; i++){
            Account sender = new Account();
            Account receiver = new Account();
            Tx tx = new Tx(sender, receiver, i+1);
            transactions.add(tx);
        }
        // create a copy to compare against
        TxBase[] copy = new TxBase[transactions.size()];
        int j = 0;
        for(TxBase tx : transactions){
            copy[j] = tx;
            j++;
        }
        // rearrange transactions
        Collections.shuffle(transactions);

        // sort using FJ
        TxSortFJ fj = new TxSortFJ(transactions);
        TxBase[] hopefullySorted = fj.sort();

        // check that it's sorted
        for(int k = 0; k < transactions.size(); k++){
            if(copy[k] != hopefullySorted[k]){
                System.out.println(copy[k]);
                System.out.println(hopefullySorted[k]);
            }
            Assert.assertEquals(copy[k], hopefullySorted[k]);
        }
    }

    @Test
    public void checkIfFasterThanJDKNineMil(){
        List<TxBase> transactions = new ArrayList<>();
        // create transactions
        for(int i = 0; i < 9000000; i++){
            Account sender = new Account();
            Account receiver = new Account();
            Tx tx = new Tx(sender, receiver, i+1);
            transactions.add(tx);
        }
        // rearrange transactions
        Collections.shuffle(transactions);

        // create a copy of the rearranged transactions
        TxBase[] copy = new TxBase[transactions.size()];
        int j = 0;
        for(TxBase tx : transactions){
            copy[j] = tx;
            j++;
        }
        // time JDK sort (mergeSort)
        Stopwatch JDKTimer = new Stopwatch();
        Arrays.sort(copy);
        double JDKTime = JDKTimer.elapsedTime();

        TxSortFJ fj = new TxSortFJ(transactions);

        // time FJ sort
        Stopwatch fjTimer = new Stopwatch();
        TxBase[] hopefullySorted = fj.sort();
        double FJTime = fjTimer.elapsedTime();

        System.out.println("FJ Time: " + FJTime);
        System.out.println("JDK Time: " + JDKTime);

        // check that FJ sort is faster than mergeSort
        Assert.assertTrue(FJTime < JDKTime);
    }
    @Test
    public void isMineFasterThanArraysParallelSort(){
        List<TxBase> transactions = new ArrayList<>();
        // create transactions
        for(int i = 0; i < 9000000; i++){
            Account sender = new Account();
            Account receiver = new Account();
            Tx tx = new Tx(sender, receiver, i+1);
            transactions.add(tx);
        }
        // rearrange transactions
        Collections.shuffle(transactions);

        // create a copy of the rearranged transactions
        TxBase[] copy = new TxBase[transactions.size()];
        int j = 0;
        for(TxBase tx : transactions){
            copy[j] = tx;
            j++;
        }
        // time Parallel sort
        Stopwatch ParallelTimer = new Stopwatch();
        Arrays.parallelSort(copy);
        double ParallelTime = ParallelTimer.elapsedTime();

        TxSortFJ fj = new TxSortFJ(transactions);

        // time FJ sort
        Stopwatch fjTimer = new Stopwatch();
        TxBase[] hopefullySorted = fj.sort();
        double FJTime = fjTimer.elapsedTime();

        System.out.println("FJ Time: " + FJTime);
        System.out.println("Parallel Time: " + ParallelTime);

        // check that FJ sort is faster than parallelSort
        Assert.assertTrue(FJTime < ParallelTime);
    }
}
