package edu.yu.da;

import java.lang.*;
import java.util.*;

public class BeatThePricingSchemes extends BeatThePricingSchemesBase{
    private double unitPrice;
    private Set<PricingScheme> schemes = new HashSet<>(); // Decide List or Set
    private int count = 1;

    private class PricingScheme{
        private double price;
        private double quantity;
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
        if(price <= 0 || quantity < 1 || quantity > MAX_MATZOS || this.schemes.size() == 20){
            throw new IllegalArgumentException();
        }
        PricingScheme scheme = new PricingScheme(price, quantity, this.count++); // I believe this makes it go up and looks cooler
        this.schemes.add(scheme);
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
    @Override
    public double cheapestPrice(int threshold) {
        if(threshold > MAX_MATZOS || threshold < 1){
            throw new IllegalArgumentException();
        }
        return 0;
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
        return null;
    }
}
