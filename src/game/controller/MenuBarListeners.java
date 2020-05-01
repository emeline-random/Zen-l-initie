package game.controller;

import game.bin.ChineseSymbol;
import game.bin.Element;
import game.bin.Level;
import game.view.GraphicMenu;
import game.view.GraphicalInterface;
import utilities.Language;
import utilities.Sound;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Java class that provides listeners to all of the menu items on the menu bar
 * on the GUI.
 */
public class MenuBarListeners {

    /**the interface that will be listened*/
    private final GraphicalInterface graphicalInterface;

    /**
     * Constructor of the class that allows to initialize the interface
     * that this the listeners made on this class will be listening
     * @param graphicalInterface the interface containing the items to listen
     */
    public MenuBarListeners (GraphicalInterface graphicalInterface){
        this.graphicalInterface = graphicalInterface;
    }

    /**
     * Allows to create a listener for the game menu, it handles clicks that are made to save a game, to save it
     * to a special place and to restart it.
     * @param save the save item
     * @param saveAs the save as item
     * @param restart the restart item
     * @param game the game to save/save as/restart
     * @return the appropriate action listener
     */
    public ActionListener gameListener(JMenuItem save, JMenuItem saveAs, JMenuItem restart, Game game){
        return e -> {
            if (e.getSource() == save){
                game.saveGame();
            } else if (e.getSource() == saveAs){
                game.saveGameAs();
            } else if (e.getSource() == restart){
                this.graphicalInterface.setVisible(false);
                this.graphicalInterface.setRestartGame(true);
                synchronized (this.graphicalInterface.getCoordinates()) {
                    this.graphicalInterface.getCoordinates().notify();
                }
            }
        };
    }

    /**
     * Allows to create a listener that will handle actions on the menu menu, it
     * allows to show the menu or to exit the application
     * @param quit the quit item
     * @param showMenu the show menu item
     * @return the appropriate listener
     */
    public ActionListener menuListener(JMenuItem quit, JMenuItem showMenu){
        return e -> {
            if (e.getSource() == showMenu){
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
            } else if (e.getSource() == quit){
                System.exit(0);
            }
        };
    }

    /**
     * Allows to create an action listener that handles actions in the settings menu. It allows to
     * call the appropriates methods to change the language or the scheme and to turn on/off the sound
     * of the application
     * @param english the english item
     * @param french the french item
     * @param sound the sound on/off item
     * @param board the matrix of elements (used to redraw the frame in case of changing scheme/language)
     * @param game the current game (used to redraw the frame in case of changing scheme/language)
     * @param level the current level (used to redraw the frame in case of changing scheme/language)
     * @param symbols the matrix of ChineseSymbols (used to redraw the frame in case of changing scheme/language)
     * @return the appropriate listener
     */
    public ActionListener settingsListener(JMenuItem english, JMenuItem french, JMenuItem sound, Element[][] board,
                                           Game game, Level level, ChineseSymbol[][] symbols){
        return e -> {
            if (e.getSource() == english){
                Language.setLanguage(Language.Languages.ENGLISH);
                this.graphicalInterface.repaintFrame(game, board, level, symbols);
            } else if (e.getSource() == french){
                Language.setLanguage(Language.Languages.FRENCH);
                this.graphicalInterface.repaintFrame(game, board, level, symbols);
            } else if (e.getSource() == sound){
                Sound.setOn(!Sound.isOn());
                sound.repaint();
            }
        };
    }

    /**
     * Allows to create a listener that handles actions on the help menu, it allows to
     * call the methods to show the rules frame and the help frame depending on the menu item clicked.
     * @param rules the rules item
     * @param help the help item
     * @return the appropriate listener
     */
    public ActionListener helpListener(JMenuItem rules, JMenuItem help){
        return e -> {
            if (e.getSource() == rules){
                GraphicMenu.showRules();
            } else if (e.getSource() == help){
                this.graphicalInterface.showHelpFrame();
            }
        };
    }

}
