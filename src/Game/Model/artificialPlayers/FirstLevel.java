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
     * is computed and the operation is done until it is correct. Waits 1 second
     * before playing.
     * @param board   the board of Elements where the game is taking place
     * @param adverse the adverse of this player
     * @param game    the current game
     * @return a correct random displacement
     */
    @Override
    public int[] play(Element[][] board, Player adverse, Game game) {
        int[] coordinates = new int[]{-1, -1, -1};
        Pawn p;
        int i;
        while (this.incorrectMove(coordinates, adverse, game)) {
            i = (int) (Math.random() * this.getPawns().size());
            p = this.getPawns().get(i);
            int j = 0;
            do {
                i = (int) (Math.random() * 8);
                coordinates = getDisplacement(i, board, p);
                j++;
            } while (this.incorrectMove(coordinates, adverse, game) && j < 8);
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
     * by the i parameter that must be between 0 and 7). Then returns the
     * array giving the coordinates of the displacement.
     * @param i the index corresponding to the chosen direction
     * @param board the matrix of Elements corresponding to the board
     * @param p the pawn to move
     * @return the array of the coordinates of the displacement
     */
    private int[] getDisplacement(int i, Element[][] board, Pawn p){
        int nbPawns;
        int[] coordinates = {-1, -1, -1};
        switch (i){
            case 0: //east
                nbPawns = MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex(), p.getColumnIndex() + nbPawns};
                break;
            case 1: //west
                nbPawns = MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex(), p.getColumnIndex() - nbPawns};
                break;
            case 2: //south
                nbPawns = MatrixUtilities.countObjectLine(board, p.getLineIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() + nbPawns, p.getColumnIndex()};
                break;
            case 3: //north
                nbPawns = MatrixUtilities.countObjectLine(board, p.getLineIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() - nbPawns, p.getColumnIndex()};
                break;
            case 4: //southwest
                nbPawns = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() + nbPawns, p.getColumnIndex() - nbPawns};
                break;
            case 5: //northeast
                nbPawns = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() - nbPawns, p.getColumnIndex() + nbPawns};
                break;
            case 6: //southeast
                nbPawns = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() + nbPawns, p.getColumnIndex() + nbPawns};
                break;
            case 7: //northwest
                nbPawns = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNUMBER(), p.getLineIndex() - nbPawns, p.getColumnIndex() - nbPawns};
                break;
        }
        return coordinates;
    }

    /**
     * Allows to create a FirstLevel artificial player based on the color chosen by its adverse.
     * If its adverse chose the white color, then the player will have a random color, else
     * it will have white color.
     * @param color the already chosen color
     * @return the new FirstLevel player.
     */
    @Override
    public ArtificialPlayer createPlayer(GameColor color) {
        if (color == GameColor.WHITE) return new FirstLevel("ord", GameColor.getRandomColor(color));
        else return new FirstLevel("ord", GameColor.WHITE);
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
