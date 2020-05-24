package tests;

import game.model.Level;
import game.model.Player;
import game.controller.Game;
import game.model.GameBoard;
import game.view.Graphic;
import org.junit.Before;
import org.junit.Test;
import utilities.GameColor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the Game class
 */
public class GameTest {

    /**
     * The game used to perform tests
     */
    private static Game game;
    /**
     * The first player of the game
     */
    private static Player player1;
    /**
     * The second player of the game
     */
    private static Player player2;

    /**
     * Allows to initialize the class attributes.
     */
    @Before
    public void setUpAll(){
        player1 = new Player("player1", GameColor.BLUE);
        player2 = new Player("player2", GameColor.PINK);
        game = new Game(new Graphic(Level.EASY), player1, player2, Level.EASY);
    }

    /**
     * Allows to make sure that the checkMove() method is correct by
     * testing different cases (such as eating an adverse pawn, trying
     * to move the Zen when it is not possible, giving out of bounds coordinates ...)
     */
    @Test
    public void checkMove() {
        assertFalse(game.checkMove(player1, new int[]{4, 8, 4}, player2));
        assertTrue(game.checkMove(player1, new int[]{4, 8, 5}, player2));
        assertFalse(game.checkMove(player1, new int[]{7, 4, 10}, player2));
        assertFalse(game.checkMove(player1, new int[]{4, 8, 6}, player2));
        assertFalse(game.checkMove(player1, new int[]{6, 8, 5}, player2));
        assertFalse(game.checkMove(player1, new int[]{-1, 2, 5}, player2));
        assertFalse(game.checkMove(player1, new int[]{-1, -1, -1}, player2));
    }

    /**
     * Allows to test the createSymbols() method by comparing the
     * size of the table of chinese symbols of the gameBoard method
     * (that has for sure the correct table) to the one created with
     * the game object of this class.
     */
    @Test
    public void createSymbols() {
        GameBoard g = new GameBoard(player1.getPawns(), player2.getPawns());
        g.createChineseSymbols();
        assertEquals(g.getSymbols().length, game.createSymbols().length);
    }

    /**
     * Allows to check that the getFirstPlayer() method returns the player
     * defined in attribute.
     */
    @Test
    public void getFirstPlayer() {
        assertEquals(player1, game.getFirstPlayer());
    }

    /**
     * Allows to test the getGameBoard() method by checking that the length of the board obtained
     * with a game board initialized with the two players defined in attributes is the same that the
     * one obtained with the getGameBoard() method on the game object of the class. Then compares the
     * rectangles of the first element in both arrays.
     */
    @Test
    public void getGameBoard() {
        GameBoard g = new GameBoard(player1.getPawns(), player2.getPawns());
        assertEquals(g.getBoard().length, game.getGameBoard().getBoard().length);
        assertEquals(g.getBoard()[0][0].getRectangle(), game.getGameBoard().getBoard()[0][0].getRectangle());
    }

    /**
     * Allows to check that the getSecondPlayer() method returns the player
     * defined in attribute.
     */
    @Test
    public void getSecondPlayer() {
        assertEquals(player2, game.getSecondPlayer());
    }
}