package game.controller;

import game.model.Element;
import game.model.Level;
import game.model.Pawn;
import game.model.Player;
import game.view.GraphicalInterface;
import utilities.InputUtilities;
import utilities.MatrixUtilities;

import javax.swing.*;
import java.awt.event.*;

/**
 * Java class that allows to listen to both game board and input panel. To do that
 * it defines methods that returns ActionListener or MouseAdapter depending on what
 * is needed to listen user's actions.
 */
public class BoardAndInputListeners {

    /**
     * the interface listened
     */
    private final GraphicalInterface graphicalInterface;

    /**
     * Constructor of the class that allows to initialize the interface that is listened.
     *
     * @param graphicalInterface the interface that will be listened
     */
    public BoardAndInputListeners(GraphicalInterface graphicalInterface) {
        if (graphicalInterface != null) {
            this.graphicalInterface = graphicalInterface;
        } else throw new IllegalArgumentException();
    }

    /**
     * Allows to create a mouse listener for the board, when the current player clicks on one
     * of its pawn, the number of it is directly print in the pawn JTextField. In HARD level if
     * the player clicks on a square, its line and its column ore printed on the corresponding
     * JTextFields.
     *
     * @param board  the board containing all the Element objects (that has line and column indexes
     * @param level  the level of the game
     * @param line   the JTextField containing the line index in HARD mode
     * @param column the JTextField containing the column index in HARD mode
     * @return MouseAdapter the adapter made to handle actions on the board
     */
    public MouseAdapter boardListener(Level level, Element[][] board, JTextField line, JTextField column) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (graphicalInterface.getPlayer() != null) {
                    boolean found = false;
                    int i = 0;
                    while (!found && i < graphicalInterface.getPlayer().getPawns().size()) {
                        if (graphicalInterface.getPlayer().getPawns().get(i).getRectangle().contains(e.getX(), e.getY())) {
                            graphicalInterface.getPawn().setText(graphicalInterface.getPlayer().getPawns().get(i).toString());
                            found = true;
                        }
                        i++;
                    }
                    if (level == Level.HARD && !found) {
                        i = 0;
                        int j;
                        while (!found && i < board.length) {
                            j = 0;
                            while (!found && j < board[i].length) {
                                if (board[i][j].getRectangle().contains(e.getX(), e.getY())) {
                                    line.setText(Integer.toString(board[i][j].getLineIndex()));
                                    column.setText(Character.toString(InputUtilities.intToChar(board[i][j].getColumnIndex())));
                                    found = true;
                                }
                                j++;
                            }
                            i++;
                        }

                    }
                }
            }

        };
    }

    /**
     * Allows ot create an action listener that is listening to user's clicks on arrows, these arrows
     * are typically used to move the selected pawn on different directions. (used only in easy mode)
     * @param board the matrix of elements representing the current game
     * @return the appropriate action listener.
     */
    public ActionListener easyModeListener(Element[][] board) {
        return e -> {
            graphicalInterface.getSelectedPawn();
            if (graphicalInterface.getCoordinates()[0] < Player.getPAWNS_NUMBER() && graphicalInterface.getCoordinates()[0] >= 0 || graphicalInterface.getCoordinates()[0] == -1) {
                Pawn p = graphicalInterface.getPlayer().getPawn(graphicalInterface.getCoordinates()[0]);
                graphicalInterface.getCoordinates()[1] = p.getLineIndex();
                graphicalInterface.getCoordinates()[2] = p.getColumnIndex();
                switch (((JButton) e.getSource()).getName()) {
                    case "e":
                        graphicalInterface.getCoordinates()[2] = p.getColumnIndex() + MatrixUtilities.countObjectLine(board, p.getLineIndex());
                        break;
                    case "w":
                        graphicalInterface.getCoordinates()[2] = p.getColumnIndex() - MatrixUtilities.countObjectLine(board, p.getLineIndex());
                        break;
                    case "n":
                        graphicalInterface.getCoordinates()[1] = p.getLineIndex() - MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                        break;
                    case "s":
                        graphicalInterface.getCoordinates()[1] = p.getLineIndex() + MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                        break;
                    case "ne":
                        int i = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                        graphicalInterface.getCoordinates()[1] = p.getLineIndex() - i;
                        graphicalInterface.getCoordinates()[2] = p.getColumnIndex() + i;
                        break;
                    case "nw":
                        i = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                        graphicalInterface.getCoordinates()[1] = p.getLineIndex() - i;
                        graphicalInterface.getCoordinates()[2] = p.getColumnIndex() - i;
                        break;
                    case "se":
                        i = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                        graphicalInterface.getCoordinates()[1] = p.getLineIndex() + i;
                        graphicalInterface.getCoordinates()[2] = p.getColumnIndex() + i;
                        break;
                    case "sw":
                        i = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                        graphicalInterface.getCoordinates()[1] = p.getLineIndex() + i;
                        graphicalInterface.getCoordinates()[2] = p.getColumnIndex() - i;
                        break;
                }
            }
            graphicalInterface.sendCoordinates(Level.EASY, null, null);
        };
    }

    /**
     * Allows to create an action listener that listens to the valid button. The valid button
     * is used to valid a displacement, this controller checks if the inputs are plausible and if they
     * are asks the view to send the coordinates to ot adapter (that will next convert the data in a
     * way that the main controller will be able to understand them)
     * @param line the textfield containing the line index
     * @param column the text field containing the column index
     * @return the appropriate action listener.
     */
    public ActionListener validButtonListener(JTextField line, JTextField column) {
        return e -> {
            this.graphicalInterface.getSelectedPawn();
            try {
                this.graphicalInterface.getCoordinates()[1] = Integer.parseInt(line.getText());
                if (column.getText().length() == 1)
                    this.graphicalInterface.getCoordinates()[2] = InputUtilities.charToInt(column.getText().charAt(0));
                else this.graphicalInterface.getCoordinates()[2] = -1;
            } catch (Exception ex) {
                this.graphicalInterface.getCoordinates()[1] = -1;
                this.graphicalInterface.getCoordinates()[2] = -1;
            }
            this.graphicalInterface.sendCoordinates(Level.HARD, line, column);
        };
    }

    /**
     * Allows to get a listener that prevents from quitting the window before saving the current game
     * @param game the game that is not saved
     * @return the window listener calling the quit() method of the game
     */
    public WindowListener windowListener(Game game){
        return new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.quit();
                super.windowClosing(e);
            }
        };
    }
}

