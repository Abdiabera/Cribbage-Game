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

        newGame.getPlayerA().createHand(new ArrayList<>());
        newGame.getPlayerB().createHand(new ArrayList<>());

        newGame.getPlayerA().addCardToHand(Card.of(4, 0)); // Add 4 of clubs to A
        assertTrue(newGame.doesSomeoneHaveLegalCards()); // A has legal card to play

        newGame.getPlayerB().addCardToHand(Card.of(11, 2)); // Add Jack of diamonds to B
        assertTrue(newGame.doesSomeoneHaveLegalCards()); // A and B have legal card to play

        newGame.getPlayerA().emptyHand(); // empty A's hand
        assertTrue(newGame.doesSomeoneHaveLegalCards()); // B has legal card to play

        newGame.getPlayerB().emptyHand();
        assertFalse(newGame.doesSomeoneHaveLegalCards()); // both player hands empty
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
    void calculatePointsInPlayedCards() {
        Game newGame = Game.of();
        newGame.resetPlayedCards();
        newGame.setGameStatus(Game.GameStatus.DEAL);

        // cards w/ value 7 and 8 make a 15. Returns 2 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(8, 2));
        newGame.addCardToPlayedCards(Card.of(7, 1));
        assertEquals(2, newGame.calculatePointsInPlayedCards());

        // cards w/ value 7, 8, 9 make a run of 3. Returns 3 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(7, 1));
        newGame.addCardToPlayedCards(Card.of(8, 2));
        newGame.addCardToPlayedCards(Card.of(9, 0));
        assertEquals(3, newGame.calculatePointsInPlayedCards());

        // cards w/ value 8, 7, 9 make a run of 3 (OK that they're out of order). Returns 3 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(8, 2));
        newGame.addCardToPlayedCards(Card.of(7, 1));
        newGame.addCardToPlayedCards(Card.of(9, 0));
        assertEquals(3, newGame.calculatePointsInPlayedCards());

        // cards w/ value 2, 3, 4, 4 make a pair. Returns 2 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(2, 2));
        newGame.addCardToPlayedCards(Card.of(3, 2));
        newGame.addCardToPlayedCards(Card.of(4, 2));
        newGame.addCardToPlayedCards(Card.of(4, 1));
        assertEquals(2, newGame.calculatePointsInPlayedCards());

        // cards w/ value 2, 3, 4, 4, 4 make a three of a kind. Returns 6 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(2, 2));
        newGame.addCardToPlayedCards(Card.of(3, 1));
        newGame.addCardToPlayedCards(Card.of(4, 0));
        newGame.addCardToPlayedCards(Card.of(4, 1));
        newGame.addCardToPlayedCards(Card.of(4, 2));
        assertEquals(6, newGame.calculatePointsInPlayedCards());

        // cards w/ value 2, 3, 4, 4, 4, 4 make a four of a kind. Returns 12 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(2, 2));
        newGame.addCardToPlayedCards(Card.of(3, 1));
        newGame.addCardToPlayedCards(Card.of(4, 0));
        newGame.addCardToPlayedCards(Card.of(4, 1));
        newGame.addCardToPlayedCards(Card.of(4, 2));
        newGame.addCardToPlayedCards(Card.of(4, 3));
        assertEquals(12, newGame.calculatePointsInPlayedCards());

        // cards w/ value 5, 5, 5 make fifteen and three of a kind. Returns 8 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(5, 2));
        newGame.addCardToPlayedCards(Card.of(5, 1));
        newGame.addCardToPlayedCards(Card.of(5, 0));
        assertEquals(8, newGame.calculatePointsInPlayedCards());
    }

    /*
    Calculates and returns points for reaching 15 in an ArrayList.

    Example (disregarding suit, as it's not consequential here):
        - Queen, 4, Ace --> 2 points
        - 7, 8 --> 2 points
        - 10, 3 --> 0 points
    */
    @Test
    void gotFifteenInPlayedCards() {
        Game newGame = Game.of();
        newGame.resetPlayedCards();
        newGame.setGameStatus(Game.GameStatus.DEAL);

        // cards w/ value 9 and 6 make a 15. Returns 2 points.
        newGame.addCardToPlayedCards(Card.of(9, 1));
        newGame.addCardToPlayedCards(Card.of(6, 2));
        assertEquals(2, newGame.gotFifteenInPlayedCards());

        // cards w/ value 8 and 6 make 14. Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(8, 1));
        newGame.addCardToPlayedCards(Card.of(6, 2));

        assertEquals(0, newGame.gotFifteenInPlayedCards());

        // cards w/ value 10 and 5 make 15. Returns 2 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(10, 1));
        newGame.addCardToPlayedCards(Card.of(5, 2));

        assertEquals(2, newGame.gotFifteenInPlayedCards());

        // cards w/ value Jack and 5 make 15. Returns 2 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(11, 1));
        newGame.addCardToPlayedCards(Card.of(5, 2));

        assertEquals(2, newGame.gotFifteenInPlayedCards());

        // cards w/ value King, 4, Ace make 15. Returns 2 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(13, 1));
        newGame.addCardToPlayedCards(Card.of(4, 2));
        newGame.addCardToPlayedCards(Card.of(1, 3));

        assertEquals(2, newGame.gotFifteenInPlayedCards());

        // cards w/ value King, 4, Ace, 3 make 18. Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(13, 1));
        newGame.addCardToPlayedCards(Card.of(4, 2));
        newGame.addCardToPlayedCards(Card.of(1, 3));
        newGame.addCardToPlayedCards(Card.of(3, 0));

        assertEquals(0, newGame.gotFifteenInPlayedCards());
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
        newGame.resetPlayedCards();
        newGame.setGameStatus(Game.GameStatus.DEAL);

        // Empty array has no multiples. Returns 0 points.
        newGame.resetPlayedCards();
        assertEquals(0, newGame.gotMultiplesInPlayedCards());

        // Array with only one card has no multiples. Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(9, 1));

        assertEquals(0, newGame.gotMultiplesInPlayedCards());

        // Array with two cards of value 9 has a pair. Returns 2 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(9, 1));
        newGame.addCardToPlayedCards(Card.of(9, 2));

        assertEquals(2, newGame.gotMultiplesInPlayedCards());

        // Array with a separated pair doesn't count. Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(9, 1));
        newGame.addCardToPlayedCards(Card.of(6, 1));
        newGame.addCardToPlayedCards(Card.of(9, 2));

        assertEquals(0, newGame.gotMultiplesInPlayedCards());

        // Array with three cards of value 9 has a three of a kind. Returns 6 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(9, 1));
        newGame.addCardToPlayedCards(Card.of(9, 2));
        newGame.addCardToPlayedCards(Card.of(9, 3));

        assertEquals(6, newGame.gotMultiplesInPlayedCards());

        // Array with three cards of value 4, separated. Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(4, 1));
        newGame.addCardToPlayedCards(Card.of(4, 2));
        newGame.addCardToPlayedCards(Card.of(3, 2));
        newGame.addCardToPlayedCards(Card.of(4, 3));

        assertEquals(0, newGame.gotMultiplesInPlayedCards());

        // Array with four cards of value 2. Returns 12 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(2, 1));
        newGame.addCardToPlayedCards(Card.of(2, 2));
        newGame.addCardToPlayedCards(Card.of(2, 0));
        newGame.addCardToPlayedCards(Card.of(2, 3));

        assertEquals(12, newGame.gotMultiplesInPlayedCards());
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
        newGame.resetPlayedCards();
        newGame.setGameStatus(Game.GameStatus.DEAL);

        // Empty array has no runs. Returns 0 points.
        newGame.resetPlayedCards();
        assertEquals(0, newGame.gotRunInPlayedCards());

        // Array with only one card has no runs. Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(9, 1));
        assertEquals(0, newGame.gotRunInPlayedCards());

        // Array with only two cards has no runs. Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(9, 1));
        newGame.addCardToPlayedCards(Card.of(8, 2));
        assertEquals(0, newGame.gotRunInPlayedCards());

        // Array with values 7, 8, 9 forms a run. Returns 3 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(7, 1));
        newGame.addCardToPlayedCards(Card.of(8, 2));
        newGame.addCardToPlayedCards(Card.of(9, 2));

        assertEquals(3, newGame.gotRunInPlayedCards());

        // Array with values 7, 9, 8 forms a run (out of order OK). Returns 3 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(7, 1));
        newGame.addCardToPlayedCards(Card.of(9, 2));
        newGame.addCardToPlayedCards(Card.of(8, 2));
        assertEquals(3, newGame.gotRunInPlayedCards());

        // Array with values 9, 8, 7 forms a run (backwards OK). Returns 3 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(9, 2));
        newGame.addCardToPlayedCards(Card.of(8, 2));
        newGame.addCardToPlayedCards(Card.of(7, 1));
        assertEquals(3, newGame.gotRunInPlayedCards());

        // Array with values 2, 1, 4, 3 forms a run (out of order OK). Returns 4 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(2, 2));
        newGame.addCardToPlayedCards(Card.of(1, 2));
        newGame.addCardToPlayedCards(Card.of(4, 1));
        newGame.addCardToPlayedCards(Card.of(3, 1));
        assertEquals(4, newGame.gotRunInPlayedCards());

        // Array with values 2, 1, 3, 7 does not forms a run (top card not included). Returns 0 points.
        newGame.resetPlayedCards();
        newGame.addCardToPlayedCards(Card.of(2, 2));
        newGame.addCardToPlayedCards(Card.of(1, 2));
        newGame.addCardToPlayedCards(Card.of(3, 1));
        newGame.addCardToPlayedCards(Card.of(7, 1));
        assertEquals(0, newGame.gotRunInPlayedCards());
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
        newGame.resetRound();

        assertEquals(0, newGame.getPlayerA().getHand().getCards().size());
        assertEquals(0, newGame.getPlayerA().getCrib().getCards().size());
        assertEquals(0, newGame.getPlayerB().getHand().getCards().size());
        assertEquals(0, newGame.getPlayerB().getCrib().getCards().size());

        Deck newDeck = Deck.of();
        assertEquals(newDeck, newGame.getDeck());

        assertNull(newGame.getFaceUpCard());

    }

    /*
    Test that setUpGame works as expected. When run, the game should:
        - be in DEAL state
        - 6 cards in each player's hand
        - runningSum set to 0
     */
    @Test
    void setUpGame() {
        Game newGame = Game.of();
        newGame.setUpGame();

        assertEquals(Game.GameStatus.DEAL, newGame.getGameStatus());

        assertEquals(6, newGame.getPlayerA().getHand().getCards().size());
        assertEquals(6, newGame.getPlayerB().getHand().getCards().size());

        assertEquals(0, newGame.getRunningSum());
    }

    /*
    Test that adding a card to the crib works.

    If the currentDealer is playerA, card should go in playerA's crib
    If the currentDealer is playerB, card should go in playerB's crib
     */
    @Test
    void addCardToCribWithoutKnowingWhoPlayedIt() {

        Game newGame = Game.of();
        newGame.setCurrentDealerA();
        Card card1 = Card.of(5, 1);
        newGame.addCardToCribWithoutKnowingWhoPlayedIt(card1);

        assertTrue(newGame.getPlayerA().getCrib().getCards().contains(card1));

        newGame.setCurrentDealerB();
        Card card2 = Card.of(3, 3);
        newGame.addCardToCribWithoutKnowingWhoPlayedIt(card2);

        assertTrue(newGame.getPlayerB().getCrib().getCards().contains(card2));
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