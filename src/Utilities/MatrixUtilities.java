package Utilities;

import game.Pawn;

import java.util.ArrayList;

public class MatrixUtilities {

    public static void showMatrix(Object[][] objects) {
        if (objects != null) {
            for (Object[] objects1 : objects) {
                System.out.println();
                for (Object object : objects1) {
                    System.out.print(object + "\t");
                }
            }
            System.out.println();
        }
    }

    public static void showPawnsMatrix(Pawn[][] pawns) {
        if (pawns != null) {
            String s = "ABCDEFGHIJK";
            for (int i = 0; i < pawns[0].length; i++) {
                System.out.print("\t" + s.charAt(i));
            }
            int j = 0;
            for (Pawn[] pawns1 : pawns) {
                System.out.println();
                System.out.print(j + "\t");
                for (Pawn pawn : pawns1) {
                    if (pawn != null) System.out.print(pawn.getColorString() + pawn + "\t" + "\u001B[0m");
                    else System.out.print("/ \t");
                }
                j++;
            }
            System.out.println();
        }
    }

    public static int countObjectLine(Object[][] objects, int line) {
        int number = 0;
        for (Object o : objects[line]) {
            if (o != null) number++;
        }
        return number;
    }

    public static int countObjectColumn(Object[][] objects, int column) {
        int number = 0;
        for (Object[] objects1 : objects) {
            if (objects1[column] != null) number++;
        }
        return number;
    }

    /**
     * count the number of objects on the diagonal from left down corner
     * to right up corner. The matrix has to be a square.
     *
     * @param objects the matrix of objects
     * @return the number of objects
     */
    public static int countObjectDiagAsc(Object[][] objects, int line, int column) {
        int number = 0;
        int i = 0;
        while (line - i >= 0 && column + i < objects[0].length) {
            if (objects[line - i][column + i] != null) number++;
            i++;
        }
        i = 1;
        while (column - i >= 0 && line + i < objects.length) {
            if (objects[line + i][column - i] != null) number++;
            i++;
        }
        return number;
    }

    /**
     * count the number of objects on the diagonal from left up corner
     * to right down corner. The matrix has to be a square.
     *
     * @param objects the matrix of objects
     * @return the number of objects
     */
    public static int countObjectDiagDesc(Object[][] objects, int line, int column) {
        int i = 0;
        int number = 0;
        while (line + i < objects.length && column + i < objects[0].length) {
            if (objects[line + i][column + i] != null) number++;
            i++;
        }
        i = 1;
        while (line - i >= 0 && column - i >= 0) {
            if (objects[line - i][column - i] != null) number++;
            i++;
        }
        System.out.print(number);
        return number;
    }

    public static int charToInt(char c) {
        switch (c) {
            case 'A':
            case 'a':
                return 0;
            case 'B':
            case 'b':
                return 1;
            case 'C':
            case 'c':
                return 2;
            case 'D':
            case 'd':
                return 3;
            case 'E':
            case 'e':
                return 4;
            case 'F':
            case 'f':
                return 5;
            case 'G':
            case 'g':
                return 6;
            case 'H':
            case 'h':
                return 7;
            case 'I':
            case 'i':
                return 8;
            case 'J':
            case 'j':
                return 9;
            case 'K':
            case 'k':
                return 10;
            default:
                return -1;
        }
    }

    public static boolean pointsConnected(Object[][] matrix, ArrayList<Pawn> list) {
        boolean connected = true;
        int j = 0;
        while (connected && j < list.size()) {
            boolean found = false;
            int line = -1;
            int column = 0;
            while (!found && line + 1 < matrix.length) {
                line++;
                for (column = 0; column < matrix[line].length; column++) {
                    if (matrix[line][column] == list.get(j)) {
                        found = true;
                        break;
                    }
                }
            }
            connected = hasNeighbour(matrix, column, line, list);
            j++;
        }
        return connected;
    }

    public static boolean hasNeighbour(Object[][] matrix, int column, int line, ArrayList<Pawn> list) {
        boolean left = (column - 1 >= 0);
        boolean right = (column + 1 < matrix[0].length);
        boolean up = (line - 1 >= 0);
        boolean down = (line + 1 < matrix.length);
        if (left) {
            if (list.contains(matrix[line][column - 1])) return true;
            if (up && list.contains(matrix[line - 1][column - 1])) return true;
            if (down && list.contains(matrix[line + 1][column - 1])) return true;
        }
        if (right) {
            if (list.contains(matrix[line][column + 1])) return true;
            if (up && list.contains(matrix[line - 1][column + 1])) return true;
            if (down && list.contains(matrix[line + 1][column + 1])) return true;
        }
        if (up && list.contains(matrix[line - 1][column])) return true;
        if (down && list.contains(matrix[line + 1][column])) return true;
        return false;
    }

    public static boolean meetAdverse(Pawn[][] matrix, ArrayList<Pawn> adversePawns, int lastLine, int lastColumn, int nextLine, int nextColumn) {
        boolean meet = false;
        int line = lastLine - nextLine;
        int column = lastColumn - nextColumn;
        if (line != 0 && column != 0) {
            if (line > 0 && column > 0) {
                for (int i = 1; lastLine - i > nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine - i][lastColumn - i]) && matrix[lastLine - i][lastColumn - i].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (line < 0 && column > 0) {
                for (int i = 1; lastLine + i < nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine + i][lastColumn - i]) && matrix[lastLine + i][lastColumn - i].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (line < 0) {
                for (int i = 1; lastLine + i < nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine + i][lastColumn + i]) && matrix[lastLine + i][lastColumn + i].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else {
                for (int i = 1; lastLine - i > nextLine; i++) {
                    if (adversePawns.contains(matrix[lastLine - i][lastColumn - i]) && matrix[lastLine - i][lastColumn - i].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            }
        } else {
            if (line > 0) {
                for (int i = lastLine - 1; i > nextLine; i--) {
                    if (adversePawns.contains(matrix[i][nextColumn]) && matrix[i][nextColumn].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (line < 0) {
                for (int i = lastLine + 1; i < nextLine; i++) {
                    if (adversePawns.contains(matrix[i][nextColumn]) && matrix[i][nextColumn].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else if (column > 0) {
                for (int i = lastColumn - 1; i > nextColumn; i--) {
                    if (adversePawns.contains(matrix[nextLine][i]) && matrix[nextLine][i].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            } else {
                for (int i = lastColumn + 1; i < nextColumn; i++) {
                    if (adversePawns.contains(matrix[nextLine][i]) && matrix[nextLine][i].getNumber() != -1) {
                        meet = true;
                        break;
                    }
                }
            }
        }
        return meet;
    }

}
