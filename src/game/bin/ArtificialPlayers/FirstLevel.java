package Game.bin.ArtificialPlayers;

import Utilities.GameColor;
import Utilities.Language;
import Utilities.MatrixUtilities;
import Game.bin.Element;
import Game.Controllers.Game;
import Game.bin.Pawn;
import Game.bin.Player;

import java.io.Serializable;

public class FirstLevel extends ArtificialPlayer implements Serializable {

    private int[] coordinates;

    public FirstLevel(String name, GameColor color) {
        super(name, color);
        this.setArtificialPlayer(true);
    }

    public FirstLevel(){
        super("", GameColor.WHITE);
    }

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

    private boolean aTest(int i, Element[][] board, Player adverse, Game game, Pawn p){
        boolean okay = false;
        int nbPawns;
        switch (i){
            case 0:
                nbPawns = MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex(), p.getColumnIndex() + nbPawns};
                break;
            case 1:
                nbPawns = MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex(), p.getColumnIndex() - nbPawns};
                break;
            case 2:
                nbPawns = MatrixUtilities.countObjectLine(board, p.getLineIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex() + nbPawns, p.getColumnIndex()};
                break;
            case 3:
                nbPawns = MatrixUtilities.countObjectLine(board, p.getLineIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex() - nbPawns, p.getColumnIndex()};
                break;
            case 4:
                nbPawns = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex() + nbPawns, p.getColumnIndex() + nbPawns};
                break;
            case 5:
                nbPawns = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex() - nbPawns, p.getColumnIndex() - nbPawns};
                break;
            case 6:
                nbPawns = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex() + nbPawns, p.getColumnIndex() + nbPawns};
                break;
            case 7:
                nbPawns = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                coordinates = new int[]{p.getNumber(), p.getLineIndex() - nbPawns, p.getColumnIndex() - nbPawns};
                break;
        }
        if(coordinates[1] < board.length && coordinates[2] < board.length && coordinates[1] >= 0 && coordinates[2] >= 0) {
            okay = game.checkMove(this, coordinates, adverse);
        }
        return okay;
    }

    @Override
    public Player createPlayer(GameColor color) {
        return new FirstLevel("ord", GameColor.getRandomColor(color));
    }

    @Override
    public String toString(){
        return Language.getText("level1");
    }
}
