package utilities;


import java.util.Scanner;

/**
 * Java class that allows the application to be available in different languages (french and english).
 * The texts are stocked in one file per language.
 */
public class Language {

    /**
     * The current file where the texts are taken from
     */
    private static String file = "/Languages/English.txt";
    /**
     * The current language
     */
    private static Languages language = Languages.ENGLISH;

    /**
     * The available languages
     */
    public enum Languages {
        FRENCH,
        ENGLISH
    }

    /**
     * Allows to set the language by changing the file where the texts are taken from.
     *
     * @param language the wanted language
     */
    public static void setLanguage(Languages language) {
        Language.language = language;
        switch (language) {
            case FRENCH:
                file = "/Languages/French.txt";
                break;
            case ENGLISH:
                file = "/Languages/English.txt";
                break;
        }
    }

    /**
     * Allows to get the text in the chosen language with an identifier.
     * In the file, line are made with an identifier and the text that
     * corresponds to it separated by the equals sign.
     *
     * @param identifier the identifier of the text line
     * @return the corresponding text
     */
    public static String getText(String identifier) {
        Scanner scanner = new Scanner(Language.class.getResourceAsStream(file));
        String s ="";
        String ret ="";
        while (scanner.hasNextLine() && !s.split("=")[0].trim().equals(identifier.trim())){
            s = scanner.nextLine();
            ret = s.split("=")[1];
        }
        return ret.trim();
    }

    /**
     * Allows to get the current language
     *
     * @return an element of the Languages enum
     */
    public static Languages getLanguage() {
        return Language.language;
    }
}
