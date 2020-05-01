package Tests;

import game.bin.Pawn;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utilities.GameColor;
import utilities.MatrixUtilities;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MatrixUtilitiesTest {

    private Object[][] objects;
    private static Pawn p;

    @BeforeAll
    static void startUp() {
        p = new Pawn(GameColor.BLUE, 5);
    }

    @BeforeEach
    void setUp() {
        this.objects = new Object[5][5];
    }

    @AfterEach
    void tearDown() {
        this.objects = null;
    }

    @Test
    void isOutOfMatrix() {
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, 5, 0));
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, -1, 0));
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, 0, 5));
        assertTrue(MatrixUtilities.isOutOfMatrix(this.objects, 0, -1));
        assertFalse(MatrixUtilities.isOutOfMatrix(this.objects, 0, 0));
        assertFalse(MatrixUtilities.isOutOfMatrix(this.objects, 4, 4));
        assertFalse(MatrixUtilities.isOutOfMatrix(this.objects, 2, 4));
    }

    @Test
    void countObjectLine() {
        assertEquals(0, MatrixUtilities.countObjectLine(this.objects, 2));
        this.objects[2][3] = p;
        assertEquals(1, MatrixUtilities.countObjectLine(this.objects, 2));
        this.objects[2][1] = p;
        this.objects[2][4] = p;
        this.objects[3][4] = p;
        assertEquals(3, MatrixUtilities.countObjectLine(this.objects, 2));
        assertEquals(1, MatrixUtilities.countObjectLine(this.objects, 3));
    }

    @Test
    void countObjectColumn() {
        assertEquals(0, MatrixUtilities.countObjectColumn(this.objects, 2));
        this.objects[3][2] = p;
        assertEquals(1, MatrixUtilities.countObjectColumn(this.objects, 2));
        this.objects[1][2] = p;
        this.objects[4][2] = p;
        this.objects[4][3] = p;
        assertEquals(3, MatrixUtilities.countObjectColumn(this.objects, 2));
        assertEquals(1, MatrixUtilities.countObjectColumn(this.objects, 3));
    }

    @Test
    void countObjectDiagAsc() {
        assertEquals(0, MatrixUtilities.countObjectDiagAsc(this.objects, 2, 3));
        this.objects[2][3] = p;
        assertEquals(1, MatrixUtilities.countObjectDiagAsc(this.objects, 2, 3));
        this.objects[4][1] = p;
        this.objects[1][4] = p;
        this.objects[0][2] = p;
        assertEquals(3, MatrixUtilities.countObjectDiagAsc(this.objects, 2, 3));
        assertEquals(1, MatrixUtilities.countObjectDiagAsc(this.objects, 1, 1));
    }

    @Test
    void countObjectDiagDesc() {
        assertEquals(0, MatrixUtilities.countObjectDiagDesc(this.objects, 2, 3));
        this.objects[2][3] = p;
        assertEquals(1, MatrixUtilities.countObjectDiagDesc(this.objects, 2, 3));
        this.objects[0][1] = p;
        this.objects[3][4] = p;
        this.objects[4][4] = p;
        assertEquals(3, MatrixUtilities.countObjectDiagDesc(this.objects, 2, 3));
        assertEquals(1, MatrixUtilities.countObjectDiagDesc(this.objects, 1, 1));
    }

    @Test
    void pointsConnected() {
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

    @Test
    void getNeighbours() {
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

    @Test
    void meetAdverse() {
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