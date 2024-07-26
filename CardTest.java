package com.demo.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    /*
     Test valid constructions of Card objects

     Examples:
         Card.of(3, 1) returns a 3 of hearts Card object
         Card.of(11, 0) returns a jack of clubs Card object
     */
    @Test
    void ofValidConstruction() {
        Card threeOfHearts = Card.of(3, 1);
        Card jackOfClubs = Card.of(11, 0);
        Card kingOfSpades = Card.of(13, 3);

        assertEquals(3, threeOfHearts.getValue());
        assertEquals(Card.Suit.HEARTS, threeOfHearts.getSuit());
        assertEquals(11, jackOfClubs.getValue());
        assertEquals(Card.Suit.CLUBS, jackOfClubs.getSuit());
        assertEquals(13, kingOfSpades.getValue());
        assertEquals(Card.Suit.SPADES, kingOfSpades.getSuit());
    }

    /*
     Test invalid constructions of Card objects

     Examples:
         - Card.of(14, 3) should throw an IllegalArgumentException for value > 13
        -  Card.of(10, -1) should throw an IllegalArgumentException for suit < 0
     */
    @Test
    void ofNotValidConstruction() {

        // illegal suit (-1)
        assertThrows(
                IllegalArgumentException.class,
                () -> Card.of(10, -1));

        // illegal suit (>3)
        assertThrows(
                IllegalArgumentException.class,
                () -> Card.of(10, 4));

        // illegal value (0)
        assertThrows(
                IllegalArgumentException.class,
                () -> Card.of(0, 2));

        // illegal value (-1)
        assertThrows(
                IllegalArgumentException.class,
                () -> Card.of(-1, 2));

        // illegal value (>13)
        assertThrows(
                IllegalArgumentException.class,
                () -> Card.of(14, 3));

        // illegal suit and value
        assertThrows(
                IllegalArgumentException.class,
                () -> Card.of(20, 5));
    }

    /*
     Expected to return suit as enum (hearts, clubs, spades, diamonds)

     Examples:
         - Card w/ value 12 and suit HEARTS returns enum HEARTS
     */
    @Test
    void getSuit() {
        Card queenOfHearts = Card.of(12, 1);
        Card twoOfClubs = Card.of(2, 0);
        Card sevenOfSpades = Card.of(7, 3);
        Card aceOfDiamonds = Card.of(1, 2);

        assertEquals(Card.Suit.HEARTS, queenOfHearts.getSuit());
        assertEquals(Card.Suit.CLUBS, twoOfClubs.getSuit());
        assertEquals(Card.Suit.SPADES, sevenOfSpades.getSuit());
        assertEquals(Card.Suit.DIAMONDS, aceOfDiamonds.getSuit());
    }

    /*
     Expected to return value as int.

     Example:
         - Card w/ value 12 and suit HEARTS returns 12.
     */
    @Test
    void getValue() {
        Card queenOfHearts = Card.of(12, 1);
        Card sevenOfSpades = Card.of(7, 3);
        Card twoOfClubs = Card.of(2, 0);
        Card aceOfDiamonds = Card.of(1, 2);

        assertEquals(12, queenOfHearts.getValue());
        assertEquals(7, sevenOfSpades.getValue());
        assertEquals(2, twoOfClubs.getValue());
        assertEquals(1, aceOfDiamonds.getValue());
    }

    /*
    Expected to return scoring value as int. Any card with value over 9 returns 10.

    Example:
        - Card w/ value 2 and suit HEARTS returns 2
        - Card w/ value 12 and suit DIAMONDS returns 10
     */
    @Test
    void getScoringValue() {
        Card aceOfHearts = Card.of(1, 1);
        Card twoOfHearts = Card.of(2, 1);
        Card tenOfClubs = Card.of(10, 0);
        Card jackOfSpades = Card.of(11, 3);
        Card queenOfDiamonds = Card.of(12, 2);
        Card kingOfDiamonds = Card.of(13, 2);

        assertEquals(1, aceOfHearts.getScoringValue());
        assertEquals(2, twoOfHearts.getScoringValue());
        assertEquals(10, tenOfClubs.getScoringValue());
        assertEquals(10, jackOfSpades.getScoringValue());
        assertEquals(10, queenOfDiamonds.getScoringValue());
        assertEquals(10, kingOfDiamonds.getScoringValue());
    }

    /*
    Expected to return value name as string.

    Examples:
        - Card w/ value 2 and suit CLUBS returns "2"
        - Card w/ value 12 and suit HEARTS returns "Queen"
    */
    @Test
    void getValueName() {
        Card twoOfClubs = Card.of(2, 0);
        Card queenOfHearts = Card.of(12, 1);

        assertEquals("2", twoOfClubs.getValueName());
        assertEquals("Queen", queenOfHearts.getValueName());

    }

    /*
    Expected to return suit name as string.

    Examples:
        - Card w/ value 2 and suit CLUBS returns "Clubs"
        - Card w/ value 12 and suit HEARTS returns "Hearts"
     */
    @Test
    void getSuitName() {
        Card twoOfClubs = Card.of(2, 0);
        Card queenOfHearts = Card.of(12, 1);

        assertEquals("Clubs", twoOfClubs.getSuitName());
        assertEquals("Hearts", queenOfHearts.getSuitName());
    }

    /*
    Expected to return suit as an int. CLUBS = 0, HEARTS = 1, DIAMONDS = 2, SPADES = 3

    Examples:
        - Card w/ value 2 and suit CLUBS returns 0
        - Card w/ value 12 and suit HEARTS returns 1
     */
    @Test
    void getSuitAsInt() {
        Card twoOfClubs = Card.of(2, 0);
        Card queenOfHearts = Card.of(12, 1);

        assertEquals(0, twoOfClubs.getSuitAsInt());
        assertEquals(1, queenOfHearts.getSuitAsInt());
    }

    /*
    Expected to return a string representing the card (suit and value)

    Examples:
        - Card w/ value 2 and suit CLUBS returns "2 of Clubs"
        - Card w/ value 12 and suit DIAMONDS returns "Queen of Diamonds"
     */
    @Test
    void toStringTest() {
        Card twoOfClubs = Card.of(2, 0);
        Card kingOfDiamonds = Card.of(13, 2);

        assertEquals("2 of Clubs", twoOfClubs.toString());
        assertEquals("King of Diamonds", kingOfDiamonds.toString());

    }

    /*
     Expected to return True if cards have same suit and value, False otherwise.

     Examples:
         - twoOfClubs and threeOfDiamonds --> false
         - twoOfClubs and threeOfClubs --> false
         - twoOfClubs and twoOfDiamonds --> false
         - twoOfClubs and another object twoOfClubs2 --> true
         - twoOfClubs and itself --> true
     */
    @Test
    void equals() {
        Card twoOfClubs = Card.of(2, 0);
        Card threeOfDiamonds = Card.of(2, 2);
        Card threeOfClubs = Card.of(3, 0);
        Card twoOfDiamonds = Card.of(2, 2);
        Card twoOfClubs2 = Card.of(2, 0);

        assertNotEquals(twoOfClubs, threeOfDiamonds); // different suit, different value
        assertNotEquals(twoOfClubs, threeOfClubs); // same suit, different value
        assertNotEquals(twoOfClubs, twoOfDiamonds); // different suit, same value
        assertEquals(twoOfClubs, twoOfClubs2); // same suit, same value
        assertEquals(twoOfClubs, twoOfClubs); // same object


    }



}