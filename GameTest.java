package com.demo.example;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    /*
    Tests the constructor.

    Checks that a game is created, and isOver is false.
     */
    @Test
    void constructor() {
        Game newGame = Game.of();
        assertFalse(newGame.isOver());
    }

    /*
     Expected: doesSomeoneHaveLegalCards() returns true when either Player A or Player B has a legal card to
        play, defined as:
            - card exists in the hand
            - card's scoring value is less than 31 - runningSum

     Examples:
         - if Player A has a Queen of Hearts and the running sum is 20, returns True
         - if Player B has a four of spades and the running sum is 26, returns True
         - if Player A has empty hand, Player B has four of spades, and running sum is 30, returns False
         - if both Player A and Player B have empty hands, returns False
     */
    @Test
    void doesSomeoneHaveLegalCards() {

        Game newGame = Game.of();

        Player playerA = Player.of();
        Player playerB = Player.of();

        playerA.createHand(new ArrayList<>());
        playerB.createHand(new ArrayList<>());

        playerA.addCardToHand(Card.of(4, 0)); // Add 4 of clubs to A
        playerB.addCardToHand(Card.of(11, 2)); // Add Jack of diamonds to B

        assertTrue(newGame.doesSomeoneHaveLegalCards(playerA, playerB, 25)); // A has legal card to play
        assertTrue(newGame.doesSomeoneHaveLegalCards(playerA, playerB, 15)); // A and B have legal card to play

        playerA.emptyHand();

        assertTrue(newGame.doesSomeoneHaveLegalCards(playerA, playerB, 20)); // B has legal card to play
        assertFalse(newGame.doesSomeoneHaveLegalCards(playerA, playerB, 30)); // neither have legal card to play

        playerB.emptyHand();

        assertFalse(newGame.doesSomeoneHaveLegalCards(playerA, playerB, 10)); // both player hands empty
    }

    /*
    Expected: returns true if player has a Card whose scoringValue is less than 31 - runningSum

    Examples:
        - if playerA has a Five of Hearts and the running sum is 25, returns true
        - if playerB has a Five of Hearts and the running sum is 28, returns false
     */
    @Test
    void hasLegalCard() {
        Game newGame = Game.of();

        Player playerA = Player.of();
        playerA.createHand(new ArrayList<>());
        playerA.addCardToHand(Card.of(5, 1));

        assertTrue(newGame.hasLegalCard(playerA, 25));
        assertFalse(newGame.hasLegalCard(playerA, 28));
    }

    /*
    Returns true if card can legally be played, based on the scoring value
       of the card and the current running sum.

    Example: given a 5 of diamonds...
        - if the runningSum is 29, return false
        - if the runningSum is 2, return true
     */
    @Test
    void isLegalCard() {
        Game newGame = Game.of();

        assertFalse(newGame.isLegalCard(Card.of(5, 2), 29));
        assertTrue(newGame.isLegalCard(Card.of(5, 2), 2));
    }

    /*
     Calculates the points in an ArrayList, with the last card being the most recently played.

     Examples (disregarding suit, as it's not consequential here):
         7, 8 —> 2 points (fifteen)
         7, 8, 9 —> 3 points (run)
         8, 7, 9 —> 3 points (run)
         2, 3, 4, 4 —> 2 (pair)
         2, 3, 4, 4, 4 —> 6 (three of a kind)
         2, 3, 4, 4, 4, 4, —> 12 (four of a kind)
     */
    @Test
    void calculatePointsInArray() {
        Game newGame = Game.of();

        // cards w/ value 7 and 8 make a 15. Returns 2 points.
        ArrayList<Card> cardArray1 = new ArrayList<>();
        cardArray1.add(Card.of(7, 1));
        cardArray1.add(Card.of(8, 2));
        assertEquals(2, newGame.calculatePointsInArray(cardArray1));

        // cards w/ value 7, 8, 9 make a run of 3. Returns 3 points.
        ArrayList<Card> cardArray2 = new ArrayList<>();
        cardArray2.add(Card.of(7, 1));
        cardArray2.add(Card.of(8, 2));
        cardArray2.add(Card.of(9, 0));
        assertEquals(3, newGame.calculatePointsInArray(cardArray2));

        // cards w/ value 8, 7, 9 make a run of 3 (OK that they're out of order). Returns 3 points.
        ArrayList<Card> cardArray3 = new ArrayList<>();
        cardArray3.add(Card.of(8, 2));
        cardArray3.add(Card.of(7, 1));
        cardArray3.add(Card.of(9, 0));
        assertEquals(3, newGame.calculatePointsInArray(cardArray3));

        // cards w/ value 2, 3, 4, 4 make a pair. Returns 2 points.
        ArrayList<Card> cardArray4 = new ArrayList<>();
        cardArray4.add(Card.of(2, 2));
        cardArray4.add(Card.of(3, 1));
        cardArray4.add(Card.of(4, 0));
        cardArray4.add(Card.of(4, 1));
        assertEquals(2, newGame.calculatePointsInArray(cardArray4));

        // cards w/ value 2, 3, 4, 4, 4 make a three of a kind. Returns 6 points.
        ArrayList<Card> cardArray5 = new ArrayList<>();
        cardArray5.add(Card.of(2, 2));
        cardArray5.add(Card.of(3, 1));
        cardArray5.add(Card.of(4, 0));
        cardArray5.add(Card.of(4, 1));
        cardArray5.add(Card.of(4, 2));
        assertEquals(6, newGame.calculatePointsInArray(cardArray5));

        // cards w/ value 2, 3, 4, 4, 4, 4 make a four of a kind. Returns 12 points.
        ArrayList<Card> cardArray6 = new ArrayList<>();
        cardArray6.add(Card.of(2, 2));
        cardArray6.add(Card.of(3, 1));
        cardArray6.add(Card.of(4, 0));
        cardArray6.add(Card.of(4, 1));
        cardArray6.add(Card.of(4, 2));
        cardArray6.add(Card.of(4, 2));
        assertEquals(12, newGame.calculatePointsInArray(cardArray6));

        // cards w/ value 5, 5, 5 make fifteen and three of a kind. Returns 8 points.
        ArrayList<Card> cardArray7 = new ArrayList<>();
        cardArray7.add(Card.of(5, 2));
        cardArray7.add(Card.of(5, 1));
        cardArray7.add(Card.of(5, 0));
        assertEquals(8, newGame.calculatePointsInArray(cardArray7));
    }

    /*
    Calculates and returns points for reaching 15 in an ArrayList.

    Example (disregarding suit, as it's not consequential here):
        - Queen, 4, Ace --> 2 points
        - 7, 8 --> 2 points
        - 10, 3 --> 0 points
    */
    @Test
    void gotFifteen() {
        Game newGame = Game.of();

        // cards w/ value 9 and 6 make a 15. Returns 2 points.
        ArrayList<Card> cardArray1 = new ArrayList<>();
        cardArray1.add(Card.of(9, 1));
        cardArray1.add(Card.of(6, 2));

        assertEquals(2, newGame.gotFifteen(cardArray1));

        // cards w/ value 8 and 6 make 14. Returns 0 points.
        ArrayList<Card> cardArray2 = new ArrayList<>();
        cardArray2.add(Card.of(8, 1));
        cardArray2.add(Card.of(6, 2));

        assertEquals(0, newGame.gotFifteen(cardArray2));

        // cards w/ value 10 and 5 make 15. Returns 2 points.
        ArrayList<Card> cardArray3 = new ArrayList<>();
        cardArray3.add(Card.of(10, 1));
        cardArray3.add(Card.of(5, 2));

        assertEquals(2, newGame.gotFifteen(cardArray3));

        // cards w/ value Jack and 5 make 15. Returns 2 points.
        ArrayList<Card> cardArray4 = new ArrayList<>();
        cardArray4.add(Card.of(11, 1));
        cardArray4.add(Card.of(5, 2));

        assertEquals(2, newGame.gotFifteen(cardArray3));

        // cards w/ value King, 4, Ace make 15. Returns 2 points.
        ArrayList<Card> cardArray5 = new ArrayList<>();
        cardArray5.add(Card.of(13, 1));
        cardArray5.add(Card.of(4, 2));
        cardArray5.add(Card.of(1, 3));

        assertEquals(2, newGame.gotFifteen(cardArray5));

        // cards w/ value King, 4, Ace, 3 make 18. Returns 0 points.
        ArrayList<Card> cardArray6 = new ArrayList<>();
        cardArray6.add(Card.of(13, 1));
        cardArray6.add(Card.of(4, 2));
        cardArray6.add(Card.of(1, 3));
        cardArray6.add(Card.of(3, 0));

        assertEquals(0, newGame.gotFifteen(cardArray6));
    }

    /*
    Expected: 2 or 0 points, depending on if sum of ArrayList scoringValues is 15 or not.

    Example (disregarding suit, as it's not consequential here):
        - Queen, 4, Ace --> 2 points
        - 7, 8 --> 2 points
        - 10, 3 --> 0 points
    */
    @Test
    void gotMultiples() {
        Game newGame = Game.of();

        // Empty array has no multiples. Returns 0 points.
        ArrayList<Card> cardArray0 = new ArrayList<>();
        assertEquals(0, newGame.gotMultiples(cardArray0));

        // Array with only one card has no multiples. Returns 0 points.
        ArrayList<Card> cardArray1 = new ArrayList<>();
        cardArray1.add(Card.of(9, 1));

        assertEquals(0, newGame.gotMultiples(cardArray1));

        // Array with two cards of value 9 has a pair. Returns 2 points.
        ArrayList<Card> cardArray2 = new ArrayList<>();
        cardArray2.add(Card.of(9, 1));
        cardArray2.add(Card.of(9, 2));

        assertEquals(2, newGame.gotMultiples(cardArray2));

        // Array with a separated pair doesn't count. Returns 0 points.
        ArrayList<Card> cardArray3 = new ArrayList<>();
        cardArray3.add(Card.of(9, 1));
        cardArray3.add(Card.of(6, 1));
        cardArray3.add(Card.of(9, 2));

        assertEquals(0, newGame.gotMultiples(cardArray3));

        // Array with three cards of value 9 has a three of a kind. Returns 6 points.
        ArrayList<Card> cardArray4 = new ArrayList<>();
        cardArray4.add(Card.of(9, 1));
        cardArray4.add(Card.of(9, 2));
        cardArray4.add(Card.of(9, 3));

        assertEquals(6, newGame.gotMultiples(cardArray4));

        // Array with three cards of value 4, separated. Returns 0 points.
        ArrayList<Card> cardArray5 = new ArrayList<>();
        cardArray5.add(Card.of(4, 1));
        cardArray5.add(Card.of(4, 2));
        cardArray5.add(Card.of(3, 2));
        cardArray5.add(Card.of(4, 3));

        assertEquals(0, newGame.gotMultiples(cardArray5));

        // Array with four cards of value 2. Returns 12 points.
        ArrayList<Card> cardArray6 = new ArrayList<>();
        cardArray6.add(Card.of(2, 1));
        cardArray6.add(Card.of(2, 2));
        cardArray6.add(Card.of(2, 0));
        cardArray6.add(Card.of(2, 3));

        assertEquals(12, newGame.gotMultiples(cardArray6));
    }

    /*
    Expected: n points (n = length of run), if run is present

    Example:
        - Player A plays a 2 of Hearts --> worth 0 points
        - Player B plays a 2 of Diamonds --> worth 2 points (pair)
        - Player A plays a 2 of Clubs --> worth 6 points (three of a kind)
        - Player B plays a Queen of Clubs --> worth 0 points
        - Player A plays a 2 of Spades --> worth 0 points (four of a kind not counted because of interrupting card: Queen of Clubs)
     */
    @Test
    void gotRun() {
        Game newGame = Game.of();

        // Empty array has no runs. Returns 0 points.
        ArrayList<Card> cardArray0 = new ArrayList<>();
        assertEquals(0, newGame.gotRun(cardArray0));

        // Array with only one card has no runs. Returns 0 points.
        ArrayList<Card> cardArray1 = new ArrayList<>();
        cardArray1.add(Card.of(9, 1));
        assertEquals(0, newGame.gotRun(cardArray1));

        // Array with only two cards has no runs. Returns 0 points.
        ArrayList<Card> cardArray2 = new ArrayList<>();
        cardArray2.add(Card.of(9, 1));
        cardArray2.add(Card.of(8, 2));
        assertEquals(0, newGame.gotRun(cardArray2));

        // Array with values 7, 8, 9 forms a run. Returns 3 points.
        ArrayList<Card> cardArray3 = new ArrayList<>();
        cardArray3.add(Card.of(7, 1));
        cardArray3.add(Card.of(8, 2));
        cardArray3.add(Card.of(9, 2));

        newGame.gotRun(cardArray3);
        assertEquals(3, newGame.gotRun(cardArray3));

        // Array with values 7, 9, 8 forms a run (out of order OK). Returns 3 points.
        ArrayList<Card> cardArray4 = new ArrayList<>();
        cardArray4.add(Card.of(7, 1));
        cardArray4.add(Card.of(9, 2));
        cardArray4.add(Card.of(8, 2));
        assertEquals(3, newGame.gotRun(cardArray4));

        // Array with values 9, 8, 7 forms a run (backwards OK). Returns 3 points.
        ArrayList<Card> cardArray5 = new ArrayList<>();
        cardArray5.add(Card.of(9, 2));
        cardArray5.add(Card.of(8, 2));
        cardArray5.add(Card.of(7, 1));
        assertEquals(3, newGame.gotRun(cardArray5));

        // Array with values 2, 1, 4, 3 forms a run (out of order OK). Returns 4 points.
        ArrayList<Card> cardArray6 = new ArrayList<>();
        cardArray6.add(Card.of(2, 2));
        cardArray6.add(Card.of(1, 2));
        cardArray6.add(Card.of(4, 1));
        cardArray6.add(Card.of(3, 1));
        assertEquals(4, newGame.gotRun(cardArray6));

        // Array with values 2, 1, 3, 7 does not forms a run (top card not included). Returns 0 points.
        ArrayList<Card> cardArray7 = new ArrayList<>();
        cardArray7.add(Card.of(2, 2));
        cardArray7.add(Card.of(1, 2));
        cardArray7.add(Card.of(3, 1));
        cardArray7.add(Card.of(7, 1));
        assertEquals(0, newGame.gotRun(cardArray7));
    }


    /*
    Expected: if currentPlayer is playerA,
        result of toggleCurrentPlayer() makes currentPlayer playerB
     */
    @Test
    void toggleCurrentPlayer() {
        Game newGame = Game.of();

        Player firstPlayer;
        if (newGame.getCurrentPlayer().equals(newGame.getPlayerA())) {
            firstPlayer = newGame.getPlayerA();
        } else {
            firstPlayer = newGame.getPlayerB();
        }

        // toggling the player should switch the current player
        newGame.toggleCurrentPlayer();
        assertNotEquals(newGame.getCurrentPlayer(), firstPlayer);

        // toggling back should switch to original
        newGame.toggleCurrentPlayer();
        assertEquals(newGame.getCurrentPlayer(), firstPlayer);
    }

    /*
    Expected: return

    Examples:
         - if Player A has a Queen of Hearts, and playerB has an empty hand, returns true
         - if Player A has a Queen of Hearts and Player B has a four of spades, returns true
         - if Player A has empty hand, Player B has four of spades, returns true
         - if both Player A and Player B have empty hands, returns false
     */
    @Test
    void doesSomeoneHaveCards() {
        Game newGame = Game.of();

        newGame.dealCard(newGame.getPlayerA());

        // A has a card, returns true
        assertTrue(newGame.doesSomeoneHaveCards());

        newGame.dealCard(newGame.getPlayerB());
        // A and B have cards, return true
        assertTrue(newGame.doesSomeoneHaveCards());

        // B has cards, return true
        newGame.getPlayerA().emptyHand();
        assertTrue(newGame.doesSomeoneHaveCards());

        // neither player has cards, return false
        newGame.getPlayerB().emptyHand();
        assertFalse(newGame.doesSomeoneHaveCards());
    }

    /*
    Checks that all hands and cribs are empty after reset round, faceUpCard is null
     */
    @Test
    void resetRound() {
        Game newGame = Game.of();
        // TODO deal cards to players?

        newGame.resetRound();

        assertEquals(0, newGame.getPlayerA().getHand().getCards().size());
        assertEquals(0, newGame.getPlayerA().getCrib().getCards().size());
        assertEquals(0, newGame.getPlayerB().getHand().getCards().size());
        assertEquals(0, newGame.getPlayerB().getCrib().getCards().size());

        Deck newDeck = Deck.of();
        assertEquals(newDeck, newGame.getDeck());

        assertNull(newGame.getFaceUpCard());

        // TODO: TEST FOR DEALER TOGGLE
    }

    /*
    Adding a negative value to the runningSum should throw Exception.
     */
    @Test
    void runningSum() {
        Game newGame = Game.of();
        assertThrows(IllegalArgumentException.class, () -> newGame.addToRunningSum(-5));
    }
}