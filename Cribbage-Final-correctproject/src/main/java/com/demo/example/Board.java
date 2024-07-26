package com.demo.example;

/*

This class represents a Board object, analogous to the board used in Cribbage for counting points.

Data:
    - playerAPoints, an int representing the points earned by Player A so far.
    - playerBPoints, the same for player B.

Responsibilities:
    - Create and represent a board; specifically, the points earned by each player.
        - Each player starts the game with 0 points.
    - Return information about the board, specifically:
        - Player A's current points
        - Player B's current points
        - If any player has won
        - If Player A has won
        - If Player B has won
    - Update the board with new information
        - Add points to Player A's score
        - Add points to Player B's score

Relies upon:
    - Game, for calling methods addToPlayerAPoints and addToPlayerBPoints and supplying the points earned by the player.
 */

public class Board {

    // track players' points. int, can only be >= 0 (no methods for subtracting points)
    private int playerAPoints;
    private int playerBPoints;

    /*
    Constructor.

    playerAPoints: int, >= 0. represents the points earned by Player A in the course of a game.
    playerBPoints: int, >= 0. represents the points earned by Player B in the course of a game.
    */

    private Board() {
        this.playerAPoints = 0;
        this.playerBPoints = 0;
    }


    /*
    Factory method. Creates and returns a new Board, with both players' points set to 0.
     */
    public static Board of() {
        return new Board();
    }

    /*
    Gets Player A's current point total.
     */
    public int getPlayerAPoints() {
        return this.playerAPoints;
    }

    /*
    Gets Player B's current point total.
     */
    public int getPlayerBPoints() {
        return this.playerBPoints;
    }

    /*
    Adds the amount specified to Player A's current point total.
    toAdd must be an int >= 0.
     */
    public void addToPlayerAPoints(int toAdd) {
        if (toAdd < 0) {
            throw new IllegalArgumentException("Points cannot be taken away");
        }
        this.playerAPoints += toAdd;
    }

    /*
    Adds the amount specified to Player B's current point total.
    Points to add must be integers and may not be negative.

    Example:
        - playerBPoints = 10. AddToPlayerBPoints(7) updates playerBPoints to 17.
        - playerBPoints = 10. AddToPlayerBPoints(-2) throws IllegalArgumentException (for negative value).
     */
    public void addToPlayerBPoints(int toAdd) {
        if (toAdd < 0) {
            throw new IllegalArgumentException("Points cannot be taken away");
        }
        this.playerBPoints += toAdd;
    }

    /*
    Returns true if Player A has won, based on if Player A has earned at least 121 points.
     */
    public boolean didPlayerAWin() {
        return getPlayerAPoints() >= 121;
    }

    /*
    Returns true if Player B has won, based on if Player B has earned at least 121 points.

    Example:
        - playerBPoints = 140. Returns true.
        - playerBPoints = 100. Returns false.
     */
    public boolean didPlayerBWin() {
        return getPlayerBPoints() >= 121;
    }

    /*
    Checks if either player has won, based on if either player has earned at least 121 points.
     */
    public boolean hasSomeoneWon() {return didPlayerAWin() || didPlayerBWin();
    }

}