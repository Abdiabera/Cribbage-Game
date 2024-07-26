package com.demo.example;

/*
This class represents the game as it runs. To do so, it relies on all the other classes.

Data:
    - Persistent throughout the game
        - deck: Deck of cards
        - playerA and playerB: two Players
        - board: a Board
    - Updated each round
        - playedCards: an ArrayList of Cards that represents cards that have been placed down during play
        - runningSum: an int that tracks the scoring value of cards placed in the playedCards, to determine eligibility
            of playing further cards
        - faceUpCard: a Card that has been pulled from the deck and will be used by both players to score
        - currentDealer and currentPlayer: point to playerA and playerB
    - Help track the game
        - isOver: boolean, true if a player has won
        - GameStatus: enum that tracks the stage of the game

Responsibilities:
    - Instantiate and return the objects needed to run a game (see above: deck, player, board)
    - Track and return the information needed to run a game (see above: playedCards, runningSum, faceUpCard,
        currentDealer, currentPlayer, isOver, GameStatus)
    - Manage flow of play based on players' hands (identify if player has legal cards to play, identify next player)
    - Calculate points earned during the Play stage, based on playedCards (pairs, fifteens, runs, go)

Relies upon:
    - Deck to create objects; deal randomly-chosen cards for players
    - Player to create objects; get, set, and add cards to player's hand and crib
    - Board to create object; get and add to players' points
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Game {

    private Deck deck; // represents Deck of cards. It's not final because we want to regularly replace it with a new Deck each round instead of getting cards back from Players
    private final Player playerA; // represents one player
    private final Player playerB; // represents another player
    public final Board board; // holds current points, determines winner
    public Card faceUpCard; // changes each round, used to score points
    private Player currentPlayer; // points to either playerA or playerB, toggles throughout
    private boolean isOver; // flips to false when a player has won
    private GameStatus gameStatus; // enum representing the stages of each round/game: NOTSTARTED, DEAL, PLAY, SHOW, RESET, OVER
    private Player currentDealer; // points to either playerA or playerB, switches each round
    private ArrayList<Card> playedCards; // holds cards playerA and playerB have played during PLAY
    private int runningSum; // holds the total of playedCards
    private ArrayList<Card> PlayerAPlayed;
    private ArrayList<Card> PlayerBPlayed;
    /*
    Enum to track the status of the game.
     */
    public enum GameStatus {
        NOTSTARTED,
        DEAL,
        PLAY,
        SHOW,
        OVER;

        /*
        Returns the string version of the GameStatus
         */
        public static GameStatus fromString(String status) {
            try {
                return GameStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Handle invalid or unknown status strings
                return null;
            }
        }
    }

    /*
    Constructor. Returns a new Game with a Deck, two Players, and Board.
    Sets isOver to false.
     */
    private Game() {
        this.deck = Deck.of();
        this.playerA = Player.of();
        this.playerB = Player.of();
        this.board = Board.of();
        this.isOver = false;

        // choose first dealer
        Random rand = new Random();
        boolean chooseFirstDealer = rand.nextBoolean();
        if (chooseFirstDealer) {
            setCurrentDealerA();
            setCurrentPlayerB();
        }
        else {
            setCurrentDealerB();
            setCurrentPlayerA();
        }
    }

    /*
    Factory method; returns new Game.
     */
    public static Game of() {
        return new Game();
    }

    /*
    Creates hands and crib (spaces) for each player.
    Note: only one player will use their crib per round.
     */
    public void setUpDeal() {
        this.deck = Deck.of();
        deck.shuffle();

        setFaceUpCard(deck.draw());

        playerA.createHand(new ArrayList<>());
        playerB.createHand(new ArrayList<>());
        playerA.createCrib(new ArrayList<>());
        playerB.createCrib(new ArrayList<>());

        this.PlayerAPlayed = new ArrayList<>();
        this.PlayerBPlayed = new ArrayList<>();

        resetPlayedCards();
        resetRunningSum();
    }

    /*
    Deals a random card from the deck to a Player.
     */
    public void dealCard(Player player) {
        Card dealt = deck.draw();
        player.addCardToHand(dealt);
    }


    /*
    Returns true if either playerA or playerB have a legal card to play.
     */
    public boolean doesSomeoneHaveLegalCards() {
        return getPlayerA().hasLegalCard(getRunningSum()) || getPlayerB().hasLegalCard(getRunningSum());
    }

    /*
    Returns the points of an ArrayList (usually the playedCards ArrayList),
        based on fifteens, pairs/three of a kind/four of a kind, runs, and GOs.

    Score =
        2 points for hitting fifteen (cards whose scoring value add to 15, e.g., (7, 8) or (King, Ace, 4).
        2 points for making a pair (2 same-value cards, uninterrupted)
        6 points for making a three of a kind (3 same-value cards, uninterrupted)
        12 points for making a four of a kind (4 same-value cards, uninterrupted)
        1 point per card in a run (cards whose values are ordered. At least 3 card. e.g., (7, 8, 9) Out of order and
            backwards OK; interrupted by outside values not OK).
        2 points for reaching 31 exactly.

     Example:
        One move may be worth many points. For example, if the following happens:
        - Player A plays a 5 of Hearts --> runningSum is 5.
        - Player B plays a 5 of Diamonds --> runningSum is 10. Pair of 5s is worth 2 points
        - Player A plays a 5 of Spades --> runningSum is 15, worth 2 points PLUS three of a kind is worth 6 points. Move total: 8 points.
        - Player B plays a 6 of Clubs --> runningSum is 20, pair of 5s is worth 2 points
        - Player A plays a 4 of Diamonds --> runningSum is 26, run of three cards (5, 6, 4) is worth 3 points.
        - Player B plays a 6 of Spades --> runningSum is 31, worth 2 points for hitting 31 exactly.
     */
    public int calculatePointsInPlayedCards() {
        int total = 0;

        // check for fifteen
        total += gotFifteenInPlayedCards();

        // check for pair/triple/quad
        total += gotMultiplesInPlayedCards();

        // check for runs
        total += gotRunInPlayedCards();

        // check for 31, earn 2 points
        if (gotThirtyOneInPlayedCards() == 2) {
            total += gotThirtyOneInPlayedCards();
        }
        // check for closest to 31, earn 1 point
        else if (!doesSomeoneHaveLegalCards()  && getGameStatus().equals(GameStatus.PLAY)) {
            total += 1;
        }

        return total;
    }

    /*
    Calculates and returns points for reaching 15 in an ArrayList.

    Returns either 0 or 2 points. If scoringValues in array sum to 15, returns 2. Otherwise, 0.

    Example (disregarding suit, which has no impact):
        - Queen, 4, Ace --> 2 points
        - 7, 8 --> 2 points
        - 10, 3 --> 0 points
    */
    public int gotFifteenInPlayedCards() {
        int sum = 0;

        for (Card card : getPlayedCards()) {
            sum += card.getScoringValue();
        }

        if (sum == 15) {
            return 2; // points earned for summing to 15
        }
        return 0;
    }

    /*
    Calculates and returns points for reaching 31.

    Returns either 0 or 2 points. If scoringValues in array sum to 31, returns 2. Otherwise, 0.

    Example (disregarding suit, which has no impact):
       - Queen, King, Queen, Ace --> 2 points
       - 7, 8, 9 --> 0 points
       - 10, 10, 10 --> 0 points
   */
    public int gotThirtyOneInPlayedCards() {
        int sum = 0;

        for (Card card : getPlayedCards()) {
            sum += card.getScoringValue();
        }

        if (sum == 31) {
            return 2; // points earned for summing to 31
        }
        return 0;
    }

    /*
    Detects and counts points for pairs/three of a kind/four of a kind in an ArrayList of Cards.

    Returns 0, 2, 6, or 12 points, depending on the number of cards (pairs, three of a kind, four of a kind)
        that match the value of the card at the end of the array (most recently-played card). No interruptions are allowed;
        matching-value cards must directly precede the most recently-played card.

    Pair: 2 points
    Three of a kind: 6 points
    Four of a kind: 12 points
    Otherwise: 0 points

    Example:
        - Player A plays a 2 of Hearts --> worth 0 points
        - Player B plays a 2 of Diamonds --> worth 2 points (pair)
        - Player A plays a 2 of Clubs --> worth 6 points (three of a kind)
        - Player B plays a Queen of Clubs --> worth 0 points
        - Player A plays a 2 of Spades --> worth 0 points (four of a kind not counted because of interrupting card: Queen of Clubs)
     */
    public int gotMultiplesInPlayedCards() {

        // multiples not possible if only one card played so far
        if (getPlayedCards().size() <= 1) {
            return 0;
        }

        // start with the most recently added card, then work backwards. pairs/triples/quads must be unbroken.
        int topCardVal = getPlayedCards().get(getPlayedCards().size() - 1).getValue();

        int countMultiples = 0; // counts the number of duplicate cards
        // iterate backwards from end (top) of cards played. Only need to examine three more cards, b/c there are only four of
        // each val in a standard deck.
        for (int i = getPlayedCards().size() - 2; i >= 0; i--) {
            if (getPlayedCards().get(i).getValue() == topCardVal) {
                countMultiples += 1;
            }
            else { // if not a same-value card, break.
                break;
            }
        }

        // return points based on number of cards
        return switch (countMultiples) {
            case 1 -> 2;
            case 2 -> 6;
            case 3 -> 12;
            default -> 0;
        };
    }

    /*
    Returns an integer representing points, depending on the number of cards in a run (three or more Cards whose
        values, when ordered, are strictly increasing).

    1 point per card. Runs must be made with the most recent card played (last in the ArrayList) and cannot be
        interrupted by other cards.

    Example:
        - Player A plays a 2 of Hearts --> worth 0 points
        - Player B plays a 3 of Diamonds --> worth 0 points
        - Player A plays a 4 of Clubs --> worth 3 points (run of three cards)
        - Player B plays an Ace of Clubs --> worth 4 points (run of four cards)
        - Player A plays a 10 of Spades --> worth 0 points
        - Player B plays a 5 of Hearts --> worth 0 points
     */
    public int gotRunInPlayedCards() {

        // runs not possible if only one or two cards played so far
        if (getPlayedCards().size() <= 2) {
            return 0;
        }

        int cardsInRun = 0;
        for (int i = 3; i <= getPlayedCards().size(); i++) {
            // add cards to new arrayList
            ArrayList<Card> tempArray = new ArrayList<>();

            for (int j = getPlayedCards().size() - 1; j > getPlayedCards().size() - i - 1; j--) {
                tempArray.add(getPlayedCards().get(j));
            }

            // sort them and check if they are purely ascending
            tempArray.sort(Comparator.comparing(Card::getValue));

            boolean isRun = true;
            for (int k = 1; k < tempArray.size(); k++) {
                // if not strictly increasing, return 0
                if (tempArray.get(k).getValue() != tempArray.get(k - 1).getValue() + 1) {
                    isRun = false;
                    break;
                }
            }
            if (isRun) {
                cardsInRun = i;
            }
        }

        return cardsInRun;
    }

    /*
    Sets the current player, based on who most recently played and who has legal cards to play.
    Used during the PLAY section.
     */
    public void chooseCurrentPlayer() {

        // if the current player is A...
        if (isCurrentPlayerA()) {
            // ... and B has a legal card to play, B gets to play
            if (getPlayerB().hasLegalCard(getRunningSum())) {
                toggleCurrentPlayer();
            }
            // ...and B doesn't have a legal card to play, A gets to play.
            // no toggling needed
        } else {
            // if the current player is B...
            // ... and A has a legal card to play, A gets to play
            if (getPlayerA().hasLegalCard(getRunningSum())) {
                toggleCurrentPlayer();
            }
            // ...and A doesn't have a legal card to play, B gets to play.
            // no toggling needed
        }
    }

    /*
    Returns true if either playerA or playerB has at least one card in their hand.
     */
    public boolean doesSomeoneHaveCards() {
        return (!getPlayerA().getHand().getCards().isEmpty() || !getPlayerB().getHand().getCards().isEmpty());
    }

    /*
    Preps the game for the next round.
    */
    public void resetRound() {
        getPlayerA().emptyHand();
        getPlayerB().emptyCrib();

        getPlayerB().emptyHand();
        getPlayerB().emptyCrib();

        this.deck = Deck.of();
        this.deck.shuffle();
        this.faceUpCard = null;

        toggleDealer();
    }


    /*
    Sets up a new game by choosing the first dealer, setting the first player, setting up the deck, and
    dealing 6 cards to each player.
     */
    public void setUpGame() {

        // instantiate players' hands

        setGameStatus(Game.GameStatus.DEAL);

        // Deal cards to players
        setUpDeal();

        for(int i=0; i<6; i++) {
            dealCard(getPlayerA());
            dealCard(getPlayerB());
        }

        resetRunningSum();
    }

    public void addCardToCribWithoutKnowingWhoPlayedIt(Card card) {
        if (getPlayerA().getHand().getCards().contains(card)){
            getPlayerA().getHand().removeCardFromHand(card);
        } else {
            getPlayerB().getHand().removeCardFromHand(card);
        }
        currentDealer.addCardToCrib(card);
    }

    public void showScore() {

        getPlayerA().createHand(getPlayerAPlayed());
        getPlayerB().createHand(getPlayerBPlayed());

        // current dealer counts second
        if (isCurrentDealerA()) {
            // count B's hand
            getBoard().addToPlayerBPoints(getPlayerB().getHand().calculatePoints(getFaceUpCard()));
            // check if B won
            if (getBoard().didPlayerBWin()) {
                setGameStatus(Game.GameStatus.OVER);
                return;
            }

            // count A's hand
            getBoard().addToPlayerAPoints(getPlayerA().getHand().calculatePoints(getFaceUpCard()));

            // count A's crib
            getBoard().addToPlayerAPoints(getPlayerA().getCrib().calculatePoints(getFaceUpCard()));

            // check if A won
            if (getBoard().didPlayerAWin()) {
                setGameStatus(Game.GameStatus.OVER);
            }
        } else {
            // count A's hand
            getBoard().addToPlayerBPoints(getPlayerA().getHand().calculatePoints(getFaceUpCard()));
            // check if A won
            if (getBoard().didPlayerAWin()) {
                setGameStatus(Game.GameStatus.OVER);
                return;
            }

            // count B's hand
            getBoard().addToPlayerAPoints(getPlayerB().getHand().calculatePoints(faceUpCard));

            // count B's crib
            getBoard().addToPlayerAPoints(getPlayerB().getCrib().calculatePoints(faceUpCard));

            // check if B won
            if (getBoard().didPlayerBWin()) {
                setGameStatus(Game.GameStatus.OVER);
            }
        }
    }

    public void play(Card sent_card) {
        if (!doesSomeoneHaveLegalCards()) {
            resetRunningSum();
        }

        if (getPlayerA().getHand().getCards().contains(sent_card)) {
            addCardToPlayedCards(sent_card);
            addToRunningSum(sent_card.getScoringValue());
            getBoard().addToPlayerAPoints(calculatePointsInPlayedCards());
            getPlayerA().getHand().removeCardFromHand(sent_card);
            addToPlayerAPlayed(sent_card);
        } else {
            addCardToPlayedCards(sent_card);
            addToRunningSum(sent_card.getScoringValue());
            getBoard().addToPlayerBPoints(calculatePointsInPlayedCards());
            getPlayerB().getHand().removeCardFromHand(sent_card);
            addToPlayerBPlayed(sent_card);
        }

        chooseCurrentPlayer();

        if (handsEmpty()) {
            setGameStatus(Game.GameStatus.SHOW);
        }
    }

    /*
    Getter for playedCards. Returns an ArrayList of Cards.
     */
    public ArrayList<Card> getPlayedCards() {
        return this.playedCards;
    }

    /*
    Empties the playedCards ArrayList.
     */
    public void resetPlayedCards() {
        this.playedCards = new ArrayList<>();
    }

    /*
    Adds a card to the playedCards ArrayList.
     */
    public void addCardToPlayedCards(Card card) {
        this.playedCards.add(card);
    }

    /*
    Getter for runningSum. Returns an int.
     */
    public int getRunningSum() {
        return this.runningSum;
    }

    /*
    Resets the runningSum to 0.
     */
    public void resetRunningSum() {
        this.runningSum = 0;
    }

    /*
    Adds toAdd (int) to runningSum.
    toAdd cannot be negative.
     */
    public void addToRunningSum(int toAdd) {
        if (toAdd < 0) {
            throw new IllegalArgumentException("runningSum cannot be decreased");
        }
        this.runningSum += toAdd;
    }


    /*
    Returns true if both playerA and playerB have empty hands.
     */
    public boolean handsEmpty(){
        return getPlayerA().getHand().getCards().isEmpty() && getPlayerB().getHand().getCards().isEmpty();
    }

    /*
    Returns a pointer to either playerA or playerB, depending on who is currently playing.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /*
    Returns a pointer to either playerA or playerB, depending on who is currently dealing.
     */
    public Player getCurrentDealer() {
        return this.currentDealer;
    }

    /*
    Sets the currentDealer to playerA
    */
    public void setCurrentDealerA() {
        this.currentDealer = this.playerA;
    }

    /*
    Sets the currentDealer to playerB
    */
    public void setCurrentDealerB() {
        this.currentDealer = this.playerB;
    }

    /*
    Returns true if the currentDealer is playerA
    */
    public boolean isCurrentDealerA() {
        return getCurrentDealer().equals(getPlayerA());
    }

    /*
    Switches the currentDealer to the other Player.
    If playerA is the currentDealer, running this method
        changes currentDealer to playerB, and vice versa.
    */
    public void toggleDealer() {
        if (getCurrentDealer().equals(getPlayerA())) setCurrentDealerB();
        else setCurrentDealerA();
    }

    /*
    Sets the currentPlayer to playerA
    */
    public void setCurrentPlayerA() {
        this.currentPlayer = this.playerA;
    }

    /*
    Sets the currentPlayer to playerB
    */
    public void setCurrentPlayerB() {
        this.currentPlayer = this.playerB;
    }

    /*
    Returns true if the currentPlayer is playerA
    */
    public boolean isCurrentPlayerA() {
        return getCurrentPlayer().equals(getPlayerA());
    }

    /*
    Switches the currentPlayer to the other Player.
    If playerA is the currentPlayer, running this method
        changes currentPlayer to playerB, and vice versa.
    */
    public void toggleCurrentPlayer() {
        if (isCurrentPlayerA()) setCurrentPlayerB();
        else setCurrentPlayerA();
    }

    /*
    Returns the current faceUpCard.
     */
    public Card getFaceUpCard() {
        return this.faceUpCard;
    }

    /*
    sets the current faceUpCard
    */
    private void setFaceUpCard(Card card) {
        this.faceUpCard = card;
    }

    /*
    Returns playerA.
     */
    public Player getPlayerA() {
        return this.playerA;
    }

    /*
    Returns playerB.
     */
    public Player getPlayerB() {
        return this.playerB;
    }

    /*
    Sets isOver to true.
     */
    public void setGameOver() {
        this.isOver = true;
    }

    /*
    Returns boolean isOver
     */
    public boolean isOver() {
        return this.isOver;
    }

    /*
    Returns the deck.
     */
    public Deck getDeck() {
        return this.deck;
    }

    /*
    Returns the gameStatus.
     */
    public GameStatus getGameStatus() {
        return this.gameStatus;
    }

    /*
    Sets the gameStatus.
     */
    public void setGameStatus(GameStatus status) {
        this.gameStatus = status;
    }

    /*
    Returns the Board
     */
    public Board getBoard() {
        return this.board;
    }

    public ArrayList<Card> getPlayerAPlayed(){
        return this.PlayerAPlayed;
    }

    public ArrayList<Card> getPlayerBPlayed(){
        return this.PlayerBPlayed;
    }

    public void addToPlayerAPlayed(Card card){
        this.PlayerAPlayed.add(card);
    }
    public void addToPlayerBPlayed(Card card){
        this.PlayerBPlayed.add(card);
    }




}
