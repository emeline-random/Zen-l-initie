package game.controller;

import game.model.ChineseSymbol;
import game.model.Element;
import game.model.Level;
import game.view.*;
import utilities.Language;
import utilities.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
     * Allows to create a listener for the game menu, it handles clicks that are made to save a game, to save it
     * to a special place and to restart it.
     *
     * @param save   the save item
     * @param saveAs the save as item
     * @return the appropriate action listener
     */
    public ActionListener gameListener(JButton save, JButton saveAs) {
        return e -> {
            if (e.getSource() == save) {
                this.game.saveGame();
            } else if (e.getSource() == saveAs) {
                this.game.saveGameAs();
            }
        };
    }

    public ActionListener settingsListener2(JComponent language, JComponent scheme, JComponent sound, PopupFrame frame) {
        return e -> {
            if (e.getSource() == language) {
                frame.getContentPane().removeAll();
                frame.add(this.graphicalInterface.getLanguagePanel(frame));
                frame.revalidate();
            } else if (e.getSource() == scheme) {
                frame.getContentPane().removeAll();
                frame.add(this.graphicalInterface.getSchemePanel(frame));
                frame.revalidate();
            } else if (e.getSource() == sound) {
                Sound.setOn(!Sound.isOn());
                Image image = Sound.getImage();
                ((IconButton)sound).changeIcon(new ImageIcon(image));
                if (Sound.isOn()) Sound.play(Sound.Sounds.BUTTON_PRESSED);
            }
        };
    }

    public ActionListener pauseListener(JComponent console, JComponent home, JComponent replay, JComponent exit) {
        return e -> {
            if (e.getSource() == console) {
                this.graphicalInterface.setVisible(false);
                this.graphicalInterface.dispose();
                this.graphicalInterface.setConsole(true);
                synchronized (this.graphicalInterface.getCoordinates()) {
                    this.graphicalInterface.getCoordinates().notify();
                }
            } else if (e.getSource() == home) {
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

    public ActionListener languageListener(JComponent fr, JComponent en) {
        return e -> {
            if (e.getSource() == fr) Language.setLanguage(Language.Languages.FRENCH);
            else if (e.getSource() == en) Language.setLanguage(Language.Languages.ENGLISH);
            this.graphicalInterface.repaintFrame(this.game, this.board, this.level, this.symbols);
        };
    }

    /**
     * Allows to create a listener that handles actions on the help menu, it allows to
     * call the methods to show the rules frame and the help frame depending on the menu item clicked.
     *
     * @param rules the rules item
     * @param help  the help item
     * @return the appropriate listener
     */
    public ActionListener helpListener(JComponent rules, JComponent help) {
        return e -> {
            if (e.getSource() == rules) {
                GraphicMenu.showRules(this.graphicalInterface);
            } else if (e.getSource() == help) {
                this.graphicalInterface.showHelpFrame();
            }
        };
    }

    public ActionListener schemesListener(SwitchScheme.Scheme scheme) {
        return e -> {
            SwitchScheme.switchScheme(scheme);
            this.graphicalInterface.repaintFrame(this.game, this.board, this.level, this.symbols);
        };
    }

    public ActionListener menusListener(JFrame frame, List<JComponent> components, String title) {
        return e -> {
            PopupFrame frame1 = new PopupFrame(frame, components);
            frame1.setTitle(title);
            frame1.showFrame();
        };
    }

    public ActionListener menusListener(PopupFrame frame) {
        return e -> frame.showFrame();
    }

}
