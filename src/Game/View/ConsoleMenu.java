package game.view;

import game.bin.artificialPlayers.*;
import game.controller.*;
import game.bin.Level;
import game.bin.Player;
import utilities.GameColor;
import utilities.InputUtilities;
import utilities.Language;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Allows to display the menu of the application in console mode. To open the menu dialog
 * the showMenu() method must be called. After that this the menu is independent and handles
 * user's actions.
 */
public class ConsoleMenu {

    /**
     * Allows to print the global menu of the application and to ask the user what he wants to do.
     * Then do the appropriate action.
     */
    public static void showMenu() {
        System.out.println("*****************************");
        System.out.println("* " + Language.getText("welcome") + " *");
        System.out.println("*****************************\n");
        System.out.println(Language.getText("change language") + "\n" + Language.getText("quit message") + "\n"
                + Language.getText("replay message") + "\n" + Language.getText("menu message") + " \n");
        System.out.println(Language.getText("choose number") + "\n1 - " + Language.getText("new") + " \n2 - "
                + Language.getText("resume") + "\n3 - " + Language.getText("see rules"));
        String choice = InputUtilities.getInputRegex(null, "^[123]$", Language.getText("number input error"), null);
        switch (choice) {
            case "1":
                showConfigMenu();
                break;
            case "2":
                try {
                    String path = InputUtilities.getInputRegex(Language.getText("get path open"), ".*.ser$", Language.getText("ser error"), null);
                    FileInputStream stream = new FileInputStream(path);
                    ObjectInputStream file = new ObjectInputStream(stream);
                    ((Game) file.readObject()).restartGame();
                    file.close();
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println(Language.getText("save game error"));
                    showMenu();
                }
                break;
            case "3":
                showRules();
                break;
        }
    }

    /**
     * Allows to print the rules of the game. Go back to the menu when "m" or "menu" is typed.
     */
    private static void showRules() {
        System.out.println("*************" + Language.getText("rules") + "*************");
        System.out.println(Language.getText("goal").substring(5));
        System.out.println(Language.getText("normal move").substring(5));
        System.out.println(Language.getText("zen move").substring(5));
        InputUtilities.getInputRegex(Language.getText("menu message"), "^m$|^menu$", null, null);
    }

    /**
     * Allows to print the menu to configure a game with one or two players and starts this game.
     */
    private static void showConfigMenu() {
        String choice = InputUtilities.getInputRegex(Language.getText("player number"),
                "^[12]$", Language.getText("number input error"), null);
        String fp = InputUtilities.getInputRegex(Language.getText("name1"), ".+", null, null);
        GameColor fc = colorChooser(null);
        String sp = null;
        GameColor sc = null;
        ArtificialPlayer player = null;
        if (choice.equals("2")) {
            sp = InputUtilities.getInputRegex(Language.getText("name2"), ".+", null, null);
            sc = colorChooser(fc);
        } else {
            player = playerChooser();
        }
        Level level;
        if (InputUtilities.getConfirmation(Language.getText("displacement question"))) level = Level.EASY;
        else level = Level.HARD;
        String m = InputUtilities.getInputRegex(Language.getText("mode selection"),
                "^[cCgG]$", Language.getText("letter input error"), null);
        GameMode mode;
        if (m.equals("G") || m.equals("g")) mode = new Graphic(level);
        else mode = new Console(level);
        Game game;
        if (choice.equals("2")) game = new Game(mode, new Player(fp, fc), new Player(sp, sc), level);
        else game = new Game(mode, new Player(fp, fc), player, level);
        game.beginGame();
    }

    /**
     * Allows to display a menu to choose the level of the artificial player.
     * @return an ArtificialPlayer of the chosen level
     */
    private static ArtificialPlayer playerChooser() {
        System.out.println(Language.getText("adverse question"));
        System.out.println("1 - " + Language.getText("level1") + "\n2 - " + Language.getText("level2"));
        ArtificialPlayer player = null;
        String choice = InputUtilities.getInputRegex(null, "^[12]$", Language.getText("number input error"), null);
        switch (choice) {
            case "1":
                player = new FirstLevel();
                break;
            case "2":
                player = new SecondLevel();
        }
        return player;
    }

    /**
     * Allows to display a menu to choose a color. If a color is already chosen
     * it is removed from the list of available colors.
     * @param alreadyChosen the already chosen color, should be null if the first player is choosing its color
     * @return the chosen GameColor object
     */
    private static GameColor colorChooser(GameColor alreadyChosen) {
        System.out.println(Language.getText("color question"));
        ArrayList<GameColor> colors = new ArrayList<>(Arrays.asList(GameColor.getColors()));
        StringBuilder choice = new StringBuilder("^0$");
        if (alreadyChosen != null) {
            colors.remove(alreadyChosen);
        }
        for (int i = 0; i < colors.size(); i++) {
            System.out.println(i + " - " + colors.get(i));
            choice.append("|^").append(i).append("$");
        }
        String chosen = InputUtilities.getInputRegex(null, new String(choice), Language.getText("number input error"), null);
        return colors.get(Integer.parseInt(chosen));
    }
}
