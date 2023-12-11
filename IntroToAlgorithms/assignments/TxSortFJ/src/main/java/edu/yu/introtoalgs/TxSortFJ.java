package edu.yu.introtoalgs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.*;

public class TxSortFJ extends TxSortFJBase{
    private List<TxBase> transactions;

    /** Constructor.
     *
     * @param transactions a list of transactions, possibly not sorted.
     */
    public TxSortFJ(List<TxBase> transactions) {
        super(transactions);
        if(transactions == null){
            throw new IllegalArgumentException();
        }
        this.transactions = transactions;
    }

    /** Returns an array of transactions, sorted in ascending order of
     * TxBase.time() values: any instances with null TxBase.time() values precede
     * all other transaction instances in the sort results.
     *
     * @return the transaction instances passed to the constructor, returned as
     * an array, and sorted as specified above.  Students MAY ONLY use the
     * ForkJoin and their own code in their implementation.
     */
    @Override
    public TxBase[] sort(){
        int parallelism = Runtime.getRuntime().availableProcessors();
        int threshold = transactions.size() / parallelism;
        SortTask task = new SortTask(threshold, transactions, 0, transactions.size());
        ForkJoinPool FJPool = new ForkJoinPool(parallelism);
        List<TxBase> sortedTransactions = FJPool.invoke(task);
        FJPool.shutdown();
        return sortedTransactions.toArray(new TxBase[0]);
    }
    private static class SortTask extends RecursiveTask<List<TxBase>> {
        private final List<TxBase> transactions;
        private final int threshold;
        private final int low;
        private final int high;

        public SortTask(int threshold, List<TxBase> transactions, int low, int high) {
            if(threshold < 0 || low < 0 || high < 0){
                throw new IllegalArgumentException();
            }
            this.transactions = transactions;
            this.threshold = threshold;
            this.low = low;
            this.high = high;
        }

        @Override
        protected List<TxBase> compute() {
            if(high - low <= threshold){
               Collections.sort(transactions);
                return transactions;
            }

            int middle = this.low + (this.high - this.low) / 2;
            List<TxBase> left = transactions.subList(0, middle);
            List<TxBase> right = transactions.subList(middle, transactions.size());
            SortTask leftTask = new SortTask(this.threshold, left, 0, left.size());
            SortTask rightTask = new SortTask(this.threshold, right, 0, right.size());
            leftTask.fork();
            List<TxBase> rightResult = rightTask.compute();
            List<TxBase> leftResult = leftTask.join();

           return merge(leftResult, rightResult);
        }

        private List<TxBase> merge(List<TxBase> left, List<TxBase> right) {
            List<TxBase> merged = new ArrayList<>(left.size() + right.size());

            int i = 0;
            int j = 0;
            while (i < left.size() && j < right.size()) {
                TxBase time1 = left.get(i);
                TxBase time2 = right.get(j);
                if (time1.compareTo(time2) <= 0) {
                    merged.add(time1);
                    i++;
                } else {
                    merged.add(time2);
                    j++;
                }
            }

            while (i < left.size()) {
                merged.add(left.get(i++));
            }

            while (j < right.size()) {
                merged.add(right.get(j++));
            }

            return merged;
        }
    }
}
