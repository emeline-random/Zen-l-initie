package Game.View;

import Game.bin.ArtificialPlayers.*;
import Game.Controllers.*;
import Game.bin.Level;
import Game.bin.Player;
import Utilities.GameColor;
import Utilities.Language;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ConsoleMenu {

    private static Console console;

    public static void showMenu(Console console) {
        ConsoleMenu.console = console;
        showGlobalMenu();
    }

    private static void showGlobalMenu() {
        System.out.println("*****************************");
        System.out.println("* " + Language.getText("welcome") + " *");
        System.out.println("*****************************");
        System.out.println();
        System.out.println(Language.getText("change language"));
        System.out.println(Language.getText("quit message"));
        System.out.println(Language.getText("replay message"));
        System.out.println(Language.getText("menu message") + " \n");
        System.out.println(Language.getText("choose number") + "\n1 - " + Language.getText("resume") +" \n2 - "
                + Language.getText("new") + "\n3 - " + Language.getText("see rules"));
        String choice = ConsoleMenu.console.getInputRegex(null, "^[123]$", Language.getText("number input error"));
        switch (choice) {
            case "1":
                try {
                    FileInputStream stream = new FileInputStream("game.ser");
                    ObjectInputStream file = new ObjectInputStream(stream);
                    ((Game) file.readObject()).restartGame();
                    file.close();
                } catch (ClassNotFoundException | IOException ex) {
                    System.out.println(Language.getText("save game error"));
                    showMenu(console);
                }
                break;
            case "2":
                showConfigMenu();
                break;
            case "3":
                showRules();
                break;
        }
    }

    private static void showRules() {
        System.out.println("*************" + Language.getText("rules") + "*************");
        System.out.println(Language.getText("goal").substring(5));
        System.out.println(Language.getText("normal move").substring(5));
        System.out.println(Language.getText("zen move").substring(5));
        console.getInputRegex(Language.getText("menu message"), "^m$|^menu$", null);
    }

    private static void showConfigMenu() {
        String choice = ConsoleMenu.console.getInputRegex(Language.getText("player number"),
                "^[12]$", Language.getText("number input error"));
        String fp = ConsoleMenu.console.getInputRegex(Language.getText("name1"), ".+", null);
        GameColor fc = colorChooser(null);
        String sp = null;
        GameColor sc = null;
        ArtificialPlayer player = null;
        if (choice.equals("2")) {
            sp = ConsoleMenu.console.getInputRegex(Language.getText("name2"), ".+", null);
            sc = colorChooser(fc);
        } else {
            player = playerChooser();
        }
        Level level;
        if (console.getConfirmation(Language.getText("displacement question"))) level = Level.EASY;
        else level = Level.HARD;
        String m = ConsoleMenu.console.getInputRegex(Language.getText("mode selection"),
                "^[cCgG]$", Language.getText("letter input error"));
        GameMode mode;
        if (m.equals("G") || m.equals("g")) mode = new Graphic(level);
        else mode = new Console(level);
        Game game;
        if (choice.equals("2")) game = new Game(mode, new Player(fp, fc), new Player(sp, sc), level);
        else game = new Game(mode, new Player(fp, fc), player, level);
        game.beginGame();
    }

    private static ArtificialPlayer playerChooser() {
        System.out.println(Language.getText("adverse question"));
        System.out.println("1 - "+ Language.getText("level1") + "\n2 - " + Language.getText("level2"));
        ArtificialPlayer player;
        String choice = ConsoleMenu.console.getInputRegex(null, "^[12]$", Language.getText("number input error"));
        switch (choice) {
            case "1":
                player = new FirstLevel();
                break;
            case "2":
                player = new SecondLevel();
                break;
            default:
                player = null;
                break;
        }
        return player;
    }

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
        String chosen = ConsoleMenu.console.getInputRegex(null, new String(choice), Language.getText("number input error"));
        return colors.get(Integer.parseInt(chosen));
    }
}
