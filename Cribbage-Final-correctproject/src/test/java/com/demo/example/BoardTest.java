package com.demo.example;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BoardTest {

    private Board board;

    /*
    Instantiate a new Board before each test.
     */
    @BeforeEach
    void setUp() {
        board = Board.of();
    }

    /*
    Test factory method. When instantiated, both of the Board's values (playerAPoints and playerBPoints) shoudl be
    set to 0.
     */
    @Test
    void testInitialPoints() {
        assertEquals(0, board.getPlayerAPoints());
        assertEquals(0, board.getPlayerBPoints());
    }

    /*
    Expected to update playerAPoints or playerBPoints with the int value specified.
    Value must be an int >0; otherwise, throw exception.

    Examples:
        - playerAPoints = 10. AddToPlayerAPoints(10) updates playerAPoints to 17.
        - playerAPoints = 10. AddToPlayerAPoints(-1) throws IllegalArgumentException (for negative value).
        - Same should happen for playerBPoints
     */
    @Test
    void testAddPoints() {
        board.addToPlayerAPoints(10);
        assertEquals(10, board.getPlayerAPoints());

        board.addToPlayerBPoints(20);
        assertEquals(20, board.getPlayerBPoints());

        assertThrows(IllegalArgumentException.class, () -> board.addToPlayerAPoints(-1));
        assertThrows(IllegalArgumentException.class, () -> board.addToPlayerBPoints(-1));
    }

     /*
    Expected to return true if Player A has won, based on if playerAPoints >= 121 points.

    Example:
        - playerAPoints = 140. Returns true.
        - playerAPoints = 100. Returns false.
     */

    @Test
    void didPlayerAWin() {
        // before game starts, playerAPoints = 0.
        assertFalse(board.didPlayerAWin());

        board.addToPlayerAPoints(10); // still not enough to win
        assertFalse(board.didPlayerAWin());

        board.addToPlayerAPoints(111);
        assertTrue(board.didPlayerAWin()); // now, player has 121 points.

    }

    /*
    Returns true if Player B has won, based on if playerBPoints >= 121 points.

    Example:
        - playerBPoints = 140. Returns true.
        - playerBPoints = 100. Returns false.
     */

    @Test
    void didPlayerBWin() {
        // before game starts, playerBPoints = 0.
        assertFalse(board.didPlayerBWin());

        board.addToPlayerBPoints(100); // still not enough to win
        assertFalse(board.didPlayerBWin());

        board.addToPlayerAPoints(21);
        assertTrue(board.didPlayerBWin()); // now, player has 121 points.

    }

    /*
    Expected to return true if either playerAPoints >= 121 or playerBPoints >= 121.

    Example:
        - playerAPoints = 100 and playerBPoints = 30 --> returns false.
        - playerAPoints = 130 and playerBPoints = 30 --> returns true.
        - playerAPoints = 130 and playerBPoints = 180 --> returns true.
     */
    @Test
    void testWinningConditions() {
        // before game starts, playerAPoints = 0 and playerBPoints = 0.
        assertFalse(board.hasSomeoneWon());

        board.addToPlayerAPoints(121);
        assertTrue(board.hasSomeoneWon());

        board.addToPlayerBPoints(121);
        // Since Player A already won, this should still return true.
        assertTrue(board.hasSomeoneWon());
    }
}