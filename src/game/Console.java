package Game;

import Utilities.InputUtilities;
import Utilities.Language;
import Utilities.MatrixUtilities;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Console implements GameMode {

    private Game game;
    private Level level;

    public Console(Level level) {
        if (level != null) {
            this.level = level;
        } else {
            throw new IllegalArgumentException();
        }
    }

    protected boolean getConfirmation(String message) {
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

    protected String getInput(String message, String[] objects, String errorMessage) {
        String string;
        if (message != null) {
            System.out.print(message);
        }
        Scanner scanner = new Scanner(System.in);
        string = scanner.next();
        this.checkQuit(string);
        this.checkMenu(string);
        if (objects != null) {
            while (!Arrays.toString(objects).contains(string)) {
                System.out.println(errorMessage);
                string = scanner.next();
                this.checkQuit(string);
                this.checkMenu(string);
            }
        }
        return string;
    }

    private void checkQuit(String s) {
        if (s.length() >= 1 && (s.equals("q") || s.equals("Q") || s.equals("quit") || s.equals("exit"))) {
            if (this.getConfirmation(Language.getText("quit confirmation"))) {
                this.game.saveAndCloseGame();
            }
        }
    }

    private void checkMenu(String s) {
        if (s.equals("menu") || s.equals("m")) {
            if (this.getConfirmation(Language.getText("menu confirmation"))) {
                this.showMenu();
            }
        }
    }

    @Override
    public void restartGame(Game game) {
        this.game = game;
        this.movePawn(game.getGameBoard().getBoard());
        game.play();
    }

    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public void movePawn(Element[][] pawns) {
        MatrixUtilities.showPawnsMatrix(pawns);
    }

    @Override
    public void endGame(Player winner, Player looser, Game game, boolean equality) {
        if (!equality) {
            System.out.println(Language.getText("congrats") + winner.getNAME() + ", " + Language.getText("win message"));
        } else {
            System.out.println(Language.getText("equality"));
        }
        if (this.getConfirmation(Language.getText("new game"))) {
            game.replay();
        } else {
            System.out.println(winner.getNAME() + " " + Language.getText("has") + " " + winner.getPoints() + " point(s)");
            System.out.println(looser.getNAME() + " " + Language.getText("has") + " " + looser.getPoints() + " point(s)");
            System.exit(0);
        }
    }

    private void showMenu() {
        ConsoleMenu.showMenu(this);
    }

    @Override
    public int[] play(Player player) {
        int[] coordinates = new int[3];
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.print(player.getNAME() + ", "+ Language.getText("pawn question"));
            try {
                coordinates[0] = scanner.nextInt();
            } catch (InputMismatchException e) {
                String s = scanner.next();
                this.checkQuit(s);
                this.checkMenu(s);
                char c = 'a';
                if (s.length() == 1) c = s.charAt(0);
                if (c == 'z') coordinates[0] = -1;
                else coordinates[0] = -2;
            }
        } while (coordinates[0] > Player.getPAWNS_NUMBER() || coordinates[0] < 0 && coordinates[0] != -1 || !player.pawnAlive(coordinates[0]));
        if (this.level == Level.HARD) {
            coordinates[1] = Integer.parseInt(this.getInput(Language.getText("line question"), InputUtilities.getStringTab(0, GameBoard.getDIMENSION()),
                    Language.getText("number input error")));
            coordinates[2] = InputUtilities.charToInt(this.getInput(Language.getText("column question"), InputUtilities.getPossibleChars(),
                    Language.getText("letter input error")).charAt(0));
        } else {
            System.out.println(Language.getText("assisted displacement question"));
            String s = this.getInput(null, new String[]{"e", "w", "n", "s", "ne", "nw", "se", "sw"}, Language.getText("character input error"));
            Pawn p = player.getPawns().get(coordinates[0]);
            coordinates[1] = p.getLineIndex();
            coordinates[2] = p.getColumnIndex();
            switch (s) {
                case "e":
                    coordinates[2] = p.getColumnIndex() + MatrixUtilities.countObjectLine(this.game.getGameBoard().getBoard(), p.getLineIndex());
                    break;
                case "w":
                    coordinates[2] = p.getColumnIndex() - MatrixUtilities.countObjectLine(this.game.getGameBoard().getBoard(), p.getLineIndex());
                    break;
                case "n":
                    coordinates[1] = p.getLineIndex() - MatrixUtilities.countObjectColumn(this.game.getGameBoard().getBoard(), p.getColumnIndex());
                    break;
                case "s":
                    coordinates[1] = p.getLineIndex() + MatrixUtilities.countObjectColumn(this.game.getGameBoard().getBoard(), p.getColumnIndex());
                    break;
                case "ne":
                    int i = MatrixUtilities.countObjectDiagAsc(this.game.getGameBoard().getBoard(), p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() - i;
                    coordinates[2] = p.getColumnIndex() + i;
                    break;
                case "nw":
                    i = MatrixUtilities.countObjectDiagDesc(this.game.getGameBoard().getBoard(), p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() - i;
                    coordinates[2] = p.getColumnIndex() - i;
                    break;
                case "se":
                    i = MatrixUtilities.countObjectDiagDesc(this.game.getGameBoard().getBoard(), p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() + i;
                    coordinates[2] = p.getColumnIndex() + i;
                    break;
                case "sw":
                    i = MatrixUtilities.countObjectDiagAsc(this.game.getGameBoard().getBoard(), p.getLineIndex(), p.getColumnIndex());
                    coordinates[1] = p.getLineIndex() + i;
                    coordinates[2] = p.getColumnIndex() - i;
                    break;
            }
            if (!MatrixUtilities.isInMatrix(this.game.getGameBoard().getBoard(), coordinates[1], coordinates[2])) {
                this.cannotMove();
                coordinates = this.play(player);
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
}
