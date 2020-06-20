package game.controller;

import game.model.*;
import game.model.artificialPlayers.ArtificialPlayer;
import game.view.Console;
import game.view.GameMode;
import game.view.Graphic;
import utilities.InputUtilities;
import utilities.Language;
import utilities.MatrixUtilities;
import utilities.Sound;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Java class that controls the games. Several parts can be made in the
 * same game as long as the configuration does not change (the players
 * are the same, the level too).
 */
public class Game implements Serializable {

    /**
     * The view in which the game will be display
     */
    transient private GameMode mode;
    /**
     * The path of the file to save the game in
     */
    private String path;
    /**
     * The level of the game
     */
    private final Level level;
    /**
     * The first player of the game
     */
    private final Player firstPlayer;
    /**
     * The second player of the game
     */
    private final Player secondPlayer;
    /**
     * The matrix of Elements that will represent a part in the game
     */
    private GameBoard gameBoard;
    /**
     * The array containing the last positions of the Zen pawn for controlling its displacements
     */
    private int[] zenMoves = {GameBoard.getDIMENSION() / 2, GameBoard.getDIMENSION() / 2, -1, -1};
    /**
     * Indicates whether the Zen was moved or not at the last turn
     */
    private boolean zenMoved = false;
    /**
     * Allows to know who moved the Zen the last time
     */
    private Player lastPlayerZ = null;

    /**
     * Constructor of the class that allows to initialize the game with
     * the two players, the mode and the level. Depending of the mode the
     * appropriate method is called to show the board at the beginning
     * of the game.
     *
     * @param mode         the view mode used
     * @param firstPlayer  the first player of the game
     * @param secondPlayer the second player of the game
     * @param level        the level of the game
     */
    public Game(GameMode mode, Player firstPlayer, Player secondPlayer, Level level) {
        if (firstPlayer != null && secondPlayer != null && mode != null && level != null) {
            this.level = level;
            this.mode = mode;
            this.firstPlayer = firstPlayer;
            if (secondPlayer instanceof ArtificialPlayer) {
                this.secondPlayer = ((ArtificialPlayer) secondPlayer).createPlayer(firstPlayer.getColor());
            } else {
                this.secondPlayer = secondPlayer;
            }
            this.gameBoard = new GameBoard(this.firstPlayer.getPawns(), this.secondPlayer.getPawns());
            this.mode.setGame(this);
            if (mode instanceof Graphic) {
                ((Graphic) mode).setAnInterface(this.gameBoard.getBoard(), this.createSymbols());
            } else {
                mode.movePawn(null, null, this.gameBoard.getBoard());
            }
        } else {
            throw new IllegalArgumentException("Game initialization error");
        }
    }

    /**
     * Allows to begin the game by setting which player
     * will play the first and calling the method play().
     */
    public void beginGame() {
        this.firstPlayer.setPlaying(true);
        this.secondPlayer.setPlaying(false);
        this.play();
    }

    /**
     * Allows to restart a saved game by asking the user in which view mode
     * he wants to play. Then create the appropriate GameMode object and
     * ask this mode to restart the game.
     *
     * @param graphic if true, the question for continuing in graphic mode will be asked in a JOptionPane
     */
    public void restartGame(boolean graphic) {
        int mode;
        if (graphic) {
            mode = JOptionPane.showConfirmDialog(null, Language.getText("mode question"), "Zen l'Initié",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        } else mode = (InputUtilities.getConfirmation(Language.getText("mode question")) ? 0 : 1);
        if (mode == JOptionPane.YES_OPTION) {
            this.mode = new Graphic(this.level);
        } else {
            this.mode = new Console(this.level);
        }
        this.mode.restartGame(this, this.gameBoard.getBoard());
    }

    /**
     * Allows to go from graphic mode to console mode
     */
    public void goToConsole() {
        this.mode = new Console(this.level);
        this.mode.restartGame(this, this.gameBoard.getBoard());
    }

    /**
     * Allows to go fro mconsole mode to graphic mode
     */
    public void goToGraphic() {
        this.mode = new Graphic(this.level);
        this.mode.restartGame(this, this.gameBoard.getBoard());
    }

    /**
     * Allows to end a part. Sends a message to the view mode with the winner, the looser
     * and a boolean that indicates whether there is an equality or not.
     */
    private void endGame() {
        boolean firstWin = MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), this.firstPlayer.getPawns());
        boolean secondWin = MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), this.secondPlayer.getPawns());
        if (firstWin && secondWin) {
            this.firstPlayer.addPoint();
            this.secondPlayer.addPoint();
            this.mode.endGame(this.firstPlayer, this.secondPlayer, true);
        } else if (firstWin) {
            this.firstPlayer.addPoint();
            this.mode.endGame(this.firstPlayer, this.secondPlayer, false);
        } else {
            this.secondPlayer.addPoint();
            this.mode.endGame(this.secondPlayer, this.firstPlayer, false);
        }
    }

    /**
     * Allows to begin a new part in the same game with the same configuration. To do this
     * it only resets de pawns of both players and create a new GameBoard object.
     */
    public void replay() {
        this.firstPlayer.resetPawns();
        this.secondPlayer.resetPawns();
        this.gameBoard = new GameBoard(this.firstPlayer.getPawns(), this.secondPlayer.getPawns());
        if (this.mode instanceof Graphic) {
            ((Graphic) this.mode).setAnInterface(this.gameBoard.getBoard(), this.createSymbols());
        } else {
            this.mode.movePawn(null, null, this.gameBoard.getBoard());
        }
        this.beginGame();
    }

    /**
     * Game loop that asks turn by turn players to play depending on the view mode chosen and
     * on whether the second player is an artificial player or not. The loop is over when one
     * of the players wins. Every displacement proposed is checked and while the displacement is
     * not correct, this method ask one more time the player to play.
     */
    public void play() {
        while (!MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), this.firstPlayer.getPawns())
                && !MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), this.secondPlayer.getPawns())) {

            int[] move;
            Player player, adverse;
            if (this.firstPlayer.isPlaying()) {
                player = this.firstPlayer;
                adverse = this.secondPlayer;
            } else {
                adverse = this.firstPlayer;
                player = this.secondPlayer;
            }
            do {
                if (player.isArtificialPlayer())
                    move = ((ArtificialPlayer) player).play(this.gameBoard.getBoard(), adverse, this);
                else move = this.mode.play(player, this.gameBoard.getBoard());
            } while (!checkMove(player, move, adverse));

            Pawn pawn = player.getPawn(move[0]);
            this.eatAdversePawn(move[1], move[2], adverse, player);
            if (pawn.getNUMBER() == -1) {
                this.zenMoved = true;
                this.zenMoves = new int[]{move[1], move[2], this.zenMoves[0], this.zenMoves[1]};
                this.lastPlayerZ = player;
            } else {
                this.zenMoved = false;
            }
            this.gameBoard.movePawn(pawn.getLineIndex(), pawn.getColumnIndex(), move[1], move[2]);
            this.mode.movePawn(player, move, this.gameBoard.getBoard());
            if (Sound.isOn()) Sound.play(Sound.Sounds.MOVE);

            this.firstPlayer.setPlaying(!this.firstPlayer.isPlaying());
            this.secondPlayer.setPlaying(!this.secondPlayer.isPlaying());
        }
        this.endGame();
    }

    /**
     * Allows to eat a pawn of the adverse if the player puts its pawn on one of
     * the adverse's pawn. The eaten pawn is removed from the list of pawns of the adverse.
     *
     * @param line    the line where the player is putting its pawn.
     * @param column  the columns where the player is putting its pawn.
     * @param adverse the adverse of the player.
     * @param player  the player playing.
     */
    private void eatAdversePawn(int line, int column, Player adverse, Player player) {
        if (this.gameBoard.getBoard()[line][column] instanceof Pawn) {
            Sound.play(Sound.Sounds.EAT);
            adverse.getPawns().remove(this.gameBoard.getBoard()[line][column]);
            if (((Pawn) this.gameBoard.getBoard()[line][column]).getNUMBER() == -1) {
                player.getPawns().remove(this.gameBoard.getBoard()[line][column]);
            }
        }
    }

    /**
     * Allows to check whether the displacement is correct or not. To be correct, the pawn must move as
     * many squares as there are pawns on the chosen line, it must not eat one of its own pawns, it must
     * not pass over one of the opponent's pawns. In the case of Zen, he may not return to the square on
     * which he was just before if he has just been moved.
     *
     * @param player  the player paying
     * @param move    the move he decides to do according to format [pawn, line, column]
     * @param adverse the adverse of the player
     * @return true if the move is correct, false otherwise
     */
    public boolean checkMove(Player player, int[] move, Player adverse) {
        boolean okay;
        Pawn pawn = player.getPawn(move[0]);
        if (MatrixUtilities.isOutOfMatrix(this.gameBoard.getBoard(), move[1], move[2]) || (this.gameBoard.getBoard()[move[1]][move[2]]
                instanceof Pawn && player.getPawns().contains(this.gameBoard.getBoard()[move[1]][move[2]])
                && ((Pawn) this.gameBoard.getBoard()[move[1]][move[2]]).getNUMBER() != -1)) {
            okay = false;
        } else {
            int line = pawn.getLineIndex() - move[1];
            int column = pawn.getColumnIndex() - move[2];
            boolean notEquals = Math.abs(line) != Math.abs(column);

            if ((line > 0 && column > 0) || (line < 0 && column < 0)) {
                okay = !notEquals && (Math.abs(column) == MatrixUtilities.countObjectDiagDesc(this.gameBoard.getBoard(), pawn.getLineIndex(), pawn.getColumnIndex()));
            } else if (line != 0 && column != 0) {
                okay = !notEquals && (Math.abs(column) == MatrixUtilities.countObjectDiagAsc(this.gameBoard.getBoard(), pawn.getLineIndex(), pawn.getColumnIndex()));
            } else if (column != 0) {
                okay = (Math.abs(column) == MatrixUtilities.countObjectLine(this.gameBoard.getBoard(), move[1]));
            } else {
                okay = (Math.abs(line) == MatrixUtilities.countObjectColumn(this.gameBoard.getBoard(), move[2]));
            }
            okay = (okay && !MatrixUtilities.meetAdverse(this.gameBoard.getBoard(), adverse.getPawns(), pawn.getLineIndex(), pawn.getColumnIndex(), move[1], move[2]));
        }
        if (okay && pawn.getNUMBER() == -1) {
            ArrayList<Pawn> allPawns = new ArrayList<>(player.getPawns());
            allPawns.addAll(adverse.getPawns());
            okay = !MatrixUtilities.getNeighbours(this.gameBoard.getBoard(), pawn.getColumnIndex(), pawn.getLineIndex(), allPawns).isEmpty();
            if (this.zenMoved && move[2] == this.zenMoves[3] && move[1] == this.zenMoves[2] && this.lastPlayerZ != null && this.lastPlayerZ != player) {
                okay = false;
                this.mode.zenAlreadyPlaced();
            }
        }
        if (!okay && !player.isArtificialPlayer()) {
            this.mode.cannotMove();
            if (Sound.isOn()) Sound.play(Sound.Sounds.IMPOSSIBLE_MOVE);
        }
        return okay;
    }

    /**
     * Allows to save the game in the same file that is identified by the path.
     * If the path is null, the method saveAs() is called.
     */
    public void saveGame() {
        if (this.path == null) this.saveGameAs();
        else this.writeObject();
    }

    /**
     * Allows to save the game as a precise destination. This destination
     * is asked in different ways depending on the view mode.
     */
    public void saveGameAs() {
        this.path = this.mode.saveAs();
        this.writeObject();
    }

    /**
     * Allows to write the object in a serialized format. The object write is this object
     * that contains the information about the players and the state of the game (the gameBoard).
     */
    private void writeObject() {
        try {
            FileOutputStream file = new FileOutputStream(this.path);
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            this.mode.saveAsFailure();
        }
    }

    /**
     * Allows to create the chinese symbols for the graphic view.
     *
     * @return the matrix of ChineseSymbols to display
     */
    public ChineseSymbol[][] createSymbols() {
        this.gameBoard.createChineseSymbols();
        return this.gameBoard.getSymbols();
    }

    /**
     * @return the first player of the game.
     */
    public Player getFirstPlayer() {
        return this.firstPlayer;
    }

    /**
     * @return the GameBoard object of the Game (used for the Demo)
     */
    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    /**
     * @return the second player of the game.
     */
    public Player getSecondPlayer() {
        return this.secondPlayer;
    }
}
