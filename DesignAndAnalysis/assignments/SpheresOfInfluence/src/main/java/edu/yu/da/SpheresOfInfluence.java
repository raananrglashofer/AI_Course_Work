package edu.yu.da;

import java.util.*;
import java.lang.*;


public class SpheresOfInfluence extends SpheresOfInfluenceBase{
    private int maxStrength;
    private int maxRight;
    private Set<Influencer> influencers = new HashSet<>();
    private Set<String> ids = new HashSet<>();
    private List<Influencer> validInfluencers = new ArrayList<>();

    private class Influencer{
        private String id;
        private int xValue;
        private int radius;
        private int leftMostIntersection; // these calculations are off and need to be using pythagorean theorem
        private int rightMostIntersection;// these calculations are off and need to be using pythagorean theorem

        public Influencer(String id, int xValue, int radius){
            this.id = id;
            this.xValue = xValue;
            this.radius = radius;
            // can't do the intersection for all influencers because they don't all reach maxStrength
//            this.leftMostIntersection = xValue - radius; // need to change to pythagorean theorem
//            this.rightMostIntersection = xValue + radius; // need to change to pythagorean theorem
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }
            if(obj == null || getClass() != obj.getClass()){
                return false;
            }

            Influencer otherInfluencer = (Influencer) obj;
            return (xValue == otherInfluencer.xValue && radius == otherInfluencer.radius);
        }

        @Override
        public int hashCode() { // maybe change the hash function and don't use objects
            return Objects.hash(xValue, radius);
        }
    }
    /**
     * Constructor that defines the MU rectangular 2D plane of student values.
     *
     * @param maxStrength the maximum "strength" value demarcating the MU 2D
     *                    plane in one dimension, must be greater than 0.
     * @param maxRight    the maximum "right religiosity" value (in MU's "left to
     *                    right" spectrum) demarcating the MU 2D plane in the other dimension, must
     *                    be greater than 0.
     */
    public SpheresOfInfluence(int maxStrength, int maxRight) {
        super(maxStrength, maxRight);
        if(maxStrength < 1 || maxRight < 1){
            throw new IllegalArgumentException();
        }
        this.maxStrength = maxStrength;
        this.maxRight = maxRight;
    }

    /** Specifies the two characteristics of an influencer.
     *
     * @param id uniquely identifies the influencer, must be non-empty.
     * @param xValue the influencer's position on the "right-to-left" spectrum,
     * represents the center of the influencer's sphere of influence.  The
     * influencer's "strength" value is in the center of the MU rectangular 2D
     * plane.  Must be a non-negative integer.
     * @param radius demarcates the extent of the influencer's influence, must be
     * greater than 0.
     * @throws IllegalArgumentException if the Javadoc constraints are violated,
     * including if an influencer with this id has previously been added or if an
     * influencer with a duplicate xValue and radius values has previously been
     * added.
     */
    @Override
    public void addInfluencer(String id, int xValue, int radius) {
        if(id.isEmpty() || xValue < 0 || radius < 1 || this.ids.contains(id)){
           throw new IllegalArgumentException();
        }
        Influencer influencer = new Influencer(id, xValue, radius);
        this.ids.add(id);
        // could already filter here
        this.influencers.add(influencer);
    }

    /** Returns the ids in the minimal set of influencers that provide complete
     * coverage of the MU rectangular 2D place.
     *
     * @return a List of the relevant ids, Collection.EMPTY_LIST if no set of the
     * supplied influencers can provide complete coverage.  The ids MUST BE
     * sorted in order of increasing lexicographical order.
     */

    // Filter influencers that go beyond the maxStrength (radius > maxStrength / 2)
    // sort by furthest to the left  - sort on xValue (nlogn)
    // greedily take xValue that goes the farthest (current xValue to next point of intersection)
    // more or less operates as a direct line down from first intersection point to next intersection point and take that rectangle
    @Override
    public List<String> getMinimalCoverageInfluencers() {
        for(Influencer influencer : this.influencers){
            if(influencer.radius > (this.maxStrength / 2)){
                this.validInfluencers.add(influencer);
                setIntersectionPoints(influencer);
            }
        }
        sortValidInfluencers();

        int currentX = 0;
        List<String> selectedInfluencersIds = new ArrayList<>();

        while (currentX < maxRight) {
            Influencer bestInfluencer = null;
            int furthestX = currentX;

            for (Influencer influencer : this.validInfluencers) {
                // Influencer is eligible if it starts before or at the currentX and extends further than any we've seen so far.
                if (influencer.leftMostIntersection <= currentX && influencer.rightMostIntersection > furthestX) {
                    bestInfluencer = influencer;
                    furthestX = influencer.rightMostIntersection;
                }
            }

            if (bestInfluencer == null) {
                // This means we couldn't find any influencer to extend the coverage from currentX.
                // Thus, full coverage is impossible with the given set of influencers.
                return Collections.emptyList();
            }

            // Add the best influencer for this segment and update currentX to the new furthest right point.
            selectedInfluencersIds.add(bestInfluencer.id);
            currentX = furthestX; // Move to the end of the best influencer's coverage

            // Optimization: remove the selected influencer from consideration in future iterations
            this.validInfluencers.remove(bestInfluencer);
        }

        // Sort the selected influencers' IDs in lexicographical order before returning.
        Collections.sort(selectedInfluencersIds);
        return selectedInfluencersIds;
    }
    // I think this math gets the intersection points correctly
    private void setIntersectionPoints(Influencer influencer){
        int a = maxStrength / 2;
        int c = influencer.radius;
        int b = (int) Math.sqrt((c * c) - (a * a));
        influencer.leftMostIntersection = influencer.xValue - b;
        influencer.rightMostIntersection = influencer.xValue + b;
    }

    private void sortValidInfluencers(){
        Comparator<Influencer> comparator = new Comparator<>() {
            // sorts by leftMostPoint and then tiebreaker is rightMostPoint
            // double check that this sorts in increasing order
            @Override
            public int compare(Influencer i1, Influencer i2) {
                if(i1.leftMostIntersection == i2.leftMostIntersection){
                    if(i1.rightMostIntersection > i2.rightMostIntersection){
                        return 1;
                    } else{
                        return -1;
                    }
                }
                if(i1.leftMostIntersection > i2.leftMostIntersection){
                    return 1;
                } else{
                    return -1;
                }
            }
        };
        Collections.sort(this.validInfluencers, comparator);
    }
}
