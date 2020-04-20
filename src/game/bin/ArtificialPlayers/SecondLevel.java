package Game.bin.ArtificialPlayers;

import Game.Controllers.Game;
import Game.Model.GameBoard;
import Game.bin.Element;
import Game.bin.Pawn;
import Game.bin.Player;
import Utilities.GameColor;
import Utilities.Language;
import Utilities.MatrixUtilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class SecondLevel extends ArtificialPlayer implements Serializable {


    public SecondLevel(String name, GameColor color) {
        super(name, color);
        this.setArtificialPlayer(true);
    }

    public SecondLevel() {
        super("", GameColor.WHITE);
    }

    @Override
    public int[] play(Element[][] board, Player adverse, Game game) {
        ArrayList<Pawn> biggestChain = this.biggestChain(board);
        int[] move;
        if (biggestChain.size() == 1) {
            move = this.goToAPoint(board, game, adverse, GameBoard.getDIMENSION() / 2, GameBoard.getDIMENSION() / 2, biggestChain);
        } else {
            move = this.goToAPoint(board, game, adverse, biggestChain.get(0).getLineIndex(), biggestChain.get(0).getColumnIndex(), biggestChain);
            if (MatrixUtilities.isOutOfMatrix(board, move[1], move[2]) || !game.checkMove(this, move, adverse)) {
                ArrayList<Pawn> littleChain = new ArrayList<>();
                littleChain.add(biggestChain.get(0));
                move = this.goToAPoint(board, game, adverse, biggestChain.get(0).getLineIndex(), biggestChain.get(0).getColumnIndex(), littleChain);
            }
        }
        return move;
    }

    private int[] goToAPoint(Element[][] board, Game game, Player adverse, int line, int column, ArrayList<Pawn> biggestChain) {
        ArrayList<Pawn> notInChain = new ArrayList<>(this.getPawns());
        notInChain.removeAll(biggestChain);
        ArrayList<Pawn> pawns = this.farFromPointOrdered(line, column, notInChain);
        boolean hasMoved;
        int[] moves;
        int i = pawns.size() - 1;
        do {
            moves = this.bestMove(pawns.get(i), board);
            hasMoved = !this.incorrectMove(moves, adverse, board, game);
            i--;
        } while (!hasMoved && i >= 0);
        if (!hasMoved) {
            i = pawns.size() - 1;
            do {
                moves = this.secondBestMove(pawns.get(i), board);
                hasMoved = !this.incorrectMove(moves, adverse, board, game);
                i--;
            } while (!hasMoved && i >= 0);
        }
        if (!hasMoved) {
            i = pawns.size() - 1;
            do {
                moves = this.randomMove(pawns.get(i), board, game, adverse);
                hasMoved = !this.incorrectMove(moves, adverse, board, game);
                i--;
            } while (!hasMoved && i >= 0);
        }
        return moves;
    }

    private ArrayList<Pawn> farFromPointOrdered(int line, int column, ArrayList<Pawn> notInChain) {
        int[] distances = new int[notInChain.size()];
        HashMap<Integer, Pawn> map = new HashMap<>();
        for (int i = 0; i < distances.length; i++) {
            map.put(Math.abs(notInChain.get(i).getColumnIndex() - line) + Math.abs(notInChain.get(i).getLineIndex() - column), notInChain.get(i));
        }
        TreeMap<Integer, Pawn> orderedMap = new TreeMap<>(map);
        return new ArrayList<>(orderedMap.values());
    }

    private int[] bestMove(Pawn pawn, Element[][] board) {
        int lineDifference = pawn.getLineIndex() - GameBoard.getDIMENSION() / 2;
        int columnDifference = pawn.getColumnIndex() - GameBoard.getDIMENSION() / 2;
        int lineNumber = MatrixUtilities.countObjectLine(board, pawn.getLineIndex());
        int columnNumber = MatrixUtilities.countObjectColumn(board, pawn.getColumnIndex());
        int[] moves = new int[3];
        moves[0] = pawn.getNumber();
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

    private int[] secondBestMove(Pawn pawn, Element[][] board) {
        int lineDifference = pawn.getLineIndex() - GameBoard.getDIMENSION() / 2;
        int columnDifference = pawn.getColumnIndex() - GameBoard.getDIMENSION() / 2;
        int lineNumber = MatrixUtilities.countObjectLine(board, pawn.getLineIndex());
        int columnNumber = MatrixUtilities.countObjectColumn(board, pawn.getColumnIndex());
        int[] moves = new int[3];
        moves[0] = pawn.getNumber();
        if (Math.abs(lineDifference) > Math.abs(columnDifference)) {
            moves[1] = pawn.getLineIndex();
            if (columnDifference > 0) moves[2] = pawn.getColumnIndex() - columnNumber;
            else moves[2] = pawn.getColumnIndex() + columnNumber;
        } else if (Math.abs(lineDifference) < Math.abs(columnDifference)) {
            moves[2] = pawn.getColumnIndex();
            if (lineDifference > 0) moves[1] = pawn.getLineIndex() - lineNumber;
            else moves[1] = pawn.getLineIndex() + lineNumber;
        }
        return moves;
    }

    private int[] randomMove(Pawn pawn, Element[][] board, Game game, Player adverse) {
        int columnNumber = MatrixUtilities.countObjectColumn(board, pawn.getColumnIndex());
        int lineNumber = MatrixUtilities.countObjectLine(board, pawn.getLineIndex());
        int[] move = {pawn.getNumber(), pawn.getLineIndex(), pawn.getColumnIndex() - lineNumber};
        if (incorrectMove(move, adverse, board, game)) {
            move[2] = pawn.getColumnIndex() + lineNumber;
        }
        if (incorrectMove(move, adverse, board, game)) {
            move[2] = pawn.getColumnIndex();
            move[1] = pawn.getLineIndex() - columnNumber;
        }
        if (incorrectMove(move, adverse, board, game)) {
            move[1] = pawn.getLineIndex() + columnNumber;
        }
        if (incorrectMove(move, adverse, board, game)) {
            int diagAscNumber = MatrixUtilities.countObjectDiagAsc(board, pawn.getLineIndex(), pawn.getColumnIndex());
            int diagDescNumber = MatrixUtilities.countObjectDiagDesc(board, pawn.getLineIndex(), pawn.getColumnIndex());
            move[1] = pawn.getLineIndex() + diagAscNumber;
            move[2] = pawn.getColumnIndex() - diagAscNumber;
            if (incorrectMove(move, adverse, board, game)) {
                move[1] = pawn.getLineIndex() + diagDescNumber;
                move[2] = pawn.getColumnIndex() + diagDescNumber;
            }
            if (incorrectMove(move, adverse, board, game)) {
                move[1] = pawn.getLineIndex() - diagAscNumber;
                move[2] = pawn.getColumnIndex() + diagAscNumber;
            }
            if (incorrectMove(move, adverse, board, game)) {
                move[1] = pawn.getLineIndex() - diagDescNumber;
                move[2] = pawn.getColumnIndex() - diagDescNumber;
            }
        }
        return move;
    }

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

    private boolean incorrectMove(int[] move, Player adverse, Element[][] board, Game game) {
        return MatrixUtilities.isOutOfMatrix(board, move[1], move[2]) || !game.checkMove(this, move, adverse);
    }

    @Override
    public Player createPlayer(GameColor color) {
        return new SecondLevel("ord", GameColor.getRandomColor(color));
    }

    @Override
    public String toString() {
        return Language.getText("level2");
    }
}
