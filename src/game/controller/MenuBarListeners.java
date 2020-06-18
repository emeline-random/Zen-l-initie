package game.controller;

import game.model.ChineseSymbol;
import game.model.Element;
import game.model.Level;
import game.view.*;
import utilities.Language;
import utilities.Sound;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Java class that provides listeners to all of the menu items on the menu bar
 * on the GUI.
 */
public class MenuBarListeners {

    /**
     * the interface that will be listened
     */
    private final GraphicalInterface graphicalInterface;
    private final Game game;
    private final Element[][] board;
    private final ChineseSymbol[][] symbols;
    private final Level level;

    /**
     * Constructor of the class that allows to initialize the interface
     * that this the listeners made on this class will be listening
     *
     * @param graphicalInterface the interface containing the items to listen
     * @param game               the current game
     * @param board              the board of the game
     * @param symbols            the symbols on the board
     */
    public MenuBarListeners(GraphicalInterface graphicalInterface, Game game, Element[][] board, ChineseSymbol[][] symbols, Level level) {
        this.graphicalInterface = graphicalInterface;
        this.game = game;
        this.board = board;
        this.symbols = symbols;
        this.level = level;
    }

    /**
     * Listener that is added to most buttons and that only allows to perform a sound of a clicked button.
     *
     * @param buttons the buttons to add the listener to
     */
    public static void buttonClickedListener(JButton... buttons) {
        for (JButton button : buttons) {
            button.addActionListener(e -> Sound.play(Sound.Sounds.BUTTON_PRESSED));
        }
    }

    /**
     * Allows to create a listener for the game menu, it handles clicks that are made to save a game and to save it
     * to a special place.
     *
     * @param save   the save item
     * @param saveAs the save as item
     * @return the appropriate action listener
     */
    public ActionListener saveListener(JButton save, JButton saveAs) {
        MenuBarListeners.buttonClickedListener(save, saveAs);
        return e -> {
            if (e.getSource() == save) {
                this.game.saveGame();
            } else if (e.getSource() == saveAs) {
                this.game.saveGameAs();
            }
        };
    }

    /**
     * Allows to create a listener for the settings menu, it handles clicks that are made to change the scheme,
     * the language and to turn on/off the sound.
     *
     * @param language the language button
     * @param scheme   the scheme button
     * @param sound    the sound button
     * @param frame    the popup frame containing the buttons
     * @return the appropriate action listener
     */
    public ActionListener settingsListener(JButton language, JButton scheme, JButton sound, PopupFrame frame) {
        MenuBarListeners.buttonClickedListener(language, scheme, sound);
        return e -> {
            if (e.getSource() == language) {
                frame.getContentPane().removeAll();
                frame.add(this.graphicalInterface.getLanguagePanel(frame));
                frame.pack();
            } else if (e.getSource() == scheme) {
                frame.getContentPane().removeAll();
                frame.add(this.graphicalInterface.getSchemePanel(frame));
                frame.pack();
            } else if (e.getSource() == sound) {
                Sound.setOn(!Sound.isOn());
                ((IconButton) sound).changeIcon(Sound.getImage(false));
                if (Sound.isOn()) Sound.play(Sound.Sounds.BUTTON_PRESSED);
            }
        };
    }

    /**
     * Allows to create a listener for the pause menu, it handles clicks that are made to go in console mode, to
     * go to the menu, to restart the current game or to exit the application
     *
     * @param console the console button
     * @param home the home button
     * @param replay the replay button
     * @param exit the exit button
     * @return the appropriate listener
     */
    public ActionListener pauseListener(JButton console, JButton home, JButton replay, JButton exit) {
        MenuBarListeners.buttonClickedListener(console, home, replay, exit);
        return e -> {
            if (e.getSource() == console) {
                this.graphicalInterface.setVisible(false);
                this.graphicalInterface.dispose();
                this.graphicalInterface.setConsole(true);
                synchronized (this.graphicalInterface.getCoordinates()) {
                    this.graphicalInterface.getCoordinates().notify();
                }
            } else if (e.getSource() == home) {
                this.backToMenu();
            } else if (e.getSource() == replay) {
                this.graphicalInterface.setVisible(false);
                this.graphicalInterface.setRestartGame(true);
                synchronized (this.graphicalInterface.getCoordinates()) {
                    this.graphicalInterface.getCoordinates().notify();
                }
            } else if (e.getSource() == exit) {
                if (JOptionPane.showConfirmDialog(null, Language.getText("menu confirmation"),
                        "Menu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        };
    }

    /**
     * Listener that simply allows to go back to the menu after a confirmation.
     * @return the listener that allows to go back to the menu
     */
    public ActionListener backToMenuListener() {
        return e -> this.backToMenu();
    }

    /**
     * Allows to ask a confirmation to the user and to eventually go back to the menu after closing the interface shown.
     */
    private void backToMenu() {
        int menu = JOptionPane.showConfirmDialog(null, Language.getText("menu confirmation"),
                "Menu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (menu == JOptionPane.YES_OPTION) {
            this.graphicalInterface.setVisible(false);
            this.graphicalInterface.dispose();
            this.graphicalInterface.setBackToMenu(true);
            synchronized (this.graphicalInterface.getCoordinates()) {
                this.graphicalInterface.getCoordinates().notify();
            }
        }
    }

    /**
     * Allows to get a listener that can change the language of the application
     * @param fr the french button
     * @param en the english button
     * @return the listener that changes the language of the application
     */
    public ActionListener languageListener(JButton fr, JButton en) {
        MenuBarListeners.buttonClickedListener(fr, en);
        return e -> {
            if (e.getSource() == fr) Language.setLanguage(Language.Languages.FRENCH);
            else if (e.getSource() == en) Language.setLanguage(Language.Languages.ENGLISH);
            this.graphicalInterface.repaintFrame(this.game, this.board, this.level, this.symbols);
        };
    }

    /**
     * Allows to create a listener that handles actions on the help menu, it allows to
     * call the methods to show the rules frame and the help frame depending on the button clicked.
     *
     * @param rules the rules button
     * @param help  the help button
     * @return the appropriate listener
     */
    public ActionListener helpListener(JButton rules, JButton help) {
        MenuBarListeners.buttonClickedListener(rules, help);
        return e -> {
            if (e.getSource() == rules) {
                GraphicMenu.showRules(this.graphicalInterface);
            } else if (e.getSource() == help) {
                this.graphicalInterface.showHelpFrame();
            }
        };
    }

    /**
     * Allows to get a listener that can change the scheme of the application to the precised scheme
     * @param scheme the scheme to pass the application to
     * @param button the button of the scheme
     * @return the listener that changes the scheme of the application to the given scheme
     */
    public ActionListener schemesListener(Scheme.Schemes scheme, JButton button) {
        MenuBarListeners.buttonClickedListener(button);
        return e -> {
            Scheme.switchScheme(scheme);
            this.graphicalInterface.repaintFrame(this.game, this.board, this.level, this.symbols);
        };
    }

    /**
     * Allows to create and to show a PopupFrame with some components and a title when a button is clicked
     * @param frame the RootPane of the PopupFrame to create
     * @param title the title of the frame
     * @param components the buttons that the PopupFrame will contain
     * @return the listener that allows to create and show the frame
     */
    public ActionListener menusListener(JFrame frame, String title, JButton... components) {
        for (JButton button : components) MenuBarListeners.buttonClickedListener(button);
        return e -> {
            PopupFrame frame1 = new PopupFrame(frame, components);
            frame1.setTitle(title);
            frame1.showFrame();
        };
    }

    /**
     * Allows to show a PopupFrame when a button is clicked
     * @param frame the frame to show
     * @return the listener that allows to create and show the frame
     */
    public ActionListener menusListener(PopupFrame frame) {
        return e -> frame.showFrame();
    }

}
