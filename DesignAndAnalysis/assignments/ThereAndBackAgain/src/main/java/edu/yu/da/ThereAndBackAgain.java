package edu.yu.da;

import java.util.*;

public class ThereAndBackAgain extends ThereAndBackAgainBase {
    private final String startVertex;
    private String goalVertex = null;
    private double goalCost = 0.0;
    private List<String> oneLargestPath = new ArrayList<>(); // this means it will be null correct?
    private List<String> otherLargestPath = new ArrayList<>();
    private boolean doItCalled = false;
    private EdgeWeightedGraph graph; // go back to private after testing
    private HashMap<String, Double> distances;

    private class VertexInfo {
        String vertex;
        double distance;
        List<String> path;

        public VertexInfo(String vertex, double distance, List<String> path) {
            this.vertex = vertex;
            this.distance = distance;
            this.path = path;
        }
    }

    /**
     * Constructor which supplies the start vertex
     *
     * @param startVertex, length must be > 0.
     * @throws IllegalArgumentException if the pre-condiitions are
     *                                  violated
     */
    public ThereAndBackAgain(String startVertex) {
        super(startVertex);
        if (startVertex.length() < 1) {
            throw new IllegalArgumentException();
        }
        this.startVertex = startVertex;
        this.graph = new EdgeWeightedGraph(startVertex); // this adds the startVertex for all intents and purposes
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
        if(v.equals(w)){
            return; // they are the same edges
        }
        Edge edge = new Edge(v, w, weight);
        if(this.graph.getEdges().size() > 1){
            for(Edge e : this.graph.edges()){
                if(e.equals(edge)){ //
                    return; // edge already exists
                }
            }
        }
        this.graph.addVertex(v); // if already there then not go through method
        this.graph.addVertex(w);
        this.graph.addEdge(edge);
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
    // dijkstra baby
    @Override
    public void doIt() {
        if(this.doItCalled){
            throw new IllegalStateException();
        }
        this.doItCalled = true;
        if(this.graph.getEdges().size() == 0){
            return;
        }
        Map<String,List<List<String>>> shortestPaths = dijkstra(this.graph, this.startVertex);
        // take map info and iterate over to set all the getters or see if no valid path
        // Also needs to check if fits requirements for valid path

        // filtering it to only be valid paths
        List<String> validPaths = new ArrayList<>();
        for(Map.Entry<String, List<List<String>>> entry : shortestPaths.entrySet()){
            if(isValidPath(entry)){
                validPaths.add(entry.getKey());
            }
        }
        // if no valid paths just end it
        if(validPaths.size() == 0){
            return;
        }
        // determine the largest path
        double maxDistance = 0;
        String goalVertex = null;
        for(String vertex : validPaths){
            if(this.distances.get(vertex) > maxDistance){
                maxDistance = this.distances.get(vertex);
                goalVertex = vertex;
            }
        }
        this.goalVertex = goalVertex;
        this.goalCost = maxDistance;
        this.oneLargestPath = shortestPaths.get(goalVertex).get(0);
        this.otherLargestPath = shortestPaths.get(goalVertex).get(1);

    }

    private boolean isValidPath(Map.Entry<String, List<List<String>>> entry){
        boolean isValid = false;
        if(entry.getValue().size() > 1){ // can potentially go there and back
            List<String> pathOne = entry.getValue().get(0);
            List<String> pathTwo = entry.getValue().get(1);
            // this isn't perfect because not checking if there are more than 2 paths and potentially 3rd is different
            // but I think multiple paths shouldn't get here then
            if(!pathOne.equals(pathTwo)){ // paths are not the same
                if(pathOne.size() == pathTwo.size()){
                    isValid = true;
                }
            }
        }
        return isValid;
    }

    private Map<String,List<List<String>>> dijkstra(EdgeWeightedGraph graph, String sourceVertex){
        this.distances = new HashMap<>();
        Map<String, List<List<String>>> paths = new HashMap<>();
        PriorityQueue<VertexInfo> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(v -> v.distance));

        for (String vertex : graph.getVertices()) {
            distances.put(vertex, Double.POSITIVE_INFINITY);
            paths.put(vertex, new ArrayList<>());
        }

        distances.put(startVertex, 0.0);
        priorityQueue.add(new VertexInfo(startVertex, 0.0, new ArrayList<>()));

        while(!priorityQueue.isEmpty()) {
            VertexInfo currentVertexInfo = priorityQueue.poll();
            String u = currentVertexInfo.vertex;
            double currentDistance = currentVertexInfo.distance;
            List<String> currentPath = currentVertexInfo.path;

            for (Edge edge : graph.adj(u)) { // double check this loop
                String v = edge.other(u);
                double edgeWeight = edge.weight();
                double newDistance = currentDistance + edgeWeight;

                if (newDistance == distances.get(v)) {
                    // Add the path from u to v to the list of paths for v
                    List<String> newPath = new ArrayList<>(currentPath);
                    if(newPath.size() == 0) {
                        newPath.add(u);
                    }
                    newPath.add(v);
                    paths.get(v).add(newPath);
                } else if (newDistance < distances.get(v)) {
                    // Update distance, clear the list of paths, and add the new path
                    distances.put(v, newDistance);
                    List<String> newPath = new ArrayList<>(currentPath);
                    if(newPath.size() == 0) {
                        newPath.add(u);
                    }
                    newPath.add(v);
                    paths.put(v, new ArrayList<>(Collections.singletonList(newPath)));
                    priorityQueue.add(new VertexInfo(v, newDistance, newPath));
                }
            }
        }
        return paths;
    }

    /** If the graph contains a "goal vertex of the longest valid path" (as
     * defined by the requirements document), returns it.  Else returns null.
     *
     * @return goal vertex of the longest valid path if one exists, null
     * otherwise.
     */
    @Override
    public String goalVertex() {
        if(!this.doItCalled){
            throw new IllegalStateException();
        }
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
        if(!this.doItCalled){
            throw new IllegalStateException();
        }
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
        if(!this.doItCalled){
            throw new IllegalStateException();
        }
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
        if(!this.doItCalled){
            throw new IllegalStateException();
        }
        if(this.otherLargestPath == null){
            return Collections.emptyList();
        }
        return this.otherLargestPath;
    }
}
