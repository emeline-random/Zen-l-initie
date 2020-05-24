package game.view;

import game.controller.BoardAndInputListeners;
import game.controller.Game;
import game.controller.MenuBarListeners;
import game.model.*;
import utilities.Language;
import utilities.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
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
     * The MenuBarListeners that will provides listeners to all menu items
     */
    private final MenuBarListeners listeners = new MenuBarListeners(this);
    /**
     * The BoardAndInputListeners object that will provides listeners for both pans and input panels
     */
    private final BoardAndInputListeners boardListeners = new BoardAndInputListeners(this);

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
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/logo.png")));
        this.configFrame();
        JPanel pawnsPanel = this.createPawnsPanel(board, symbols);
        JTextField line = new JTextField(3);
        JTextField column = new JTextField(3);
        JPanel inputPanel = this.createInputPanel(board, level, line, column);
        JPanel infoPanel = this.createPlayersInfoPanel(game);
        this.historyPanel = this.createHistoryPanel();
        JSplitPane rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, this.historyPanel);
        JSplitPane leftPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, infoPanel, pawnsPanel);
        leftPanel.setOneTouchExpandable(true);
        rightPanel.setOneTouchExpandable(true);
        this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel), BorderLayout.CENTER);
        pawnsPanel.addMouseListener(this.boardListeners.boardListener(level, board, line, column));
        JMenuBar menuBar = new JMenuBar();
        this.createMenuButton(menuBar);
        this.createGameButton(game, menuBar);
        this.createSettingsButton(game, board, level, symbols, menuBar);
        this.createHelpButton(menuBar);
        this.add(menuBar, BorderLayout.PAGE_START);
        this.pawn.requestFocus();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Allows to do the main configurations of the frame.
     */
    private void configFrame() {
        this.setTitle("Zen l'initié");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 500));
        this.setLayout(new BorderLayout());
    }

    /**
     * Allows to create the valid button and its appropriate
     * listener used if the level is HARD. On click the coordinates are
     * scanned and sent to the sendCoordinates(...) method.
     *
     * @param line   the JTextField containing the line index
     * @param column the JTextField containing the column index
     * @return the valid JButton
     */
    private JButton createValidButton(JTextField line, JTextField column) {
        JButton valid = new JButton("go");
        valid.addActionListener(this.boardListeners.validButtonListener(line, column));
        valid.setAlignmentX(Component.CENTER_ALIGNMENT);
        valid.setFocusable(true);
        return valid;
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
                    g.drawString(strings[i], (int) ((i + 1.25) * Element.getSizeUnity()), (int) (Element.getSizeUnity() * 0.75));
                    g.drawString(Integer.toString(i), (int) (Element.getSizeUnity() * 0.5), (int) ((i + 1.75) * Element.getSizeUnity()));
                }
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(Element.getSizeUnity() * (GameBoard.getDIMENSION() + 2), Element.getSizeUnity() * (GameBoard.getDIMENSION() + 2));
            }

            @Override
            public Dimension getMinimumSize() {
                return new Dimension(150, 150);
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
        this.playerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(this.playerName);
        this.playerName.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        if (level == Level.HARD) {
            p.add(this.playerName);
            this.addCenteredComponent(p, new JLabel(Language.getText("pawn message")));
            p.add(Box.createVerticalGlue());
            this.pawn.setMaximumSize(new Dimension(100, 20));
            p.add(this.pawn);
            this.addCenteredComponent(p, new JLabel(Language.getText("line message")));
            p.add(Box.createVerticalGlue());
            line.setHorizontalAlignment(JTextField.CENTER);
            column.setHorizontalAlignment(JTextField.CENTER);
            line.setMaximumSize(new Dimension(100, 20));
            p.add(line);
            this.addCenteredComponent(p, new JLabel(Language.getText("column message")));
            column.setMaximumSize(new Dimension(100, 20));
            p.add(Box.createVerticalGlue());
            p.add(column);
            p.add(Box.createVerticalGlue());
            p.add(this.createValidButton(line, column));
        } else {
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(3, 3));
            ActionListener action = this.boardListeners.easyModeListener(board);
            String[] jButtons = {"nw", "n", "ne", "w", "e", "sw", "s", "se"};
            for (int i = 0; i < jButtons.length; i++) {
                JButton button = new JButton();
                button.setName(jButtons[i]);
                button.addActionListener(action);
                button.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/arrows/"
                        + button.getName() + ".png")).getScaledInstance(Element.getSizeUnity(), Element.getSizeUnity(), Image.SCALE_DEFAULT)));
                if (i == 4) buttons.add(this.pawn);
                buttons.add(button);
            }
            p.add(Box.createVerticalGlue());
            p.add(buttons);
            p.add(Box.createVerticalGlue());
        }
        p.add(this.information);
        return p;
    }

    /**
     * Allows to get the number of the pawn in the pawn JTextField
     */
    public void getSelectedPawn() {
        try {
            coordinates[0] = Integer.parseInt(pawn.getText());
        } catch (Exception ex) {
            char letter = 'a';
            if (pawn.getText().length() == 1) letter = pawn.getText().charAt(0);
            if (letter == 'z') coordinates[0] = -1;
            else coordinates[0] = -2;
        }
    }

    /**
     * Allows to create the panel that will display the history
     *
     * @return the history panel
     */
    private JPanel createHistoryPanel() {
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.add(new JLabel(Language.getText("history")));
        historyPanel.setMinimumSize(new Dimension(50, 100));
        return historyPanel;
    }

    /**
     * Allows to create a vertical panel that shows information about the players
     * such as their name and the color they choose.
     *
     * @param game the current game
     * @return the panel displaying information on players
     */
    private JPanel createPlayersInfoPanel(Game game) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 1));
        JLabel color1 = new JLabel() {
            @Override
            public void paintComponent(Graphics graphics) {
                graphics.setColor(game.getFirstPlayer().getCOLOR());
                drawPlayerPawn(graphics, this.getWidth());
            }


        };
        JLabel color2 = new JLabel() {
            @Override
            public void paintComponent(Graphics graphics) {
                graphics.setColor(game.getSecondPlayer().getCOLOR());
                drawPlayerPawn(graphics, this.getWidth());
            }
        };
        JLabel info = new JLabel(Language.getText("infoPanel"));
        JLabel name1 = new JLabel(game.getFirstPlayer().getNAME());
        JLabel name2 = new JLabel(game.getSecondPlayer().getNAME());
        info.setHorizontalAlignment(SwingConstants.CENTER);
        name1.setHorizontalAlignment(SwingConstants.CENTER);
        name2.setHorizontalAlignment(SwingConstants.CENTER);
        infoPanel.add(info);
        infoPanel.add(name1);
        infoPanel.add(color1);
        infoPanel.add(name2);
        infoPanel.add(color2);
        panel.add(infoPanel, BorderLayout.CENTER);
        JLabel label = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/logo.png"))//ImageIO.read(new File("src/pictures/logo.png"))
                .getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label, BorderLayout.PAGE_END);
        return panel;
    }

    /**
     * Allows to draw the pawn of one player, used to create the panel
     * with the players information
     *
     * @param graphics the graphics of the component to draw the pawn on
     * @param width    the width of the component
     */
    private void drawPlayerPawn(Graphics graphics, int width) {
        graphics.fillOval((int) (width / 2 - 0.375 * Element.getSizeUnity()), 0,
                (int) (Element.getSizeUnity() * 0.75), (int) (Element.getSizeUnity() * 0.75));
        graphics.setColor(Color.BLACK);
        graphics.drawOval((int) (width / 2 - 0.375 * Element.getSizeUnity()), 0,
                (int) (Element.getSizeUnity() * 0.75), (int) (Element.getSizeUnity() * 0.75));
    }

    /**
     * Allows to create the menu concerning the game (save the game
     * and replay it)
     *
     * @param game    the current game
     * @param menuBar the JMenuBar on which the menu will be add
     */
    private void createGameButton(Game game, JMenuBar menuBar) {
        JMenu menu = new JMenu(Language.getText("game"));
        JMenuItem save = new JMenuItem(Language.getText("save"));
        JMenuItem saveAs = new JMenuItem(Language.getText("save as"));
        JMenuItem restart = new JMenuItem(Language.getText("restart"));
        ActionListener gameListener = this.listeners.gameListener(save, saveAs, restart, game);
        save.addActionListener(gameListener);
        saveAs.addActionListener(gameListener);
        restart.addActionListener(gameListener);
        menu.add(save);
        menu.add(saveAs);
        menu.add(restart);
        menuBar.add(menu);
    }

    /**
     * Allows to create about the menu options (returning to the menu
     * and quit the application).
     *
     * @param menuBar the JMenuBar on which the menu will be add
     */
    private void createMenuButton(JMenuBar menuBar) {
        JMenu menu = new JMenu("Menu");
        JMenuItem showMenu = new JMenuItem(Language.getText("back menu"));
        JMenuItem quit = new JMenuItem(Language.getText("quit"));
        ActionListener listener = this.listeners.menuListener(quit, showMenu);
        showMenu.addActionListener(listener);
        quit.addActionListener(listener);
        menu.add(showMenu);
        menu.add(quit);
        menuBar.add(menu);
    }

    /**
     * Allows to create the menu with the settings options (allows to change the
     * language, the scheme and to turn on/off the sound)
     *
     * @param game    the current game (used to repaint the frame if the scheme is changed)
     * @param board   the board to display (used to repaint the frame if the scheme is changed)
     * @param level   the level of the game (used to repaint the frame if the scheme is changed)
     * @param symbols the symbols to display (used to repaint the frame if the scheme is changed)
     * @param menuBar the JMenuBar on which the menu will be add
     */
    private void createSettingsButton(Game game, Element[][] board, Level level, ChineseSymbol[][] symbols, JMenuBar menuBar) {
        JMenu menu = new JMenu(Language.getText("settings"));
        JMenu language = new JMenu(Language.getText("language")) {
            @Override
            protected void paintComponent(Graphics g) {
                Image i;
                if (Language.getLanguage() == Language.Languages.FRENCH)
                    i = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/france.png"));//file = new File("pictures/france.png");
                else
                    i = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/english.png"));//file = new File("pictures/english.png");
                this.setIcon(new ImageIcon(i.getScaledInstance(20, 10, Image.SCALE_DEFAULT)));
                super.paintComponent(g);
            }
        };
        JMenuItem french = new JMenuItem(Language.getText("fr"));
        JMenuItem english = new JMenuItem(Language.getText("en"));
        language.add(french);
        language.add(english);
        JMenu scheme = new JMenu(Language.getText("scheme"));
        for (SwitchScheme.Scheme s : SwitchScheme.Scheme.values()) {
            JMenuItem item = new JMenuItem(Language.getText(s.name()));
            item.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwitchScheme.switchScheme(s);
                    repaintFrame(game, board, level, symbols);
                }
            });
            scheme.add(item);
        }
        JMenuItem sound = new JMenuItem(Language.getText("sound")) {
            @Override
            public void paintComponent(Graphics graphics) {
                Image i;
                if (Sound.isOn())
                    i = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/soundOn.png"));
                else
                    i = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/soundOff.png"));
                this.setIcon(new ImageIcon(i.getScaledInstance(20,
                        20, Image.SCALE_DEFAULT)));
                super.paintComponent(graphics);
            }
        };
        ActionListener listener = this.listeners.settingsListener(english, french, sound, board, game, level, symbols);
        english.addActionListener(listener);
        french.addActionListener(listener);
        sound.addActionListener(listener);
        menu.add(scheme);
        menu.add(language);
        menu.add(sound);
        menuBar.add(menu);
    }

    /**
     * Allows to create the menu with the help options (show the rules
     * and a panel that explains the main features of the application)
     *
     * @param menuBar the JMenuBar on which the menu will be add
     */
    private void createHelpButton(JMenuBar menuBar) {
        JMenu menu = new JMenu();
        menu.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/pictures/help/help.png"))));//"pictures/help/help.png"));
        JMenuItem rules = new JMenuItem(Language.getText("rules"));
        JMenuItem help = new JMenuItem(Language.getText("help"));
        ActionListener listener = this.listeners.helpListener(rules, help);
        rules.addActionListener(listener);
        help.addActionListener(listener);
        menu.add(rules);
        menu.add(help);
        menuBar.add(menu);
    }

    /**
     * Allows to create and to show a frame with the main
     * features of the application and a quick explanation about displacements
     * in graphic mode.
     */
    public void showHelpFrame() {
        JFrame frame = new JFrame("Zen l'Initié");
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
        frame.setBounds(new Rectangle(1000, 600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
        GraphicalInterface.this.getContentPane().removeAll();
        GraphicalInterface.this.setJMenuBar(null);
        GraphicalInterface.this.showFrame(game, board, level, symbols);
        GraphicalInterface.this.revalidate();
        GraphicalInterface.this.repaint();
    }

    /**
     * Allows to add a label at the center of a panel
     *
     * @param panel the panel to add the component into
     * @param label the label to be add
     */
    private void addCenteredComponent(JPanel panel, JLabel label) {
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(label);
    }

    /**
     * Allows to get the input coordinates
     *
     * @return the move coordinates in format [pawn, line, column]
     */
    public int[] getCoordinates() {
        return this.coordinates;
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
        this.information.getParent().revalidate();
        this.information.getParent().repaint();
        this.revalidate();
    }

    /**
     * Allows to add an history line in the history panel.
     *
     * @param player the player who played
     * @param move   the coordinates of the move to add
     */
    protected void addHistory(Player player, int[] move) {
        JLabel label = new JLabel(player.getNAME() + " " + Language.getText("moved") + " " +
                move[0] + " " + Language.getText("to line") + " " + move[1] + " " +
                Language.getText("to column") + " " + move[2]);
        this.historyPanel.add(label);
        if (this.historyPanel.getComponentCount() - 1 > 15) {
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
        this.playerName.setText(player.getNAME());
        this.repaint();
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
     * Allows to get the Pawn TextField (to create the demo)
     *
     * @return the pawn field
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

}