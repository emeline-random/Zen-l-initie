package game.model.artificialPlayers;

import game.model.Element;
import game.model.Pawn;
import game.model.Player;
import utilities.GameColor;
import utilities.Language;
import utilities.MatrixUtilities;
import game.controller.Game;

import java.io.Serializable;

/**
 * Java class that allows to create an artificial player against which a human player will be able
 * to play in one-player games. This level is very easy because this player only does random displacements.
 */
public class FirstLevel extends ArtificialPlayer implements Serializable {

    /** the array containing the coordinates of the move that will be done*/
    private int[] coordinates;

    /**
     * Constructor of the class that initializes an artificial player of level 1
     * and that sets the artificialPlayer boolean to true in the superclass Player
     * (which is the superclass of its superclass)
     * @param name the name of the player
     * @param color the color of the player
     */
    public FirstLevel(String name, GameColor color) {
        super(name, color);
        this.setArtificialPlayer(true);
    }

    /**
     * Constructor of the class that creates a temporary artificial player
     * object. The goal of this object should be to use its createPlayer method
     * to obtain a fully competent player.
     */
    public FirstLevel(){
        super("", GameColor.WHITE);
    }

    /**
     * Allows to compute the move that will be done by the player. In this level,
     * the move done is a random move. This means that a pawn is chosen randomly
     * and then the direction where it goes is chosen randomly too. The displacement
     * is tested and the operation is done until it is correct.Waits 1 second
     * before playing.
     * @param board   the board of Elements where the game is taking place
     * @param adverse the adverse of this player
     * @param game    the current game
     * @return a correct random displacement
     */
    @Override
    public int[] play(Element[][] board, Player adverse, Game game) {
        boolean hasMove = false;
        coordinates = new int[3];
        Pawn p;
        int i;
        while (!hasMove) {
            i = (int) (Math.random() * this.getPawns().size());
            p = this.getPawns().get(i);
            int j = 0;
            while (!hasMove && j < 8){

                i = (int) (Math.random() * 8);
                hasMove = aTest(i, board, adverse, game, p);
                j++;
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return coordinates;
    }

    /**
     * Allows to compute a test for a displacement. First creating the
     * array corresponding to the displacement (the direction is indicated
     * by the i parameter that must be between 0 and 7). Then tests this
     * displacement and returns true if it is correct. This method updates
     * the coordinates instance variable.
     * @param i the index corresponding to the chosen direction
     * @param board the matrix of Elements corresponding to the board
     * @param adverse the adverse of this player
     * @param game the current game
     * @param p the pawn to move
     * @return true is the move is correct, false otherwise
     */
    private boolean aTest(int i, Element[][] board, Player adverse, Game game, Pawn p){
        int nbPawns;
        switch (i){
            case 0:
                nbPawns = MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex(), p.getColumnIndex() + nbPawns};
                break;
            case 1:
                nbPawns = MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex(), p.getColumnIndex() - nbPawns};
                break;
            case 2:
                nbPawns = MatrixUtilities.countObjectLine(board, p.getLineIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() + nbPawns, p.getColumnIndex()};
                break;
            case 3:
                nbPawns = MatrixUtilities.countObjectLine(board, p.getLineIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() - nbPawns, p.getColumnIndex()};
                break;
            case 4:
                nbPawns = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() + nbPawns, p.getColumnIndex() + nbPawns};
                break;
            case 5:
                nbPawns = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() - nbPawns, p.getColumnIndex() - nbPawns};
                break;
            case 6:
                nbPawns = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() + nbPawns, p.getColumnIndex() + nbPawns};
                break;
            case 7:
                nbPawns = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() - nbPawns, p.getColumnIndex() - nbPawns};
                break;
        }
        return game.checkMove(this, coordinates, adverse);
    }

    /**
     * Allows to create a FirstLevel artificial player based on
     * the color chosen by its adverse.
     * @param color the already chosen color
     * @return the new FirstLevel player.
     */
    @Override
    public Player createPlayer(GameColor color) {
        return new FirstLevel("ord", GameColor.getRandomColor(color));
    }

    /**
     * Allows to print the level of this player in the corresponding
     * language.
     * @return the String representation of a FirstLevel player
     */
    @Override
    public String toString(){
        return Language.getText("level1");
    }
}
