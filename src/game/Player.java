package game;

import Utilities.MatrixUtilities;

import java.awt.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Player {

    private final String name;
    private int points;
    private boolean hasWon = false;
    private final Color color;
    private final static int PAWNSNUMBER = 12;
    private boolean isPlaying;
    private ArrayList<Pawn> pawns = new ArrayList<>();

    public Player(String name, Color color) {
        if (color != null && color != Color.red) {
            this.color = color;
        } else {
            this.color = Color.BLACK;
        }
        if (name != null) {
            this.name = name;
        } else {
            this.name = "Player";
        }
        generatePawns();
    }

    private void generatePawns() {
        for (int i = 0; i < PAWNSNUMBER; i++) {
            this.pawns.add(new Pawn(this.color, this, i));
        }
    }

    protected int[] play() {
        Scanner scanner = new Scanner(System.in);
        int[] coordinates = new int[3];
        do {
            System.out.print(this.name + ", what pawn do you want to move? ");
            try {
                coordinates[0] = scanner.nextInt();
            } catch (InputMismatchException e){
                char c = scanner.next().charAt(0);
                if(c == 'z') coordinates[0] = -1;
                else coordinates[0] = -2;
            }
        }while (coordinates[0] > PAWNSNUMBER || coordinates[0] < 0 && coordinates[0] != -1);
        boolean correctEntry;
        do {
            try {
                correctEntry = true;
                System.out.print("To which line ? (number) ");
                coordinates[1] = scanner.nextInt();
                System.out.print("To which column ? (letter) ");
                coordinates[2] = MatrixUtilities.charToInt(scanner.next().charAt(0));
            } catch (InputMismatchException e){
                correctEntry = false;
            }
        }while (!correctEntry || coordinates[1] > GameBoard.DIMENSION || coordinates[2] > GameBoard.DIMENSION ||
                coordinates[1] < 0 || coordinates[2] < 0);
        return coordinates;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    protected void addPoint() {
        this.points++;
    }

    public void setPlaying(boolean playing) {
        this.isPlaying = playing;
    }

    public ArrayList<Pawn> getPawns() {
        return this.pawns;
    }

    public String getName() {
        return this.name;
    }
}
