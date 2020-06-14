package game.view;

import game.controller.BoardAndInputListeners;
import game.controller.Game;
import game.controller.MenuBarListeners;
import game.model.*;
import utilities.Language;
import utilities.Sound;
import utilities.ViewUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Java class that allows to create and to show a JFrame that displays
 * a board and some information about the player. Also allows to get
 * user input to move pawns. To work this class needs to be linked to
 * a Graphic object.
 */
public class GraphicalInterface extends JFrame {

    /**
     * The current player
     */
    private Player player;
    /**
     * The JTextField that will contain the number of the pawn to move
     */
    private final JTextField pawn;
    /**
     * The JLabel that will contain the name of the current player
     */
    private final JLabel playerName;
    /**
     * The JLabel displaying informations as needed (such as impossible displecement)
     */
    private final JLabel information;
    /**
     * The JPanel containing the history of the last moves
     */
    private JPanel historyPanel;
    /**
     * The array containing the displacement coordinates in format [pawn, line, column]
     */
    private final int[] coordinates;
    /**
     * true if the user clicked on the menu button
     */
    private boolean backToMenu = false;
    /**
     * true if the user clicked on the replay button
     */
    private boolean restartGame = false;
    /**
     * true if the user clicked on the console button
     */
    private boolean console = false;
    /**
     * The MenuBarListeners that will provides listeners to all menu items
     */
    private final MenuBarListeners listeners;
    /**
     * The BoardAndInputListeners object that will provides listeners for both pans and input panels
     */
    private final BoardAndInputListeners boardListeners = new BoardAndInputListeners(this);
    /**
     * The dimension of the frames
     */
    private static Dimension dimension;

    /**
     * Constructor of the class that allows to initialize variables.
     * Allows to get the game that will take place in the frame, the level
     * of this game and the board and the symbols to display.
     *
     * @param game    the current game
     * @param level   the level of the game
     * @param board   the board to display
     * @param symbols the symbols to display
     */
    public GraphicalInterface(Game game, Level level, Element[][] board, ChineseSymbol[][] symbols) {
        SwitchScheme.switchScheme(SwitchScheme.Scheme.LIGHT);
        if (game != null && level != null) {
            this.pawn = new JTextField(3);
            this.pawn.setHorizontalAlignment(JTextField.CENTER);
            this.playerName = new JLabel(Language.getText("name"));
            this.information = new JLabel();
            this.coordinates = new int[3];
            this.listeners = new MenuBarListeners(this, game, board, symbols, level);
            this.showFrame(game, board, level, symbols);
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Allows to create all panels and listeners of the frame and then makes it visible
     *
     * @param game    the current game
     * @param board   the board to display
     * @param level   the level of the game
     * @param symbols the symbols to display
     */
    private void showFrame(Game game, Element[][] board, Level level, ChineseSymbol[][] symbols) {
        this.setIconImage(ViewUtilities.getImage("/pictures/logo.png"));
        this.configFrame();
        JTextField line = new JTextField(3);
        JTextField column = new JTextField(3);
        JPanel pawnsPanel = this.createPawnsPanel(board, symbols);
        JPanel inputPanel = this.createInputPanel(board, level, line, column);
        JPanel infoPanel = this.createPlayersInfoPanel(game);
        this.historyPanel = new JPanel();
        this.historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        this.historyPanel.add(new JLabel(Language.getText("history")));
        JPanel menuPanel = this.createOptionsPanel();
        pawnsPanel.addMouseListener(this.boardListeners.boardListener(level, board, line, column));
        JSplitPane rightPanel = this.getSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, this.historyPanel, 0.4);
        JSplitPane leftPanel = this.getSplitPane(JSplitPane.VERTICAL_SPLIT, infoPanel, menuPanel, 0.5);
        JSplitPane centerPanel = this.getSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, pawnsPanel, 0.15);
        JSplitPane totalPanel = this.getSplitPane(JSplitPane.HORIZONTAL_SPLIT, centerPanel, rightPanel, 0.85);
        pawnsPanel.addMouseListener(this.boardListeners.boardListener(level, board, line, column));
        this.add(totalPanel, BorderLayout.CENTER);
        this.pawn.requestFocus();
        if (dimension == null) {
            this.pack();
            this.setLocationRelativeTo(null);
        } else this.setSize(dimension);
        this.setVisible(true);
    }

    /**
     * Allows to do the main configurations of the frame.
     */
    private void configFrame() {
        this.setTitle("Zen l'initié");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1000, 450));
        this.setLayout(new BorderLayout());
    }

    private JSplitPane getSplitPane(int orientation, JComponent panel1, JComponent panel2, double percentage) {
        JSplitPane pane = new JSplitPane(orientation, panel1, panel2);
        pane.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pane.setDividerLocation(percentage);
                super.componentResized(e);
            }

            @Override
            public void componentShown(ComponentEvent e) {
                pane.setDividerLocation(percentage);
                super.componentShown(e);
            }
        });
        pane.setOneTouchExpandable(true);
        return pane;
    }

    /**
     * Allows to check the coordinates and to display a message in the
     * information panel if they are not correct, otherwise it notifies
     * to the current Graphic view that the coordinates are available.
     * The JTextFields containing line and column index are cleared if not null.
     *
     * @param level  the level of the game
     * @param line   the JTextField containing line index in HARD mode (can be null)
     * @param column the JTextField containing column index in HARD mode (can be null)
     */
    public void sendCoordinates(Level level, JTextField line, JTextField column) {
        if ((coordinates[0] < Player.getPAWNS_NUMBER() && coordinates[0] >= 0 || coordinates[0] == -1) && player.getPawn(coordinates[0]) != null
                && coordinates[1] < GameBoard.getDIMENSION() && coordinates[1] >= 0 && coordinates[2] < GameBoard.getDIMENSION() && coordinates[2] >= 0) {
            synchronized (getCoordinates()) {
                getCoordinates().notify();
            }
            pawn.setText("");
            if (level == Level.HARD) {
                line.setText("");
                column.setText("");
            }
            information.setText("");
        } else {
            addInformation(Language.getText("entries error"), true);
            Sound.play(Sound.Sounds.IMPOSSIBLE_MOVE);
        }
    }

    /**
     * Allows to create a panel representing the board of the game by overriding
     * the paintComponent and the getPreferredSize of JPanel class.
     *
     * @param board   the board to display
     * @param symbols the matrix containing the symbols to display
     * @return the JPanel representing the board
     */
    private JPanel createPawnsPanel(Element[][] board, ChineseSymbol[][] symbols) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ViewUtilities.getImage("/pictures/pawns_background.png", this.getWidth(), this.getHeight()),
                        0, 0, null);
                int widthSpace = this.getWidth() - (GameBoard.getDIMENSION() + 1) * Element.getSizeUnity();
                if (widthSpace > 200) {
                    Image image = ViewUtilities.getImage("/pictures/bamboo.png");
                    int width = (this.getHeight() - 10) * image.getWidth(null) / image.getHeight(null);
                    g.drawImage(ViewUtilities.getImage(image, width, this.getHeight() - 10), this.getWidth() - width,
                            0, null);
                }
                if (this.getHeight() < this.getWidth()) {
                    Element.setSizeUnity((int) ((this.getHeight() * 0.9) / GameBoard.getDIMENSION()));
                } else {
                    Element.setSizeUnity((int) ((this.getWidth() * 0.9) / GameBoard.getDIMENSION()));
                }
                Pawn.setDiameter(Element.getSizeUnity() - Element.getSizeUnity() / 4);
                Graphics2D g2 = (Graphics2D) g.create();
                for (int i = 0; i < board.length; i++) {
                    for (int j = 0; j < board[i].length; j++) {
                        if (board[i][j] instanceof Pawn) {
                            board[i][j].paint(g);
                        } else if (symbols[i][j] != null) {
                            symbols[i][j].paint(g);
                        } else {
                            board[i][j].paint(g);
                        }
                    }
                }
                for (int i = 0; i <= board.length; i++) {
                    g.setColor(Color.black);
                    g.drawLine(Element.getSizeUnity(), (i + 1) * Element.getSizeUnity(),
                            Element.getSizeUnity() * (board.length + 1), (i + 1) * Element.getSizeUnity());
                    g.drawLine((i + 1) * Element.getSizeUnity(), Element.getSizeUnity(),
                            (i + 1) * Element.getSizeUnity(), Element.getSizeUnity() * (board.length + 1));
                }
                String[] strings = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
                for (int i = 0; i < GameBoard.getDIMENSION(); i++) {
                    g.drawString(strings[i], (int) ((i + 1.5) * Element.getSizeUnity()), (int) (Element.getSizeUnity() * 0.75));
                    g.drawString(Integer.toString(i), (int) (Element.getSizeUnity() * 0.5), (int) ((i + 1.75) * Element.getSizeUnity()));
                }
                g2.dispose();
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(500, 500);
            }
        };
    }

    /**
     * Allows to create the panel where the input of the players will be made
     * depending on the chosen level. If the level is EASY then this panel displays
     * buttons with arrows on which the player can click to move the pawn in the corresponding
     * direction. If level is HARD then it displays 3 JTextFields and a valid button.
     *
     * @param board  the board used especially to compute the moves in EASY level
     * @param level  the level of the game
     * @param line   the JTextField containing the line index in HARD level
     * @param column the JTextField containing the column index in HARD level
     * @return the appropriate input panel
     */
    private JPanel createInputPanel(Element[][] board, Level level, JTextField line, JTextField column) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        ViewUtilities.addCenteredComponent(p, this.playerName, true);
        if (level == Level.HARD) {
            this.pawn.setMaximumSize(new Dimension(100, 20));
            line.setMaximumSize(new Dimension(100, 20));
            column.setMaximumSize(new Dimension(100, 20));
            JButton validButton = new JButton("go");
            validButton.addActionListener(this.boardListeners.validButtonListener(line, column));
            ViewUtilities.addAllCentered(p, Arrays.asList(new JLabel(Language.getText("pawn message")), this.pawn,
                    new JLabel(Language.getText("line message")), line, new JLabel(Language.getText("column message")),
                    column, validButton), true);
        } else {
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(3, 3));
            ActionListener action = this.boardListeners.easyModeListener(board);
            String[] jButtons = {"nw", "n", "ne", "w", "e", "sw", "s", "se"};
            for (int i = 0; i < jButtons.length; i++) {
                JButton button = new JButton();
                button.setName(jButtons[i]);
                button.addActionListener(action);
                button.setIcon(ViewUtilities.getImageIcon("/pictures/arrows/" + button.getName() + ".png",
                        Element.getSizeUnity(), Element.getSizeUnity()));
                if (i == 4) buttons.add(this.pawn);
                buttons.add(button);
            }
            ViewUtilities.addCenteredComponent(p, buttons, true);
        }
        ViewUtilities.addCenteredComponent(p, this.information, true);
        return p;
    }

    /**
     * Allows to get the number of the pawn in the pawn JTextField
     */
    public void getSelectedPawn() {
        try {
            this.coordinates[0] = Integer.parseInt(this.pawn.getText());
        } catch (Exception ex) {
            if (this.pawn.getText().trim().equals("z") || this.pawn.getText().trim().equals("Z"))
                this.coordinates[0] = -1;
            else this.coordinates[0] = -2;
        }
    }

    /**
     * Allows to create a vertical panel that shows information about the players
     * such as their name and the color they choose.
     *
     * @param game the current game
     * @return the panel displaying information on players
     */
    private JPanel createPlayersInfoPanel(Game game) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        JLabel label = new JLabel(ViewUtilities.getImageIcon("/pictures/logo.png", 100, 100));
        ViewUtilities.addAllCentered(infoPanel, Arrays.asList(this.getPlayerPanel(game.getFirstPlayer(), 1),
                this.getPlayerPanel(game.getSecondPlayer(), 2), label), true);
        return infoPanel;
    }

    /**
     * Allows to draw the pawn of one player, used to create the panel
     * with the players information
     */
    private JPanel getPlayerPanel(Player player, int playerNumber) {
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
        JPanel subPanel = new JPanel(new GridLayout(2, 1));
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        JLabel playerIcon = new JLabel(this.getIcon("/pictures/icons/user" + playerNumber + ".png"));
        JLabel playerPawn = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                if (this.getIcon().getIconHeight() - 1 != (int) (Element.getSizeUnity() * 0.75))
                    this.setIcon(getPawnIcon(player.getColor(), player.getName().substring(0, 1)));
                super.paintComponent(g);
            }
        };
        playerPawn.setIcon(getPawnIcon(player.getColor(), player.getName().substring(0, 1)));
        ViewUtilities.addAll(playerPanel, Arrays.asList(Box.createHorizontalBox(), playerIcon, Box.createHorizontalGlue()));
        ViewUtilities.addCenteredComponent(subPanel, new JLabel(player.getName()), false);
        ViewUtilities.addCenteredComponent(subPanel, playerPawn, false);
        playerPanel.add(subPanel);
        playerPanel.add(Box.createHorizontalGlue());
        return playerPanel;
    }

    private ImageIcon getPawnIcon(Color color, String s) {
        BufferedImage image = new BufferedImage((int) (Element.getSizeUnity() * 0.75 + 1), (int) (Element.getSizeUnity() * 0.75 + 1),
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(color);
        graphics.fillOval(0, 0, (int) (Element.getSizeUnity() * 0.75), (int) (Element.getSizeUnity() * 0.75));
        graphics.setColor(Color.BLACK);
        graphics.drawOval(0, 0, (int) (Element.getSizeUnity() * 0.75), (int) (Element.getSizeUnity() * 0.75));
        graphics.drawString(s, (float) (image.getWidth() * 0.45), (float) (image.getHeight() * 0.55));
        graphics.dispose();
        return new ImageIcon(image);
    }

    private JPanel createOptionsPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 1));
        JButton save = new IconButton(ViewUtilities.getSchemeIcon("save.png"), true);
        JButton saveAs = new IconButton(ViewUtilities.getSchemeIcon("saveAs.png"), true);
        ViewUtilities.addActionListener(this.listeners.gameListener(save, saveAs), Arrays.asList(save, saveAs));
        ViewUtilities.addAll(panel, Arrays.asList(this.createPauseMenu(), save, saveAs, this.createSettingsMenu(),
                this.createHelpMenu()));
        return panel;
    }

    private JButton createHelpMenu() {
        JButton button = new IconButton(ViewUtilities.getSchemeIcon("question.png"), true);
        JButton help = new IconButton(this.getIcon("/pictures/icons/help.png"), false);
        JButton rules = new IconButton(this.getIcon("/pictures/icons/rules.png"), false);
        ViewUtilities.addActionListener(this.listeners.helpListener(rules, help), Arrays.asList(rules, help));
        List<JComponent> menus = Arrays.asList(new JComponent[]{help, rules});
        button.addActionListener(this.listeners.menusListener(this, menus, "Help"));
        return button;
    }

    private JButton createSettingsMenu() {
        JButton button = new IconButton(ViewUtilities.getSchemeIcon("settings.png"), true);
        JButton scheme = new IconButton(this.getIcon("/pictures/icons/scheme.png"), false);
        JButton language = new IconButton(this.getIcon("/pictures/icons/language.png"), false);
        JButton sound = new IconButton(new ImageIcon(Sound.getImage()), false);
        List<JComponent> components = Arrays.asList(scheme, language, sound);
        PopupFrame frame = new PopupFrame(this, components);
        frame.setTitle("settings");
        ActionListener listener = this.listeners.settingsListener2(language, scheme, sound, frame);
        ViewUtilities.addActionListener(listener, Arrays.asList(scheme, language, sound));
        button.addActionListener(this.listeners.menusListener(frame));
        return button;
    }

    private JButton createPauseMenu() {
        JButton button = new IconButton(ViewUtilities.getSchemeIcon("pause.png"), true);
        JButton console = new IconButton(this.getIcon("/pictures/icons/console.png"), false);
        JButton home = new IconButton(this.getIcon("/pictures/icons/home_logo.png"), false);
        JButton restart = new IconButton(this.getIcon("/pictures/icons/restart.png"), false);
        JButton exit = new IconButton(this.getIcon("/pictures/icons/exit.png"), false);
        ViewUtilities.addActionListener(this.listeners.pauseListener(console, home, restart, exit),
                Arrays.asList(home, console, exit, restart));
        button.addActionListener(this.listeners.menusListener(this, Arrays.asList(home, console, exit, restart), "Pause"));
        return button;
    }

    public JPanel getLanguagePanel(PopupFrame frame) {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        JButton fr = new IconButton(this.getIcon("/pictures/icons/france.png"), false);
        JButton en = new IconButton(this.getIcon("/pictures/icons/english.png"), false);
        ViewUtilities.addActionListener(this.listeners.languageListener(fr, en), Arrays.asList(fr, en));//TODO les drapeaux
        ViewUtilities.addAll(panel, Arrays.asList(frame.getHomeButton(), fr, en));
        return panel;
    }

    public JPanel getSchemePanel(PopupFrame frame) {
        JPanel panel = new JPanel(new GridLayout(1, 3));
        JButton light = new IconButton(this.getIcon("/pictures/icons/lightScheme.png"), false);//TODO des meilleures images?
        JButton dark = new IconButton(this.getIcon("/pictures/icons/darkScheme.png"), false);//TODO ajouter le thème du système
        light.addActionListener(this.listeners.schemesListener(SwitchScheme.Scheme.LIGHT));
        dark.addActionListener(this.listeners.schemesListener(SwitchScheme.Scheme.DARK));
        ViewUtilities.addAll(panel, Arrays.asList(frame.getHomeButton(), light, dark));
        return panel;
    }

    private ImageIcon getIcon(String path) {
        Image image = ViewUtilities.getImage(path);
        int width = PopupFrame.getComponentsHeight() * image.getWidth(null) / image.getHeight(null);
        return ViewUtilities.getImageIcon(image, width, PopupFrame.getComponentsHeight());
    }

    /**
     * Allows to create and to show a frame with the main
     * features of the application and a quick explanation about displacements
     * in graphic mode.
     */
    public void showHelpFrame() {
        JDialog frame = new JDialog(this, true);
        JEditorPane pane = new JTextPane();
        pane.setContentType("text/html");
        pane.setEditable(false);
        try {
            if (Language.getLanguage() == Language.Languages.FRENCH)
                pane.setPage(Objects.requireNonNull(getClass().getClassLoader().getResource("Languages/FrenchHelp.html")).toURI().toURL());
            else
                pane.setPage(Objects.requireNonNull(getClass().getClassLoader().getResource("Languages/EnglishHelp.html")).toURI().toURL());
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        frame.add(pane);
        ViewUtilities.showDialog(frame, "Zen l'initié", 1000, 600);
    }

    /**
     * Allows to repaint completely the frame and to create again
     * all elements that it is containing. Useful to change the
     * language or the scheme of the application.
     *
     * @param game    the current game
     * @param board   the board to display
     * @param level   the level of the game
     * @param symbols the symbols to display
     */
    public void repaintFrame(Game game, Element[][] board, Level level, ChineseSymbol[][] symbols) {
        GraphicalInterface.dimension = new Dimension(this.getSize());
        this.getContentPane().removeAll();
        this.showFrame(game, board, level, symbols);
        this.revalidate();
    }

    /**
     * Allows to add information in the information label,
     * this method can add information or replace the current information
     *
     * @param text  the text to show
     * @param clear true if the text must be erase before showing the new one
     */
    protected void addInformation(String text, boolean clear) {
        if (clear) this.information.setText(text);
        else this.information.setText(this.information.getText() + " " + text);
    }

    /**
     * Allows to add an history line in the history panel.
     *
     * @param player the player who played
     * @param move   the coordinates of the move to add
     */
    protected void addHistory(Player player, int[] move) {
        JLabel label1 = new JLabel(player.getName() + "-> " + Language.getText("pawn message") +
                " " + move[0] + " - " + Language.getText("line message") + " " + move[1] +
                " - " + Language.getText("column message") + " " + move[2]);
        this.historyPanel.add(label1);
        while ((this.historyPanel.getComponentCount() + 1) * 20 > this.historyPanel.getHeight()) {
            this.historyPanel.remove(1);
        }
        this.historyPanel.revalidate();
    }

    /**
     * Allows to set the current player
     *
     * @param player the current player
     */
    protected void setPlayer(Player player) {
        this.player = player;
        this.playerName.setText(player.getName());
    }

    /**
     * Allows to indicate if the game needs to be restarted
     *
     * @param restartGame true if the game needs to be restarted, false otherwise
     */
    public void setRestartGame(boolean restartGame) {
        this.restartGame = restartGame;
    }

    /**
     * Allows to indicate if the user wants to go back to the menu and exit the game
     *
     * @param backToMenu true if the user wqants to go back to the menu, false otherwise.
     */
    public void setBackToMenu(boolean backToMenu) {
        this.backToMenu = backToMenu;
    }

    /**
     * Allows to indicate if the user wants to go in console mode
     *
     * @param console true if the mode needs to be swiped to console, false otherwise
     */
    public void setConsole(boolean console) {
        this.console = console;
    }

    /**
     * @return true if the user clicked on the menu button, false otherwise
     */
    protected boolean isBackToMenu() {
        return this.backToMenu;
    }

    /**
     * @return true if the user clicked on the restart button, false otherwise
     */
    protected boolean isRestartGame() {
        return this.restartGame;
    }

    /**
     * @return true if the user clicked on the console button, false otherwise
     */
    protected boolean isConsole() {
        return this.console;
    }

    /**
     * @return the Pawn TextField (to create the demo)
     */
    public JTextField getPawn() {
        return pawn;
    }

    /**
     * @return the current player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * @return the move coordinates in format [pawn, line, column]
     */
    public int[] getCoordinates() {
        return this.coordinates;
    }
}