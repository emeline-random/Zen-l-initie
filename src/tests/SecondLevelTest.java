package tests;

import game.controller.Game;
import game.model.GameBoard;
import game.model.Level;
import game.model.Player;
import game.model.artificialPlayers.ArtificialPlayer;
import game.model.artificialPlayers.SecondLevel;
import game.view.Console;
import org.junit.Test;
import utilities.GameColor;
import utilities.Language;

import static org.junit.jupiter.api.Assertions.*;

public class SecondLevelTest {

    /**
     * Allows to make sure that the default name for an artificial player is "ord"
     * and to check that it is possible to change this name by using the appropriate
     * constructor.
     */
    @Test
    public void getName() {
        ArtificialPlayer player = new SecondLevel().createPlayer(GameColor.WHITE);
        assertEquals(player.getName(), "ord");
        ArtificialPlayer player1 = new SecondLevel("James", GameColor.WHITE);
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
        ArtificialPlayer player = new SecondLevel().createPlayer(GameColor.WHITE);
        assertTrue(player instanceof SecondLevel);
        assertTrue(player.isArtificialPlayer());
        assertNotSame(player.getColor(), GameColor.WHITE);
        ArtificialPlayer player1 = new SecondLevel().createPlayer(GameColor.BLUE);
        assertSame(player1.getColor(), GameColor.WHITE);
    }

    /**
     * Allows ot heck that the to string method returns first player if the language is english
     * and premier niveau if the language is french.
     */
    @Test
    public void testToString() {
        assertEquals("second level", new SecondLevel().toString());
        Language.setLanguage(Language.Languages.FRENCH);
        assertEquals("second niveau", new SecondLevel().toString());
    }

    /**
     * Allows to check that the length of the array brought back by the play method is 3
     * and that the number of the pawn, the line and the column are valid.
     */
    @Test
    public void play(){
        ArtificialPlayer player = new SecondLevel();
        Player adverse = new Player("Guillaume", GameColor.GREEN);
        Game game  = new Game(new Console(Level.HARD), player, adverse, Level.HARD);
        assertEquals(3, player.play(game.getGameBoard().getBoard(), adverse, game).length);
        int[] play = player.play(game.getGameBoard().getBoard(), adverse, game);
        assertTrue(play[1] >=0 && play[1] < GameBoard.getDIMENSION());
        assertTrue(play[2] >=0 && play[2] < GameBoard.getDIMENSION());
        assertTrue(play[0] >= -1 && play[0] < Player.getPAWNS_NUMBER());
    }
}