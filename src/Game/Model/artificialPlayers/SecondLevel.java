package game.model.artificialPlayers;

import game.controller.Game;
import game.model.Element;
import game.model.GameBoard;
import game.model.Pawn;
import game.model.Player;
import utilities.GameColor;
import utilities.Language;
import utilities.MatrixUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

/**
 * Java class that allows to create an artificial player against which a human player will be able
 * to play in one-player games. This level is more difficult than the one in the FirstLevel class.
 */
public class SecondLevel extends ArtificialPlayer implements Serializable {

    /**
     * Constructor of the class that initializes an artificial player of level 2
     * and that sets the artificialPlayer boolean to true in the superclass Player
     * (which is the superclass of its superclass)
     * @param name the name of the player
     * @param color the color of the player
     */
    public SecondLevel(String name, GameColor color) {
        super(name, color);
        this.setArtificialPlayer(true);
    }

    /**
     * Constructor of the class that creates a temporary artificial player
     * object. The goal of this object should be to use its createPlayer method
     * to obtain a fully competent player.
     */
    public SecondLevel() {
        super("", GameColor.WHITE);
    }

    /**
     * Allows to compute the move that will be done by the player. In this level and
     * while all the pawns are separated, the most far pawn will be bring back to the
     * center of the pawn. After this, this player will try to bring back the pawn
     * that is the most far to the biggest chain of pawns to this chain. Waits 1 second
     * before playing.
     * @param board   the board of Elements where the game is taking place
     * @param adverse the adverse of this player
     * @param game    the current game
     * @return a correct displacement
     */
    @Override
    public int[] play(Element[][] board, Player adverse, Game game) {
        ArrayList<Pawn> biggestChain = this.biggestChain(board);
        int[] move;
        if (biggestChain.size() == 1) {
            move = this.goToAPoint(board, game, adverse, GameBoard.getDIMENSION() / 2, GameBoard.getDIMENSION() / 2, biggestChain);
        } else {
            move = this.goToAPoint(board, game, adverse, biggestChain.get(0).getLineIndex(), biggestChain.get(0).getColumnIndex(), biggestChain);
            if (this.incorrectMove(move, adverse, game)) {
                ArrayList<Pawn> littleChain = new ArrayList<>();
                littleChain.add(biggestChain.get(0));
                move = this.goToAPoint(board, game, adverse, biggestChain.get(0).getLineIndex(), biggestChain.get(0).getColumnIndex(), littleChain);
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return move;
    }

    /**
     * Allows to bring back the most far pawn of a point to this point, if it's not
     * possible, the second more far point if bring back ... If all the best moves for
     * all pawns that are not in chain are impossibles, the second best move is done.
     * If it is still not possible, a pretty random move is done.
     * @param board the board of Elements where the game is taking place
     * @param game the current game
     * @param adverse the adverse of this player
     * @param line the line index of the point to which the pawns are to be moved closer together
     * @param column the column index of the point to which the pawns are to be moved closer together
     * @param biggestChain the ArrayList containing the pawns that are in the biggest chain of pawns on the board
     * @return the best move to do considering this method
     */
    private int[] goToAPoint(Element[][] board, Game game, Player adverse, int line, int column, ArrayList<Pawn> biggestChain) {
        ArrayList<Pawn> notInChain = new ArrayList<>(this.getPawns());
        notInChain.removeAll(biggestChain);
        ArrayList<Pawn> pawns = this.farFromPointOrdered(line, column, notInChain);
        boolean hasMoved;
        int[] moves;
        int i = pawns.size() - 1;
        do {
            moves = this.bestMove(pawns.get(i), board, line, column);
            hasMoved = !this.incorrectMove(moves, adverse, game);
            i--;
        } while (!hasMoved && i >= 0);
        if (!hasMoved) {
            i = pawns.size() - 1;
            do {
                moves = this.secondBestMove(pawns.get(i), board, line, column);
                hasMoved = !this.incorrectMove(moves, adverse, game);
                i--;
            } while (!hasMoved && i >= 0);
        }
        if (!hasMoved) {
            i = pawns.size() - 1;
            do {
                moves = this.randomMove(pawns.get(i), board, game, adverse);
                hasMoved = !this.incorrectMove(moves, adverse, game);
                i--;
            } while (!hasMoved && i >= 0);
        }
        return moves;
    }

    /**
     * Allows to get the ArrayList containing the pawns that are not in the biggest chain
     * sorted by their distance to the point defined by the line and the column passed
     * in parameters.
     * @param line the line index of the point to which the pawns are to be moved closer together
     * @param column the column index of the point to which the pawns are to be moved closer together
     * @param notInChain the ArrayList of pawns that are not in the biggest chain.
     * @return the sorted ArrayList of pawns
     */
    private ArrayList<Pawn> farFromPointOrdered(int line, int column, ArrayList<Pawn> notInChain) {
        int[] distances = new int[notInChain.size()];
        HashMap<Integer, Pawn> map = new HashMap<>();
        for (int i = 0; i < distances.length; i++) {
            map.put(Math.abs(notInChain.get(i).getColumnIndex() - line) + Math.abs(notInChain.get(i).getLineIndex() - column), notInChain.get(i));
        }
        TreeMap<Integer, Pawn> orderedMap = new TreeMap<>(map);
        return new ArrayList<>(orderedMap.values());
    }

    /**
     * Allows to compute the best move for a pawn considering its distance to the point identified
     * by the line and the column. If the difference between the line where the pawn is and the line
     * of the point is higher than the difference between the column, the pawn move on the line. Else
     * it moves on the column. If both differences are the same the pawn moves diagonally.
     * @param pawn the pawn to move
     * @param board the board of Elements where the game is taking place
     * @param line the line of the point
     * @param column the line of the column
     * @return the coordinates of the displacement according to the format [pawn, line, column]
     */
    private int[] bestMove(Pawn pawn, Element[][] board, int line, int column) {
        int lineDifference = pawn.getLineIndex() - line;
        int columnDifference = pawn.getColumnIndex() - column;
        int lineNumber = MatrixUtilities.countObjectLine(board, pawn.getLineIndex());
        int columnNumber = MatrixUtilities.countObjectColumn(board, pawn.getColumnIndex());
        int[] moves = new int[3];
        moves[0] = pawn.getNUMBER();
        if (Math.abs(lineDifference) > Math.abs(columnDifference)) {
            moves[2] = pawn.getColumnIndex();
            if (lineDifference > 0) moves[1] = pawn.getLineIndex() - columnNumber;
            else moves[1] = pawn.getLineIndex() + columnNumber;
        } else if (Math.abs(lineDifference) < Math.abs(columnDifference)) {
            moves[1] = pawn.getLineIndex();
            if (columnDifference > 0) moves[2] = pawn.getColumnIndex() - lineNumber;
            else moves[2] = pawn.getColumnIndex() + lineNumber;
        } else {
            int diagAscNumber = MatrixUtilities.countObjectDiagAsc(board, pawn.getLineIndex(), pawn.getColumnIndex());
            int diagDescNumber = MatrixUtilities.countObjectDiagDesc(board, pawn.getLineIndex(), pawn.getColumnIndex());
            if (lineDifference > 0) {
                moves[1] = pawn.getLineIndex() + MatrixUtilities.countObjectDiagAsc(board, pawn.getLineIndex(), pawn.getColumnIndex());
                if (columnDifference > 0) {
                    moves[1] = pawn.getLineIndex() - diagDescNumber;
                    moves[2] = pawn.getColumnIndex() - diagDescNumber;
                } else {
                    moves[1] = pawn.getLineIndex() - diagAscNumber;
                    moves[2] = pawn.getColumnIndex() + diagAscNumber;
                }
            } else {
                if (columnDifference > 0) {
                    moves[1] = pawn.getLineIndex() + diagAscNumber;
                    moves[2] = pawn.getColumnIndex() - diagAscNumber;
                } else {
                    moves[1] = pawn.getLineIndex() + diagDescNumber;
                    moves[2] = pawn.getColumnIndex() + diagDescNumber;
                }
            }
        }
        return moves;
    }

    /**
     * Allows to compute the best move for a pawn considering its distance to the point identified
     * by the line and the column.
     * @param pawn the pawn to move
     * @param board the matrix of Elements where the game is taking place
     * @param line the line of the point
     * @param column the line of the column
     * @return the coordinates of the displacement according to the format [pawn, line, column]
     */
    private int[] secondBestMove(Pawn pawn, Element[][] board, int line, int column) {
        int lineDifference = pawn.getLineIndex() - line;
        int columnDifference = pawn.getColumnIndex() - column;
        int lineNumber = MatrixUtilities.countObjectLine(board, pawn.getLineIndex());
        int columnNumber = MatrixUtilities.countObjectColumn(board, pawn.getColumnIndex());
        int[] moves = new int[3];
        moves[0] = pawn.getNUMBER();
        if (Math.abs(lineDifference) > Math.abs(columnDifference)) {
            moves[1] = pawn.getLineIndex();
            if (columnDifference > 0) moves[2] = pawn.getColumnIndex() - columnNumber;
            else moves[2] = pawn.getColumnIndex() + columnNumber;
        } else {
            moves[2] = pawn.getColumnIndex();
            if (lineDifference > 0) moves[1] = pawn.getLineIndex() - lineNumber;
            else moves[1] = pawn.getLineIndex() + lineNumber;
        }
        return moves;
    }

    /**
     * Allows to compute all the possible moves for one pawn. If this pawn can move
     * in any direction, this method will find it and returns the first possible
     * displacement that is found.
     * @param pawn the pawn to move
     * @param board the matrix of Elements where the game is taking place
     * @param game the current game
     * @param adverse the adverse of this player
     * @return the coordinates of the displacement according to the format [pawn, line, column]
     */
    private int[] randomMove(Pawn pawn, Element[][] board, Game game, Player adverse) {
        int columnNumber = MatrixUtilities.countObjectColumn(board, pawn.getColumnIndex());
        int lineNumber = MatrixUtilities.countObjectLine(board, pawn.getLineIndex());
        int[] move = {pawn.getNUMBER(), pawn.getLineIndex(), pawn.getColumnIndex() - lineNumber};
        if (incorrectMove(move, adverse, game)) {
            move[2] = pawn.getColumnIndex() + lineNumber;
        }
        if (incorrectMove(move, adverse, game)) {
            move[2] = pawn.getColumnIndex();
            move[1] = pawn.getLineIndex() - columnNumber;
        }
        if (incorrectMove(move, adverse, game)) {
            move[1] = pawn.getLineIndex() + columnNumber;
        }
        if (incorrectMove(move, adverse, game)) {
            int diagAscNumber = MatrixUtilities.countObjectDiagAsc(board, pawn.getLineIndex(), pawn.getColumnIndex());
            int diagDescNumber = MatrixUtilities.countObjectDiagDesc(board, pawn.getLineIndex(), pawn.getColumnIndex());
            move[1] = pawn.getLineIndex() + diagAscNumber;
            move[2] = pawn.getColumnIndex() - diagAscNumber;
            if (incorrectMove(move, adverse, game)) {
                move[1] = pawn.getLineIndex() + diagDescNumber;
                move[2] = pawn.getColumnIndex() + diagDescNumber;
            }
            if (incorrectMove(move, adverse, game)) {
                move[1] = pawn.getLineIndex() - diagAscNumber;
                move[2] = pawn.getColumnIndex() + diagAscNumber;
            }
            if (incorrectMove(move, adverse, game)) {
                move[1] = pawn.getLineIndex() - diagDescNumber;
                move[2] = pawn.getColumnIndex() - diagDescNumber;
            }
        }
        return move;
    }

    /**
     * Allows to get the biggest chain of pawns of this player in the current game board in an
     * ArrayList of pawns.
     * @param board the matrix of Elements where the game is taking place
     * @return the biggest chain of pawns.
     */
    private ArrayList<Pawn> biggestChain(Element[][] board) {
        ArrayList<Pawn> toDo = new ArrayList<>(this.getPawns());
        ArrayList<ArrayList<Pawn>> chains = new ArrayList<>();
        while (!toDo.isEmpty()) {
            ArrayList<Pawn> toTreat = new ArrayList<>();
            toTreat.add(toDo.get(0));
            ArrayList<Pawn> aChain = new ArrayList<>();
            toDo.remove(toDo.get(0));
            while (!toTreat.isEmpty()) {
                aChain.add(toTreat.get(0));
                toTreat.addAll(MatrixUtilities.getNeighbours(board, toTreat.get(0).getColumnIndex(), toTreat.get(0).getLineIndex(), toDo));
                toDo.removeAll(toTreat);
                toTreat.remove(0);
            }
            chains.add(aChain);
        }
        toDo = new ArrayList<>();
        for (ArrayList<Pawn> list : chains) {
            if (list.size() > toDo.size()) {
                toDo = list;
            }
        }
        return toDo;
    }

    /**
     * Allows to create a SecondLevel artificial player based on
     * the color chosen by its adverse.
     * @param color the already chosen color
     * @return the new FirstLevel player.
     */
    @Override
    public ArtificialPlayer createPlayer(GameColor color) {
        if (color == GameColor.WHITE) return new SecondLevel("ord", GameColor.getRandomColor(color));
        else return new SecondLevel("ord", GameColor.WHITE);
    }

    /**
     * Allows to print the level of this player in the corresponding
     * language.
     * @return the String representation of a SecondLevel player
     */
    @Override
    public String toString() {
        return Language.getText("level2");
    }
}
