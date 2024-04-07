package edu.yu.da;

import java.lang.*;
import java.util.*;

public class BeatThePricingSchemes extends BeatThePricingSchemesBase{
    private double unitPrice;
    private Set<PricingScheme> schemes = new HashSet<>(); // Decide List or Set
    private int count = 1;
    private double[] bestPrices = new double[MAX_MATZOS+1]; // store optimal prices for each number of Matzos
    private List<Integer> optimalDecisions = new ArrayList<>();
    private boolean priceSchemesAdded = false;
    private Map<Integer, List<Integer>> optimalPriceMap = new HashMap<>(); // store optimal schemes
    private boolean cheapestPriceCalled = false;

    private class PricingScheme{
        private double price;
        private int quantity;
        private int number; // to keep track which number scheme this is
        public PricingScheme(double price, int quantity, int number){
            this.price = price;
            this.quantity = quantity;
            this.number = number;
        }
    }

    /** Defines the maximum number of price schemes that a client may supply to a
     * given BeatThePricingSchemes instance.
     */
    public final static int MAX_SCHEMES = 20;

    /** Defines the maximum number of matzos that a customer may supply as their
     * purchase threshold.
     */
    public final static int MAX_MATZOS = 100;

    /** Identifies the "unit price" price scheme (all other pricing schemes are
     * identified by the order they were added 1..N
     */
    public final static int UNIT_PRICE_DECISION = 0;

    /** Constructor: client specifies the price of a single quantity of the
     * desired item.
     *
     * @param unitPrice the price-per-single-unit, must be greater than 0.
     * @throw IllegalArgumentException if the parameter pre-conditions are
     * violated.
     */
    public BeatThePricingSchemes(double unitPrice){
        super(unitPrice);
        if(unitPrice <= 0){
            throw new IllegalArgumentException();
        }
        this.unitPrice = unitPrice;
        PricingScheme scheme = new PricingScheme(unitPrice, 1, 0);
        this.schemes.add(scheme);
    }
    /** Adds a pricing scheme to be considered when making the "select optimal
     * pricing schemes" decision.
     *
     * @param price the price to be paid for the specified quantity, must be
     * greater than 0.
     * @param quantity, which for the sake of DP, cannot exceed MAX_MATZOS and
     * must be greater than zero.
     * @throw IllegalArgumentException if the parameter pre-conditions are violated.
    //* @see MAX_SCHEMES
     */
    @Override
    public void addPricingScheme(double price, int quantity) {
        if(price <= 0 || quantity < 1 || quantity > MAX_MATZOS || this.schemes.size() == 21){
            throw new IllegalArgumentException();
        }
        PricingScheme scheme = new PricingScheme(price, quantity, this.count++); // I believe this makes it go up and looks cooler
        // if cost per matzah is greater than unitPrice CPM then never will get used
        if((scheme.price / scheme.quantity) <= this.unitPrice){
            this.schemes.add(scheme);
            this.priceSchemesAdded = true;;
        }
    }
    /** Returns the cheapest price needed to buy at least threshold items.  Thus
     * the quantity bought may exceed the threshold, as long as that is the
     * cheapest price for threshold number of items given the current set of
     * price schemas.
     *
     * @param threshold the minimum number of items to be purchased, cannot
     * exceed MAX_MATZOS, and must be greater than zero.
     * @return the cheapest price required to purchase at least the threshold
     * quantity.
     * @throw IllegalArgumentException if the parameter pre-conditions are violated.
    //* @see MAX_MATZOS
     */
    // if priceSchemes were added since last call need to use a new map
    @Override
    public double cheapestPrice(int threshold) {
        if(threshold > MAX_MATZOS || threshold < 1){
            throw new IllegalArgumentException();
        }
        this.cheapestPriceCalled = true;
//        // nothing changed / no price schemes added so don't need to do extra work
//        // doesn't work if that threshold hasn't been hit yet though
//        if(optimalPriceMap.containsKey(threshold) && !this.priceSchemesAdded){
//            return this.bestPrices[threshold];
//        }

        computeCheapestPrice(threshold);
        priceSchemesAdded = false; // Reset flag after computation

        // very hacky and should be met with caution
        Double cheapest = (double) Integer.MAX_VALUE;
        int index = threshold;
        for(int i = threshold; i < MAX_MATZOS; i++){
            if(this.bestPrices[i] < cheapest){
                cheapest = this.bestPrices[i];
                index = i;
            }
        }
        this.optimalDecisions = this.optimalPriceMap.get(index);
        return cheapest;
    }

    private void computeCheapestPrice(int threshold) {
        // fill price array with unit price
        this.bestPrices[0] = 0;
        for(int i = 1; i < MAX_MATZOS + 1; i++){
            this.bestPrices[i] = i * unitPrice;
            List<Integer> list = new ArrayList<>();
            for(int j = 0; j < i; j++){
                list.add(0);
            }
            for(PricingScheme scheme : this.schemes){
                if(scheme.quantity <= i){
                    if(this.bestPrices[i] > this.bestPrices[i - scheme.quantity] + scheme.price){
                        this.bestPrices[i] = this.bestPrices[i - scheme.quantity] + scheme.price;

                        List<Integer> previousBestSchemeList = new ArrayList<>();
                        if(optimalPriceMap.get(i - scheme.quantity) != null){
                            previousBestSchemeList = new ArrayList<>(optimalPriceMap.get(i - scheme.quantity));
                            //list = optimalPriceMap.get(i - scheme.quantity);
                        }
                        previousBestSchemeList.add(scheme.number);
                        list = previousBestSchemeList;
                    }
                }
            }
           // System.out.println("This is the list for: " + i + " " + list);
            this.optimalPriceMap.put(i, list);
        }
        //System.out.println(optimalPriceMap.get(4));
    }


    /** Returns a list of optimal price scheme decisions corresponding to the
     * cheapest price.  If a unit price decision is made, it's represented by the
     * UNIT_PRICE_DECISION constant.  Otherwise, a price scheme is represented by
     * the order in which it was added to this instance: 1..N
     *
     //* @see UNIT_PRICE_DECISION
     //* @see cheapestPrice
     */
    @Override
    public List<Integer> optimalDecisions() {
        if(!this.cheapestPriceCalled){
            throw new IllegalStateException();
        }
        return this.optimalDecisions;
    }
}
