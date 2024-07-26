package com.demo.example;

/*
This class represents a Card object, analogous to a card from a standard 52-card deck.

Data: enum suit, int value

Responsibilities:
    - Create and represent a Card object.
        - Factory method takes in two ints (one for suit, one for value) and creates a Card object.
    - Return values of a Card, including:
        - Card's value (int 1-13; represents ace, 2, 3, ... king)
        - Card's scoring value (int 1-10; matches value, except for jack/queen/king, which are all 10). Used in tallying points by Hand and Game
        - Card's value as String (e.g., "Ace", "2"). Used when printing.
        - Card's suit (enum Suit - one of CLUBS, HEARTS, DIAMONDS, SPADES)
        - Card's suit as integer (CLUBS = 0, HEARTS = 1, DIAMONDS = 2, SPADES = 3). Used for communication with front end. Same mapping used for factory method to
            allow just ints to be used to create new cards.
        - Card's suit as String (e.g., "Clubs", "Hearts"). Used when printing.

Relies upon: None.
 */

public class Card {
    public enum Suit {
        CLUBS,
        HEARTS,
        DIAMONDS,
        SPADES
    }

    private final Suit suit; // enum: clubs, hearts, diamonds, spades
    private final int value; // int value between 1-13 representing ace, 2, 3, ... 10, jack, queen, king.

    /*
    Constructor.
    value: int that represents the value (Ace, 2, 3, ... King) of a card. Must be between 1 and 13, inclusive.
    suit: represents the suit (clubs, hearts, etc) of a card. Must be a valid enum from Suit
    */
    private Card(int value, Suit suit){
        if (value < 1 || value > 13) {
            throw new IllegalArgumentException("Not a valid value");
        }

        this.suit = suit;
        this.value = value;
    }


    /*
    factory method to create Card.
        - allows ints to be used to represent suit and value:
            - Value is directly mapped
            - Suit is mapped: CLUBS = 0, HEARTS = 1, DIAMONDS = 2, SPADES = 3
       Examples:
            - of(3, 1) creates a Card with value 3 and suit HEARTS
            - of(11, 3) creates a CARD with value 11 (equivalent to Jack) and suit DIAMONDS
     */
    public static Card of(int value, int suit) {
        if (suit < 0 || suit > 3) {
            throw new IllegalArgumentException("Not a valid suit");
        }
        else if (value < 1 || value > 13) {
            throw new IllegalArgumentException("Not a valid value");
        }

        Suit suitOfCard = switch (suit) {
            case 0 -> Suit.CLUBS;
            case 1 -> Suit.HEARTS;
            case 2 -> Suit.DIAMONDS;
            case 3 -> Suit.SPADES;
            default -> null;
        };

        return new Card(value, suitOfCard);
    }

    // getter for a card's suit (e.g., DIAMONDS)
    public Suit getSuit(){
        return this.suit;
    }

    // getter for a card's value (e.g., 7, 11)
    public int getValue(){
        return this.value;
    }

    /*
    Returns the computable value of a card, used in scoring.

    - Scoring value is equivalent to field value, except for values >10 (i.e., to Jack, Queen, King). All values
        > 10 have a scoring value of 10.

    - Examples:
        Card w/ value 7 and suit HEARTS returns 7.
        Card w/ value 11 and suit CLUBS returns 10.
    */
    public int getScoringValue(){
        if (this.value <= 10) {
            return this.value;
        } else {
            return 10;
        }
    }

    // returns the name of a value. (e.g., value = 13, returns "King")
    public String getValueName(){
        switch (this.value){
            case 1:
                return "Ace";
            case 2:
                return "2";
            case 3:
                return "3";
            case 4:
                return "4";
            case 5:
                return "5";
            case 6:
                return "6";
            case 7:
                return "7";
            case 8:
                return "8";
            case 9:
                return "9";
            case 10:
                return "10";
            case 11:
                return "Jack";
            case 12:
                return "Queen";
            case 13:
                return "King";

        }

        return null;
    }

    // returns a string version of the suit (e.g., Suit.DIAMONDS returns "Diamonds")
    public String getSuitName(){
        return switch (this.suit) {
            case CLUBS -> "Clubs";
            case HEARTS -> "Hearts";
            case DIAMONDS -> "Diamonds";
            case SPADES -> "Spades";
        };
    }

    // getter for a card's suit as an integer, similar to how it's created with of (e.g., DIAMONDS returns 2)
    public int getSuitAsInt(){
        return switch (this.suit) {
            case CLUBS -> 0;
            case HEARTS -> 1;
            case DIAMONDS -> 2;
            case SPADES -> 3;
        };
    }

    /*
    Returns true if the card is legal to play, based on the runningSum
     */
    public boolean isLegalCard(int runningSum) {
        return (runningSum + getScoringValue()) <= 31;
    }

    /*
    Represents a card when printed

    Examples:
    - Card w/ value 11 and suit DIAMONDS returns Queen of Diamonds
    - Card w/ value 2 and suit CLUBS returns 2 of Clubs
    */
    @Override
    public String toString() {
        return getValueName() + " of " + getSuitName();
    }

    /*
    Tests if two cards are equal. Returns True if the cards have the same suit and same value.

    Examples:
    - 2 of Diamonds and 2 of Spades --> False
    - 2 of Diamonds and 4 of Diamonds --> False
    - 2 of Diamonds and 2 of Diamonds --> True

     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Card) {
            return this.suit == ((Card)other).suit && this.value == ((Card)other).value;
        }
        return false;
    }

}