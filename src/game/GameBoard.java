package game;

import Utilities.MatrixUtilities;

import java.util.ArrayList;
import java.util.Iterator;

public class GameBoard {

    private Pawn[][] board;
    private ArrayList<Pawn> firstPlayer;
    private ArrayList<Pawn> secondPlayer;
    //    private GraphicalInterface anInterface;
    public final static int DIMENSION = 11;

    public GameBoard(ArrayList<Pawn> firstPlayerPawns, ArrayList<Pawn> secondPlayerPawns, String mode) {
        if (firstPlayerPawns != null && secondPlayerPawns != null) {
            this.firstPlayer = firstPlayerPawns;
            this.secondPlayer = secondPlayerPawns;
            this.board = new Pawn[GameBoard.DIMENSION][GameBoard.DIMENSION];
            placePlayerPawns();
            createZenPawn();
            MatrixUtilities.showPawnsMatrix(this.board);
//            if(mode.equals("graphic")){
//               this.anInterface = new GraphicalInterface(this.board);
//            }
        } else {
            System.out.print("GameBoard initialization error");
        }
    }

    private void createZenPawn(){
        Pawn pawn = new Pawn();
        this.firstPlayer.add(pawn);
        this.secondPlayer.add(pawn);
        placeOnePawn(DIMENSION/2, DIMENSION/2, pawn);
    }

    private void placePlayerPawns() {
        // first player
        Iterator<Pawn> firstP = this.firstPlayer.iterator();
        placeOnePawn(DIMENSION - 1, 0, firstP.next());
        placeOnePawn(DIMENSION / 2, 0, firstP.next());
        placeFourPawns(3, 2, firstP);
        placeFourPawns(1, 4, firstP);
        placeOnePawn(DIMENSION / 2, DIMENSION - 1, firstP.next());
        placeOnePawn(0, DIMENSION - 1, firstP.next());
        // second player
        Iterator<Pawn> secondP = this.secondPlayer.iterator();
        placeOnePawn(0, 0, secondP.next());
        placeOnePawn(0, DIMENSION / 2, secondP.next());
        placeFourPawns(4, 1, secondP);
        placeFourPawns(2, 3, secondP);
        placeOnePawn(DIMENSION - 1, DIMENSION / 2, secondP.next());
        placeOnePawn(DIMENSION - 1, DIMENSION - 1, secondP.next());
    }

    private void placeFourPawns(int column, int line, Iterator<Pawn> pawns) {
        placeOnePawn(column, line, pawns.next());
        placeOnePawn(DIMENSION - column - 1, line, pawns.next());
        placeOnePawn(DIMENSION - column - 1, DIMENSION - line - 1, pawns.next());
        placeOnePawn(column, DIMENSION - line - 1, pawns.next());
    }

    private void placeOnePawn(int column, int line, Pawn pawn) {
        board[line][column] = pawn;
        pawn.movePawn(line, column);
    }

    protected void movePawn(int lastLine, int lastColumn, int nextLine, int nextColumn) {
        this.board[nextLine][nextColumn] = this.board[lastLine][lastColumn];
        this.board[lastLine][lastColumn] = null;
        this.board[nextLine][nextColumn].setPosition(nextLine, nextColumn);
        MatrixUtilities.showPawnsMatrix(this.board);
    }

//    public GraphicalInterface getAnInterface() {
//        return anInterface;
//    }

    protected Pawn findPawn(int number, Player player) {
        Pawn thePawn = null;
        for (Pawn pawn : player.getPawns()) {
            if (pawn.getNumber() == number) {
                thePawn = pawn;
                break;
            }
        }
        return thePawn;
    }

    public Pawn[][] getBoard() {
        return board;
    }

    public static int getDIMENSION() {
        return DIMENSION;
    }
}
