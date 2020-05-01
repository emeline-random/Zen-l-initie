package utilities;

import game.controller.Game;
import game.view.ConsoleMenu;

import java.util.Scanner;

/**
 * Utility class that allows to do some conversions between integer and characters.
 */
public class InputUtilities {

    /**
     * Allows to convert an integer value to a character. The number 0 corresponds to the letter a,
     * the number 1 to the letter b, ... The maximum handled number is 10. If the number is not
     * between 0 and 10, this method returns the character space.
     *
     * @param i the integer to convert
     * @return the char associated with the value
     */
    public static char intToChar(int i) {
        switch (i) {
            case 0:
                return 'A';
            case 1:
                return 'B';
            case 2:
                return 'C';
            case 3:
                return 'D';
            case 4:
                return 'E';
            case 5:
                return 'F';
            case 6:
                return 'G';
            case 7:
                return 'H';
            case 8:
                return 'I';
            case 9:
                return 'J';
            case 10:
                return 'K';
            default:
                return ' ';
        }
    }

    /**
     * Allows to convert a character into an integer (do the opposite conversion
     * of the intToChar(int i) method that is define above). It handles both lower
     * and upper case and associate a (or A) to 0, b (or B) to 1, ... The character
     * handled correspond to the characters from a to k. If the character passed is
     * not one of these the value returned is -1.
     *
     * @param c the char to convert
     * @return the integer associated with the char
     */
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

    /**
     * Allows to get a confirmation to a yes/no question from the user.
     *
     * @param message the question to display
     * @return true if the user confirms (responds yes), false otherwise.
     */
    public static boolean getConfirmation(String message) {
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

    /**
     * Allows to get an input from the user. This input has to match the given regular expression and
     * an error message will be display until the user give a correct input.
     *
     * @param message      the first message to display
     * @param regex        the regular expression that the input needs to match
     * @param errorMessage the error message printed if the input is not correct
     * @param game         the game that is used to check the input (for saving and restarting game), may be null
     * @return the input value matching the regular expression
     */
    public static String getInputRegex(String message, String regex, String errorMessage, Game game) {
        String s;
        if (message != null) {
            System.out.println(message);
        }
        boolean error = false;
        Scanner scanner = new Scanner(System.in);
        do {
            if (error && errorMessage != null) System.out.println(errorMessage);
            s = scanner.nextLine();
            checkInput(s, game);
            error = true;
        } while (!s.matches(regex));
        return s;
    }

    /**
     * Allows to check an input, if a precise function is recognise such as quitting the
     * game (input "q", "quit", "exit" or "Q"), this action is performed.
     *
     * @param s    the String to analyse
     * @param game the game to save or restart (may be null)
     */
    private static void checkInput(String s, Game game) {
        switch (s.trim()) {
            case "fr":
                Language.setLanguage(Language.Languages.FRENCH);
                System.out.println("langue changée");
                break;
            case "en":
                Language.setLanguage(Language.Languages.ENGLISH);
                System.out.println("language changed");
                break;
            case "q":
            case "Q":
            case "quit":
            case "exit":
                if (getConfirmation(Language.getText("quit confirmation"))) System.exit(0);
                break;
            case "menu":
            case "m":
                if (getConfirmation(Language.getText("menu confirmation"))) ConsoleMenu.showMenu();
                break;
            case "r":
            case "replay":
                try {
                    game.replay();
                } catch (Exception e) {
                    System.out.println(Language.getText("replay error"));
                }
                break;
            case "save":
                try {
                    game.saveGame();
                    System.out.println(Language.getText("game saved"));
                } catch (Exception e) {
                    System.out.println(Language.getText("save error"));
                }
        }
    }
}
