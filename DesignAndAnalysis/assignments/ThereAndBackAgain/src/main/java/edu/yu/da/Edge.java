package edu.yu.da;
// https://algs4.cs.princeton.edu/43mst/Edge.java.html
public class Edge implements Comparable<Edge> {

    private final String v;
    private final String w;
    private final double weight;

    /**
     * Initializes an edge between vertices {@code v} and {@code w} of
     * the given {@code weight}.
     *
     * @param v      one vertex
     * @param w      the other vertex
     * @param weight the weight of this edge
     * @throws IllegalArgumentException if either {@code v} or {@code w}
     *                                  is a negative integer
     * @throws IllegalArgumentException if {@code weight} is {@code NaN}
     */
    public Edge(String v, String w, double weight) {
        if (v.length() < 1) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (w.length() < 1) throw new IllegalArgumentException("vertex index must be a non-negative integer");
        if (weight < 0) throw new IllegalArgumentException("Weight is negative");
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /**
     * Returns the weight of this edge.
     *
     * @return the weight of this edge
     */
    public double weight() {
        return weight;
    }

    /**
     * Returns either endpoint of this edge.
     *
     * @return either endpoint of this edge
     */
    public String either() {
        return v;
    }

    /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param vertex one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     *                                  endpoints of this edge
     */
    public String other(String vertex) {
        if (vertex.equals(v)) return w;
        else if (vertex .equals(w)) return v;
        else throw new IllegalArgumentException("Illegal endpoint");
    }

    /**
     * Compares two edges by weight.
     * Note that {@code compareTo()} is not consistent with {@code equals()},
     * which uses the reference equality implementation inherited from {@code Object}.
     *
     * @param that the other edge
     * @return a negative integer, zero, or positive integer depending on whether
     * the weight of this is less than, equal to, or greater than the
     * argument edge
     */
    @Override
    public int compareTo(Edge that) {
        return Double.compare(this.weight, that.weight);
    }

    /**
     * Returns a string representation of this edge.
     *
     * @return a string representation of this edge
     */
    public String toString() {
        return String.format("%d-%d %.5f", v, w, weight);
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Edge otherEdge = (Edge) obj;
        return (v.equals(otherEdge.v) && w.equals(otherEdge.w) && weight == otherEdge.weight) ||
                (v.equals(otherEdge.w) && w.equals(otherEdge.v) && weight == otherEdge.weight);
    }

    @Override
    public int hashCode() {
        int result = v.hashCode() + w.hashCode();
        long temp = Double.doubleToLongBits(weight);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}