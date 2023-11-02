package edu.yu.introtoalgs;

public class EQIQ extends EQIQBase{
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
    }

    @Override
    public boolean canNepotismSucceed() {
        return false;
    }

    @Override
    public double getNumberEQQuestions() {
        return 0;
    }

    @Override
    public double getNumberIQQuestions() {
        return 0;
    }

    @Override
    public double getNumberOfSecondsSuccess() {
        return 0;
    }
}
