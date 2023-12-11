package edu.yu.introtoalgs;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

public class Tx extends TxBase {
    private Account sender;
    private Account receiver;
    private int amount;
    private LocalDateTime time;
    private long id;
    private static final AtomicLong counter = new AtomicLong(1); // make sure this is allowed and correct

    /**
     * Constructor.
     *
     * @param sender   non-null initiator of the transaction
     * @param receiver non-null recipient
     * @param amount   positive-integer-valued amount transfered in the
     *                 transaction.
     */
    public Tx(Account sender, Account receiver, int amount) {
        super(sender, receiver, amount);
        if (amount < 1) {
            throw new IllegalArgumentException();
        }
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        LocalDateTime time = LocalDateTime.now();
        this.time = time;
        if(this.sender() == null || this.receiver() == null){
            throw new IllegalArgumentException();
        }
        this.id = counter.getAndIncrement();
    }

    @Override
    public Account receiver() {
        return this.receiver;
    }

    @Override
    public Account sender() {
        return this.sender;
    }

    @Override
    public int amount() {
        return this.amount;
    }

    /**
     * Returns a unique non-negative identifier.
     */
    @Override
    public long id() {
        return this.id;
    }

    /**
     * Returns the time that the Tx was created or null.
     */
    @Override
    public LocalDateTime time() {
        return this.time;
    }

    @Override
    public void setTimeToNull() {
        this.time = null;
    }

    @Override
    public String toString() {
        return "Tx{" +
                "sender=" + sender() +
                ", receiver=" + receiver() +
                ", amount=" + amount() +
                ", id=" + id() +
                ", time=" + time() +
                '}';
    }
 // double check if this works and if parameter can be TxBase
    // might need to use the comparator import
    @Override
    public int compareTo(TxBase other) {
        if (this.time == null && other.time() == null) {
            return 0; // Both are considered equal
        }

        if (this.time == null) {
            return -1; // This instance is considered "worse" than a non-null other
        }

        if (other.time() == null) {
            return 1; // This instance is considered "better" than a null other
        }
        int temp = this.time.compareTo(other.time());
        if(temp != 0){
            return temp;
        }else{
            if(this.id() < other.id()){
                return -1;
            } else{
                return 1;
            }
        }
    }
}

