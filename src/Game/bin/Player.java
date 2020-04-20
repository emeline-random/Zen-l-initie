package Game.bin;

import Utilities.GameColor;

import java.io.Serializable;
import java.util.ArrayList;


public class Player implements Serializable {

    /**The name of the player*/
    private final String NAME;
    /**The number of points of the player*/
    private int points;
    /**The color of the player*/
    private final GameColor color;
    /**The number of pawns for all players*/
    private final static int PAWNS_NUMBER = 12;
    /**True if it's the player's turn to play.*/
    private boolean isPlaying;
    /**True if the player is not a human player*/
    private boolean artificialPlayer = false;
    /**The list of alive pawns of the player*/
    private final ArrayList<Pawn> pawns = new ArrayList<>();

    /**
     * Constructor of the class, allows to create a player with it's name and it's color,
     * then initialize the list of pawns by calling resetPawn() method.
     * @param name the name of the player
     * @param color the color of the player
     */
    public Player(String name, GameColor color) {
        if (name != null) {
            this.color = color;
            this.NAME = name;
            resetPawns();
        } else {
            throw new IllegalArgumentException("Player initialization error");
        }
    }

    /**
     * Allows to create the appropriate number of pawns
     * in the list of pawns of the player depending on PAWNS_NUMBER
     * variable.
     */
    public void resetPawns() {
        this.pawns.clear();
        for(int i = 0; i < PAWNS_NUMBER; i++) {
            this.pawns.add(new Pawn(this.color, i));
        }
    }

    /**
     * Allows to add one point to the player.
     */
    public void addPoint() {
        this.points++;
    }

    /**
     * Allows to set the value of isPlaying variable, should be true
     * if it is this player turn to play.
     * @param playing The new value for the isPlaying variable.
     */
    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    /**
     * Allows to set the value of ArtificialPlayer variable, should
     * be true only if the player is not a human player.
     * @param artificialPlayer the new value of artificialPlayer variable.
     */
    public void setArtificialPlayer(boolean artificialPlayer) {
        this.artificialPlayer = artificialPlayer;
    }

    /**
     * Allows to know if this player is the current player.
     * @return true if it's this player turn to play, false otherwise
     */
    public boolean isPlaying() {
        return this.isPlaying;
    }

    /**
     * Allows to know if this player is an artificial player or not.
     * @return true if the player is an artificial player, false otherwise
     */
    public boolean isArtificialPlayer() {
        return this.artificialPlayer;
    }

    /**
     * Allows to get a precise pawn identified by it's number. The search is
     * made on the list of pawns of this player and if no pawn corresponds to the
     * searched number, this method returns null.
     * @param index the number of the pawn
     * @return the corresponding pawn or null if there's none
     */
    public Pawn getPawn(int index){
        Pawn pawn = null;
        for (Pawn p : this.pawns){
            if (index == p.getNumber()){
                pawn = p;
                break;
            }
        }
        return pawn;
    }

    /**
     * Allows to get all the pawns alive of the player
     * @return the pawns ArrayList<>
     */
    public ArrayList<Pawn> getPawns() {
        return this.pawns;
    }

    /**
     * @return the number of points of this player
     */
    public int getPoints(){
        return this.points;
    }

    /**
     * @return the color chosen by the player
     */
    public GameColor getColor() {
        return this.color;
    }

    /**
     * @return the name of the player
     */
    public String getNAME() {
        return this.NAME;
    }

    /**
     * @return the number of pawns for all players
     */
    public static int getPAWNS_NUMBER() {
        return Player.PAWNS_NUMBER;
    }

}
