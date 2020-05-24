package tests;

import game.model.Pawn;
import game.model.Player;
import game.model.GameBoard;
import org.junit.Test;
import utilities.GameColor;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the GameBoard class.
 */
public class GameBoardTest {

    /**
     * Allows to test the constructor by testing it throws the appropriate exception if one of the
     * ArrayList passed in parameters is null. Then tests the movePawn(...) method. To do this, a GameBoard
     * object is created, then we test that an exception is thrown is case of wrong arguments. After this,
     * we check that the first object is an instance of Pawn and then move it to another square and test if
     * this square contains the pawn and the last square an Element but not a Pawn.
     */
    @Test
    public void movePawn() {
        ArrayList<Pawn> pawns = new ArrayList<>();
        ArrayList<Pawn> pawns1;
        assertThrows(IllegalArgumentException.class, () -> new GameBoard(pawns, null));
        pawns1 = new ArrayList<>();
        for (int i = 0; i < Player.getPAWNS_NUMBER(); i++) {
            pawns.add(new Pawn(GameColor.CYAN, i));
            pawns1.add(new Pawn(GameColor.CYAN, i));
        }
        GameBoard board = new GameBoard(pawns, pawns1);
        assertThrows(IndexOutOfBoundsException.class, () -> board.movePawn(0, 0, -1, 0));
        assertTrue(board.getBoard()[0][0] instanceof Pawn);
        assertFalse(board.getBoard()[0][3] instanceof Pawn);
        board.movePawn(0, 0, 0, 3);
        assertTrue(board.getBoard()[0][3] instanceof Pawn);
        assertFalse(board.getBoard()[0][0] instanceof Pawn);
        assertNotNull(board.getBoard()[0][0]);
    }
}