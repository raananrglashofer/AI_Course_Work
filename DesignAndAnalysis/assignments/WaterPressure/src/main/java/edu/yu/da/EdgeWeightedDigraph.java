package edu.yu.da;

import java.util.HashMap;
import java.util.HashSet;
import java.util.*;
// https://algs4.cs.princeton.edu/44sp/EdgeWeightedDigraph.java.html
public class EdgeWeightedDigraph {
    // going to have to make similar changes to this API like I did for ThereAndBackAgain
    private static final String NEWLINE = System.getProperty("line.separator");

    private int V = 0;  // number of vertices in this digraph
    private int E = 0;  // number of edges in this digraph
    private Bag<DirectedEdge>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v
    private HashMap<String, Integer> vertexIndices = new HashMap<>();
    private HashSet<String> vertices = new HashSet<>();
    private HashSet<DirectedEdge> edges = new HashSet<>();

    /**
     * Initializes an empty edge-weighted digraph with {@code V} vertices and 0 edges.
     *
     * //@param V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedDigraph(String v) {
        if (v.length() < 0){
            throw new IllegalArgumentException("Number of vertices in a Digraph must be non-negative");
        }
        this.vertexIndices.put(v, 0);
        this.vertices.add(v);
        this.indegree = new int[1];
        this.adj = (Bag<DirectedEdge>[]) new Bag[1];
        this.V++;
    }

    public void addVertex(String vertex) {
        if (!vertexIndices.containsKey(vertex)) {
            vertexIndices.put(vertex, V);
            this.vertices.add(vertex);
            Bag<DirectedEdge>[] newAdj = (Bag<DirectedEdge>[]) new Bag[V+1];
            int[] newIndegree = new int[V+1];
            for (int v = 0; v < V; v++) {
                indegree[v] = newIndegree[v];
                newAdj[v] = adj[v];
            }
            newAdj[V] = new Bag<DirectedEdge>(); // i don't think this line is neccesary
            adj = newAdj;
            this.indegree = newIndegree; // really got to double check this indegree[]
            V++; // increase count after vertex added
        }
    }

    /**
     * Returns the number of vertices in this edge-weighted digraph.
     *
     * @return the number of vertices in this edge-weighted digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted digraph.
     *
     * @return the number of edges in this edge-weighted digraph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(String v) {
        if (v.length() < 0 || !vertexIndices.containsKey(v)) {
            throw new IllegalArgumentException("Invalid Vertex");
        }
    }

    /**
     * Adds the directed edge {@code e} to this edge-weighted digraph.
     *
     * @param e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *                                  and {@code V-1}
     */
    public void addEdge(DirectedEdge e) {
        String v = e.from();
        String w = e.to();
        validateVertex(v);
        validateVertex(w);
        if (adj[vertexIndices.get(v)] == null) {
            adj[vertexIndices.get(v)] = new Bag<DirectedEdge>();
        }
        adj[vertexIndices.get(v)].add(e);
        indegree[vertexIndices.get(w)]++;
        E++;
        this.edges.add(e);
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<DirectedEdge> adj(String v) {
        validateVertex(v);
        if(adj[vertexIndices.get(v)] == null){
            adj[vertexIndices.get(v)] = new Bag<DirectedEdge>();
        }
        return adj[vertexIndices.get(v)];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(String v) {
        validateVertex(v);
        if(adj[vertexIndices.get(v)] == null){
            adj[vertexIndices.get(v)] = new Bag<DirectedEdge>();
        }
        return adj[vertexIndices.get(v)].size();
    }

    /**
     * Returns the number of directed edges incident to vertex {@code v}.
     * This is known as the <em>indegree</em> of vertex {@code v}.
     *
     * @param v the vertex
     * @return the indegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int indegree(String v) {
        validateVertex(v);
        if(adj[vertexIndices.get(v)] == null){
            adj[vertexIndices.get(v)] = new Bag<DirectedEdge>();
        }
        return indegree[vertexIndices.get(v)];
    }

    /**
     * Returns all directed edges in this edge-weighted digraph.
     * To iterate over the edges in this edge-weighted digraph, use foreach notation:
     * {@code for (DirectedEdge e : G.edges())}.
     *
     * @return all edges in this edge-weighted digraph, as an iterable
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> list = new Bag<DirectedEdge>();
        for (String vertex : vertexIndices.keySet()) {
            for (DirectedEdge e : adj(vertex)) { // needs to be reviewed
                list.add(e);
            }
        }
        return list;
    }

    /**
     * Returns a string representation of this edge-weighted digraph.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     * followed by the <em>V</em> adjacency lists of edges
     */
//    public String toString() {
//        StringBuilder s = new StringBuilder();
//        s.append(V + " " + E + NEWLINE);
//        for (int v = 0; v < V; v++) {
//            s.append(v + ": ");
//            for (DirectedEdge e : adj[v]) {
//                s.append(e + "  ");
//            }
//            s.append(NEWLINE);
//        }
//        return s.toString();
//    }

    public Set<String> getVertices(){
        return this.vertices;
    }

    public Set<DirectedEdge> getEdges(){
        return this.edges;
    }

    public int getVertexIndex(String vertex){
        return this.vertexIndices.get(vertex);
    }

}
