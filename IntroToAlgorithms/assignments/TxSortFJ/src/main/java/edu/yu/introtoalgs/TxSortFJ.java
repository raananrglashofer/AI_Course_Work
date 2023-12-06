package edu.yu.introtoalgs;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.RecursiveTask;
import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

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
        List<TxBase> sortedTransactions = ForkJoinPool.commonPool().invoke(new SortTask(transactions));
        return sortedTransactions.toArray(new TxBase[0]);
    }
    private static class SortTask extends RecursiveTask<List<TxBase>> {
        private final List<TxBase> transactions;

        public SortTask(List<TxBase> transactions) {
            this.transactions = transactions;
        }

        @Override
        protected List<TxBase> compute() {
            if (transactions.size() <= 1){
                return transactions;
            }

            int middle = transactions.size() / 2;
            SortTask leftTask = new SortTask(transactions.subList(0, middle));
            SortTask rightTask = new SortTask(transactions.subList(middle, transactions.size()));

            invokeAll(leftTask, rightTask);

            List<TxBase> leftResult = leftTask.join();
            List<TxBase> rightResult = rightTask.join();

            return merge(leftResult, rightResult);
        }

        private List<TxBase> merge(List<TxBase> left, List<TxBase> right) {
            List<TxBase> merged = new ArrayList<>(left.size() + right.size());

            int i = 0;
            int j = 0;
            while (i < left.size() && j < right.size()) {
                LocalDateTime time1;
                if (left.get(i) == null || left.get(i).time() == null) {
                    time1 = LocalDateTime.MIN;
                } else {
                    time1 = left.get(i).time();
                }

                LocalDateTime time2;
                if (right.get(j) == null || right.get(j).time() == null) {
                    time2 = LocalDateTime.MIN;
                } else {
                    time2 = right.get(j).time();
                }
                if (time1.compareTo(time2) <= 0) {
                    merged.add(left.get(i++));
                } else {
                    merged.add(right.get(j++));
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
