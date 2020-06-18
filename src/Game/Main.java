package game;

import game.view.ConsoleMenu;
import game.view.GraphicMenu;
import utilities.InputUtilities;

import java.awt.*;
import java.io.Console;
import java.io.IOException;

/**
 * Allows to start the application, the application is supposed to be started by clicking on it or by executing
 * the jar in a console. Using sources to start the application will fail because the application is made to be
 * started from the jar. To start the application from the sources, you simply need to comment all lines of the
 * main method except the two lines 24 and 25.
 */
public class Main {
    public static void main(String[] args) throws IOException {
//        Console console = System.console();
//        if (console == null && !GraphicsEnvironment.isHeadless()) {
//            String filename = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
//            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""});
//        } else {
            if (InputUtilities.getConfirmation("Do you want to go in graphic mode ? (y/n)")) new GraphicMenu().showMenu();
            else ConsoleMenu.showMenu();
//        }
    }
}
