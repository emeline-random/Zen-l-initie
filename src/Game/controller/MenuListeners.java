package game.controller;

import game.model.Level;
import game.model.Player;
import game.model.artificialPlayers.ArtificialPlayer;
import game.view.*;
import utilities.GameColor;
import utilities.Language;
import utilities.Sound;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Java class that provides all listeners for all of the menu buttons.
 */
public class MenuListeners {

    /**
     * the menu listened by this class
     */
    private final GraphicMenu menu;

    /**
     * Constructor of the class that allows to initialize the listened menu view that will be modified
     * depending on the user's action.
     *
     * @param menu the menu to listen
     */
    public MenuListeners(GraphicMenu menu) {
        this.menu = menu;
    }

    /**
     * Allows to create an action listener that allows to start the game by taking the information in the different
     * panels and checks the information to see if they are correct, the game is started on ly if they are.
     *
     * @param name1           the JTextField containing the name of the first player
     * @param graphicBox      the checkBox that is ticked when the game will be in graphic mode
     * @param consoleBox      the checkBox that is ticked when the game will be in console mode
     * @param playerNumber    the number of players in the game
     * @param colors1         the JComboBox that contains the color chosen by the first player
     * @param colors2         the JComboBox that contains the color chosen by the second player (if 2 players will play)
     * @param name2           the name of the second player (if 2 players will play)
     * @param level           the checkBox that is ticked when the game will be play with assisted pawns displacement
     * @param playerJComboBox the JComboBox that contains the artificial player chosen (if 1 player will play)
     * @return the action listener that will allow to perform an action to start the game if all information are corrects
     */
    public ActionListener startButtonListener(JTextField name1, JCheckBoxMenuItem graphicBox, JCheckBoxMenuItem consoleBox,
                                              int playerNumber, JComboBox<GameColor> colors1, JComboBox<GameColor> colors2, JTextField name2,
                                              JCheckBox level, JComboBox<ArtificialPlayer> playerJComboBox) {
        return e -> {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            if (name1.getText().trim().length() > 0 && (graphicBox.isSelected() || consoleBox.isSelected()) && !(graphicBox.isSelected() && consoleBox.isSelected()) &&
                    ((playerNumber == 2 && colors1.getSelectedItem() != colors2.getSelectedItem() && name2.getText().trim().length() > 0) || playerNumber == 1)) {
                Level level1;
                GameMode mode1;
                Player secondPlayer;
                Player firstPlayer = new Player(name1.getText(), (GameColor) colors1.getSelectedItem());
                if (level.isSelected()) level1 = Level.EASY;
                else level1 = Level.HARD;
                if (graphicBox.isSelected()) mode1 = new Graphic(level1);
                else mode1 = new Console(level1);
                if (playerNumber == 2)
                    secondPlayer = new Player(name2.getText(), (GameColor) colors2.getSelectedItem());
                else secondPlayer = (ArtificialPlayer) playerJComboBox.getSelectedItem();
                this.menu.setGame(new Game(mode1, firstPlayer, secondPlayer, level1));
                this.menu.setStartGame(true);
                this.menu.dispose();
                synchronized (this.menu.getLock()) {
                    this.menu.getLock().notify();
                }
            }
        };
    }

    /**
     * Allows to create a listener for the home button in the menu, this button allows to show the homepage.
     * @return the home button listener
     */
    public ActionListener homeListener() {
        return (e) -> {
            this.menu.remove(this.menu.getCenterPanel());
            this.menu.remove(this.menu.getPageEndPanel());
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.showGlobalMenu();
        };
    }

    /**
     * Allows to create a listener for the new game button. It allows to show the configuration
     * page of a new game
     * @return the new game button listener
     */
    public ActionListener newGameListener() {
        return (e) -> {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.showModeSelectionMenu();
        };
    }

    /**
     * Allows to create a listener for the resume game button. It allows to show a JFileChooser
     * so that the user chooses the game he wants to open and then tells to the menu object that
     * the user decided to quit the menu to open a saved game.
     * @return the resume game button listener
     */
    public ActionListener resumeGameListener() {
        return (e) -> {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.getContentPane().removeAll();
            Scheme.Schemes oldScheme = Scheme.getCurrentScheme();
            try {
                Scheme.switchScheme(Scheme.Schemes.SYSTEM);
                JFileChooser chooser = new JFileChooser(".");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "ser files", "ser");
                chooser.setFileFilter(filter);
                int choose = chooser.showOpenDialog(new JFrame());
                if (choose == JFileChooser.APPROVE_OPTION) {
                    FileInputStream stream = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
                    ObjectInputStream file = new ObjectInputStream(stream);
                    this.menu.setGame((Game) file.readObject());
                    this.menu.setRestart(true);
                    file.close();
                    this.menu.dispose();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, Language.getText("save game error"), "Zen l'Initié", JOptionPane.ERROR_MESSAGE);
            } finally {
                Scheme.switchScheme(oldScheme);
                synchronized (this.menu.getLock()) {
                    this.menu.getLock().notify();
                }
            }
        };
    }

    /**
     * Allows to create a listener for the rules button. It show a new frame that
     * displays rules.
     * @return the rules button listener
     */
    public ActionListener rulesListener() {
        return (e) -> {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            GraphicMenu.showRules(this.menu);
        };
    }

    /**
     * Allows to create a listener for the sound button. It allows to turn on and off the sound
     * and to change the image displayed on the sound button.
     * @param soundButton the button to turn on/off the sound
     * @return the sound button listener
     */
    public ActionListener soundListener(JButton soundButton) {
        return (e) -> {
            if (Sound.isOn()) {
                Sound.setOn(false);
            } else {
                Sound.setOn(true);
                Sound.play(Sound.Sounds.BUTTON_PRESSED);
            }
            soundButton.setIcon(null);
        };
    }

    /**
     * Allows to create a listener for the demo button. It only says to the menu
     * that the user wants to quit the menu to see the demo of the game.
     * @return the demo button listener
     */
    public ActionListener demoListener() {
        return (e) -> {
            this.menu.dispose();
            this.menu.setDemo(true);
            synchronized (this.menu.getLock()) {
                this.menu.getLock().notify();
            }
        };
    }

    /**
     * Creates a listener that allows to choose the number of player in a new game,
     * the appropriate configuration menu is then shown.
     * @param onePlayer the one player button
     * @param twoPlayers the two players button
     * @return the listener for both one and two players buttons
     */
    public ActionListener playerNumberListener(JButton onePlayer, JButton twoPlayers) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == onePlayer) {
                    Sound.play(Sound.Sounds.BUTTON_PRESSED);
                    menu.showConfigMenu(1);
                } else if (e.getSource() == twoPlayers) {
                    Sound.play(Sound.Sounds.BUTTON_PRESSED);
                    menu.showConfigMenu(2);
                }
            }
        };
    }

    /**
     * Creates a listener that allows to change the language of the application.
     * @param fr the french button
     * @param en the english button
     * @return the languages buttons listener
     */
    public ActionListener languageListener(JButton fr, JButton en){
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == en) {
                    Sound.play(Sound.Sounds.BUTTON_PRESSED);
                    Language.setLanguage(Language.Languages.ENGLISH);
                    menu.changeLanguage();
                } else if (e.getSource() == fr) {
                    Sound.play(Sound.Sounds.BUTTON_PRESSED);
                    Language.setLanguage(Language.Languages.FRENCH);
                    menu.changeLanguage();
                }
            }
        };
    }

}
