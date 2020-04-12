package Game;

import Utilities.GameColor;

import java.io.Serializable;
import java.util.ArrayList;


public class Player implements Serializable {

    private final String NAME;
    private int points;
    private GameColor color;
    private final static int PAWNS_NUMBER = 12;
    private boolean isPlaying;
    private boolean artificialPlayer = false;
    private ArrayList<Pawn> pawns = new ArrayList<>();

    public Player(String name, GameColor color) {
        if (name != null) {
            this.color = color;
            this.NAME = name;
            resetPawns();
        } else {
            throw new IllegalArgumentException("Player initialization error");
        }
    }

    protected void resetPawns() {
        this.pawns.clear();
        for(int i = 0; i < PAWNS_NUMBER; i++) {
            this.pawns.add(new Pawn(this.color, this, i));
        }
    }

    protected boolean pawnAlive(int number){
        boolean alive = false;
        for(Pawn pawn : this.pawns){
            if (pawn.getNumber() == number) {
                alive = true;
                break;
            }
        }
        return alive;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    protected void addPoint() {
        this.points++;
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    public ArrayList<Pawn> getPawns() {
        return this.pawns;
    }

    public static int getPAWNS_NUMBER() {
        return Player.PAWNS_NUMBER;
    }

    public int getPoints(){
        return this.points;
    }

    public GameColor getColor() {
        return this.color;
    }

    public boolean isArtificialPlayer() {
        return this.artificialPlayer;
    }

    public void setArtificialPlayer(boolean artificialPlayer) {
        this.artificialPlayer = artificialPlayer;
    }

    public String getNAME() {
        return this.NAME;
    }
}
