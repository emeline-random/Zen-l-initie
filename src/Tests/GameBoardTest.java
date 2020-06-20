package tests;

import game.model.ChineseSymbol;
import game.model.GameBoard;
import game.model.Pawn;
import game.model.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.GameColor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the GameBoard class.
 */
public class GameBoardTest {

    /**
     * The GameBoard used to do the tests
     */
    private GameBoard gameBoard;

    /**
     * Allows to reset the game board used before each test
     */
    @Before
    public void setUp(){
        ArrayList<Pawn> pawns = new ArrayList<>();
        ArrayList<Pawn> pawns1;
        pawns1 = new ArrayList<>();
        for (int i = 0; i < Player.getPAWNS_NUMBER(); i++) {
            pawns.add(new Pawn(GameColor.CYAN, i));
            pawns1.add(new Pawn(GameColor.CYAN, i));
        }
        this.gameBoard = new GameBoard(pawns, pawns1);
    }

    /**
     * Allows to delete the GameBoard object of the class
     */
    @After
    public void tearDown(){
        this.gameBoard = null;
    }

    /**
     * Allows to test the constructor by testing it throws the appropriate exception if one of the
     * ArrayList passed in parameters is null. Then tests the movePawn(...) method. To do this, a GameBoard
     * object is created, then we test that an exception is thrown is case of wrong arguments. After this,
     * we check that the first object is an instance of Pawn and then move it to another square and test if
     * this square contains the pawn and the last square an Element but not a Pawn.
     */
    @Test
    public void movePawn() {
        ArrayList<Pawn> pawns = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> new GameBoard(pawns, null));
        assertThrows(IndexOutOfBoundsException.class, () -> this.gameBoard.movePawn(0, 0, -1, 0));
        assertTrue(this.gameBoard.getBoard()[0][0] instanceof Pawn);
        assertFalse(this.gameBoard.getBoard()[0][3] instanceof Pawn);
        this.gameBoard.movePawn(0, 0, 0, 3);
        assertTrue(this.gameBoard.getBoard()[0][3] instanceof Pawn);
        assertFalse(this.gameBoard.getBoard()[0][0] instanceof Pawn);
        assertNotNull(this.gameBoard.getBoard()[0][0]);
    }

    /**
     * Allows to test the private method createZenPawn by checking that
     * the size of both players pawns' lists increased and that the last
     * element of both lists are the same.
     * @throws NoSuchMethodException if the method was not found in the class
     * @throws InvocationTargetException if the method cannot be applied
     * @throws IllegalAccessException if the method cannot be accessed
     */
    @Test
    public void createZenPawn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Pawn pawn = new Pawn(GameColor.RED, -1);
        ArrayList<Pawn> pawns = new ArrayList<>();
        ArrayList<Pawn> pawns1 = new ArrayList<>();
        for (int i = 0; i < Player.getPAWNS_NUMBER(); i++) {
            pawns.add(pawn);
            pawns1.add(pawn);
        }
        Method method = GameBoard.class.getDeclaredMethod("createZenPawn");
        method.setAccessible(true);
        GameBoard gameBoard = new GameBoard(pawns, pawns1);
        method.invoke(gameBoard);
        assertEquals(Player.getPAWNS_NUMBER() + 2, pawns.size()); //+2 because the constructor already add one
        assertEquals(Player.getPAWNS_NUMBER() + 2, pawns1.size());
        assertEquals(pawns.get(pawns.size() - 1), pawns1.get(pawns1.size() - 1));
    }

    /**
     * Allows to test the private method placeOnePawn() by checking that the pawn placed
     * is the same that the pawn in the grid after its insertion.
     * @throws NoSuchMethodException if the method was not found in the class
     * @throws InvocationTargetException if the method cannot be applied
     * @throws IllegalAccessException if the method cannot be accessed
     */
    @Test
    public void placeOnePawn() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Pawn pawn = new Pawn(GameColor.RED, -1);
        Method method = GameBoard.class.getDeclaredMethod("placeOnePawn", int.class, int.class, Pawn.class);
        method.setAccessible(true);
        method.invoke(this.gameBoard, 8, 3, pawn);
        assertEquals(pawn, this.gameBoard.getBoard()[3][8]);
    }

    /**
     * Allows to test the createChineseSymbols() method and the private method createOneSymbol(). First
     * a single symbol is add and then they are all created.
     * @throws NoSuchMethodException if the method was not found in the class
     * @throws InvocationTargetException if the method cannot be applied
     * @throws IllegalAccessException if the method cannot be accessed
     * @throws NoSuchFieldException if the field was not found in the class
     */
    @Test
    public void createChineseSymbols() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Field field = GameBoard.class.getDeclaredField("symbols");
        field.setAccessible(true);
        field.set(this.gameBoard, new ChineseSymbol[GameBoard.getDIMENSION()][GameBoard.getDIMENSION()]);
        Method method = GameBoard.class.getDeclaredMethod("createOneSymbol", int.class, int.class);
        method.setAccessible(true);
        method.invoke(this.gameBoard, 8, 3);
        assertNotNull(this.gameBoard.getSymbols()[8][3]);
        assertNull(this.gameBoard.getSymbols()[0][5]);
        this.gameBoard.createChineseSymbols();
        assertNotNull(this.gameBoard.getSymbols()[0][5]);
    }
}