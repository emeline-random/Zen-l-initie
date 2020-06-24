package game.model;

import utilities.GameColor;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Allows to create the board on which the players will play. This board is actualized at
 * every displacement.
 */
public class GameBoard implements Serializable {

    /**
     * The matrix representing the board, it has no empty square
     */
    private final Element[][] board;
    /**
     * The matrix containing the chinese symbols for the graphic view (that never changes so it is not saved)
     */
    transient private ChineseSymbol[][] symbols;
    /**
     * The list of pawns of the first player
     */
    private final ArrayList<Pawn> firstPlayer;
    /**
     * The list of pawns of the second player
     */
    private final ArrayList<Pawn> secondPlayer;
    /**
     * The dimension of the board (11 * 11)
     */
    private final static int DIMENSION = 11;

    /**
     * Constructor of the class, initializes the two lists of pawns and then create the zen pawn
     * and place the pawns on the board. After this it asks to complete the board with Element objects.
     * @param firstPlayerPawns The list of pawns of the first player
     * @param secondPlayerPawns The list of pawns of the second player
     */
    public GameBoard(ArrayList<Pawn> firstPlayerPawns, ArrayList<Pawn> secondPlayerPawns) {
        if (firstPlayerPawns != null && secondPlayerPawns != null) {
            this.firstPlayer = firstPlayerPawns;
            this.secondPlayer = secondPlayerPawns;
            this.board = new Element[GameBoard.getDIMENSION()][GameBoard.getDIMENSION()];
            this.placePlayersPawns();
            this.createZenPawn();
            this.completeBoard();
        } else {
            throw new IllegalArgumentException("GameBoard initialization error");
        }
    }

    /**
     * Allows to create the Zen pawn, to add it to both first and second player lists
     * of pawns and place it at the center of the board.
     */
    private void createZenPawn() {
        Pawn pawn = new Pawn(GameColor.RED, -1);
        this.firstPlayer.add(pawn);
        this.secondPlayer.add(pawn);
        placeOnePawn(GameBoard.DIMENSION / 2, GameBoard.DIMENSION / 2, pawn);
    }

    /**
     * Allows to place all the pawns on the board.
     */
    private void placePlayersPawns() {
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

    /**
     * Allows to place a group of four pawns on the plate (the four pawns are placed
     * symmetrically) according to the coordinates of one pawn.
     * @param column the column of one pawn
     * @param line the line of one pawn
     * @param pawns the pawns to place
     */
    private void placeFourPawns(int column, int line, Iterator<Pawn> pawns) {
        placeOnePawn(column, line, pawns.next());
        placeOnePawn(GameBoard.DIMENSION - column - 1, line, pawns.next());
        placeOnePawn(GameBoard.DIMENSION - column - 1, GameBoard.DIMENSION - line - 1, pawns.next());
        placeOnePawn(column, GameBoard.DIMENSION - line - 1, pawns.next());
    }

    /**
     * Allows to place one pawn on the board at a precise line and column.
     * Also updates the position of the Element object (that the pawn extends).
     * @param column the column to put the pawn to
     * @param line the line to put the pawn to
     * @param pawn the pawn to place
     */
    private void placeOnePawn(int column, int line, Pawn pawn) {
        this.board[line][column] = pawn;
        pawn.setPosition(line, column);
    }

    /**
     * Allows to create the  chinese symbols and to place them on a second
     * board (so that even if a pawn goes on a square where there's a chinese
     * symbol, we don't loose its emplacement).
     */
    public void createChineseSymbols() {
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

    /**
     * Allows to create one ChineseSymbol and to place it on the matrix
     * of ChineseSymbols, the name of the picture that represents the symbol
     * is stored in "res.pictures/symbols/lineColumn.png".
     * @param line the line to place the symbol on
     * @param column the column to place the symbol on
     */
    private void createOneSymbol(int line, int column) {
        try {
            this.symbols[line][column] = new ChineseSymbol(line, column, ImageIO.read(getClass()
                    .getResourceAsStream("/res/pictures/symbols/" +line+""+column+".png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows to complete the board of pawns with Element objects.
     */
    private void completeBoard() {
        for (int i = 0; i < this.board.length; i++) {
            for (int j = 0; j < this.board[i].length; j++) {
                if (this.board[i][j] == null) {
                    this.board[i][j] = new Element(i, j);
                }
            }
        }
    }

    /**
     * Allows to move a pawn on the board. It places a nex Element object
     * where the pawn was.
     * @param lastLine the line where the pawn is
     * @param lastColumn the column where the pawn is
     * @param nextLine the line where the pawn goes
     * @param nextColumn the column where the pawn goes
     */
    public void movePawn(int lastLine, int lastColumn, int nextLine, int nextColumn) {
        this.board[nextLine][nextColumn] = this.board[lastLine][lastColumn];
        this.board[lastLine][lastColumn] = new Element(lastLine, lastColumn);
        this.board[nextLine][nextColumn].setPosition(nextLine, nextColumn);
    }

    /**
     * @return the board of Element objects
     */
    public Element[][] getBoard() {
        return this.board;
    }

    /**
     * @return the matrix of ChineseSymbol objects
     */
    public ChineseSymbol[][] getSymbols() {
        return this.symbols;
    }

    /**
     * @return the dimension of the board
     */
    public static int getDIMENSION() {
        return GameBoard.DIMENSION;
    }
}
