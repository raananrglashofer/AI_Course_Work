package edu.yu.introtoalgs;
import java.util.*;

public class EQIQ extends EQIQBase {
    private boolean nepotismSuccess = false;
    private double EQQuestions = -1;
    private double IQQuestions = -1;
    private double seconds = -1;
    private int questions;
    private int nepIndex;
    private Student[] students;

    private class Student implements Comparable<Student> {
        //private final int index;
        private final boolean isNep;
        // rate is broken into seconds per question
        private double eq;
        // rate is broken into seconds per question
        private double iq;
        private double time;

        private Student(double eq, double iq, boolean isNep){
            this.isNep = isNep;
            this.eq = 3600 / eq;
            this.iq = 3600 / iq;
        }
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true; // Same object reference, so they are equal.
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false; // Not the same class or null, so they are not equal.
            }
            Student otherStudent = (Student) obj;
            return iq == otherStudent.iq && eq == otherStudent.eq && isNep == otherStudent.isNep;
        }

        @Override
        public int compareTo(Student student){
            return Double.compare(this.time, student.time);
        }
    }

    /**
     * Constructor: supplies the information needed to solve the EQIQ problem.
     * When the constructor invocation completes successfully, clients invocation
     * of every other API method must return in O(1) time.
     *
     * @param //totalQuestion the number of questions on the candidate interview
     *                        test, must be greater than 1
     * @param eqSuccessRate   the ith element of this array specifies the success
     *                        rate of the ith candidate for EQ questions.  Client maintains ownership.
     * @param iqSuccessRate   the ith element of this array specifies the success
     *                        rate of the ith candidate for IQ questions.  Client maintains ownership.
     *                        <p>
     *                        NOTE: the size of the two arrays must be identical, and greater than one.
     * @param nepotismIndex   the index in the above arrays that specifies the
     *                        values of the nepotism candidate.  Candidate indices are numbered
     *                        0..nCandidates -1.
     */
    public EQIQ(int totalQuestions, double[] eqSuccessRate, double[] iqSuccessRate, int nepotismIndex) {
        super(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
        // really don't need to check both that eqSuccessRate and iqSuccessRate are < 2 because I check that they are equal
        if (totalQuestions < 2 || eqSuccessRate.length != iqSuccessRate.length || eqSuccessRate.length < 2 || iqSuccessRate.length < 2) {
            throw new IllegalArgumentException(); // I assume it's IAE
        }
        this.questions = totalQuestions;
        this.nepIndex = nepotismIndex;
        this.students = new Student[eqSuccessRate.length];
        for (int i = 0; i < eqSuccessRate.length; i++) {
            if(i != nepotismIndex) {
                Student student = new Student(eqSuccessRate[i], iqSuccessRate[i], false);
                this.students[i] = student;
            } else {
                Student student = new Student(eqSuccessRate[i], iqSuccessRate[i], true);
                this.students[i] = student;
            }
        }
        binarySearch();
//        for (Student s : students) {
//            System.out.println(s.time);
//        }
//        // this is a good check, but program now needs to end
//        if(checkIfMaxEQOrIQ(eqSuccessRate,iqSuccessRate, nepotismIndex) == true){
//            return; // i want to close the constructor
//        }


    }

    @Override
    public boolean canNepotismSucceed() {
        return this.nepotismSuccess;
    }

    @Override
    public double getNumberEQQuestions() {
        return this.EQQuestions;
    }

    @Override
    public double getNumberIQQuestions() {
        return this.IQQuestions;
    }

    @Override
    public double getNumberOfSecondsSuccess() {
        return this.seconds;
    }

    // do a binary search of students array and continue sorting after each calculation
    // if nepotism candidate is the best then compare to second best student
    // binary search continues until the max between nepotism candidate and second best is found or nepotism can't win

    // what to work on tomorrow
        // I need to double-check the breakup of questions on lines 143-144 and 151-152
    private double binarySearch() {
        double bestMargin = 0;
        double floor = 0;
        double ceiling = questions;
        double mid = 1; // just to access later
        while (ceiling - floor > .001) {
            mid = (floor + ceiling) / 2.0;
//            System.out.println(mid);
//            System.out.println(bestMargin);
            calculateTimes(mid);
            Arrays.sort(this.students); //, Comparator.comparingDouble(student -> student.time));
//            for(Student s : students){
//                System.out.println(s.time);
//            }
//            for (Student s : students) {
//                System.out.println("Time: " + s.time + ", isNep: " + s.isNep);
//            }
            if(this.students[0].isNep && (this.students[1].time - this.students[0].time) > bestMargin){
                ceiling = mid - .001;
                bestMargin = this.students[1].time - this.students[0].time;
            } else {
                floor = mid + .001;
            }
            //System.out.println("Floor: " + floor + ", Ceiling: " + ceiling + ", Mid: " + mid + ", Best Margin: " + bestMargin);
        }
        if (bestMargin <= 0) {
            this.nepotismSuccess = false;
            this.EQQuestions = -1;
            this.IQQuestions = -1;
            this.seconds = -1;
        } else {
            this.nepotismSuccess = true;
            this.IQQuestions = mid;
            this.EQQuestions = this.questions - mid; //  double check this heavy
            this.seconds = bestMargin;
        }
        return bestMargin;
    }

    private void calculateTimes(double mid) {
        double iqQuestions = mid;
        double eqQuestions =  this.questions - mid;
        for (Student s : this.students) {
            s.time = (iqQuestions * s.iq) + (eqQuestions * s.eq);
        }
    }
}

    // i thought this would be a good check to see if it is the max IQ or EQ
    // that would be an easy winner because then it is just either all IQ or EQ questions and helps with sorting later
    // the one flaw is I won't be able to determine how many seconds better it is
    // an alternative approach would be to sort and then check if nepotism index is the top guy and then compare to next best guy

//    private boolean checkIfSoleLeaderEQ(int totalQuestions, double[] eqSuccessRate, int nepotismIndex){
//        double nepEQ = eqSuccessRate[nepotismIndex];
//        for(int i = 0; i < eqSuccessRate.length; i++){
//            if(eqSuccessRate[i] >= nepEQ){
//                return false;
//            }
//        }
//        EQQuestions
//    }
// method to check if there is a candidate that nepotism can't beat
//// returns true if there is a candidate that is too strong
//private boolean checkIfMaxEQOrIQ(double[] eqSuccessRate, double[] iqSuccessRate, int nepotismIndex){
//    for(int i = 0; i < eqSuccessRate.length; i++){
//        if(i != nepotismIndex) {
//            if (eqSuccessRate[i] >= eqSuccessRate[nepotismIndex] && iqSuccessRate[i] >= iqSuccessRate[nepotismIndex]) {
//                this.nepotismSuccess = false;
//                this.EQQuestions = 0;
//                this.IQQuestions = 0;
//                this.seconds = 0;
//                return true;
//            }
//        }
//    }
//    return false;
//}
//    // calculates how many seconds ahead nep wins by
//    private void setNumberOfSecondsSuccess(double EQNep, double IQNep, double loserEQ, double loserIQ){
//        if(!this.nepotismSuccess){
//            this.seconds = -1;
//        }
//        // convert hour to seconds
//        double EQNepSeconds = EQNep / 3600;
//        double IQNepSeconds = IQNep / 3600;
//        double loserEQSeconds = loserEQ / 3600;
//        double loserIQSeconds = loserIQ / 3600;
//        double nepScore = (EQNepSeconds * getNumberEQQuestions()) + (IQNepSeconds * getNumberIQQuestions());
//        double loserScore = (loserEQSeconds * getNumberEQQuestions()) + (loserIQSeconds * getNumberIQQuestions());
//        this.seconds = nepScore - loserScore;
//    }
