import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class BigOIt2 extends BigOIt2Base{
    private List<Double> ratioList = new ArrayList<>();
    private List<Double> timeList = new ArrayList<>();
    public BigOIt2(){
    }

    @Override
    public double doublingRatio(String bigOMeasurable, long timeOutInMs) {
        BigOMeasurable algorithm = reflexiveAlgorithm(bigOMeasurable);
        double time = 0.0; // variable to keep track of time
        int n = 1; // starting size variable
        while(true){
            algorithm.setup(n);
            Stopwatch timer = new Stopwatch();
            algorithm.execute();
            time = timer.elapsedTime();
            //System.out.println("This is the time " + time);
            if(time >= timeOutInMs){
                return Double.NaN;
            }
            ratioFirstCoupleElements(time); // can't compare until after first three iterations
            if(ratioList.size() > 2){
                double ratio = time / timeList.get(timeList.size()-1);
                ratio = Math.round(ratio * 10.0) / 10.0;
                //System.out.println(timeList.get(timeList.size()-1));
                //System.out.println("This is the ratio " + ratio);
                if(compareRatios(ratio)){ // true if ratio stabilized
                    return ratio; //Math.round(ratio * 10.0) / 10.0;
                } else{
                    if(timeList.size() > 3) {
                        ratioList.add(ratio);
                    }
                    timeList.add(time);
                }
            }
            time = 0.0;
            n *= 2;
        }
    }

    private static class Stopwatch {
        private final long start;
        private Stopwatch() {
            start = System.nanoTime();
        }
        private double elapsedTime() {
            long now = System.nanoTime();
            return (now - start) / 1000000.0;
        }
    }

    private BigOMeasurable reflexiveAlgorithm(String bigOMeasurable){
        Constructor<?> constructor;
        BigOMeasurable algorithm;
        try {
            Class<?> algorithmClass = Class.forName(bigOMeasurable);
            if (!BigOMeasurable.class.isAssignableFrom(algorithmClass)) {
                throw new IllegalArgumentException("Class does not implement BigOMeasurable.");
            }
            constructor = algorithmClass.getDeclaredConstructor();
            constructor.setAccessible(true); // Make it accessible
            algorithm = (BigOMeasurable) constructor.newInstance();
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e){
            throw new IllegalArgumentException(); // i think this is correct
        }
        return algorithm;
    }

    private void ratioFirstCoupleElements(double time){
        if(ratioList.size() == 0){
            timeList.add(time);
            ratioList.add(0.0);
        } else if(ratioList.size() == 1){
            ratioList.add(time / timeList.get(0));
            timeList.add(time);
        } else if(ratioList.size() == 2){
            ratioList.add(time / timeList.get(1));
            //timeList.add(time);
        }
    }

    private boolean compareRatios(double ratio){
        if(Math.abs(ratio - ratioList.get(ratioList.size() - 1)) <= .1 && Math.abs(ratio - ratioList.get(ratioList.size() - 2)) <= .1){
            return true;
        } else {
            return false;
        }
    }
}