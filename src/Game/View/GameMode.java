package game.view;


import game.controller.Game;
import game.bin.Element;
import game.bin.Player;

/**
 * Allows to establish the needed functions for the views of the application.
 */
public interface GameMode {

    /**
     * Has to set the game displayed by the view
     */
    void setGame(Game game);

    /**
     * Has to display the board and then needs to call the play() method of the game
     */
    void restartGame(Game game, Element[][] board);

    /**
     * Has to print a little message with the winner and the number of points of both players, then displays the menu
     */
    void endGame(Player winner, Player looser, boolean equality);

    /**
     * Has to actualize the view of the board when a pawn is moved.
     */
    void movePawn(Player player, int[] move, Element[][] board);

    /**
     * Has to ask the player the move he wants to do and has to check
     * the input values (that means checking that the values are not out of bounds)
     * and to transform them in the correct format.
     * @param player the player that should play
     * @param board the board of Element objects on which the game is taking place
     * @return the coordinates of the displacement according to the format [pawn, line, column]
     */
    int[] play(Player player, Element[][] board);

    /**
     * Has to inform the player that the coordinates he gave were incorrect.
     */
    void cannotMove();

    /**
     * Has to inform the player that the coordinates he gave were not correct because the zen has
     * already been displaced by the other player at the same place.
     */
    void zenAlreadyPlaced();

    /**
     * Has to ask the player the path of the file he wants to save the game into. The String
     * must ends with .ser.
     * @return the path of the file
     */
    String saveAs();

    /**
     * Has to inform the user that the save of its game has not been performed.
     */
    void saveAsFailure();
}
