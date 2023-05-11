public class Deck {
    public class Card {
        enum Suits {Diamonds, Hearts, Spades, Clubs};
        // Each card has a number and a suit
        private int number;
        private Suits suit;
        public Card(Suits suit, int num) {
            this.suit = suit;
            this.number = num;
        }
        public void getCardPrinted() {
            System.out.println(number + " of " + suit.toString());
        }
        public int getNumber(){
            return this.number;
        }
    }
    // Each deck is an array of cards
    private Card [] deck;
    //Our constructor fills a 52 index array with each card
    public Deck () {
        deck = new Card[52];
        int counter = 0;
        for (Deck.Card.Suits suit : Card.Suits.values()) {
            for (int i = 2; i < 15; i++) {
                deck[counter] = new Card(suit, i);
                counter++;
            }
        }
    }
    //pull a random card from the deck
    //need to make sure we can't pull the same card twice
    public Card pickRandomCard () {
        int r = (int) (Math.random() * (51));
        if (this.deck[r] != null) {
            return deck[r];
        }
        else {
            return pickRandomCard();
        }
    }

    //return the current deck
    public Card[] getDeck () {
        return this.deck;
    }



}
