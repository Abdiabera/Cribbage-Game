package com.demo.example;

/*

This class represents a Player, analogous to a human player in Cribbage.
Data:
    - Hand hand. Represents the player's hand; changes in each round.
    - Hand crib. Represents the player's crib; changes in each round (only has cards when Game sets this Player as dealer)

Responsibilities:
    - Create and represent a Player
        - Upon instantiation, Player's hand and crib fields are set to empty ArrayLists
    - Add cards to hand
    - Remove cards from hand
    - Add cards to crib
    - Remove cards from crib
    - Completely empty hand
    - Completely empty crib

Relies upon:
    - Card for Card objects that populate ArrayLists for hand and crib
    - Hand for representing hand and crib
 */

import java.util.ArrayList;

public class Player {

    // Hand objects hold the cards of the hand and crib.
    private Hand hand;
    private Hand crib;

    /*
    Constructor.

    Sets hand and crib to new, empty ArrayLists.
     */
    private Player() {
        this.hand = Hand.of(new ArrayList<>());
        this.crib = Hand.of(new ArrayList<>());
    }

    /*
    Factory method. Returns a new Player object.
     */
    public static Player of() {
        return new Player();
    }

    /*
    Sets hand to a new, empty ArrayList. (Used by Game during play)
     */
    public void createHand(ArrayList<Card> cards) {
        this.hand = Hand.of(cards);
    }

    /*
    Sets crib to a new, empty ArrayList. (Used by Game during play)
     */
    public void createCrib(ArrayList<Card> cards) {
        //ArrayList<Card> cards = new ArrayList<>();
        this.crib = Hand.of(cards);
    }

    /*
    Adds card to the hand.
     */
    public void addCardToHand(Card card) {
        this.hand.addCard(card);
    }

    /*
    Adds card to the crib.
     */
    public void addCardToCrib(Card card) {
        this.crib.addCard(card);
    }

    /*
    Removes all items from the hand ArrayList.
     */
    public void emptyHand() {
        this.hand.removeAll();
    }
/*
    Removes all items from the crib ArrayList.
     */

    public void emptyCrib() {
        this.crib.removeAll();
    }

    /*
    Returns the player's hand.
     */
    public Hand getHand() {
        return this.hand;
    }

    /*
    Returns the player's crib.
     */
    public Hand getCrib() {
        return this.crib;
    }

    /*
    Returns true the player has a legal card, based on the inputted runningSum
     */

    public boolean hasLegalCard(int runningSum) {
        if (getHand().getCards().isEmpty()) {
            return false;
        }
        for (Card card : getHand().getCards()) {
            if (card.isLegalCard(runningSum)) {
                return true;
            }
        }
        return false;
    }

}