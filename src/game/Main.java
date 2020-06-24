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
 * main method except the lines from line 20 to the end.
 * @author Breit Hoarau Emeline
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Console console = System.console(); //to be commented to execute from sources
        if (console == null && !GraphicsEnvironment.isHeadless()) { //to be commented to execute from sources
            String filename = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6); //to be commented to execute from sources
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", "cmd", "/k", "java -jar \"" + filename + "\""}); //to be commented to execute from sources
        } else { //to be commented to execute from sources
            System.out.println(
                    "::::::::: :::::::::: ::::    :::      :::       ::: ::::::::::: ::::    ::: ::::::::::: ::::::::::: ::::::::::: :::::::::: \n" +
                            "     :+:  :+:        :+:+:   :+:      :+:       :+      :+:     :+:+:   :+:     :+:         :+:         :+:     :+:        \n" +
                            "    +:+   +:+        :+:+:+  +:+      +:+               +:+     :+:+:+  +:+     +:+         +:+         +:+     +:+        \n" +
                            "   +#+    +#++:++#   +#+ +:+ +#+      +#+               +#+     +#+ +:+ +#+     +#+         +#+         +#+     +#++:++#   \n" +
                            "  +#+     +#+        +#+  +#+#+#      +#+               +#+     +#+  +#+#+#     +#+         +#+         +#+     +#+        \n" +
                            " #+#      #+#        #+#   #+#+#      #+#               #+#     #+#   #+#+#     #+#         #+#         #+#     #+#        \n" +
                            "######### ########## ###    ####      ##########    ########### ###    #### ###########     ###     ########### ########## ");
            if (InputUtilities.getConfirmation("Welcome in Zen l'Initié ! Do you want to go in graphic mode ? (y/n)"))
                new GraphicMenu().showMenu();
            else ConsoleMenu.showMenu();
        } //to be commented to execute from sources
    }
}
