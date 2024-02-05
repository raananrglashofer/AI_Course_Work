package edu.yu.da;

import java.util.*;

// https://algs4.cs.princeton.edu/43mst/EdgeWeightedGraph.java
public class EdgeWeightedGraph {
    private static final String NEWLINE = System.getProperty("line.separator");
    private int V = 0;
    private int E = 0;
    private Bag<Edge>[] adj;
    private HashMap<String, Integer> vertexIndices;
    private HashSet<String> vertices = new HashSet<>();
    private HashSet<Edge> edges = new HashSet<>();

    /**
     * Initializes an empty edge-weighted graph with {@code V} vertices and 0 edges.
     *
     * @param  //V the number of vertices
     * @throws IllegalArgumentException if {@code V < 0}
     */
    public EdgeWeightedGraph(String v) {
        this.vertexIndices = new HashMap<>();
        this.vertexIndices.put(v, 0);
        this.vertices.add(v);
        this.adj = (Bag<Edge>[]) new Bag[1];
        this.V++; // start vertex in
    }

    public void addVertex(String vertex) {
        if (!vertexIndices.containsKey(vertex)) {
            vertexIndices.put(vertex, V);
            this.vertices.add(vertex);
            Bag<Edge>[] newAdj = (Bag<Edge>[]) new Bag[V+1]; // double check this + 1 --> it does make sense because V is zero indexed
            for (int v = 0; v < V; v++) {
                newAdj[v] = adj[v];
            }
            newAdj[V] = new Bag<Edge>(); // i don't think this line is neccesary
            adj = newAdj;
            V++; // increase count after vertex added
        }
    }
    // don't need to check if already have edge because I do it in addEdge() in ThereAndBackAgain class
    public void addEdge(Edge e) {
        String v = e.either();
        String w = e.other(v);
        validateVertex(v);
        validateVertex(w);
        if (adj[vertexIndices.get(v)] == null) {
            adj[vertexIndices.get(v)] = new Bag<Edge>();
        }
        if (adj[vertexIndices.get(w)] == null) {
            adj[vertexIndices.get(w)] = new Bag<Edge>();
        }
        adj[vertexIndices.get(v)].add(e);
        adj[vertexIndices.get(w)].add(e);
        E++;
        this.edges.add(e);
    }
    /**
     * Returns the number of vertices in this edge-weighted graph.
     *
     * @return the number of vertices in this edge-weighted graph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-weighted graph.
     *
     * @return the number of edges in this edge-weighted graph
     */
    public int E() {
        return E;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(String v) {
        if (v.length() < 0 || !vertexIndices.containsKey(v))
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Returns the edges incident on vertex {@code v}.
     *
     * @param  v the vertex
     * @return the edges incident on vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Edge> adj(String v) {
        validateVertex(v);
        return adj[vertexIndices.get(v)];
    }

    /**
     * Returns the degree of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the degree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int degree(String v) {
        validateVertex(v);
        return adj[vertexIndices.get(v)].size();
    }

    /**
     * Returns all edges in this edge-weighted graph.
     * To iterate over the edges in this edge-weighted graph, use foreach notation:
     * {@code for (Edge e : G.edges())}.
     *
     * @return all edges in this edge-weighted graph, as an iterable
     */
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (String vertex : vertexIndices.keySet()) {
            int selfLoops = 0;
            for (Edge e : adj(vertex)) {  // Use get method to obtain the index
                String otherVertex = e.other(vertex);
                if (vertex.compareTo(otherVertex) < 0) {
                    list.add(e);
                }
                // add only one copy of each self loop (self loops will be consecutive)
                else if (vertex.equals(otherVertex)) {
                    if (selfLoops % 2 == 0) list.add(e);
                    selfLoops++;
                }
            }
        }
        return list;
    }


    /**
     * Returns a string representation of the edge-weighted graph.
     * This method takes time proportional to <em>E</em> + <em>V</em>.
     *
     * @return the number of vertices <em>V</em>, followed by the number of edges <em>E</em>,
     *         followed by the <em>V</em> adjacency lists of edges
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

    public Set<String> getVertices(){
        return this.vertices;
    }

    public HashSet<Edge> getEdges(){
        return this.edges;
    }
}