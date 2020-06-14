package tests;

import game.controller.Game;
import game.model.Level;
import game.model.Player;
import game.model.artificialPlayers.ArtificialPlayer;
import game.model.artificialPlayers.FirstLevel;
import game.view.Console;
import org.junit.Test;
import utilities.GameColor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the ArtificialPlayer class that has only one implemented method.
 */
public class ArtificialPlayerTest {

    /**
     * Allows to test the incorrectMove() method that should return true if a the checkMove() method
     * of game returns false and conversely.
     * @throws NoSuchMethodException if the method was not found in the class
     * @throws InvocationTargetException if the method cannot be applied
     * @throws IllegalAccessException if the method cannot be accessed
     */
    @Test
    public void incorrectMove() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ArtificialPlayer player = new FirstLevel().createPlayer(GameColor.WHITE);
        Player adverse = new Player("Alan", GameColor.BLUE);
        Game game = new Game(new Console(Level.EASY), player, adverse, Level.EASY);
        Method method = ArtificialPlayer.class.getDeclaredMethod("incorrectMove", int[].class, Player.class, Game.class);
        method.setAccessible(true);
        int[] move = new int[]{-1, 2, 5};
        assertTrue((boolean) method.invoke(player, move, adverse, game));
        assertEquals(method.invoke(player, move, adverse, game), !game.checkMove(player, move, adverse));
        move = new int[]{4, 8, 5};
        assertFalse((boolean) method.invoke(player, move, adverse, game));
        assertEquals(method.invoke(player, move, adverse, game), !game.checkMove(player, move, adverse));
    }
}