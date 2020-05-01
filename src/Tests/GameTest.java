package Tests;

import game.bin.Level;
import game.bin.Player;
import game.controller.Game;
import game.model.GameBoard;
import game.view.Graphic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utilities.GameColor;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static Game game;
    private static Player player1;
    private static Player player2;

    @BeforeAll
    static void setUpAll(){
        player1 = new Player("player1", GameColor.BLUE);
        player2 = new Player("player2", GameColor.PINK);
        game = new Game(new Graphic(Level.EASY), player1, player2, Level.EASY);
    }

    @Test
    void checkMove() {
        assertFalse(game.checkMove(player1, new int[]{4, 8, 4}, player2));
        assertTrue(game.checkMove(player1, new int[]{4, 8, 5}, player2));
        assertFalse(game.checkMove(player1, new int[]{7, 4, 10}, player2));
        assertFalse(game.checkMove(player1, new int[]{4, 8, 6}, player2));
        assertFalse(game.checkMove(player1, new int[]{6, 8, 5}, player2));
    }

    @Test
    void createSymbols() {
        GameBoard g = new GameBoard(player1.getPawns(), player2.getPawns());
        g.createChineseSymbols();
        assertEquals(g.getSymbols().length, game.createSymbols().length);
    }

    @Test
    void getFirstPlayer() {
        assertEquals(player1, game.getFirstPlayer());
    }

    @Test
    void getGameBoard() {
        GameBoard g = new GameBoard(player1.getPawns(), player2.getPawns());
        assertEquals(g.getBoard().length, game.getGameBoard().getBoard().length);
    }

    @Test
    void getSecondPlayer() {
        assertEquals(player2, game.getSecondPlayer());
    }
}