package game.view;

import game.controller.Game;
import game.controller.MenuListeners;
import game.model.Element;
import game.model.artificialPlayers.ArtificialPlayer;
import game.model.artificialPlayers.FirstLevel;
import game.model.artificialPlayers.SecondLevel;
import utilities.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Allows to display the menu of the game in graphic mode. To open the menu dialog
 * the showMenu() method must be called. After that this the menu is independent and handles
 * user's actions, when the menu is displayed the application Thread is blocked until the user
 * made a significant action (closing the window, starting or restarting a game).
 */
public class GraphicMenu extends JFrame {

    /**
     * The panel that will be displayed at the center of the frame
     */
    protected JPanel centerPanel;
    /**
     * The panel  that will be displayed at the bottom of the frame
     */
    private JPanel pageEndPanel;
    /**
     * The game object that might be started or restarted
     */
    private Game game;
    /**
     * The current menu page index
     */
    private int menu = 0;
    /**
     * True if the game object has to be started
     */
    private boolean startGame = false;
    /**
     * True if the game object has to be restarted
     */
    private boolean restart = false;
    /**
     * Element that allows to block the current Thread, notified when a significant action is done
     */
    private static final Element LOCK = new Element();
    /**
     * True if the demo needs to be shown
     */
    private boolean demo = false;
    /**
     * The listener that will listen to all of the buttons in the menu
     */
    private MenuListeners listener;

    /**
     * Allows to call the configuration of the menu frame and to block the application thread
     * using the LOCK Element.
     */
    public void showMenu() {
        SwingUtilities.invokeLater(this::configFrame);
        try {
            synchronized (LOCK) {
                LOCK.wait();
            }
        } catch (InterruptedException e) {
            this.showMenu();
        }
        if (this.startGame) this.game.beginGame();
        else if (this.restart) this.game.restartGame();
        else if (this.demo) new Demo().showDemo();
        else showMenu();
    }

    /**
     * Allows to configure the frame and to create the panels that will be into it.
     */
    protected void configFrame() {
        SwitchScheme.switchScheme(SwitchScheme.Scheme.LIGHT);
        this.listener = new MenuListeners(this);
        this.setSize(930, 535);
        this.setTitle("Zen l'Initié");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setIconImage(ViewUtilities.getImage("/pictures/logo.png"));
        this.setContentPane(new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                g.drawImage(ViewUtilities.getImage("/pictures/background.png", GraphicMenu.this.getWidth(),
                        GraphicMenu.this.getHeight()), 0, 0, null);
            }
        });
        this.setLayout(new BorderLayout());
        this.createUpperPanel();
        this.pageEndPanel = new JPanel(new BorderLayout());
        this.pageEndPanel.setOpaque(false);
        this.pageEndPanel.add(createSoundButton(), BorderLayout.LINE_START);
        this.centerPanel = new JPanel();
        this.setLocationRelativeTo(null);
        this.showGlobalMenu();
    }

    /**
     * Allows to create the JPanel that is displayed on the top of the frame with the language panel
     * and a JButton that shows the global menu on click.
     */
    private void createUpperPanel() {
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridBagLayout());
        JLabel spaceLabel = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                if (this.getBorder().getBorderInsets(this).bottom != (int) (GraphicMenu.this.getHeight() / 2.5)) {
                    this.setBorder(BorderFactory.createEmptyBorder(0, 0, (int) (GraphicMenu.this.getHeight() / 2.5), 0));
                }
            }
        };
        spaceLabel.setOpaque(false);
        spaceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 240, 0));
        JButton home = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                if (this.getIcon() == null || this.getIcon().getIconWidth() != GraphicMenu.this.getWidth() / 18) {
                    this.setIcon(ViewUtilities.getImageIcon("/pictures/icons/home_logo.png", GraphicMenu.this.getWidth() / 18,
                            GraphicMenu.this.getHeight() / 11));
                }
                super.paintComponent(g);
            }
        };
        home.setBackground(new Color(26, 14, 5));
        home.addActionListener(this.listener.homeListener());
        home.setFocusable(false);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        upperPanel.add(home, constraints);
        constraints.anchor = GridBagConstraints.NORTHEAST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.1;
        constraints.gridx = 2;
        upperPanel.add(this.createLanguagePanel(), constraints);
        constraints.anchor = GridBagConstraints.CENTER;
        upperPanel.add(spaceLabel, constraints);
        upperPanel.setOpaque(false);
        this.add(upperPanel, BorderLayout.PAGE_START);
    }

    /**
     * Allows to show the global menu with the possibility to restart a game, to see the rules or
     * to begin a new game.
     */
    public void showGlobalMenu() {
        this.menu = 0;
        this.remove(this.centerPanel);
        JButton newGame = new RoundRectButton(Language.getText("new"), 10, 5, this);
        JButton resumeGame = new RoundRectButton(Language.getText("resume"), 10, 5, this);
        JButton rules = new RoundRectButton(Language.getText("see rules"), 10, 5, this);
        newGame.addActionListener(this.listener.newGameListener());
        resumeGame.addActionListener(this.listener.resumeGameListener());
        rules.addActionListener(this.listener.rulesListener());
        rules.setFocusable(false);
        this.centerPanel = new JPanel();
        this.centerPanel.add(newGame);
        this.centerPanel.add(resumeGame);
        this.changePageEndButton(rules);
        this.centerPanel.setOpaque(false);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.add(this.pageEndPanel, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    /**
     * Allows to create a button that will always be shown on the menu to turn on or off the sounf
     * of the application.
     *
     * @return the sound button
     */
    private JButton createSoundButton() {
        JButton sound = new JButton() {
            @Override
            public void paintComponent(Graphics graphics) {
                if (this.getIcon() == null || this.getIcon().getIconWidth() != GraphicMenu.this.getWidth() / 18) {
                    this.setIcon(ViewUtilities.getImageIcon(Sound.getImage(), GraphicMenu.this.getWidth() / 18,
                            GraphicMenu.this.getHeight() / 11));
                }
                super.paintComponent(graphics);
            }
        };
        sound.addActionListener(this.listener.soundListener(sound));
        sound.setBackground(new Color(26, 14, 5));
        sound.setFocusable(false);
        return sound;
    }

    /**
     * Allows to display in a new JFrame the rules of the game in the current language, the
     * frame can stay open while playing.
     */
    public static void showRules(JFrame owner) {
        JDialog rulesFrame = new JDialog(owner, true);
        rulesFrame.add(new JLabel(ViewUtilities.getImageIcon("/pictures/logo.png", 200, 200)), BorderLayout.PAGE_START);
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel(ViewUtilities.getImageIcon("/pictures/help/players.png", 200, 120)));
        panel.add(new JLabel(Language.getText("goal")));
        panel.add(new JLabel(Language.getText("normal move")));
        panel.add(new JLabel(ViewUtilities.getImageIcon("/pictures/help/pawn.png", 160, 90)));
        panel.add(new JLabel(ViewUtilities.getImageIcon("/pictures/help/zen.png", 160, 90)));
        panel.add(new JLabel(Language.getText("zen move")));
        rulesFrame.add(panel, BorderLayout.CENTER);
        rulesFrame.add(new JLabel("Zen l'Initié - Breit Hoarau Emeline"), BorderLayout.PAGE_END);
        ViewUtilities.showDialog(rulesFrame, "Zen l'Initié - " + Language.getText("rules"), 800, 700);
    }

    /**
     * Allows to show a menu that allows to choose the number of players in a new game.
     */
    public void showModeSelectionMenu() {
        this.menu = 1;
        this.remove(this.centerPanel);
        this.centerPanel = new JPanel();
        JButton onePlayer = new RoundRectButton("1 " + Language.getText("player"), 10, 5, this);
        JButton twoPlayers = new RoundRectButton("2 " + Language.getText("players"), 10, 5, this);
        JButton demo = new RoundRectButton("Demo", 10, 5, this);
        ViewUtilities.addActionListener(this.listener.playerNumberListener(onePlayer, twoPlayers), Arrays.asList(onePlayer, twoPlayers));
        demo.addActionListener(this.listener.demoListener());
        this.centerPanel.add(onePlayer);
        this.centerPanel.add(twoPlayers);
        this.centerPanel.setOpaque(false);
        this.changePageEndButton(demo);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.revalidate();
    }

    /**
     * Allows to show the configuration menu for a new game depending on the number of players chose in
     * the menu shown before.
     *
     * @param playerNumber the number of players in the game
     */
    public void showConfigMenu(int playerNumber) {
        this.menu = playerNumber + 1;
        this.remove(this.centerPanel);
        this.centerPanel = new JPanel();
        JLabel mode = new JLabel("mode");
        mode.setHorizontalAlignment(SwingConstants.CENTER);
        JCheckBoxMenuItem graphicBox = new JCheckBoxMenuItem(Language.getText("graphic"), true);
        JCheckBoxMenuItem consoleBox = new JCheckBoxMenuItem(Language.getText("console"), false);
        JCheckBox level = new JCheckBox(Language.getText("displacement help"));
        DefaultComboBoxModel<ArtificialPlayer> playersModel = new DefaultComboBoxModel<>(new ArtificialPlayer[]{new FirstLevel(), new SecondLevel()});
        JComboBox<ArtificialPlayer> playerJComboBox = new JComboBox<>(playersModel);

        JLabel player1 = new JLabel(Language.getText("player1"));
        player1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        player1.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel player2 = new JLabel(Language.getText("player2"));
        player2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        player2.setHorizontalAlignment(SwingConstants.CENTER);
        JTextField name1 = new JTextField(Language.getText("name"));
        JTextField name2 = new JTextField(Language.getText("name"));
        DefaultComboBoxModel<GameColor> model1 = new DefaultComboBoxModel<>(GameColor.getColors());
        DefaultComboBoxModel<GameColor> model2 = new DefaultComboBoxModel<>(GameColor.getColors());
        JComboBox<GameColor> colors1 = new JComboBox<>(model1);
        JComboBox<GameColor> colors2 = new JComboBox<>(model2);

        JButton button = new RoundRectButton(Language.getText("start"), 10, 10, this);
        button.addActionListener(this.listener.startButtonListener(name1, graphicBox, consoleBox, playerNumber, colors1,
                colors2, name2, level, playerJComboBox));
        if (playerNumber == 2) this.centerPanel.setLayout(new GridLayout(4, 3));
        else this.centerPanel.setLayout(new GridLayout(3, 3));
        this.centerPanel.setBackground(new Color(230, 216, 202));
        this.centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        ViewUtilities.addAll(this.centerPanel, Arrays.asList(mode, graphicBox, consoleBox, player1, name1, colors1));
        if (playerNumber == 2) {
            ViewUtilities.addAll(this.centerPanel, Arrays.asList(player2, name2, colors2, new JLabel()));
        } else {
            JLabel adverse = new JLabel(Language.getText("adverse level"));
            adverse.setHorizontalAlignment(SwingConstants.CENTER);
            this.centerPanel.add(adverse);
            this.centerPanel.add(playerJComboBox);
        }
        this.centerPanel.add(level);
        this.changePageEndButton(button);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.revalidate();
    }

    /**
     * Allows to create a panel that will always be shown on the menu that allows to
     * change the language of the application.
     *
     * @return the panel with JButtons that change the language
     */
    private JPanel createLanguagePanel() {
        JButton fr = new JButton(ViewUtilities.getImageIcon("/pictures/icons/france.png", 30, 20));
        JButton en = new JButton(ViewUtilities.getImageIcon("/pictures/icons/english.png", 30, 20));
        ViewUtilities.addActionListener(this.listener.languageListener(fr, en), Arrays.asList(fr, en));
        JPanel languages = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(new Color(26, 14, 5));
                g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        languages.setPreferredSize(new Dimension(50, 40));
        languages.add(en);
        languages.add(fr);
        languages.setOpaque(false);
        return languages;
    }

    /**
     * Allows to change the button displayed at the bottom of the frame
     *
     * @param button the new JButton to display
     */
    private void changePageEndButton(JButton button) {
        if (this.pageEndPanel.getComponentCount() >= 2) this.pageEndPanel.remove(1);
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(button);
        this.pageEndPanel.add(panel, BorderLayout.CENTER);
    }

    /**
     * Allows to change the language while staying on the same menu page.
     */
    public void changeLanguage() {
        switch (this.menu) {
            case 0:
                this.showGlobalMenu();
                break;
            case 1:
                this.showModeSelectionMenu();
                break;
            case 2:
                this.showConfigMenu(1);
                break;
            case 3:
                this.showConfigMenu(2);
                break;
        }
    }

    /**
     * @return the panel at the center of the frame
     */
    public JPanel getCenterPanel() {
        return this.centerPanel;
    }

    /**
     * @return the panel at the end of the page
     */
    public JPanel getPageEndPanel() {
        return this.pageEndPanel;
    }

    /**
     * Allows to indicate if the demo needs to be shown
     *
     * @param demo true if the demo needs to be shown, false otherwise
     */
    public void setDemo(boolean demo) {
        this.demo = demo;
    }

    /**
     * @return get the Element that blocks the application thread
     */
    public Element getLock() {
        return LOCK;
    }

    /**
     * @return the menu page index that is currently displayed
     */
    public int getMenu() {
        return this.menu;
    }

    /**
     * Allows to indicate if the game needs to be restarted
     *
     * @param restart true if the game needs to be restarted, false otherwise
     */
    public void setRestart(boolean restart) {
        this.restart = restart;
    }

    /**
     * Allows to set the game object that will be started or restarted
     *
     * @param game the selected/configures game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Allows to indicate if the game needs to be started
     *
     * @param startGame true if the game needs to be started, false otherwise
     */
    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}
