package edu.yu.da;
// https://algs4.cs.princeton.edu/44sp/DirectedEdge.java.html

public class DirectedEdge {
    private final String v;
    private final String w;
    private final double weight;

    /**
     * Initializes a directed edge from vertex {@code v} to vertex {@code w} with
     * the given {@code weight}.
     *
     * @param v      the tail vertex
     * @param w      the head vertex
     * @param weight the weight of the directed edge
     * @throws IllegalArgumentException if either {@code v} or {@code w}
     *                                  is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public DirectedEdge(String v, String w, double weight) {
        if (v.length() < 1) throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (w.length() < 1) throw new IllegalArgumentException("Vertex names must be non-negative integers");
        if (Double.isNaN(weight)) throw new IllegalArgumentException("Weight is NaN");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /**
     * Returns the tail vertex of the directed edge.
     *
     * @return the tail vertex of the directed edge
     */
    public String from() {
        return v;
    }

    /**
     * Returns the head vertex of the directed edge.
     *
     * @return the head vertex of the directed edge
     */
    public String to() {
        return w;
    }

    /**
     * Returns the weight of the directed edge.
     *
     * @return the weight of the directed edge
     */
    public double weight() {
        return weight;
    }

    /**
     * Returns a string representation of the directed edge.
     *
     * @return a string representation of the directed edge
     */
    public String toString() {
        return v + "->" + w + " " + String.format("%5.2f", weight);
    }
}
