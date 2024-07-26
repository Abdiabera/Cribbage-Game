package com.demo.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    /*
    Test valid construction with factory method.

    Expected to return a player with an empty hand and crib.
     */
    @Test
    void factoryMethod() {
        Player newPlayer = Player.of();

        assertTrue(newPlayer.getHand().getCards().isEmpty());
        assertTrue(newPlayer.getCrib().getCards().isEmpty());
    }

    /*
    Test that createHand functions as expected: creates a Hand object and sets the player's hand field equal to it.

    Example:
        - newPlayer.createHand(cards), given that cards is a valid, non-empty arrayList of Cards, results in newPlayer
            having its hand field set to the Hand containing the arrayList of Cards.
    */
    @Test
    void createHand() {

        // regular hand of cards should show up as Player's hand
        ArrayList<Card> myCards = new ArrayList<>();
        myCards.add(Card.of(2, 1)); // adds a 2 of hearts
        myCards.add(Card.of(3, 2)); // adds a 3 of diamonds

        Player newPlayer = Player.of();
        newPlayer.createHand(myCards);

        Hand myHand = Hand.of(myCards);

        assertEquals(myHand, newPlayer.getHand());

        // empty hand (no cards) should still be assignable
        ArrayList<Card> myCards2 = new ArrayList<>();

        Player newPlayer2 = Player.of();
        newPlayer2.createHand(myCards2);

        Hand myHand2 = Hand.of(myCards2);

        assertEquals(myHand2, newPlayer2.getHand());

    }

    /*
    Test that createCrib functions as expected: creates a Hand object and sets the player's crib field equal to it.

     Example:
        - newPlayer.createCrib(cards), given that cards is a valid, non-empty arrayList of Cards, results in newPlayer
            having its crib field set to the Hand containing the ArrayList of Cards.
     */
    @Test
    void createCrib() {

        // regular hand of cards should show up as Player's hand
        ArrayList<Card> myCards = new ArrayList<>();
        myCards.add(Card.of(2, 1)); // adds a 2 of hearts
        myCards.add(Card.of(3, 2)); // adds a 3 of diamonds

        Player newPlayer = Player.of();
        newPlayer.createCrib(myCards);

        Hand myHand = Hand.of(myCards);

        assertEquals(myHand, newPlayer.getCrib());

        // empty hand (no cards) should still be assignable
        ArrayList<Card> myCards2 = new ArrayList<>();

        Player newPlayer2 = Player.of();
        newPlayer2.createCrib(myCards2);

        Hand myHand2 = Hand.of(myCards2);

        assertEquals(myHand2, newPlayer2.getCrib());

    }

    /*
     Test that Player's hand adds card with addCardToHand.

     Example:
        - newPlayer.addCardToHand(new Card) --> newPlayer.getHand() contains new Card.
     */
    @Test
    void addCardToHand() {
        ArrayList<Card> myCards = new ArrayList<>();
        Card card1 = (Card.of(2, 1)); // adds a 2 of hearts
        Card card2 = (Card.of(3, 2)); // adds a 3 of diamonds
        Card card3 = Card.of(4, 3); // adds a 4 of spades
        myCards.add(card1);
        myCards.add(card2);
        myCards.add(card3);
        Hand myHand = Hand.of(myCards);

        // Player with empty Hand
        Player newPlayer = Player.of();
        newPlayer.createHand(new ArrayList<>());
        newPlayer.addCardToHand(card1);
        newPlayer.addCardToHand(card2);
        newPlayer.addCardToHand(card3);

        assertEquals(myHand, newPlayer.getHand());

    }

    /*
     Test that Player's crib adds card with addCardToCrib.

     Example:
        - newPlayer.addCardToCrib(new Card) --> newPlayer.getCrib() contains new Card.
     */
    @Test
    void addCardToCrib() {
        ArrayList<Card> myCards = new ArrayList<>();
        Card card1 = (Card.of(2, 1)); // adds a 2 of hearts
        Card card2 = (Card.of(3, 2)); // adds a 3 of diamonds
        Card card3 = Card.of(4, 3); // adds a 4 of spades
        myCards.add(card1);
        myCards.add(card2);
        myCards.add(card3);
        Hand myHand = Hand.of(myCards);

        // Player with empty Hand
        Player newPlayer = Player.of();
        newPlayer.createCrib(new ArrayList<>());
        newPlayer.addCardToCrib(card1);
        newPlayer.addCardToCrib(card2);
        newPlayer.addCardToCrib(card3);

        assertEquals(myHand, newPlayer.getCrib());
    }

    /*
     Expected: emptyHand should set player's hand equal to a Hand object w/ its cards field
        set to an empty ArrayList.

     Example:
        - newPlayer.emptyHand() --> newPlayer.getHand() is equal to new Hand with no Cards.
     */

    @Test
    void emptyHand() {

        // regular hand of cards added to Player's hand
        ArrayList<Card> myCards = new ArrayList<>();
        myCards.add(Card.of(2, 1)); // adds a 2 of hearts
        myCards.add(Card.of(3, 2)); // adds a 3 of diamonds

        Player newPlayer = Player.of();
        newPlayer.createHand(myCards);

        newPlayer.emptyHand();
        assertTrue(newPlayer.getHand().getCards().isEmpty()); // emptying hand makes the cards ArrayList empty

        // if createHand() has not been run, player's hand is a Hand with no cards in its ArrayList

        Player newPlayer2 = Player.of();

        newPlayer2.emptyHand();
        assertTrue(newPlayer2.getHand().getCards().isEmpty()); // emptying hand makes the cards ArrayList empty

    }

    /*
     Expected: emptyCrib should set a player's crib equal to a Hand object w/ its cards field
        set to an empty ArrayList.

     Example:
        - newPlayer.emptyCrib() --> newPlayer.getCrib() is equal to new Hand with no Cards.
     */
    @Test
    void emptyCrib() {

        // regular hand of cards added to Player's crib
        ArrayList<Card> myCards = new ArrayList<>();
        myCards.add(Card.of(2, 1)); // adds a 2 of hearts
        myCards.add(Card.of(3, 2)); // adds a 3 of diamonds

        Player newPlayer = Player.of();
        newPlayer.createCrib(myCards);

        newPlayer.emptyCrib();
        assertTrue(newPlayer.getCrib().getCards().isEmpty()); // emptying hand makes the cards ArrayList empty

        // if createHand() has not been run, player's crib is a Hand with no cards in its ArrayList

        Player newPlayer2 = Player.of();

        newPlayer2.emptyCrib();
        assertTrue(newPlayer2.getCrib().getCards().isEmpty()); // emptying hand makes the cards ArrayList empty

    }
}