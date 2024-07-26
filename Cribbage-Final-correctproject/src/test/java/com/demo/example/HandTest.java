package com.demo.example;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class HandTest {

    /*
    Tests that the constructor creates an empty ArrayList to hold Cards.
     */
    @Test
    void testConstructor(){
        Hand newHand = Hand.of(new ArrayList<>());

        assertTrue(newHand.getCards().isEmpty());
    }

    /*
    Expected to return an ArrayList of Strings that represent Cards.

    Example:
        - Hand of Cards: value 8, suit SPADES and value 12, suit DIAMONDS
            returns ArrayList with "8 of Spades" and "Queen of Diamonds".
     */
    @Test
    void getHandAsStrings() {
        ArrayList<String> goalOutput= new ArrayList<>();
        goalOutput.add("8 of Spades");
        goalOutput.add("Queen of Diamonds");

        ArrayList<Card> cardsForHand = new ArrayList<>();
        cardsForHand.add(Card.of(8, 3));
        cardsForHand.add(Card.of(12, 2));
        Hand testHand = Hand.of(cardsForHand);

        assertEquals(goalOutput, testHand.getHandAsStrings());

    }

    /*
    Expected to return the correct score for the hand: 14 points.

    Cards:
        - 7 of Diamonds
        - 8 of Clubs
        - 8 of Diamonds
        - 9 of Hearts
        - 10 of Hearts (faceUpCard)

    Score:
        - 4 points from fifteens (7, 8 clubs) and (7, 8 diamonds)
        - 6 points from runs (7, 8 clubs, 9) and (7, 8 diamonds, 9)
        - 2 points from pairs (8 clubs, 8 diamonds)
        - 0 points from flushes
     TOTAL EXPECTED SCORE: 14 points
     */
    @Test
    void testHandScoringFourteen(){
        Card sevenDiamonds = Card.of(7, 2);
        Card eightClubs = Card.of(8, 0);
        Card eightDiamonds = Card.of(8, 2);
        Card nineHearts = Card.of(9, 1);
        Card tenHearts = Card.of(10, 1);
        ArrayList<Card> cardsOfHandOne = new ArrayList<>();
        cardsOfHandOne.add(sevenDiamonds);
        cardsOfHandOne.add(eightDiamonds);
        cardsOfHandOne.add(eightClubs);
        cardsOfHandOne.add(nineHearts);

        Hand handOne = Hand.of(cardsOfHandOne);

        int expectedFifteensPoints = 4;
        int expectedRunsPoints = 8;
        int expectedMultiplesPoints = 2;
        int expectedFlushPoints = 0;
        int expectedHandScore = 14;

        assertEquals(expectedFifteensPoints, handOne.calculateFifteensPoints(tenHearts));
        assertEquals(expectedRunsPoints, handOne.countRunPoints(tenHearts));
        assertEquals(expectedMultiplesPoints, handOne.countPairsPoints(tenHearts));
        assertEquals(expectedFlushPoints, handOne.countFlushPoints(tenHearts));
        assertEquals(expectedHandScore, handOne.calculatePoints(tenHearts));
    }

    /*
    Expected to return the correct score for the hand: 20 points.

    Cards:
        - Queen of Clubs
        - Queen of Hearts
        - Queen of Diamonds
        - Queen of Spades
        - 5 of Clubs (faceUpCard)

    Score:
        - 8 points from fifteens (Queen, 5) x four different Queens
        - 0 points from runs
        - 12 points from pairs (four Queens)
        - 0 points from flushes
     TOTAL EXPECTED SCORE: 20 points
     */
    @Test
    void testHandScoringTwenty(){
        Card queenClubs = Card.of(12, 0);
        Card queenHearts = Card.of(12, 1);
        Card queenDiamonds = Card.of(12, 2);
        Card queenSpades = Card.of(12, 3);
        Card fiveClubs = Card.of(5, 0);

        ArrayList<Card> cardsOfHandTwo = new ArrayList<>();
        cardsOfHandTwo.add(queenClubs);
        cardsOfHandTwo.add(queenHearts);
        cardsOfHandTwo.add(queenDiamonds);
        cardsOfHandTwo.add(queenSpades);

        Hand handTwo = Hand.of(cardsOfHandTwo);

        int expectedMultiplesPoints = 12;
        int expectedFifteensPoints = 8;
        int expectedHandScore = 20;

        assertEquals(expectedFifteensPoints, handTwo.calculateFifteensPoints(fiveClubs));
        assertEquals(expectedMultiplesPoints, handTwo.countPairsPoints(fiveClubs));
        assertEquals(expectedHandScore, handTwo.calculatePoints(fiveClubs));

    }

    /*
    Expected to return the correct score for the hand: 20 points.

    Cards:
        - 5 of Hearts
        - 5 of Diamonds
        - 5 of Spades
        - 10 of Clubs
        - Jack of Hearts (faceUpCard)

    Score:
        - 14 points from fifteens (10, 5) x 3 different 5s, (Jack, 5) x 3 different 5s, (5, 5, 5)
        - 0 points from runs
        - 6 points from pairs (three 5s)
        - 0 points from flushes
     TOTAL EXPECTED SCORE: 20 points
     */
    @Test
    void testHandScoringTripleFiveTenJack() {
        Card fiveHearts = Card.of(5, 1);
        Card fiveDiamonds = Card.of(5, 2);
        Card fiveSpades = Card.of(5, 3);
        Card tenClubs = Card.of(10, 0);
        Card jackHearts = Card.of(11, 1);

        ArrayList<Card> cardsOfHandThree = new ArrayList<>();
        cardsOfHandThree.add(fiveHearts);
        cardsOfHandThree.add(fiveDiamonds);
        cardsOfHandThree.add(fiveSpades);
        cardsOfHandThree.add(tenClubs);

        Hand handThree = Hand.of(cardsOfHandThree);

        int expectedFifteensPoints = 14; // 3 combinations of 15
        int expectedMultiplesPoints = 6;  // 3 of a kind (5s)
        int expectedHandScore = 20;

        assertEquals(expectedFifteensPoints, handThree.calculateFifteensPoints(jackHearts));
        assertEquals(expectedMultiplesPoints, handThree.countPairsPoints(jackHearts));
        assertEquals(expectedHandScore, handThree.calculatePoints(jackHearts));
    }

    /*
    Expected to return the correct score for the hand: 0 points.

    Cards:
        - 2 of Hearts
        - 7 of Spades
        - 9 of Spades
        - Jack of Spades
        - Ace of Hearts (faceUpCard)

    Score:
        - 0 points from fifteens
        - 0 points from runs
        - 0 points from pairs
        - 0 points from flushes
     TOTAL EXPECTED SCORE: 0 points
     */
    @Test
    void testHandScoringNoScore() {
        Card twoHearts = Card.of(2, 1);
        Card sevenSpades = Card.of(7, 3);
        Card nineSpades = Card.of(9, 3);
        Card jackSpades = Card.of(11, 3);
        Card aceHearts = Card.of(1, 1);

        ArrayList<Card> cardsOfHandFour = new ArrayList<>();
        cardsOfHandFour.add(twoHearts);
        cardsOfHandFour.add(sevenSpades);
        cardsOfHandFour.add(nineSpades);
        cardsOfHandFour.add(jackSpades);

        Hand handFour = Hand.of(cardsOfHandFour);

        int expectedFifteensPoints = 0;
        int expectedMultiplesPoints = 0;
        int expectedHandScore = 0;

        assertEquals(expectedFifteensPoints, handFour.calculateFifteensPoints(aceHearts));
        assertEquals(expectedMultiplesPoints, handFour.countPairsPoints(aceHearts));
        assertEquals(expectedHandScore, handFour.calculatePoints(aceHearts));
    }

    /*
    Expected to return the correct score for the hand: 5 points (including bonus flush point
        for faceUpCard).

    Cards:
        - 2 of Spades
        - 7 of Spades
        - 9 of Spades
        - Jack of Spades
        - Ace of Spades (faceUpCard)

    Score:
        - 0 points from fifteens
        - 0 points from runs
        - 0 points from pairs
        - 5 points from flushes (all cards in hand and faceUpcard are Spades)
     TOTAL EXPECTED SCORE: 5 points
     */
    @Test
    void testHandScoringFlushWithAce() {
        Card twoSpades = Card.of(2, 3);
        Card sevenSpades = Card.of(7, 3);
        Card nineSpades = Card.of(9, 3);
        Card jackSpades = Card.of(11, 3);
        Card aceSpades = Card.of(1, 3);

        ArrayList<Card> cardsOfHandFive = new ArrayList<>();
        cardsOfHandFive.add(twoSpades);
        cardsOfHandFive.add(sevenSpades);
        cardsOfHandFive.add(nineSpades);
        cardsOfHandFive.add(jackSpades);

        Hand handFive = Hand.of(cardsOfHandFive);

        int expectedFlushPoints = 5;
        int expectedHandScore = 5;

        assertEquals(expectedFlushPoints, handFive.countFlushPoints(aceSpades));
        assertEquals(expectedHandScore, handFive.calculatePoints(aceSpades));
    }






}