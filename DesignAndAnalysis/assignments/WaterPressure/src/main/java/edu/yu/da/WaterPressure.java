package edu.yu.da;
import java.util.*;
public class WaterPressure extends WaterPressureBase{
    private String initialInputPump; // probs best to make a pump class
    private String secondInputPump;
    private boolean minAmountCalled = false;
    //private Set<Channel> channels = new HashSet<>();
    private Set<String> vertices = new HashSet<>();
    private Set<EdgeWeightedDigraph> digraph;
//    private class Channel{
//        private String from;
//        private String to;
//        private double weight;
//        private Channel(String from, String to, double weight){
//            this.from = from;
//            this.to = to;
//            this.weight = weight;
//        }
//    }

    /** Constructor which supplies the initial input pump.
     *
     //* @param initialStartPump, length must be greater than 0.
     * @throws IllegalArgumentException if the pre-conditions are violated.
     */
    public WaterPressure(String initialInputPump){
        super(initialInputPump);
        if(initialInputPump.length() < 1){
            throw new IllegalArgumentException();
        }
        this.initialInputPump = initialInputPump;
       // this.digraph = new EdgeWeightedDigraph(initialInputPump);
    }

    /** Adds a second input pump, differing from the initial input pump, to the
     * channel system.
     *
     * The second input pump must already be in the channel system (via
     * addBlockage): this method only designates the pump as also being an input
     * pump.
     *
     * @param secondInputPump, length must be greater than 0.
     * @throws IllegalArgumentException if the pre-conditions are violated.
     */
    @Override
    public void addSecondInputPump(String secondInputPump) {
        if(secondInputPump.length() < 1){
            throw new IllegalArgumentException();
        }
        this.secondInputPump = secondInputPump;
    }

    /** Specifies a blockage on a channel running from pump station v to pump
     * station w.  The presence of a blockage implies that water can only flow on
     * the channel if a quantity of water greater or equal to "blockage" is
     * pumped by pump station v to w.
     *
     * The two pump stations must differ from one another, and no channel can
     * already exist between the two pump stations.
     *
     * @param v specifies a pump station, length must be > 0.
     * @param w specifies a pump station, length must be > 0.
     * @param blockage the magnitude of the blockage on the channel, must be > 0.
     * @throws IllegalStateException if minAmount() has previously been invoked.
     * @throws IllegalArgumentException if the other pre-conditions are violated.
     */
    @Override
    public void addBlockage(String v, String w, double blockage) {
        // still need to check if channel already exists
        if(v.length() < 1 || w.length() < 1 || blockage <= 0 || v.equals(w)){
            throw new IllegalArgumentException();
        }
        if(this.minAmountCalled){
            throw new IllegalStateException();
        }

    }

    /** Client asks implementation to determine the minimum amount of water that
     * must be supplied to the initial input pump to ensure that water reaches
     * every pump station in the existing channel system.  If a second input pump
     * has been added to the channel system, the sematics of "minimum amount" is
     * the "minimum amount of water that must be supplied to BOTH input pump
     * stations".
     *
     * @return the minimum amount of water that must be supplied to the input
     * pump(s) to ensure that water reaches every pump station.  If the channel
     * system has been misconfigured such that no amount of water pumped from the
     * input pump stations can get water to all the pump stations, returns -1.0
     * as as sentinel value.
     */
    @Override
    public double minAmount() {
        return -1.0;
    }
}
