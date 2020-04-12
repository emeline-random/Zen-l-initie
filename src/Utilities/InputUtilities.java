package Utilities;

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
     * Allows to get a table of number converted to String objects. The table returned
     * is made of all the integer values between begin and end (the end index being excluded).
     *
     * @param begin the first number that will be in the table
     * @param end   the last number that will be in the table + 1
     * @return the string table that represents the numbers
     */
    public static String[] getStringTab(int begin, int end) {
        String[] strings;
        if (begin <= end) {
            strings = new String[end - begin];
            for (int i = begin; i < end; i++) {
                strings[i] = Integer.toString(i);
            }
        } else {
            throw new IllegalArgumentException();
        }
        return strings;
    }

    /**
     * Allows to get all the characters that are handled by this convert class.
     * The characters go from a to k included ans are converted to String objects.
     *
     * @return the table of all handled String objects
     */
    public static String[] getPossibleChars() {
        return new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"};
    }

    /**
     * Allows to convert a character into an integer (do the opposite conversion
     * of the intToChar(int i) method that is define above). It handles both lower
     * and upper case and associate a (or A) to 0, b (or B) to 1, ... The character
     * handled are defined in the getPossibleChars() method and corresponds to the
     * characters from a to k. If the character passed is not one of these the value
     * returned is -1.
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
}
