package Game;

import ArtificialPlayers.ArtificialPlayer;
import Utilities.Language;
import Utilities.MatrixUtilities;

import javax.swing.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Game implements Serializable {

    transient private GameMode mode;
    private Level level;
    private Player firstPlayer;
    private Player secondPlayer;
    private GameBoard gameBoard;
    private int[] zenMoves = {GameBoard.getDIMENSION() / 2, GameBoard.getDIMENSION() / 2, -1, -1};
    private boolean zenMoved = false;
    private Player lastPlayerZ = null;

    public Game(GameMode mode, Player firstPlayer, Player secondPlayer, Level level) {
        if (firstPlayer != null && mode != null && level != null) {
            this.level = level;
            this.mode = mode;
            this.firstPlayer = firstPlayer;
            if (secondPlayer instanceof ArtificialPlayer) {
                this.secondPlayer = ((ArtificialPlayer) secondPlayer).createPlayer(firstPlayer.getColor());
            } else {
                this.secondPlayer = secondPlayer;
            }
            this.gameBoard = new GameBoard(this.firstPlayer.getPawns(), this.secondPlayer.getPawns());
            if (mode instanceof Graphic) {
                Element.setPairColor(this.firstPlayer.getColor());
                Element.setImpairColor(this.secondPlayer.getColor());
                ((Graphic) mode).setAnInterface(this);
            } else {
                mode.movePawn(this.gameBoard.getBoard());
                ((Console) this.mode).setGame(this);
            }
        } else {
            throw new IllegalArgumentException("Game initialization error");
        }
    }

    public void restartGame(){
        int mode = JOptionPane.showConfirmDialog(null, Language.getText("mode question"), "Zen l'Initié",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (mode == JOptionPane.YES_NO_OPTION){
            this.mode = new Graphic(this.level);
        } else {
            this.mode = new Console(this.level);
        }
        this.mode.restartGame(this);
    }

    protected void beginGame() {
        firstPlayer.setPlaying(true);
        secondPlayer.setPlaying(false);
        play();
    }

    protected void play() {
        while (!MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), firstPlayer.getPawns())
                && !MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), secondPlayer.getPawns())) {

            int[] move;
            Player player;
            Player adverse;
            if (firstPlayer.isPlaying()) {
                player = firstPlayer;
                adverse = secondPlayer;
            } else {
                adverse = firstPlayer;
                player = secondPlayer;
            }
            do {
                if (player.isArtificialPlayer())
                    move = ((ArtificialPlayer) player).play(this.gameBoard.getBoard(), adverse, this);
                else move = this.mode.play(player);
            } while (!checkMove(player, move, adverse));

            Pawn pawn = GameBoard.findPawn(move[0], player);
            this.eatAdversePawn(move[1], move[2], adverse, player);
            int lastLine = pawn.getLineIndex();
            int lastColumn = pawn.getColumnIndex();
            this.gameBoard.movePawn(lastLine, lastColumn, move[1], move[2]);
            this.mode.movePawn(this.gameBoard.getBoard());

            this.firstPlayer.setPlaying(!this.firstPlayer.isPlaying());
            this.secondPlayer.setPlaying(!this.secondPlayer.isPlaying());
        }
        endGame();
    }

    private void endGame() {
        boolean firstWin = MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), this.firstPlayer.getPawns());
        boolean secondWin = MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), this.secondPlayer.getPawns());
        if (firstWin && secondWin) {
            this.firstPlayer.addPoint();
            this.secondPlayer.addPoint();
            this.mode.endGame(this.firstPlayer, this.secondPlayer, this, true);
        } else if (firstWin) {
            this.firstPlayer.addPoint();
            this.mode.endGame(this.firstPlayer, this.secondPlayer, this, false);
        } else {
            this.secondPlayer.addPoint();
            this.mode.endGame(this.secondPlayer, this.firstPlayer, this, false);
        }
    }

    public boolean checkMove(Player player, int[] move, Player adverse) {
        boolean okay;
        Pawn pawn = GameBoard.findPawn(move[0], player);

        if (this.gameBoard.getBoard()[move[1]][move[2]] instanceof Pawn && player.getPawns().contains(this.gameBoard.getBoard()[move[1]][move[2]])
                && ((Pawn) this.gameBoard.getBoard()[move[1]][move[2]]).getNumber() != -1) {
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

            if (pawn.getNumber() == -1) {
                ArrayList<Pawn> allPawns = new ArrayList<>(player.getPawns());
                allPawns.addAll(adverse.getPawns());
                okay = okay && !MatrixUtilities.getNeighbours(this.gameBoard.getBoard(), pawn.getColumnIndex(), pawn.getLineIndex(), allPawns).isEmpty();
                if (move[2] == this.zenMoves[3] && move[1] == this.zenMoves[2] && this.lastPlayerZ != null && this.lastPlayerZ != player && this.zenMoved) {
                    okay = false;
                    this.mode.zenAlreadyPlaced();
                } else {
                    this.zenMoved = true;
                    this.zenMoves[3] = this.zenMoves[1];
                    this.zenMoves[2] = this.zenMoves[0];
                    this.zenMoves[0] = move[1];
                    this.zenMoves[1] = move[2];
                    this.lastPlayerZ = player;
                }
            } else {
                this.zenMoved = false;
            }
        }

        if (!okay && !player.isArtificialPlayer()) this.mode.cannotMove();
        return okay;
    }

    protected void replay() {
        this.firstPlayer.resetPawns();
        this.secondPlayer.resetPawns();
        this.gameBoard = new GameBoard(this.firstPlayer.getPawns(), this.secondPlayer.getPawns());
        if (this.mode instanceof Graphic) {
            ((Graphic) mode).setAnInterface(this);
        }
        beginGame();
    }

    protected void saveAndCloseGame() {
        try {
            FileOutputStream file = new FileOutputStream("game.ser");
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(this);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void eatAdversePawn(int line, int column, Player adverse, Player player) {
        if (this.gameBoard.getBoard()[line][column] instanceof Pawn) {
            adverse.getPawns().remove(this.gameBoard.getBoard()[line][column]);
            if (((Pawn) this.gameBoard.getBoard()[line][column]).getNumber() == -1) {
                player.getPawns().remove(this.gameBoard.getBoard()[line][column]);
            }
        }
    }

    public GameBoard getGameBoard() {
        return this.gameBoard;
    }

    public Player getFirstPlayer() {
        return this.firstPlayer;
    }

    public Player getSecondPlayer() {
        return this.secondPlayer;
    }
}
