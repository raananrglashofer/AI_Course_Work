package edu.yu.da;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.*;

public class ThereAndBackAgain extends ThereAndBackAgainBase{
    private String startVertex;
    private String goalVertex = null;
    private double goalCost = 0.0;
    private List<String> oneLargestPath = new ArrayList<>(); // this means it will be null correct?
    private List<String> otherLargestPath = new ArrayList<>();
    private boolean doItCalled = false;
    private EdgeWeightedGraph graph; // I think client would initialize it

    /** Constructor which supplies the start vertex
     *
     * @param startVertex, length must be > 0.
     * @throws IllegalArgumentException if the pre-condiitions are
     * violated
     */
    public ThereAndBackAgain(String startVertex){
        super(startVertex);
        if(startVertex.length() < 1){
            throw new IllegalArgumentException();
        }
        this.startVertex = startVertex;

    }
    // chatGPT
    private class EdgeWeightedGraph {

        private final int V; // Number of vertices
        private int E; // Number of edges
        private Map<Integer, List<Edge>> adjacencyList; // Adjacency list representation
        private Set<Edge> allEdges; // Set of all edges in the graph

        // Edge class representing a weighted edge
        private static class Edge { // can/should this be static
            private final String v; // Vertex on one end
            private final String w; // Vertex on the other end
            private final double weight; // Weight of the edge

            public Edge(String v, String w, double weight) {
                this.v = v;
                this.w = w;
                this.weight = weight;
            }

            @Override
            public String toString() {
                return String.format("%d-%d %.2f", v, w, weight);
            }
        }
        // no edges are added to the graph yet
        // Constructor
        public EdgeWeightedGraph(int V) {
            if (V < 0) throw new IllegalArgumentException("Number of vertices must be non-negative");
            this.V = V;
            this.E = 0;
            this.adjacencyList = new HashMap<>();
            this.allEdges = new HashSet<>();

            // Initialize adjacency list
            for (int v = 0; v < V; v++) {
                adjacencyList.put(v, new ArrayList<>());
            }
        }
    }



    /** Adds a weighted undirected edge between vertex v and vertex w.  The two
     * vertices must differ from one another, and an edge between the two
     * vertices cannot have been added previously.
     *
     * @param v specifies a vertex, length must be > 0.
     * @param w specifies a vertex, length must be > 0.
     * @param weight the edge's weight, must be > 0.
     * @throws IllegalStateException if doIt() has previously been invoked.
     * @throws IllegalArgumentException if the other pre-conditions are violated.
     */
    @Override
    public void addEdge(String v, String w, double weight) {
        if(this.doItCalled){
            throw new IllegalStateException();
        }
        if(v.length() < 1 || w.length() < 1 || weight < 0){
            throw new IllegalArgumentException();
        }
        // check if fits the requirements to be added as an edge
        EdgeWeightedGraph.Edge edge = new EdgeWeightedGraph.Edge(v, w, weight);
        if(!v.equals(w)){
            this.graph.allEdges.add(edge); // don't need to check if set has the edge because set.add() will do it for me
        }
    }

    /** Client informs implementation that the graph is fully constructed and
     * that the ThereAndBackAgainBase algorithm should be run on the graph.
     * After the method completes, the client is permitted to invoke the
     * solution's getters.
     *
     * Note: once invoked, the implementation must ignore subsequent calls to
     * this method.
     * @throws IllegalStateException if doIt() has previously been invoked.
     */

    // so this is basically a setter for the rest of the methods
    @Override
    public void doIt() {
        if(this.doItCalled){
            throw new IllegalStateException();
        }
        this.doItCalled = true;
    }

    /** If the graph contains a "goal vertex of the longest valid path" (as
     * defined by the requirements document), returns it.  Else returns null.
     *
     * @return goal vertex of the longest valid path if one exists, null
     * otherwise.
     */
    @Override
    public String goalVertex() {
        return this.goalVertex;
    }

    /** Returns the cost (sum of the edge weights) of the longest valid path if
     * one exists, 0.0 otherwise.
     *
     * @return the cost if the graph contains a longest valid path, 0.0
     * otherwise.
     */
    @Override
    public double goalCost() {
        return this.goalCost;
    }

    /** If a longest valid path exists, returns an ordered sequence of vertices
     * (beginning with the start vertex, and ending with the goal vertex)
     * representing that path.
     *
     * IMPORTANT: given the existence of (by definition) two longest valid paths,
     * this method returns the List with the LESSER of the two List.hashCode()
     * instances.
     *
     * @return one of the two longest paths, Collections.EMPTY_LIST if the graph
     * doesn't contain a longest valid path.
     */
    @Override
    public List<String> getOneLongestPath() {
        if(this.oneLargestPath == null){
            return Collections.emptyList();
        }
        return this.oneLargestPath;
    }

    /** If a longest valid path exists, returns the OTHER ordered sequence of
     * vertices (beginning with the start vertex, and ending with the goal
     * vertex) representing that path.
     *
     * IMPORTANT: given the existence of (by definition) two longest valid paths,
     * this method returns the List with the GREATER of the two List.hashCode()
     * instances.
     *
     * @return the other of the two longest paths, Collections.EMPTY_LIST if the
     * graph doesn't contain a longest valid path.
     */
    @Override
    public List<String> getOtherLongestPath() {
        if(this.otherLargestPath == null){
            return Collections.emptyList();
        }
        return this.otherLargestPath;
    }
}
