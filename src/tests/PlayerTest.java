package tests;

import game.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.GameColor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the Player class
 */
public class PlayerTest {

    /**
     * the player used in tests
     */
    Player player;

    /**
     * reset the value of the player before each test
     */
    @Before
    public void setUp() {
        this.player = new Player("Name", GameColor.RED);
    }

    /**
     * delete the player
     */
    @After
    public void tearDown() {
        this.player = null;
    }

    /**
     * Allows to check that the resetPawns() method reset the number of pawns in the
     * player's pawns list to the value defined in the Player class.
     */
    @Test
    public void resetPawns() {
        this.player.getPawns().clear();
        assertEquals(0, this.player.getPawns().size());
        this.player.resetPawns();
        assertEquals(Player.getPAWNS_NUMBER(), this.player.getPawns().size());
    }

    /**
     * Allows to check that the addPoint() method as one point to
     * the number of points of the player. To do this, some points
     * are added and then the getPoints() method is used.
     */
    @Test
    public void addPoint() {
        assertEquals(0, this.player.getPoints());
        this.player.addPoint();
        assertEquals(1, this.player.getPoints());
        this.player.addPoint();
        this.player.addPoint();
        assertEquals(3, this.player.getPoints());
    }

    /**
     * Allows to check briefly the setPlaying() method
     */
    @Test
    public void setPlaying() {
        assertFalse(this.player.isPlaying());
        this.player.setPlaying(true);
        assertTrue(this.player.isPlaying());
        this.player.setPlaying(false);
        assertFalse(this.player.isPlaying());
    }

    /**
     * Allows to check briefly the setArtificialPlayer() method
     */
    @Test
    public void setArtificialPlayer() {
        assertFalse(this.player.isArtificialPlayer());
        this.player.setArtificialPlayer(true);
        assertTrue(this.player.isArtificialPlayer());
        this.player.setArtificialPlayer(false);
        assertFalse(this.player.isArtificialPlayer());
    }

    /**
     * Allows to check that the getPawn() method returns the correct pawn
     * in the ArrayList, or null if the asked pawn does not exist in the
     * ArrayList of the player.
     */
    @Test
    public void getPawn() {
        assertEquals(this.player.getPawns().get(0), this.player.getPawn(0));
        assertEquals(this.player.getPawns().get(11), this.player.getPawn(11));
        assertNull(this.player.getPawn(12));
        assertNull(this.player.getPawn(-1));
    }

    /**
     * Allows to check briefly the getPawns() method
     */
    @Test
    public void getPawns() {
        this.player.resetPawns();
        assertEquals(12, this.player.getPawns().size());
        this.player.getPawns().remove(0);
        assertEquals(11, this.player.getPawns().size());
    }

    /**
     * Allows to check briefly the getCOLOR() method
     */
    @Test
    public void getCOLOR() {
        assertEquals(GameColor.RED, this.player.getColor());
        Player p = new Player("name", GameColor.BLUE);
        assertEquals(GameColor.BLUE, p.getColor());
    }

    /**
     * Allows to check briefly the getName() method
     */
    @Test
    public void getNAME() {
        assertEquals("Name", this.player.getName());
        Player p = new Player("Jean", GameColor.BLUE);
        assertEquals("Jean", p.getName());
    }

    /**
     * Allows to check briefly the getPAWNS_NUMBER() method
     */
    @Test
    public void getPAWNS_NUMBER() {
        assertEquals(12, Player.getPAWNS_NUMBER());
    }
}