package edu.yu.da;
import java.util.*;
public class WaterPressure extends WaterPressureBase{
    private final String initialInputPump; // probs best to make a pump class
    private String secondInputPump;
    private boolean minAmountCalled = false;
    private boolean secondPumpAdded = false;
    private Set<String> vertices = new HashSet<>();
    private HashMap<String, Vertex> vertexMap = new HashMap<>();
    private EdgeWeightedDigraph digraph;
    private double primFromFirst = -1.0;
    private double primFromSecond = -1.0;
    private boolean areAllTouched = false;
    private class Vertex{ // I will figure out where to implement this guy
        private String v;
        private boolean touched; // has the vertex been touched when running minAmount() on the graph at either call
        public Vertex(String v){
            this.v = v;
            this.touched = false;
        }
    }

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
        this.vertices.add(initialInputPump);
        this.digraph = new EdgeWeightedDigraph(initialInputPump);
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
        if(secondInputPump.length() < 1 || this.initialInputPump.equals(secondInputPump) || !this.vertices.contains(secondInputPump)){
            throw new IllegalArgumentException();
        }
        if(this.secondPumpAdded){
            throw new IllegalStateException();
        }
        this.secondInputPump = secondInputPump;
        this.secondPumpAdded = true;
        this.minAmountCalled = false;
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
    // this method is the same as last assignment
    // so once grades are out make sure no mistakes were made here
    @Override
    public void addBlockage(String v, String w, double blockage) {
        if(this.minAmountCalled){
            throw new IllegalStateException();
        }
        if(v.length() < 1 || w.length() < 1 || blockage <= 0 || v.equals(w)){
            throw new IllegalArgumentException();
        }
        DirectedEdge edge = new DirectedEdge(v, w, blockage);
        if(this.digraph.E() > 1){
            for(DirectedEdge e : this.digraph.edges()){ // need to do null checks in this method in EdgeWeightedDGraph class or here
                if(e.equals(edge)){ //
                    throw new IllegalArgumentException(); // edge already exists
                }
            }
        }
        this.vertices.add(v); // set will add if not in it
        this.vertices.add(w);
        this.digraph.addVertex(v); // if already there then not go through method
        this.digraph.addVertex(w);
        this.digraph.addEdge(edge);

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

    // not returning total cost of path
    // really going to return largest edge weight
    @Override
    public double minAmount() {
        if(this.minAmountCalled){
            throw new IllegalStateException(); // double check this
        }
        this.minAmountCalled = true;
        // no channels in the system
        if(this.digraph.getEdges().size() == 0){  //ask what should be  --> check piazza
            return 0.0;
        }
        // one channel in the system, but both pumps are input pumps
        if(this.vertices.size() == 2 && secondPumpAdded){
            return 0.0;
        }
        double minAmount;
        if(!secondPumpAdded){
            prim(this.digraph, this.initialInputPump);
            minAmount = this.primFromFirst;
        } else{
            prim(this.digraph, this.secondInputPump);
            if(areAllTouched && this.primFromFirst < this.primFromSecond){ //|| this.primFromSecond == -1.0)){
                minAmount = this.primFromFirst;
            } else{
                minAmount = this.primFromSecond;
            }
        }
        allVerticesTouched();
        if(!areAllTouched){
            minAmount = -1.0;
        }
        return minAmount;
    }

    private void prim(EdgeWeightedDigraph digraph, String startVertex){
        PriorityQueue<DirectedEdge> minHeap = new PriorityQueue<>(Comparator.comparingDouble(DirectedEdge::weight));
        Set<String> mstVertices = new HashSet<>();
        double largestEdge = -1.0;

        // my only issue with this is that it will overwrite the vertices when secondInput is called
        // probably best to move it to add blockage
        // this check might provide a solution to the above problem
        if(!this.secondPumpAdded) {
            for (String vertex : digraph.getVertices()) {
                this.vertexMap.put(vertex, new Vertex(vertex)); // none of them are touched now
            }
        }
        if(secondPumpAdded){
            mstVertices.add(this.initialInputPump);
            mstVertices.add(startVertex);
            this.vertexMap.get(this.initialInputPump).touched = true;
            this.vertexMap.get(startVertex).touched = true;
        } else {
            // add initial input pump
            mstVertices.add(startVertex);
            this.vertexMap.get(startVertex).touched = true;
        }

        while(mstVertices.size() < digraph.getVertices().size()){
            for(String vertex : mstVertices){
                Iterable<DirectedEdge> neighbors = digraph.adj(vertex); // null check please
                for(DirectedEdge edge : neighbors){
                    if(!mstVertices.contains(edge.to())){
                        minHeap.offer(edge);
                    }
                }
            }

            if(!minHeap.isEmpty()){
                DirectedEdge minEdge = minHeap.poll();
                String toVertex = minEdge.to();

                mstVertices.add(toVertex);
                largestEdge = Math.max(largestEdge, minEdge.weight());
                    this.vertexMap.get(toVertex).touched = true;
            } else{
                // graph is not connected
                break;
            }
        }
        if(!secondPumpAdded){
            this.primFromFirst = largestEdge;
        } else{
            this.primFromSecond = largestEdge;
        }
    }

    private void allVerticesTouched(){
         this.areAllTouched = true;
        for(Vertex v : vertexMap.values()){
            if(!v.touched){
                this.areAllTouched = false;
            }
        }
    }
}
