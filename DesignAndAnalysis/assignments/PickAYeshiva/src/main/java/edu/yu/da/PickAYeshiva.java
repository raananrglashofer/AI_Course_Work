package edu.yu.da;

import java.util.*;
public class PickAYeshiva extends PickAYeshivaBase{
    private double[] facultyRatioRankings;
    private double[] cookingRatioRankings;
    private class Yeshiva{
        private double facultyRanking;
        private double cookingRanking;
        private int index;
        public Yeshiva(double facultyRanking, double cookingRanking, int index){
            this.facultyRanking = facultyRanking;
            this.cookingRanking = cookingRanking;
            this.index = index;
        }

        @Override
        public boolean equals(Object obj){
            if(this == obj){
                return true;
            }
            if(obj == null || getClass() != obj.getClass()){
                return false;
            }

            Yeshiva otherYesiva = (Yeshiva) obj;
            return (facultyRanking == otherYesiva.facultyRanking && cookingRanking == otherYesiva.cookingRanking);
        }

        @Override
        public int hashCode() { // maybe change the hash function and don't use objects
            return Objects.hash(facultyRanking, cookingRanking);
        }
    }
    /** Constructor which supplies the yeshiva rankings in terms of two factors
     * of interest.  The constructor executes a divide-and-conquer algorithm to
     * determine the minimum number of yeshiva-to-yeshiva comparisons required to
     * make a "which yeshiva to attend" decision.  The getters can be accessed in
     * O(1) time after the constructor executes successfully.
     *
     * It is the client's responsibility to ensure that no pair of
     * facultyRatioRankings and cookingRankings values are duplicates.
     *
     * @param facultyRatioRankings Array whose ith element is the value of the
     * ith yeshiva with respect to its faculty-to-student ratio (Rabbeim etc).
     * Client maintains ownership.  Can't be null and must be same length as the
     * other parameter.
     * @param cookingRankings Array whose ith element is the value of the ith
     * yeshiva with respect to the quality of the cooking.  Client maintains
     * ownership.  Can't be null and must be same length as other parameter.
     * @throws IllegalArgumentException if pre-conditions are violated.
     */
    public PickAYeshiva(double[] facultyRatioRankings, double[] cookingRankings) {
        super(facultyRatioRankings, cookingRankings);
        if(facultyRatioRankings == null || cookingRankings == null || facultyRatioRankings.length != cookingRankings.length){
            throw new IllegalArgumentException();
        }

        // sub-classed implementation may want to add code here
    }

    /** Returns an array of yeshiva faculty ranking ratio values that MUST be
     * evaluated (along with the yeshiva's cooking rankings) to make the best
     * "which yeshiva to attend" decision.
     *
     * @return An array, that together with the other getter, represents the
     * MINIMUM set of yeshivos that must be evaluated.  The ith element of this
     * array MUST BE associated with the ith element of the other getter's array.
     * //@see getCookingRankings
     */
    @Override
    public double[] getFacultyRatioRankings() {
        return this.facultyRatioRankings;
    }

    /** Returns an array of yeshiva cooking ranking values that MUST be evaluated
     * (along with the yeshiva's faculty ratio rankings) to make the best "which
     * yeshiva to attend" decision.
     *
     * @return An array, that together with the other getter, represents the
     * MINIMUM set of yeshivos that must be evaluated.  The ith element of this
     * array MUST BE associated with the ith element of the other getter's array.
     */// @see getFacultyRatioRankings
    // *
    @Override
    public double[] getCookingRankings() {
        return this.facultyRatioRankings;
    }
}
