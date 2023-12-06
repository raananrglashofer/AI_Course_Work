import edu.yu.introtoalgs.Tx;
import edu.yu.introtoalgs.TxBase;
import edu.yu.introtoalgs.TxSortFJ;
import edu.yu.introtoalgs.TxSortFJBase;
import edu.yu.introtoalgs.Account;
import org.junit.Test;
import java.util.Random;
import org.testng.asserts.SoftAssert;
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
    // threshold right now is 10000 then gets wonky
    public void checkIfSortedSimple(){
        List<TxBase> transactions = new ArrayList<>();
        // create transactions
        for(int i = 0; i < 3; i++){
            Account sender = new Account();
            Account receiver = new Account();
            Tx tx = new Tx(sender, receiver, i);
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
            Assert.assertEquals(copy[k], hopefullySorted[k]);
        }
    }

    @Test
    public void checkIfFasterThanJDKSmall(){
        List<TxBase> transactions = new ArrayList<>();
        // create transactions
        for(int i = 0; i < 3; i++){
            Account sender = new Account();
            Account receiver = new Account();
            Tx tx = new Tx(sender, receiver, i);
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
        double FJTime = JDKTimer.elapsedTime();

        System.out.println(FJTime);
        System.out.println(JDKTime);

        // check that FJ sort is faster than mergeSort
        Assert.assertTrue(FJTime < JDKTime);
    }
//    @Test
//    public void doesItSort(){
//        final SoftAssert softAssert = new SoftAssert();
//        final int nAccounts = 2 ;
//        final int nTxs = 5 ;
//        final List <TxBase> txs = new ArrayList <>() ;
//        final Account [ ] accounts = new Account [ nAccounts ] ;
//        for ( int i =0; i<nAccounts ; i ++) {
//            accounts [ i ] = new Account ( ) ;
//        }
//        for ( int i =0; i<nTxs ; i ++) {
//// being silly here: no point in making this look more real
//            Random random = new Random();
//            final Account account1 = accounts [random.nextInt (0 , nAccounts ) ] ;
//            final Account account2 = accounts [random.nextInt (0 , nAccounts ) ] ;
//            txs.add ( new Tx ( account1 , account2 , 1 ) ) ;
//        }
//        Collections.shuffle(txs) ;
//        try {
//            final TxSortFJBase txSortFJ = new TxSortFJ ( txs ) ;
//            final TxBase [ ] fjTxs = txSortFJ.sort ( ) ;
//            final boolean isSorted = isSorted(fjTxs) ;
//            softAssert.assertTrue( isSorted ,
//                    "*** Txs should have been (but are not) sorted" )
//            ;
//        }
//        catch ( Exception e ) {
//            final String msg = "Unexpected exception running test: " ;
//            softAssert.fail(msg+e.toString( ) ) ;
//        }
//        finally {
//            softAssert.assertAll( "demo" ) ;
//        }
//    }
}
