package tests;

import org.junit.Test;
import utilities.Language;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Allows to test the Language class
 */
public class LanguageTest {

    /**
     * Allows to change the language using setLanguage and to check that the general
     * language of the class has benn changed from french to english and reverse.
     */
    @Test
    public void setLanguage() {
        Language.setLanguage(Language.Languages.ENGLISH);
        assertEquals(Language.Languages.ENGLISH, Language.getLanguage());
        Language.setLanguage(Language.Languages.FRENCH);
        assertEquals(Language.Languages.FRENCH, Language.getLanguage());
    }

    /**
     * Allows to test the getText method by checking it provides the correct
     * text for two identifiers in both languages.
     */
    @Test
    public void getText() {
        assertEquals("New game", Language.getText("new"));
        assertEquals("Game rules", Language.getText("rules"));
        Language.setLanguage(Language.Languages.FRENCH);
        assertEquals("Nouvelle partie", Language.getText("new"));
        assertEquals("Règles du jeu", Language.getText("rules"));
        Language.setLanguage(Language.Languages.ENGLISH);
    }
}