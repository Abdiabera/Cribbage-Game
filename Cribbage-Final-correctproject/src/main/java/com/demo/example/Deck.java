package com.demo.example;

import java.util.ArrayList;
import java.util.Collections;

/*
This class represents a Deck of Cards, analogous to a standard 52-card deck.
Data: Card objects held in ArrayList
Responsibilities:
    - Create and represent a deck of 52 Card objects
    - Deal random Card
    - Shuffle deck
    - Identify if the deck contains a Card
    - Return all Cards in the deck
    - Return a Card at a specific index (used only in testing)
Relies upon: Card class to create Card objects
Invariant: Only one instance of each unique card can exist, this is verified in the add method

 */

public class Deck {

    private final ArrayList<Card> allCards; // all Cards must be unique

    /*
    Constructor.

    Creates 52 unique cards (Ace of Clubs, 2 of Clubs ... King of Spades) and adds them to allCards.
     */
    private Deck(){
        allCards = new ArrayList<>();

        // Add all 52 cards to allCards
        for (int i = 1; i <= 13; i++) { // iterate over all possible values
            for (int j = 0; j < 4; j++) { // iterate over all possible Suit
                Card newCard = Card.of(i, j);
                allCards.add(newCard);
            }
        }
    }

    /*
    Factory method. Returns a new Deck().
     */
    public static Deck of(){
        return new Deck();
    }

    /*
    Returns the card at the specified index.
     */
    // returns card at given index
    public Card getCard(int index) {
        return this.allCards.get(index);
    }

    /*
    Returns True if deck contains the specified card.
     */
    public boolean containsCard(Card card) {
        return this.allCards.contains(card);
    }

    /*
    Returns all cards in the deck.
     */
    public ArrayList<Card> getAllCards() {
        return allCards;
    }

    /*
    Shuffles the deck.
    */
    public void shuffle(){
        Collections.shuffle(this.allCards);
    }


    /*
    Adds a card to the deck, if the deck does not already contain the card.
    */
    public void addCardToDeck(Card card){
        if (!containsCard(card)) {
            allCards.add(card);
        }
    }

    /*
    Removes a random card from the deck.
    */
    public Card draw() {
        if (allCards.isEmpty()) {
            throw new IllegalArgumentException("Can't draw from an empty deck");
        } else {
            shuffle();
            return allCards.remove(0);
        }
    }

    /*
    Returns true if the other Deck contains the exact same set of cards (no missing cards, no extra cards).
    */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Deck) {
            // Check that all of our Cards are in the other Deck
            for (Card card : this.getAllCards()) {
                if (!((Deck) other).containsCard(card)) {
                    return false;
                }
            }
            // Check that all of the Cards in the other Deck are in this Deck
            for (Card card1 : ((Deck) other).getAllCards()) {
                if (!this.containsCard(card1)) {
                    return false;
                }
            }
        } else {
            return false; // if other is not a Deck
        }
        return true;
    }


}