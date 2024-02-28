package edu.yu.da;

import java.util.*;
public class PickAYeshiva extends PickAYeshivaBase{
    private double[] facultyRatioRankings;
    private double[] cookingRatioRankings;
    private List<Yeshiva> allYeshivas = new ArrayList<>();
    private List<Yeshiva> validYeshivaChoices = new ArrayList<>();
    private class Yeshiva{      // implements Comparable<Yeshiva>
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

            Yeshiva otherYeshiva = (Yeshiva) obj;
            return (facultyRanking == otherYeshiva.facultyRanking && cookingRanking == otherYeshiva.cookingRanking);
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
        // make all Yeshivas
        for(int i = 0; i < facultyRatioRankings.length; i++){
            Yeshiva yeshiva = new Yeshiva(facultyRatioRankings[i], cookingRankings[i], i);
            this.allYeshivas.add(yeshiva);
        }
        Comparator<Yeshiva> comparator = new Comparator<>() {
            // sorts by faculty ranking and then tiebreaker is cooking ranker
            // sorts in descending order
            @Override
            public int compare(Yeshiva y1, Yeshiva y2) {
                if(y1.facultyRanking == y2.facultyRanking){
                    if(y1.cookingRanking > y2.cookingRanking){
                        return -1;
                    } else{
                        return 1;
                    }
                }
                if(y1.facultyRanking > y2.facultyRanking){
                    return -1;
                } else{
                    return 1;
                }
            }
        };
        Collections.sort(this.allYeshivas, comparator);
        Yeshiva yeshiva = allYeshivas.get(0); // best faculty yeshiva
        this.validYeshivaChoices.add(yeshiva);
        for(int j = 1; j < allYeshivas.size(); j++){
            if(yeshiva.cookingRanking <= allYeshivas.get(j).cookingRanking){
                checkAgainstOtherYeshivas(allYeshivas.get(j));
            }
        }
        // put scores in arrays
        this.facultyRatioRankings = new double[validYeshivaChoices.size()];
        this.cookingRatioRankings = new double[validYeshivaChoices.size()];
        for(int k = 0; k < validYeshivaChoices.size(); k++){
            this.facultyRatioRankings[k] = validYeshivaChoices.get(k).facultyRanking;
            this.cookingRatioRankings[k] = validYeshivaChoices.get(k).cookingRanking;
        }
    }

    private void checkAgainstOtherYeshivas(Yeshiva yeshiva){
        for(Yeshiva yesh : this.validYeshivaChoices){
            if(yeshiva.facultyRanking <= yesh.facultyRanking && yeshiva.cookingRanking <= yesh.cookingRanking){
                return; // there is a yeshiva that beats it
            }
        }
        // if not beaten then it's valid
        this.validYeshivaChoices.add(yeshiva);
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
        return this.cookingRatioRankings;
    }
}
