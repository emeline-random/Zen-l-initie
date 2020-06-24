package tests;

import game.controller.Game;
import game.model.GameBoard;
import game.model.Level;
import game.model.Player;
import game.model.artificialPlayers.ArtificialPlayer;
import game.model.artificialPlayers.FirstLevel;
import game.view.Console;
import org.junit.Test;
import utilities.GameColor;
import utilities.Language;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the FirstLevel class.
 */
public class FirstLevelTest {

    /**
     * Allows to make sure that the default name for an artificial player is "ord"
     * and to check that it is possible to change this name by using the appropriate
     * constructor.
     */
    @Test
    public void getName() {
        ArtificialPlayer player = new FirstLevel().createPlayer(GameColor.WHITE);
        assertEquals(player.getName(), "ord");
        ArtificialPlayer player1 = new FirstLevel("James", GameColor.WHITE);
        assertEquals(player1.getName(), "James");
    }

    /**
     * Allows to test the createPlayer method by checking it returns a FirstLevel object
     * and it sets the artificialPlayer boolean in Player class to true. Also checks that
     * if the already chose color is white, the color of the artificial player created will
     * be different and that if the already chose color is different than white, the artificial
     * player created will use white.
     */
    @Test
    public void createPlayer() {
        ArtificialPlayer player = new FirstLevel().createPlayer(GameColor.WHITE);
        assertTrue(player instanceof FirstLevel);
        assertTrue(player.isArtificialPlayer());
        assertNotSame(player.getColor(), GameColor.WHITE);
        ArtificialPlayer player1 = new FirstLevel().createPlayer(GameColor.BLUE);
        assertSame(player1.getColor(), GameColor.WHITE);
    }

    /**
     * Allows ot heck that the to string method returns first player if the language is english
     * and premier niveau if the language is french.
     */
    @Test
    public void testToString() {
        assertEquals("first level", new FirstLevel().toString());
        Language.setLanguage(Language.Languages.FRENCH);
        assertEquals("premier niveau", new FirstLevel().toString());
    }

    /**
     * Allows to check that the length of the array brought back by the play method is 3
     * and that the number of the pawn, the line and the column are valid.
     */
    @Test
    public void play(){
        ArtificialPlayer player = new FirstLevel();
        Player adverse = new Player("Guillaume", GameColor.GREEN);
        Game game  = new Game(new Console(Level.EASY), player, adverse, Level.EASY);
        assertEquals(3, player.play(game.getGameBoard().getBoard(), adverse, game).length);
        int[] play = player.play(game.getGameBoard().getBoard(), adverse, game);
        assertTrue(play[0] >= -1 && play[0] < Player.getPAWNS_NUMBER());
        assertTrue(play[1] >=0 && play[1] < GameBoard.getDIMENSION());
        assertTrue(play[2] >=0 && play[2] < GameBoard.getDIMENSION());
    }
}