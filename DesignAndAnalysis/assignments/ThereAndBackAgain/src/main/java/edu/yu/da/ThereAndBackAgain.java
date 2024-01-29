package edu.yu.da;

import java.util.List;

public class ThereAndBackAgain extends ThereAndBackAgainBase{

    public ThereAndBackAgain(String startVertex){
        super(startVertex);

    }
    @Override
    public void addEdge(String v, String w, double weight) {

    }

    @Override
    public void doIt() {

    }

    @Override
    public String goalVertex() {
        return null;
    }

    @Override
    public double goalCost() {
        return 0;
    }

    @Override
    public List<String> getOneLongestPath() {
        return null;
    }

    @Override
    public List<String> getOtherLongestPath() {
        return null;
    }
}
