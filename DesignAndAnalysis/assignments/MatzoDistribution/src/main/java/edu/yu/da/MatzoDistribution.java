package edu.yu.da;

import java.util.*;

public class MatzoDistribution extends MatzoDistributionBase{
    private FlowNetwork flowNetwork;
    private String sourceWarehouse;
    private String destinationWarehouse;
    private int sourceConstraint;
    private Set<Warehouse> warehouses = new HashSet<>();
    private Set<FlowEdge> edges = new HashSet<>();
    private Set<String> warehouseIds = new HashSet<>();

    private class Warehouse{
        private String warehouseId;
        private int constraint;

        public Warehouse(String warehouseId, int constraint){
            this.warehouseId = warehouseId;
            this.constraint = constraint;
            warehouseIds.add(warehouseId);
        }

        @Override
        public boolean equals(Object obj) {
            // Check if the object is compared with itself
            if (this == obj) {
                return true;
            }

            // Check if the object is an instance of Warehouse
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }

            // Typecast obj to Warehouse so that we can compare data members
            Warehouse warehouse = (Warehouse) obj;

            // Compare the data members and return accordingly
            return warehouseId.equals(warehouse.warehouseId);
        }
    }

    /** Constructor: defines the two "endpoints" of the distribution network.
     *
     * @param sourceWarehouse names the warehouse that initiates the matzo
     * distribution, cannot be blank, must differ from destinationWarehouse.
     * @param sourceConstraint positive-valued-integer representing an upper
     * bound on the amount of matzo packages that can be distributed per day from
     * the source warehouse.
     * @param destinationWarehouse names the warehouse to which all matzos must
     * ultimately be delivered, cannot be blank, must differ from sourceWarehouse.
     * @throws IllegalArgumentException if the parameter pre-conditions are not
     * met.
     */
    public MatzoDistribution(String sourceWarehouse, int sourceConstraint, String destinationWarehouse){
        super(sourceWarehouse, sourceConstraint, destinationWarehouse);
        if(sourceWarehouse.isBlank() || destinationWarehouse.isBlank()|| sourceWarehouse.equals(destinationWarehouse) || sourceConstraint < 1){
            throw new IllegalArgumentException();
        }
        this.sourceWarehouse = sourceWarehouse;
        this.sourceConstraint = sourceConstraint;
        this.destinationWarehouse = destinationWarehouse;
        // create two warehouses - don't have constraint for destination warehouse so need to figure that out
        // going to add it to the warehouseId set
        Warehouse source = new Warehouse(sourceWarehouse, sourceConstraint);
        this.warehouses.add(source);
        this.warehouseIds.add(destinationWarehouse);
        this.flowNetwork = new FlowNetwork(sourceWarehouse);
        this.flowNetwork.addVertex(destinationWarehouse); // very debatable and need to get confirmation on this
    }

    /** Adds a warehouse to the distribution network.
     *
     * @param warehouseId uniquely identifies the warehouse, cannot previously
     * have been added to the network, cannot be "blank".
     * @param constraint positive-valued-integer representing an upper bound on
     * the amount of matzo packages that can be distributed per day from that
     * warehouse.
     * @throws IllegalArgumentException if the parameter pre-conditions are not
     * met.
     */
    @Override
    public void addWarehouse(String warehouseId, int constraint) {
        if(warehouseId.isBlank() || constraint < 1){
            throw new IllegalArgumentException();
        }
        if(this.warehouseIds.contains(warehouseId)){
            throw new IllegalArgumentException();
        }
        // might delete later
        Warehouse warehouse = new Warehouse(warehouseId, constraint);
        this.warehouses.add(warehouse);
    }


    /** Specify that a road exists from warehouse1 to warehouse2 with a
     * constraint on the capacity of the road.
     *
     * @param w1 warehouse 1, must have already been added to the distribution
     * network, different from w2, cannot be blank.
     * @param w2 warehouse 2, must have already been added to the distribution
     * network, different from w1, cannot be blank.
     * @param constraint positive-valued-integer, representing an upper bound on
     * the amount of matzo packages per day that can be distributed on this road.
     */
    @Override
    public void roadExists(String w1, String w2, int constraint) {
        if(constraint < 1 || w1.isBlank() || w2.isBlank() || w1.equals(w2) || !this.warehouseIds.contains(w1) || !this.warehouseIds.contains(w2)){
            throw new IllegalArgumentException();
        }
        // if road going into source
        // if road leaving destination
        if(w2.equals(sourceWarehouse) || w1.equals(destinationWarehouse)){
            throw new IllegalArgumentException();
        }

        FlowEdge edge = new FlowEdge(w1, w2, constraint);
        // double check if need to do this
        if(this.flowNetwork.E() > 0){
            for(FlowEdge e : this.flowNetwork.edges()){ // need to do null checks in this method in EdgeWeightedDGraph class or here
                if(e.to().equals(edge.to()) || e.from().equals(edge.from())){ //can't be two roads between same warehouses
                    throw new IllegalArgumentException(); // edge already exists
                }
            }
        }
        this.flowNetwork.addEdge(edge);
        this.edges.add(edge);
    }

    /** Returns the maximum amount of matzos per day that the source warehouse
     * can deliver to the destination warehouse.
     *
     * @return the maximum per-day amount of matzos that can be distributed given
     * the distribution network's constraints.
     */
    @Override
    public int max() {
        return 0;
    }
}
