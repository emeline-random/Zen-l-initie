package utilities;

import game.bin.Element;
import game.bin.Pawn;

import java.util.ArrayList;

public class MatrixUtilities {

    /**
     * Allows to print a matrix of Element objects with indexing of rows and columns with numbers and letters.
     * If the Element is a Pawn the color of the string representation of the pawn is the color of the pawn object.
     *
     * @param elements the matrix of elements to print
     */
    public static void showMatrix(Element[][] elements) {
        if (elements != null) {
            String s = "ABCDEFGHIJK";
            for (int i = 0; i < elements[0].length; i++) {
                System.out.print("\t" + s.charAt(i));
            }
            int j = 0;
            for (Element[] pawns1 : elements) {
                System.out.println();
                System.out.print(j + "\t");
                for (Element pawn : pawns1) {
                    if (pawn instanceof Pawn)
                        System.out.print(((Pawn) pawn).getANSIColor() + pawn + "\t" + "\u001B[0m");
                    else System.out.print("/ \t");
                }
                j++;
            }
            System.out.println();
        }
    }

    /**
     * Allows to know if a point is in a matrix or not. It simply checks if
     * the values of the column and row are between 0 and the size of the matrix.
     *
     * @param objects the matrix where we want to know if the point exist
     * @param line    the line index of the point
     * @param column  the column index of the point
     * @return true if the point is in the matrix, false otherwise
     */
    public static boolean isOutOfMatrix(Object[][] objects, int line, int column) {
        return line < 0 || column < 0 || line >= objects.length || column >= objects[line].length;
    }

    /**
     * count the number of Pawn objects on a column, the index of the line
     * where the count is made is identified by the parameter line
     *
     * @param objects the matrix of objects
     * @param line    the index of the line
     * @return the number of Pawns
     */
    public static int countObjectLine(Object[][] objects, int line) {
        int number = 0;
        for (Object o : objects[line]) {
            if (o instanceof Pawn) number++;
        }
        return number;
    }

    /**
     * count the number of Pawn objects on a column, the index of the column
     * where the count is made is identified by the parameter column
     *
     * @param objects the matrix of objects
     * @param column  the index of the column
     * @return the number of Pawns
     */
    public static int countObjectColumn(Object[][] objects, int column) {
        int number = 0;
        for (Object[] objects1 : objects) {
            if (objects1[column] instanceof Pawn) number++;
        }
        return number;
    }

    /**
     * count the number of Pawn objects on the diagonal from left down corner
     * to right up corner. The matrix has to be a square.
     *
     * @param objects the matrix of objects
     * @param line    the line index of the point through which the diagonal runs
     * @param column  the column of the point through which the diagonal runs
     * @return the number of Pawns
     */
    public static int countObjectDiagAsc(Object[][] objects, int line, int column) {
        int number = 0;
        int i = 0;
        while (line - i >= 0 && column + i < objects[0].length) {
            if (objects[line - i][column + i] instanceof Pawn) number++;
            i++;
        }
        i = 1;
        while (column - i >= 0 && line + i < objects.length) {
            if (objects[line + i][column - i] instanceof Pawn) number++;
            i++;
        }
        return number;
    }

    /**
     * Count the number of Pawn objects on the diagonal from left up corner
     * to the right down corner. The matrix has to be a square. The diagonal
     * is passing by the point identified by the line and the column index.
     *
     * @param objects the matrix of objects
     * @param line    the line index of the point through which the diagonal runs
     * @param column  the column of the point through which the diagonal runs
     * @return the number of Pawns
     */
    public static int countObjectDiagDesc(Object[][] objects, int line, int column) {
        int i = 0;
        int number = 0;
        while (line + i < objects.length && column + i < objects[0].length) {
            if (objects[line + i][column + i] instanceof Pawn) number++;
            i++;
        }
        i = 1;
        while (line - i >= 0 && column - i >= 0) {
            if (objects[line - i][column - i] instanceof Pawn) number++;
            i++;
        }
        return number;
    }

    /**
     * Allows to know if a list of Pawn objects are connected in a matrix of Element objects.
     * Pawn are said to be connected if they are in contact with at least one other pawn.
     * I.e. if it touches a pawn sideways or diagonally in the matrix.
     *
     * @param matrix the matrix where the pawns are
     * @param list   the list of pawn
     * @return true if all pawns of the list are linked to each other, false otherwise.
     */
    public static boolean pointsConnected(Object[][] matrix, ArrayList<Pawn> list) {
        ArrayList<Pawn> toDo = new ArrayList<>();
        toDo.add(list.get(0));
        ArrayList<Pawn> done = new ArrayList<>();
        while (!toDo.isEmpty()) {
            Pawn pawn = toDo.remove(0);
            ArrayList<Pawn> toAdd = getNeighbours(matrix, pawn.getColumnIndex(), pawn.getLineIndex(), list);
            for (Pawn pawn1 : toAdd) {
                if (!done.contains(pawn1)) {
                    toDo.add(pawn1);
                }
            }
            done.add(pawn);
        }
        boolean connected = true;
        int i = 0;
        while (i < list.size() && connected) {
            if (!done.contains(list.get(i))) {
                connected = false;
            }
            i++;
        }
        return connected;
    }

    /**
     * Allows to get the neighbours of a point in a matrix. The point is identified by its position (column and line)
     * and the neighbours to consider are given in the list parameter.
     *
     * @param matrix the matrix to look in
     * @param column the column of the point
     * @param line the line of the point
     * @param list the list of potential neighbours
     * @return the list of actual neighbours (that is actually a part of the potential neighbours list or an empty list)
     */
    public static ArrayList<Pawn> getNeighbours(Object[][] matrix, int column, int line, ArrayList<Pawn> list) {
        ArrayList<Pawn> neighbours = new ArrayList<>();
        boolean left = (column - 1 >= 0);
        boolean right = (column + 1 < matrix[0].length);
        boolean up = (line - 1 >= 0);
        boolean down = (line + 1 < matrix.length);
        if (left) {
            if (list.contains(matrix[line][column - 1])) neighbours.add((Pawn) matrix[line][column - 1]);
            if (up && list.contains(matrix[line - 1][column - 1])) neighbours.add((Pawn) matrix[line - 1][column - 1]);
            if (down && list.contains(matrix[line + 1][column - 1]))
                neighbours.add((Pawn) matrix[line + 1][column - 1]);
        }
        if (right) {
            if (list.contains(matrix[line][column + 1])) neighbours.add((Pawn) matrix[line][column + 1]);
            if (up && list.contains(matrix[line - 1][column + 1])) neighbours.add((Pawn) matrix[line - 1][column + 1]);
            if (down && list.contains(matrix[line + 1][column + 1]))
                neighbours.add((Pawn) matrix[line + 1][column + 1]);
        }
        if (up && list.contains(matrix[line - 1][column])) neighbours.add((Pawn) matrix[line - 1][column]);
        if (down && list.contains(matrix[line + 1][column])) neighbours.add((Pawn) matrix[line + 1][column]);
        return neighbours;
    }

    /**
     * Allows to know if during a displacement a Pawn object will meet another Pawn. The considered matrix (where the pawns are)
     * is a matrix of Element objects. The adverse paws that is not supposed to meet the pawn are stocked in adversePawns ArrayList
     * and the coordinates of the displacement are given with the line and the column.
     * @param matrix the matrix where the pawns are
     * @param adversePawns the list of adverse pawns
     * @param lastLine the line where the pawn that is going to do a displacement is
     * @param lastColumn the column where the pawn that is going to do a displacement is
     * @param nextLine the line where the pawn will be after its displacement
     * @param nextColumn the column where the pawn will be after its displacement
     * @return true if the pawn meets an adverse pawn, false otherwise
     */
    public static boolean meetAdverse(Object[][] matrix, ArrayList<Pawn> adversePawns, int lastLine, int lastColumn, int nextLine, int nextColumn) {
        boolean meet = false;
        int line = lastLine - nextLine;
        int column = lastColumn - nextColumn;
        if (line != 0 && column != 0) {
            if (line > 0 && column > 0) {
                for (int i = 1; lastLine - i > nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine - i][lastColumn - i]) && ((Pawn) matrix[lastLine - i][lastColumn - i]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (line < 0 && column > 0) {
                for (int i = 1; lastLine + i < nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine + i][lastColumn - i]) && ((Pawn) matrix[lastLine + i][lastColumn - i]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (line < 0) {
                for (int i = 1; lastLine + i < nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine + i][lastColumn + i]) && ((Pawn) matrix[lastLine + i][lastColumn + i]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else {
                for (int i = 1; lastLine - i > nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine - i][lastColumn + i]) && ((Pawn) matrix[lastLine - i][lastColumn + i]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            }
        } else {
            if (line > 0) {
                for (int i = lastLine - 1; i > nextLine; i--) {
                    if (adversePawns.contains(matrix[i][nextColumn]) && ((Pawn) matrix[i][nextColumn]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (line < 0) {
                for (int i = lastLine + 1; i < nextLine; i++) {
                    if (adversePawns.contains(matrix[i][nextColumn]) && ((Pawn) matrix[i][nextColumn]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (column > 0) {
                for (int i = lastColumn - 1; i > nextColumn; i--) {
                    if (adversePawns.contains(matrix[nextLine][i]) && ((Pawn) matrix[nextLine][i]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else {
                for (int i = lastColumn + 1; i < nextColumn; i++) {
                    if (adversePawns.contains(matrix[nextLine][i]) && ((Pawn) matrix[nextLine][i]).getNUMBER() != -1) {
                        meet = true;
                        break;
                    }
                }
            }
        }
        return meet;
    }

}
