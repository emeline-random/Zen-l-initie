package Tests;

import game.bin.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.GameColor;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    Player player;

    @BeforeEach
    void setUp() {
        this.player = new Player("Name", GameColor.RED);
    }

    @AfterEach
    void tearDown() {
        this.player = null;
    }

    @Test
    void resetPawns() {
        this.player.getPawns().clear();
        assertEquals(0, this.player.getPawns().size());
        this.player.resetPawns();
        assertEquals(Player.getPAWNS_NUMBER(), this.player.getPawns().size());
    }

    @Test
    void addPoint() {
        assertEquals(0, this.player.getPoints());
        this.player.addPoint();
        assertEquals(1, this.player.getPoints());
        this.player.addPoint();
        this.player.addPoint();
        assertEquals(3, this.player.getPoints());
    }

    @Test
    void setPlaying() {
        assertFalse(this.player.isPlaying());
        this.player.setPlaying(true);
        assertTrue(this.player.isPlaying());
        this.player.setPlaying(false);
        assertFalse(this.player.isPlaying());
    }

    @Test
    void setArtificialPlayer() {
        assertFalse(this.player.isArtificialPlayer());
        this.player.setArtificialPlayer(true);
        assertTrue(this.player.isArtificialPlayer());
        this.player.setArtificialPlayer(false);
        assertFalse(this.player.isArtificialPlayer());
    }

    @Test
    void getPawn() {
        assertEquals(this.player.getPawns().get(0), this.player.getPawn(0));
        assertEquals(this.player.getPawns().get(11), this.player.getPawn(11));
        assertNull(this.player.getPawn(12));
        assertNull(this.player.getPawn(-1));
    }

    @Test
    void getPawns() {
        this.player.resetPawns();
        assertEquals(12, this.player.getPawns().size());
        this.player.getPawns().remove(0);
        assertEquals(11, this.player.getPawns().size());
    }

    @Test
    void getCOLOR() {
        assertEquals(GameColor.RED, this.player.getCOLOR());
        Player p = new Player("name", GameColor.BLUE);
        assertEquals(GameColor.BLUE, p.getCOLOR());
    }

    @Test
    void getNAME() {
        assertEquals("Name", this.player.getNAME());
        Player p = new Player("Jean", GameColor.BLUE);
        assertEquals("Jean", p.getNAME());
    }

    @Test
    void getPAWNS_NUMBER() {
        assertEquals(12, Player.getPAWNS_NUMBER());
    }
}