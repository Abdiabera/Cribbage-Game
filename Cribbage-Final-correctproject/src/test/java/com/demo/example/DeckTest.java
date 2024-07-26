package com.demo.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    /*
    Tests factory method.

    Example:
        - When creating a new Deck, the size should be 52 Cards.
            The first card should be Ace of Clubs; last card, King of Spades.
     */
    @Test
    void of() {
        Deck testDeck = Deck.of();

        Card aceOfClubs = Card.of(1, 0);
        Card kingOfSpades = Card.of(13, 3);

        assertEquals(52, testDeck.getAllCards().size());
        assertEquals(aceOfClubs, testDeck.getCard(0));
        assertEquals(kingOfSpades, testDeck.getCard(51));
    }

    /*
    Expected:
        - Removing and returning a card to the deck should make the deck equal to its
            original state.
     */
    @Test
    void addAndReturnCard() {
        Deck testDeck = Deck.of();
        Deck testDeck2 = Deck.of();

        // decks are the same at the beginning
        assertEquals(testDeck, testDeck2);

        testDeck.shuffle();

        // adding card back in makes deck the same as beginning
        Card drawCard = testDeck.draw();
        testDeck.addCardToDeck(drawCard);
        assertEquals(testDeck, testDeck2);

        // can't add the same card back to the deck. deck will remain unchanged.
        testDeck.addCardToDeck(drawCard);
        assertEquals(testDeck, testDeck2);
    }

    /*
     Expected:
        - If decks have the same cards, return true. Otherwise return false.

     Examples:
        - Deck with Card removed is not equal to full Deck. Return false.
        - Two newly created Decks. Return true.
     */
    @Test
    void equals() {
        Deck testDeck = Deck.of();
        Deck testDeck2 = Deck.of();

        // decks are the same at the beginning
        assertEquals(testDeck, testDeck2);

        // remove card from deck
        testDeck.draw();
        assertNotEquals(testDeck2, testDeck);

        Deck testDeck3 = Deck.of();
        Deck testDeck4 = Deck.of();

        assertEquals(testDeck3, testDeck4);
    }
}