package edu.yu.da;
// https://algs4.cs.princeton.edu/code/edu/princeton/cs/algs4/FordFulkerson.java.html
import java.util.*;
public class FordFulkerson {
    private static final double FLOATING_POINT_EPSILON = 1.0E-11;
    private final int V;          // number of vertices
    private boolean[] marked;     // marked[v] = true iff s->v path in residual graph
    private FlowEdge[] edgeTo;    // edgeTo[v] = last edge on shortest residual s->v path
    private double value;         // current value of max flow
    private HashMap<String, Integer> map;

    /**
     * Compute a maximum flow and minimum cut in the network {@code G}
     * from vertex {@code s} to vertex {@code t}.
     *
     * @param  G the flow network
     * @param  s the source vertex
     * @param  t the sink vertex
     * @throws IllegalArgumentException unless {@code 0 <= s < V}
     * @throws IllegalArgumentException unless {@code 0 <= t < V}
     * @throws IllegalArgumentException if {@code s == t}
     * @throws IllegalArgumentException if initial flow is infeasible
     */
    public FordFulkerson(FlowNetwork G, String s, String t) {
        V = G.V();
        validate(G.getVertexIndices().get(s));
        validate(G.getVertexIndices().get(t));
        this.map = G.getVertexIndices();
        if (s.equals(t))               throw new IllegalArgumentException("Source equals sink");
        if (!isFeasible(G, s, t)) throw new IllegalArgumentException("Initial flow is infeasible");
        // while there exists an augmenting path, use it
        value = excess(G, t);
        while (hasAugmentingPath(G, s, t)) {

            // compute bottleneck capacity
            double bottle = Double.POSITIVE_INFINITY;
            for (String v = t; !v.equals(s); v = edgeTo[this.map.get(v)].other(v)) { // double check this line
                bottle = Math.min(bottle, edgeTo[this.map.get(v)].residualCapacityTo(v));
            }

            // augment flow
            for (String v = t; !v.equals(s); v = edgeTo[this.map.get(v)].other(v)) {
                edgeTo[this.map.get(v)].addResidualFlowTo(v, bottle);
            }

            value += bottle;
        }

        // check optimality conditions
        assert check(G, s, t);
    }

    /**
     * Returns the value of the maximum flow.
     *
     * @return the value of the maximum flow
     */
    public double value()  {
        return value;
    }

    /**
     * Returns true if the specified vertex is on the {@code s} side of the mincut.
     *
     * @param  v vertex
     * @return {@code true} if vertex {@code v} is on the {@code s} side of the mincut;
     *         {@code false} otherwise
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public boolean inCut(int v)  {
        validate(v);
        return marked[v];
    }

    // throw an IllegalArgumentException if v is outside prescribed range
    private void validate(int v)  {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }


    // is there an augmenting path?
    // if so, upon termination edgeTo[] will contain a parent-link representation of such a path
    // this implementation finds a shortest augmenting path (fewest number of edges),
    // which performs well both in theory and in practice
    private boolean hasAugmentingPath(FlowNetwork G, String s, String t) {
        edgeTo = new FlowEdge[G.V()];
        marked = new boolean[G.V()];

        // breadth-first search
        Queue<String> queue = new ArrayDeque<>();
        queue.add(s);
        marked[this.map.get(s)] = true;
        while (!queue.isEmpty() && !marked[this.map.get(t)]) {
            String v = queue.remove();

            for (FlowEdge e : G.adj(v)) {
                String w = e.other(v);

                // if residual capacity from v to w
                if (e.residualCapacityTo(w) > 0) {
                    if (!marked[this.map.get(w)]) {
                        edgeTo[this.map.get(w)] = e;
                        marked[this.map.get(w)] = true;
                        queue.add(w);
                    }
                }
            }
        }

        // is there an augmenting path?
        return marked[this.map.get(t)];
    }



    // return excess flow at vertex v
    private double excess(FlowNetwork G, String v) {
        double excess = 0.0;
        for (FlowEdge e : G.adj(v)) {
            if (v.equals(e.from())) excess -= e.flow();
            else excess += e.flow();
        }
        return excess;
    }

    // return excess flow at vertex v
    private boolean isFeasible(FlowNetwork G, String s, String t) {

        // check that capacity constraints are satisfied
        for (String v : this.map.keySet()) {
            for (FlowEdge e : G.adj(v)) {
                if (e.flow() < -FLOATING_POINT_EPSILON || e.flow() > e.capacity() + FLOATING_POINT_EPSILON) {
                    System.err.println("Edge does not satisfy capacity constraints: " + e);
                    return false;
                }
            }
        }

        // check that net flow into a vertex equals zero, except at source and sink
        if (Math.abs(value + excess(G, s)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at source = " + excess(G, s));
            System.err.println("Max flow = " + value);
            return false;
        }
        if (Math.abs(value - excess(G, t)) > FLOATING_POINT_EPSILON) {
            System.err.println("Excess at sink   = " + excess(G, t));
            System.err.println("Max flow         = " + value);
            return false;
        }
        for (String v : map.keySet()) {
            if (v.equals(s) || v.equals(t)) continue;
            else if (Math.abs(excess(G, v)) > FLOATING_POINT_EPSILON) {
                System.err.println("Net flow out of " + v + " doesn't equal zero");
                return false;
            }
        }
        return true;
    }



    // check optimality conditions
    private boolean check(FlowNetwork G, String s, String t) {

        // check that flow is feasible
        if (!isFeasible(G, s, t)) {
            System.err.println("Flow is infeasible");
            return false;
        }

        // check that s is on the source side of min cut and that t is not on source side
        if (!inCut(this.map.get(s))) {
            System.err.println("source " + s + " is not on source side of min cut");
            return false;
        }
        if (inCut(this.map.get(t))) {
            System.err.println("sink " + t + " is on source side of min cut");
            return false;
        }

        // check that value of min cut = value of max flow
        double mincutValue = 0.0;
        for (String v : this.map.keySet()) {
            for (FlowEdge e : G.adj(v)) {
                if ((v.equals(e.from()) && inCut(this.map.get(e.from())) && !inCut(this.map.get(e.to())))){
                    mincutValue += e.capacity();
                }
            }
        }

        if (Math.abs(mincutValue - value) > FLOATING_POINT_EPSILON) {
            System.err.println("Max flow value = " + value + ", min cut value = " + mincutValue);
            return false;
        }

        return true;
    }

}