package game;

import Utilities.MatrixUtilities;

import java.util.ArrayList;

public class Game {

    private GameMode MODE;
    private Player firstPlayer;
    private Player secondPlayer;
    private GameBoard gameBoard;

    public Game(GameMode mode, Player firstPlayer, Player secondPlayer) {
        if (firstPlayer != null && mode != null) {
            this.MODE = new GameMode();
            this.firstPlayer = firstPlayer;
            if (secondPlayer != null) this.secondPlayer = secondPlayer;
            else this.secondPlayer = new Player(null, null); // TODO à changer

            this.gameBoard = new GameBoard(this.firstPlayer.getPawns(), this.secondPlayer.getPawns(), this.MODE.getMode());
            beginGame();
        } else {
            System.out.println("Game initialization error");
        }
    }

    private void beginGame() {
        firstPlayer.setPlaying(true);
        secondPlayer.setPlaying(false);
        play();
    }

    private void play() {
        while (!MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), firstPlayer.getPawns())
                && !MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), secondPlayer.getPawns())) {

            int[] move;
            Player player;
            Player adverse;
            if (firstPlayer.isPlaying()) {
                player = firstPlayer;
                adverse = secondPlayer;
            }
            else {
                adverse = firstPlayer;
                player = secondPlayer;
            }
            do {
                move = player.play();
            } while (!checkMove(player, move, adverse));

            Pawn pawn = this.gameBoard.findPawn(move[0], player);
            this.gameBoard.movePawn(pawn.getLinePosition(), pawn.getColumnPosition(), move[1], move[2]);

            firstPlayer.setPlaying(!firstPlayer.isPlaying());
            secondPlayer.setPlaying(!secondPlayer.isPlaying());
        }
        if (MatrixUtilities.pointsConnected(this.gameBoard.getBoard(), firstPlayer.getPawns())) {
            firstPlayer.addPoint();
        } else {
            secondPlayer.addPoint();
        }
    }

    private boolean checkMove(Player player, int[] move, Player adverse) {
        boolean okay;
        if (player.getPawns().contains(this.gameBoard.getBoard()[move[1]][move[2]])) okay = false;
        else {
            Pawn pawn = this.gameBoard.findPawn(move[0], player);
            int line = pawn.getLinePosition() - move[1];
            int column = pawn.getColumnPosition() - move[2];
            if ((line > 0 && column > 0) || (line < 0 && column < 0)){
                okay = (Math.abs(column) == MatrixUtilities.countObjectDiagDesc(this.gameBoard.getBoard(), pawn.getLinePosition(), pawn.getColumnPosition()));
            }
            else if (line != 0 && column != 0)  {
                okay = (Math.abs(column) == MatrixUtilities.countObjectDiagAsc(this.gameBoard.getBoard(), pawn.getLinePosition(), pawn.getColumnPosition()));
            }
            else if (column != 0) okay = (Math.abs(column) == MatrixUtilities.countObjectLine(this.gameBoard.getBoard(), move[1]));
            else okay = (Math.abs(line) == MatrixUtilities.countObjectColumn(this.gameBoard.getBoard(), move[2]));

            okay = (okay && !MatrixUtilities.meetAdverse(this.gameBoard.getBoard(), adverse.getPawns(), pawn.getLinePosition(), pawn.getColumnPosition(), move[1], move[2]));

            if(pawn.getNumber() == -1){
                ArrayList<Pawn> allPawns = new ArrayList<>(player.getPawns());
                allPawns.addAll(adverse.getPawns());
                okay = okay && MatrixUtilities.hasNeighbour(this.gameBoard.getBoard(), pawn.getColumnPosition(), pawn.getLinePosition(), allPawns);
            }

        }
        if (!okay) System.out.println("You can't move your pawn there, try again !");
        return okay;
    }
}
