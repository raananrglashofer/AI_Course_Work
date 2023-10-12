package edu.yu.introtoalgs;
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
        int n = 10; // starting size variable
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
                //System.out.println(timeList.get(timeList.size()-1));
                //System.out.println("This is the ratio " + ratio);
                if(compareRatios(ratio)){ // true if ratio stabilized
                    return Math.round(ratio * 10.0) / 10.0;
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



/* package edu.yu.introtoalgs;

import java.lang.reflect.Constructor; // can i import this??
import java.lang.reflect.InvocationTargetException; // can i import this??
import java.util.concurrent.*;

public class BigOIt2 extends BigOIt2Base {

    public BigOIt2() {
    }

    // use Reflexive API to create an instance of big0Measurable class for specific algorithm
    // then call the setup(n) method and pass in the instance's n value
    // then set up timer for the execute method and store it results somewhere
    // understand doubling ratios more and math behind it
    @Override
    public double doublingRatio(String bigOMeasurable, long timeOutInMs) {
        try {
            Class<?> algorithmClass = Class.forName(bigOMeasurable);
            if (!BigOMeasurable.class.isAssignableFrom(algorithmClass)) {
                throw new IllegalArgumentException("Class does not implement BigOMeasurable.");
            }
            Constructor<?> constructor = algorithmClass.getDeclaredConstructor();
            constructor.setAccessible(true); // Make it accessible
            double time = 0;
            int n = 1; // not sure what number to start this out
            ExecutorService executorService = Executors.newFixedThreadPool(1); // switch to 2
            while (true) {
                final int currentN = n;
                Future<Double> executeFuture = executorService.submit(() -> {
                    final int localN = currentN;
                    BigOMeasurable algorithm = (BigOMeasurable) constructor.newInstance();
                    algorithm.setup(localN);
                    long startTime = System.nanoTime();
                    algorithm.execute();
                    long endTime = System.nanoTime();
                    double executionTimeMs = (endTime - startTime) / 1000000.0; // supposed to be divided by 1000000
                    //System.out.println("The execution time is " + executionTimeMs);
//                    System.out.println(startTime);
//                    System.out.println(endTime);
                    return executionTimeMs;
                });

//                Future<Void> timeOutFuture = executorService.submit(() -> {
//                    try {
//                        Thread.sleep(timeOutInMs);
//                        executeFuture.cancel(true);
//                    } catch (InterruptedException e) {
//                        // not sure what to put here
//                    }
//                    return null;
//                });

                try {
                    double currentTime = executeFuture.get(timeOutInMs, TimeUnit.MILLISECONDS); // Use a timeout here
                    System.out.println("The currentTime is " + currentTime);
                    System.out.println("The previousTime is " + time);
                    if (time > 0) {
                        double ratio = currentTime / time;
                        System.out.println("The ratio is " + ratio);
                        // Check if the ratio has stabilized (within a certain threshold)
                        if (Math.abs(ratio - 4.0) < 0.1) { // what to do for 3.0
                            return ratio;
                        }
                    }

                    time = currentTime;
                    n *= 2; // Double the input size
                } catch (TimeoutException e) {
                    executeFuture.cancel(true); // Cancel the algorithm if it's still running
                    return Double.NaN; // Timeout occurred
                } catch (InterruptedException | ExecutionException e) {
                    // Handle other exceptions that might occur during task execution
                }
            }
        } catch (ClassNotFoundException | NoSuchMethodException e){
                 //|
                 //IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new IllegalArgumentException("Invalid class name or constructor error: " + e.getMessage(), e);
        }
    }
}

 */