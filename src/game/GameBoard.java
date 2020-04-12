package Game;


import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class GameBoard implements Serializable {

    private Element[][] board;
    transient private ChineseSymbol[][] symbols;
    private ArrayList<Pawn> firstPlayer;
    private ArrayList<Pawn> secondPlayer;
    private final static int DIMENSION = 11;

    public GameBoard(ArrayList<Pawn> firstPlayerPawns, ArrayList<Pawn> secondPlayerPawns) {
        if (firstPlayerPawns != null && secondPlayerPawns != null) {
            this.firstPlayer = firstPlayerPawns;
            this.secondPlayer = secondPlayerPawns;
            this.board = new Element[GameBoard.getDIMENSION()][GameBoard.getDIMENSION()];
            placePlayerPawns();
            createZenPawn();
            createChineseSymbols();
            completeBoard();
        } else {
            throw new IllegalArgumentException("GameBoard initialization error");
        }
    }

    protected void createChineseSymbols() {
        this.symbols = new ChineseSymbol[GameBoard.getDIMENSION()][GameBoard.getDIMENSION()];
        createOneSymbol(10, 10);
        createOneSymbol(10, 0);
        createOneSymbol(0, 10);
        createOneSymbol(5, 5);
        for (int i = 0; i < 6; i++) {
            createOneSymbol(i, 5 - i);
            createOneSymbol(i, 5 + i);
            createOneSymbol(10 - i, 5 - i);
            createOneSymbol(10 - i, 5 + i);
        }
    }

    private void createOneSymbol(int line, int column) {
        try {
            this.symbols[line][column] = new ChineseSymbol(line, column, ImageIO.read(new File("pictures/symbols/" + line + "" + column + ".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createZenPawn() {
        Pawn pawn = new Pawn();
        this.firstPlayer.add(pawn);
        this.secondPlayer.add(pawn);
        placeOnePawn(GameBoard.DIMENSION / 2, GameBoard.DIMENSION / 2, pawn);
    }

    private void placePlayerPawns() {
        // first player
        Iterator<Pawn> firstP = this.firstPlayer.iterator();
        placeOnePawn(GameBoard.DIMENSION - 1, 0, firstP.next());
        placeOnePawn(GameBoard.DIMENSION / 2, 0, firstP.next());
        placeFourPawns(3, 2, firstP);
        placeFourPawns(1, 4, firstP);
        placeOnePawn(GameBoard.DIMENSION / 2, GameBoard.DIMENSION - 1, firstP.next());
        placeOnePawn(0, GameBoard.DIMENSION - 1, firstP.next());
        // second player
        Iterator<Pawn> secondP = this.secondPlayer.iterator();
        placeOnePawn(0, 0, secondP.next());
        placeOnePawn(0, GameBoard.DIMENSION / 2, secondP.next());
        placeFourPawns(4, 1, secondP);
        placeFourPawns(2, 3, secondP);
        placeOnePawn(GameBoard.DIMENSION - 1, GameBoard.DIMENSION / 2, secondP.next());
        placeOnePawn(GameBoard.DIMENSION - 1, GameBoard.DIMENSION - 1, secondP.next());
    }

    private void placeFourPawns(int column, int line, Iterator<Pawn> pawns) {
        placeOnePawn(column, line, pawns.next());
        placeOnePawn(GameBoard.DIMENSION - column - 1, line, pawns.next());
        placeOnePawn(GameBoard.DIMENSION - column - 1, GameBoard.DIMENSION - line - 1, pawns.next());
        placeOnePawn(column, GameBoard.DIMENSION - line - 1, pawns.next());
    }

    private void placeOnePawn(int column, int line, Pawn pawn) {
        this.board[line][column] = pawn;
        pawn.movePawn(line, column);
    }

    private void completeBoard() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j] == null) {
                    this.board[i][j] = new Element(i, j);
                }
            }
        }
    }

    protected void movePawn(int lastLine, int lastColumn, int nextLine, int nextColumn) {
        this.board[nextLine][nextColumn] = this.board[lastLine][lastColumn];
        this.board[lastLine][lastColumn] = new Element(lastLine, lastColumn);
        this.board[nextLine][nextColumn].setPosition(nextLine, nextColumn);
    }

    protected static Pawn findPawn(int number, Player player) {
        Pawn thePawn = null;
        for (Pawn pawn : player.getPawns()) {
            if (pawn.getNumber() == number) {
                thePawn = pawn;
                break;
            }
        }
        return thePawn;
    }

    public Element[][] getBoard() {
        return this.board;
    }

    public ChineseSymbol[][] getSymbols() {
        return this.symbols;
    }

    public static int getDIMENSION() {
        return GameBoard.DIMENSION;
    }
}
