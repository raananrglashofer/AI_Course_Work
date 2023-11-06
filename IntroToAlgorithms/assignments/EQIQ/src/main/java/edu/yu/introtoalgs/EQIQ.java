package edu.yu.introtoalgs;

public class EQIQ extends EQIQBase{
    private boolean nepotismSuccess = false;
    private double EQQuestions;
    private double IQQuestions;
    private double seconds;
    /** Constructor: supplies the information needed to solve the EQIQ problem.
     * When the constructor invocation completes successfully, clients invocation
     * of every other API method must return in O(1) time.
     *
     * @param //totalQuestion the number of questions on the candidate interview
     * test, must be greater than 1
     * @param eqSuccessRate the ith element of this array specifies the success
     * rate of the ith candidate for EQ questions.  Client maintains ownership.
     * @param iqSuccessRate the ith element of this array specifies the success
     * rate of the ith candidate for IQ questions.  Client maintains ownership.
     *
     * NOTE: the size of the two arrays must be identical, and greater than one.
     * @param nepotismIndex the index in the above arrays that specifies the
     * values of the nepotism candidate.  Candidate indices are numbered
     * 0..nCandidates -1.
     */
    public EQIQ(int totalQuestions, double[] eqSuccessRate, double[] iqSuccessRate, int nepotismIndex) {
        super(totalQuestions, eqSuccessRate, iqSuccessRate, nepotismIndex);
        // really don't need to check both that eqSuccessRate and iqSuccessRate are < 2 because I check that they are equal
        if(totalQuestions < 2 || eqSuccessRate.length != iqSuccessRate.length || eqSuccessRate.length < 2 || iqSuccessRate.length < 2){
            throw new IllegalArgumentException(); // I assume it's IAE
        }
        // this is a good check, but program now needs to end
        if(checkIfMaxEQOrIQ(eqSuccessRate,iqSuccessRate, nepotismIndex) == true){
            return; // i want to close the constructor 
        }


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
    // method to check if there is a candidate that nepotism can't beat
    // returns true if there is a candidate that is too strong
    private boolean checkIfMaxEQOrIQ(double[] eqSuccessRate, double[] iqSuccessRate, int nepotismIndex){
        for(int i = 0; i < eqSuccessRate.length; i++){
            if(i != nepotismIndex) {
                if (eqSuccessRate[i] >= eqSuccessRate[nepotismIndex] && iqSuccessRate[i] >= iqSuccessRate[nepotismIndex]) {
                    this.nepotismSuccess = false;
                    this.EQQuestions = 0;
                    this.IQQuestions = 0;
                    this.seconds = 0;
                    return true;
                }
            }
        }
        return false;
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
}
