package Game.View;

import Game.Controllers.Game;
import Game.bin.*;
import Utilities.InputUtilities;
import Utilities.Language;
import Utilities.MatrixUtilities;

import java.util.Scanner;

public class Console implements GameMode {

    private Game game;
    private final Level level;

    public Console(Level level) {
        if (level != null) {
            this.level = level;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void restartGame(Game game, Element[][] board) {
        this.game = game;
        this.movePawn(null, null, board);
        game.play();
    }

    @Override
    public void endGame(Player winner, Player looser, boolean equality) {
        if (!equality) {
            System.out.println(Language.getText("congrats") + winner.getNAME() + ", " + Language.getText("win message"));
        } else {
            System.out.println(Language.getText("equality"));
        }
        if (this.getConfirmation(Language.getText("new game"))) {
            this.game.replay();
        } else {
            System.out.println(winner.getNAME() + " " + Language.getText("has") + " " + winner.getPoints() + " point(s)");
            System.out.println(looser.getNAME() + " " + Language.getText("has") + " " + looser.getPoints() + " point(s)");
            ConsoleMenu.showMenu(this);
        }
    }

    @Override
    public void movePawn(Player player, int[] move, Element[][] board) {
        MatrixUtilities.showMatrix(board);
    }

    @Override
    public int[] play(Player player, Element[][] board) {
        int[] coordinates = new int[3];

        StringBuilder choices = new StringBuilder("^" + player.getPawns().get(0).getNumber() + "$");
        for (Pawn p : player.getPawns()) {
            if (p.getNumber() != -1) choices.append("|^").append(p.getNumber()).append("$");
            else choices.append("|^z$");
        }
        String input = this.getInputRegex(player.getNAME() + ", " + Language.getText("pawn question"), new String(choices),
                Language.getText("pawn error"));
        if (input.equals("z")) input = "-1";
        coordinates[0] = Integer.parseInt(input);

        if (this.level == Level.HARD) {
            coordinates[1] = Integer.parseInt(this.getInputRegex(Language.getText("line question"), "^[0-9]|10$",
                    Language.getText("number input error")));
            coordinates[2] = InputUtilities.charToInt(this.getInputRegex(Language.getText("column question"), "^[a-k]|[A-K]$",
                    Language.getText("letter input error")).charAt(0));
        } else {
            System.out.println(Language.getText("assisted displacement question"));
            String s = this.getInputRegex(null, "^[nsew]$|^[ns][ew]$", Language.getText("character input error"));
            Pawn p = player.getPawns().get(coordinates[0]);
            coordinates[1] = p.getLineIndex();
            coordinates[2] = p.getColumnIndex();
            switch (s) {
                case "e":
                    coordinates[2] = p.getColumnIndex() + MatrixUtilities.countObjectLine(board, p.getLineIndex());
                    break;
                case "w":
                    coordinates[2] = p.getColumnIndex() - MatrixUtilities.countObjectLine(board, p.getLineIndex());
                    break;
                case "n":
                    coordinates[1] = p.getLineIndex() - MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                    break;
                case "s":
                    coordinates[1] = p.getLineIndex() + MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                    break;
                case "ne":
                    int i = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() - i;
                    coordinates[2] = p.getColumnIndex() + i;
                    break;
                case "nw":
                    i = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() - i;
                    coordinates[2] = p.getColumnIndex() - i;
                    break;
                case "se":
                    i = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() + i;
                    coordinates[2] = p.getColumnIndex() + i;
                    break;
                case "sw":
                    i = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() + i;
                    coordinates[2] = p.getColumnIndex() - i;
                    break;
            }
            if (MatrixUtilities.isOutOfMatrix(board, coordinates[1], coordinates[2])) {
                this.cannotMove();
                coordinates = this.play(player, board);
            }
        }
        return coordinates;
    }

    @Override
    public void cannotMove() {
        System.out.println(Language.getText("pawn displacement error"));
    }

    @Override
    public void zenAlreadyPlaced() {
        System.out.print(Language.getText("zen displacement error"));
    }

    @Override
    public String saveAs() {
        return this.getInputRegex(Language.getText("get path"), "^.+\\.ser$", Language.getText("ser error"));
    }

    @Override
    public void saveAsFailure() {
        System.out.println(Language.getText("save error"));
    }

    public boolean getConfirmation(String message) {
        System.out.println(message);
        Scanner scanner = new Scanner(System.in);
        String confirmation = scanner.next();
        while (!confirmation.equals("y") && !confirmation.equals("yes") && !confirmation.equals("n") && !confirmation.equals("no")
                && !confirmation.equals("Y") && !confirmation.equals("N")) {
            System.out.println(Language.getText("yn input error"));
            confirmation = scanner.next();
        }
        return confirmation.equals("y") || confirmation.equals("Y") || confirmation.equals("yes");
    }

    public String getInputRegex(String message, String regex, String errorMessage) {
        String s;
        if (message != null) {
            System.out.println(message);
        }
        boolean error = false;
        Scanner scanner = new Scanner(System.in);
        do {
            if (error && errorMessage != null) System.out.println(errorMessage);
            s = scanner.nextLine();
            this.checkQuit(s);
            this.checkMenu(s);
            this.checkLanguage(s);
            this.checkRestart(s);
            this.checkSave(s);
            error = true;
        } while (!s.matches(regex));
        return s;
    }

    private void checkLanguage(String s) {
        if (s.equals("fr")) {
            Language.setLanguage(Language.Languages.FRENCH);
            System.out.println("langue changée");
        } else if (s.equals("en")) {
            Language.setLanguage(Language.Languages.ENGLISH);
            System.out.println("language changed");
        }
    }

    private void checkQuit(String s) {
        if (s.length() >= 1 && (s.equals("q") || s.equals("Q") || s.equals("quit") || s.equals("exit"))) {
            if (this.getConfirmation(Language.getText("quit confirmation"))) {
                System.exit(0);
            }
        }
    }

    private void checkMenu(String s) {
        if (s.equals("menu") || s.equals("m")) {
            if (this.getConfirmation(Language.getText("menu confirmation"))) {
                ConsoleMenu.showMenu(this);
            }
        }
    }

    private void checkRestart(String s) {
        if (s.equals("r") || s.equals("replay")) {
            try {
                this.game.replay();
            } catch (Exception e) {
                System.out.println(Language.getText("replay error"));
            }
        }
    }

    private void checkSave(String s) {
        if (s.equals("save")) {
            try {
                this.game.saveGame();
                System.out.println(Language.getText("game saved"));
            } catch (Exception e) {
                System.out.println(Language.getText("replay error"));
            }
        }
    }

}
