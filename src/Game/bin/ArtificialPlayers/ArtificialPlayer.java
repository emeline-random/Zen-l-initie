package game.bin.artificialPlayers;

import game.bin.Element;
import game.controller.Game;
import game.bin.Player;
import utilities.GameColor;

/**
 * Java class that is extended by all the ArtificialPlayers of the game
 * (FirstLevel and SecondLevel).
 */
public abstract class ArtificialPlayer extends Player {

    /**
     * Allows to create an ArtificialPlayer by calling the constructor of
     * the super class with the arguments name and color.
     *
     * @param name  the name of the player
     * @param color the color of the player
     */
    public ArtificialPlayer(String name, GameColor color) {
        super(name, color);
    }

    /**
     * Allows to get the move done by the player, the value returned is
     * at the same format than the value returned for a move done by a
     * "normal" player.
     *
     * @param board   the board of Elements where the game is taking place
     * @param adverse the adverse of this player
     * @param game    the current game
     * @return the array of integers that contains the number of the pawn to move, the line index
     * and the column index to move it to
     */
    public abstract int[] play(Element[][] board, Player adverse, Game game);

    /**
     * Allows to create an ArtificialPlayer that has a different color
     * than the one chosen by the other player.
     * @param color the already chosen color
     * @return a new ArtificialPlayer ready to play
     */
    public abstract Player createPlayer(GameColor color);

}
