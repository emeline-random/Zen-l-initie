package tests;

import game.model.Pawn;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.GameColor;
import utilities.MatrixUtilities;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the MatrixUtilities class
 */
public class MatrixUtilitiesTest {

    /**
     * The object matrix used in all tests
     */
    private Object[][] objects;
    /**
     * A pawn object used in all tests
     */
    private static Pawn p;

    /**
     * Allows to reset the values of the objects matrix and the pawn before each test
     */
    @Before
    public void startUp() {
        p = new Pawn(GameColor.BLUE, 5);
        this.objects = new Object[5][5];
    }

    /**
     * Allows to delete the objects matrix.
     */
    @After
    public void tearDown() {
        this.objects = null;
    }

    /**
     * Allows to perform some correct and error cases of the isOutOfMatrix method.
     * Checks that it returns true if the value given are out of bounds
     */
    @Test
    public void isOutOfMatrix() {
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, 5, 0));
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, -1, 0));
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, 0, 5));
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, 0, -1));
        assertFalse(MatrixUtilities.isOutOfMatrix(this.objects, 0, 0));
        assertFalse(MatrixUtilities.isOutOfMatrix(this.objects, 4, 4));
        assertFalse(MatrixUtilities.isOutOfMatrix(this.objects, 2, 4));
    }

    /**
     * Allows to test the countObjectLine() method by checking the number returned
     * when adding the pawn of the class to some squares. The comparison is made
     * on different lines.
     */
    @Test
    public void countObjectLine() {
        assertEquals(0, MatrixUtilities.countObjectLine(this.objects, 2));
        this.objects[2][3] = p;
        assertEquals(1, MatrixUtilities.countObjectLine(this.objects, 2));
        this.objects[2][1] = p;
        this.objects[2][4] = p;
        this.objects[3][4] = p;
        assertEquals(3, MatrixUtilities.countObjectLine(this.objects, 2));
        assertEquals(1, MatrixUtilities.countObjectLine(this.objects, 3));
    }

    /**
     * Allows to test the countObjectColumn() method by checking the number returned
     * when adding the pawn of the class to some squares. The comparison is made
     * on different columns.
     */
    @Test
    public void countObjectColumn() {
        assertEquals(0, MatrixUtilities.countObjectColumn(this.objects, 2));
        this.objects[3][2] = p;
        assertEquals(1, MatrixUtilities.countObjectColumn(this.objects, 2));
        this.objects[1][2] = p;
        this.objects[4][2] = p;
        this.objects[4][3] = p;
        assertEquals(3, MatrixUtilities.countObjectColumn(this.objects, 2));
        assertEquals(1, MatrixUtilities.countObjectColumn(this.objects, 3));
    }

    /**
     * Allows to test the countObjectDiagAsc() method by checking the number returned
     * when adding the pawn of the class to some squares. The comparison is made
     * on different ascending diagonals (an ascending diagonal is a diagonal that goes
     * from left bottom to right top).
     */
    @Test
    public void countObjectDiagAsc() {
        assertEquals(0, MatrixUtilities.countObjectDiagAsc(this.objects, 2, 3));
        this.objects[2][3] = p;
        assertEquals(1, MatrixUtilities.countObjectDiagAsc(this.objects, 2, 3));
        this.objects[4][1] = p;
        this.objects[1][4] = p;
        this.objects[0][2] = p;
        assertEquals(3, MatrixUtilities.countObjectDiagAsc(this.objects, 2, 3));
        assertEquals(1, MatrixUtilities.countObjectDiagAsc(this.objects, 1, 1));
    }

    /**
     * Allows to test the countObjectDiagDesc() method by checking the number returned
     * when adding the pawn of the class to some squares. The comparison is made
     * on different descending diagonals (a descending diagonal is a diagonal that goes
     * from left top to right bottom).
     */
    @Test
    public void countObjectDiagDesc() {
        assertEquals(0, MatrixUtilities.countObjectDiagDesc(this.objects, 2, 3));
        this.objects[2][3] = p;
        assertEquals(1, MatrixUtilities.countObjectDiagDesc(this.objects, 2, 3));
        this.objects[0][1] = p;
        this.objects[3][4] = p;
        this.objects[4][4] = p;
        assertEquals(3, MatrixUtilities.countObjectDiagDesc(this.objects, 2, 3));
        assertEquals(1, MatrixUtilities.countObjectDiagDesc(this.objects, 1, 1));
    }

    /**
     * Allows to test the pointsConnected() method by adding and removing
     * pawns from the ArrayList in which the points that should be connected
     * are and adding pawns to the objects matrix.
     */
    @Test
    public void pointsConnected() {
        Pawn p1 = new Pawn(GameColor.BLUE, 5);
        Pawn p2 = new Pawn(GameColor.BLUE, 5);
        Pawn p3 = new Pawn(GameColor.BLUE, 5);
        this.objects[0][0] = p1;
        this.objects[0][2] = p2;
        ArrayList<Pawn> pawns = new ArrayList<>();
        pawns.add(p1);
        pawns.add(p2);
        assertFalse(MatrixUtilities.pointsConnected(this.objects, pawns));
        this.objects[0][1] = p2;
        this.objects[0][2] = null;
        assertTrue(MatrixUtilities.pointsConnected(this.objects, pawns));
        this.objects[1][1] = p3;
        pawns.add(p3);
        assertTrue(MatrixUtilities.pointsConnected(this.objects, pawns));
    }

    /**
     * Allows to check that the getNeighbours method returns the correct
     * number of neighbours in different cases (diagonally particularly).
     */
    @Test
    public void getNeighbours() {
        Pawn p1 = new Pawn(GameColor.BLUE, 5);
        Pawn p2 = new Pawn(GameColor.BLUE, 5);
        Pawn p3 = new Pawn(GameColor.BLUE, 5);
        Pawn p4 = new Pawn(GameColor.BLUE, 5);
        Pawn p5 = new Pawn(GameColor.BLUE, 5);
        Pawn p6 = new Pawn(GameColor.BLUE, 5);
        Pawn[] p = {p1, p2, p3, p4, p5, p6};
        ArrayList<Pawn> pawns = new ArrayList<>(Arrays.asList(p));
        this.objects[2][2] = p1;
        assertEquals(0, MatrixUtilities.getNeighbours(this.objects, 2, 2, pawns).size());
        this.objects[3][2] = p2;
        assertEquals(1, MatrixUtilities.getNeighbours(this.objects, 2, 2, pawns).size());
        this.objects[4][1] = p3;
        this.objects[3][3] = p4;
        assertEquals(3, MatrixUtilities.getNeighbours(this.objects, 2, 3, pawns).size());
        this.objects[2][1] = p5;
        this.objects[4][2] = p6;
        assertEquals(5, MatrixUtilities.getNeighbours(this.objects, 2, 3, pawns).size());
        assertEquals(2, MatrixUtilities.getNeighbours(this.objects, 1, 2, pawns).size());
        assertEquals(3, MatrixUtilities.getNeighbours(this.objects, 2, 4, pawns).size());
        pawns.remove(p4);
        assertEquals(2, MatrixUtilities.getNeighbours(this.objects, 2, 4, pawns).size());
    }

    /**
     * Allows to check that the meetAdverse() method returns the correct value. To do this, to
     * ArrayList of pawns are created (one for each player) and the pawns of these lists are add
     * to the objects matrix. Then different tests in different configurations are performed.
     */
    @Test
    public void meetAdverse() {
        Pawn p1 = new Pawn(GameColor.BLUE, 5);
        Pawn p2 = new Pawn(GameColor.BLUE, 5);
        Pawn p3 = new Pawn(GameColor.BLUE, 5);
        Pawn p4 = new Pawn(GameColor.BLUE, 5);
        Pawn p5 = new Pawn(GameColor.BLUE, 5);
        this.objects[1][1] = p1;
        this.objects[1][3] = p2;
        this.objects[3][3] = p3;
        this.objects[2][2] = p4;
        this.objects[3][2] = p5;
        ArrayList<Pawn> firstPlayer = new ArrayList<>();
        firstPlayer.add(p1);
        firstPlayer.add(p2);
        firstPlayer.add(p3);
        ArrayList<Pawn> secondPlayer = new ArrayList<>();
        secondPlayer.add(p4);
        secondPlayer.add(p5);
        assertFalse(MatrixUtilities.meetAdverse(this.objects, secondPlayer, 1, 3, 1, 0));
        assertTrue(MatrixUtilities.meetAdverse(this.objects, secondPlayer, 1, 1, 4, 4));
        assertTrue(MatrixUtilities.meetAdverse(this.objects, secondPlayer, 3, 3, 3, 0));
        assertTrue(MatrixUtilities.meetAdverse(this.objects, secondPlayer, 1, 1, 4, 4));
        assertTrue(MatrixUtilities.meetAdverse(this.objects, firstPlayer, 2, 2, 0, 4));
        assertFalse(MatrixUtilities.meetAdverse(this.objects, firstPlayer, 2, 2, 4, 2));
        assertFalse(MatrixUtilities.meetAdverse(this.objects, firstPlayer, 3, 2, 2, 1));
    }
}