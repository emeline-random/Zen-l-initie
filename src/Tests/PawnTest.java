package Tests;

import game.bin.Pawn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.GameColor;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {

    Pawn p;

    @BeforeEach
    void setUp() {
        this.p = new Pawn(GameColor.RED, 2);
    }

    @AfterEach
    void tearDown() {
        this.p = null;
    }

    @Test
    void testToString() {
        assertEquals("2", this.p.toString());
        Pawn pawn = new Pawn(GameColor.BLUE, -1);
        assertEquals("z", pawn.toString());
    }

    @Test
    void getNUMBER() {
        assertEquals(2, this.p.getNUMBER());
        Pawn pawn = new Pawn(GameColor.BLUE, -1);
        assertEquals(-1, pawn.getNUMBER());
    }

    @Test
    void getANSIColor() {
        assertEquals(GameColor.RED.ANSI_CODE, this.p.getANSIColor());
        Pawn pawn = new Pawn(GameColor.BLUE, -1);
        assertEquals(GameColor.BLUE.ANSI_CODE, pawn.getANSIColor());
    }
}