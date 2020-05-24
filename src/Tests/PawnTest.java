package tests;

import game.model.Pawn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.GameColor;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the Pawn class
 */
public class PawnTest {

    /**
     * Te pawn used to perform tests
     */
    Pawn p;

    /**
     * Allows to reset the value of the pawn before each test
     */
    @Before
    public void setUp() {
        this.p = new Pawn(GameColor.RED, 2);
    }

    /**
     * Allows to delete the value of the p Pawn.
     */
    @After
    public void tearDown() {
        this.p = null;
    }

    /**
     * Allows to test the toString() method on a "normal" pawn
     * (ie. that has a number that is at least equals to 0) and
     * on the Zen pawn (that has a 'z' for representation)
     */
    @Test
    public void testToString() {
        assertEquals("2", this.p.toString());
        Pawn pawn = new Pawn(GameColor.BLUE, -1);
        assertEquals("z", pawn.toString());
    }

    /**
     * Allows to test that the getNUMBER() method returns the expected
     * number on a "normal" pawn and on the Zen pawn.
     */
    @Test
    public void getNUMBER() {
        assertEquals(2, this.p.getNUMBER());
        Pawn pawn = new Pawn(GameColor.BLUE, -1);
        assertEquals(-1, pawn.getNUMBER());
    }

    /**
     * Allows to test that the ansi escape code returned by the
     * getANSIColor() corresponds to the value that is saved in
     * the GameColor class.
     */
    @Test
    public void getANSIColor() {
        assertEquals(GameColor.RED.ANSI_CODE, this.p.getANSIColor());
        Pawn pawn = new Pawn(GameColor.BLUE, -1);
        assertEquals(GameColor.BLUE.ANSI_CODE, pawn.getANSIColor());
    }
}