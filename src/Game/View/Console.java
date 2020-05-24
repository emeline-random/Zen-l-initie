package game.view;

import game.controller.Game;
import game.model.Element;
import game.model.Level;
import game.model.Pawn;
import game.model.Player;
import utilities.InputUtilities;
import utilities.Language;
import utilities.MatrixUtilities;

/**
 * Allows to create a view to play in console. A game must have a view (that implements GameMode) to be
 * displayed and to work correctly. It allows to interact with the users.
 */
public class Console implements GameMode {

    /**
     * The game to be displayed
     */
    private Game game;
    /**
     * The level of the game
     */
    private final Level level;

    /**
     * Allows to create a console view with a precised level.
     * @throws IllegalArgumentException if the level is null
     * @param level the level that the game will be played in
     */
    public Console(Level level) {
        if (level != null) {
            this.level = level;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Allows to set the current game
     * @param game the displayed game
     */
    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Allows to restart a saved game, it performs the movePawn() method to
     * print the board and then calls game.play().
     * @param game the current game
     * @param board the board to display
     */
    @Override
    public void restartGame(Game game, Element[][] board) {
        this.game = game;
        this.movePawn(null, null, board);
        game.play();
    }

    /**
     * Allows to print a message depending on who won and asks the players if they want to
     * replay this game. If the answer is true the replay() method of the game is called, otherwise
     * the console menu is displayed.
     * @param winner the winner of the game
     * @param looser the looser of the game
     * @param equality true if there's an equality, else otherwise
     */
    @Override
    public void endGame(Player winner, Player looser, boolean equality) {
        if (equality) {
            System.out.println(Language.getText("equality"));
        } else {
            System.out.println(Language.getText("congrats") + winner.getNAME() + ", " + Language.getText("win message"));
        }
        if (InputUtilities.getConfirmation(Language.getText("new game"))) {
            this.game.replay();
        } else {
            System.out.println(winner.getNAME() + " " + Language.getText("has") + " " + winner.getPoints() + " point(s)");
            System.out.println(looser.getNAME() + " " + Language.getText("has") + " " + looser.getPoints() + " point(s)");
            ConsoleMenu.showMenu();
        }
    }

    /**
     * Allows to actualize the view of the board by printing it.
     * @param player the player who just played
     * @param move the move done in format [pawn, line, column]
     * @param board the board that will be display
     */
    @Override
    public void movePawn(Player player, int[] move, Element[][] board) {
        MatrixUtilities.showMatrix(board);
    }

    /**
     * Allows to get the coordinates of a move. First the number of the pawn to move is asked to the user, then
     * when it is correct if the mode is easy the user has to indicate a direction, else he has to type the line
     * and the column index.
     * @param player the player that should play
     * @param board the board of Element objects on which the game is taking place
     * @return the coordinates of the move in the format [pawn, line, column].
     */
    @Override
    public int[] play(Player player, Element[][] board) {
        int[] coordinates = new int[3];

        StringBuilder choices = new StringBuilder("^" + player.getPawns().get(0).getNUMBER() + "$");
        for (Pawn p : player.getPawns()) {
            if (p.getNUMBER() != -1) choices.append("|^").append(p.getNUMBER()).append("$");
            else choices.append("|^z$");
        }
        String input = InputUtilities.getInputRegex(player.getNAME() + ", " + Language.getText("pawn question"), new String(choices),
                Language.getText("pawn error"), this.game);
        if (input.equals("z")) input = "-1";
        coordinates[0] = Integer.parseInt(input);

        if (this.level == Level.HARD) {
            coordinates[1] = Integer.parseInt(InputUtilities.getInputRegex(Language.getText("line question"), "^[0-9]|10$",
                    Language.getText("number input error"), this.game));
            coordinates[2] = InputUtilities.charToInt(InputUtilities.getInputRegex(Language.getText("column question"), "^[a-k]|[A-K]$",
                    Language.getText("letter input error"), this.game).charAt(0));
        } else {
            System.out.println(Language.getText("assisted displacement question"));
            String s = InputUtilities.getInputRegex(null, "^[nsew]$|^[ns][ew]$", Language.getText("character input error"), this.game);
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
        }
        return coordinates;
    }

    /**
     * Prints a message that says that the move is incorrect.
     */
    @Override
    public void cannotMove() {
        System.out.println(Language.getText("pawn displacement error"));
    }

    /**
     * Prints a message that says that the Zen was already at this position.
     */
    @Override
    public void zenAlreadyPlaced() {
        System.out.print(Language.getText("zen displacement error"));
    }

    /**
     * Asks the user a path ending with .ser
     * @return the path
     */
    @Override
    public String saveAs() {
        return InputUtilities.getInputRegex(Language.getText("get path"), "^.+\\.ser$", Language.getText("ser error"), this.game);
    }

    /**
     * Prints a message saying that the save of the game has not been performed due to a problem with the file/path.
     */
    @Override
    public void saveAsFailure() {
        System.out.println(Language.getText("save error"));
    }

}
