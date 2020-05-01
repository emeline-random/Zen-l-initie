package game.controller;

import game.bin.Level;
import game.bin.Player;
import game.bin.artificialPlayers.ArtificialPlayer;
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
 * Java class that is a listener for all menu buttons and that listens to
 * this menu.
 */
public class MenuListener implements ActionListener {

    /**
     * the menu listened by this class
     */
    private GraphicMenu menu;
    /**
     * the rules jButton
     */
    private JButton rules;
    /**
     * the buttons to go to the home page of the menu
     */
    private JButton home;
    /**
     * the button that allows to create a new game
     */
    private JButton newGame;
    /**
     * the button that allows to resume a chosen game
     */
    private JButton resumeGame;
    /**
     * the button that allows to tur, on/off the sound
     */
    private JButton sound;
    /**
     * the button that allows to show the demo
     */
    private JButton demo;
    /**
     * the button that allows to play on one player mode
     */
    private JButton onePlayer;
    /**
     * the button that allows to play in two players mode
     */
    private JButton twoPlayers;
    /**
     * the button that allows to set the language to english
     */
    private JButton en;
    /**
     * the button that allows to set the language to french
     */
    private JButton fr;

    /**
     * Constructor of the class that allows to initialize the listened menu view that will be modified
     * depending on the user's action.
     * @param menu the menu to listen
     */
    public MenuListener(GraphicMenu menu) {
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
    public ActionListener getStartButtonListener(JTextField name1, JCheckBoxMenuItem graphicBox, JCheckBoxMenuItem consoleBox,
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
     * Allows to compare the source of the event to the class variables buttons so that
     * if the source matches to one of it, the corresponding action is performed.
     *
     * @param e the event heard
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.home) {
            this.menu.remove(this.menu.getCenterPanel());
            this.menu.remove(this.menu.getPageEndPanel());
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.showGlobalMenu();
        } else if (e.getSource() == this.newGame) {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.showModeSelectionMenu();
        } else if (e.getSource() == this.resumeGame) {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.getContentPane().removeAll();
            SwitchScheme.Scheme oldScheme = SwitchScheme.getCurrentScheme();
            try {
                SwitchScheme.switchScheme(SwitchScheme.Scheme.SYSTEM);
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
                SwitchScheme.switchScheme(oldScheme);
                synchronized (this.menu.getLock()) {
                    this.menu.getLock().notify();
                }
            }
        } else if (e.getSource() == this.sound) {
            if (Sound.isOn()) Sound.setOn(false);
            else {
                Sound.setOn(true);
                Sound.play(Sound.Sounds.BUTTON_PRESSED);
            }
            this.sound.repaint();
        } else if (e.getSource() == this.demo) {
            this.menu.dispose();
            this.menu.setDemo(true);
            synchronized (this.menu.getLock()) {
                this.menu.getLock().notify();
            }
        } else if (e.getSource() == this.onePlayer) {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.showConfigMenu(1);
        } else if (e.getSource() == this.twoPlayers) {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            this.menu.showConfigMenu(2);
        } else if (e.getSource() == this.rules) {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            GraphicMenu.showRules();
        } else if (e.getSource() == this.en) {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            Language.setLanguage(Language.Languages.ENGLISH);
            this.menu.changeLanguage();
        } else if (e.getSource() == this.fr) {
            Sound.play(Sound.Sounds.BUTTON_PRESSED);
            Language.setLanguage(Language.Languages.FRENCH);
            this.menu.changeLanguage();
        }
    }

    /**
     * Allows to set the menu that is listened by this controller.
     *
     * @param menu the view of the menu
     */
    public void setMenu(GraphicMenu menu) {
        this.menu = menu;
    }

    /**
     * @param home Allows to set the home button
     */
    public void setHome(JButton home) {
        this.home = home;
    }

    /**
     * @param newGame Allows to set the New Game button
     */
    public void setNewGame(JButton newGame) {
        this.newGame = newGame;
    }

    /**
     * @param resumeGame Allows to set the Resume Game button
     */
    public void setResumeGame(JButton resumeGame) {
        this.resumeGame = resumeGame;
    }

    /**
     * @param sound Allows to set the button to turn on/off the sound
     */
    public void setSound(JButton sound) {
        this.sound = sound;
    }

    /**
     * @param demo Allows to set the button to see the demo
     */
    public void setDemo(JButton demo) {
        this.demo = demo;
    }

    /**
     * @param onePlayer Allows to set the button to play in one player mode
     */
    public void setOnePlayer(JButton onePlayer) {
        this.onePlayer = onePlayer;
    }

    /**
     * @param twoPlayers Allows to set the button to play in two players mode
     */
    public void setTwoPlayers(JButton twoPlayers) {
        this.twoPlayers = twoPlayers;
    }

    /**
     * @param rules Allows to set the button that allows to see rules
     */
    public void setRules(JButton rules) {
        this.rules = rules;
    }

    /**
     * @param en Allows to set the english language button
     */
    public void setEn(JButton en) {
        this.en = en;
    }

    /**
     * @param fr Allows to set the french language button
     */
    public void setFr(JButton fr) {
        this.fr = fr;
    }
}
