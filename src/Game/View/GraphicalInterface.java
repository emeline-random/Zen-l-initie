package Game.View;

import Game.Controllers.Game;
import Game.Model.GameBoard;
import Game.bin.*;
import Utilities.InputUtilities;
import Utilities.Language;
import Utilities.MatrixUtilities;
import Utilities.Sound;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphicalInterface extends JFrame {

    private final Element[][] board;
    private final Level level;
    final transient private ChineseSymbol[][] symbols;
    private Player player;
    private final JTextField pawn;
    private JTextField line;
    private JTextField column;
    private final JLabel playerName;
    private final JLabel information;
    private JPanel historyPanel;
    private final int[] coordinates;
    private JButton valid;
    private boolean backToMenu = false;
    private boolean restartGame = false;
    private JMenuBar bar;

    public GraphicalInterface(Game game, Level level, Element[][] board, ChineseSymbol[][] symbols) {
        SwitchScheme.switchScheme(SwitchScheme.Scheme.LIGHT);
        if (game != null && level != null) {
            this.level = level;
            this.pawn = new JTextField(3);
            this.pawn.setHorizontalAlignment(JTextField.CENTER);
            this.playerName = new JLabel(Language.getText("name"));
            if (level == Level.HARD) {
                this.line = new JTextField(3);
                this.line.setHorizontalAlignment(JTextField.CENTER);
                this.column = new JTextField(3);
                this.column.setHorizontalAlignment(JTextField.CENTER);
            }
            this.information = new JLabel();
            this.coordinates = new int[3];
            this.bar = new JMenuBar();
            this.board = board;
            this.symbols = symbols;
            showFrame(game);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void showFrame(Game game) {
        try {
            this.setIconImage(ImageIO.read(new File("pictures/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.configFrame();
        JPanel pawnsPanel = this.createPawnsPanel();
        JPanel inputPanel = this.createInputPanel();
        JPanel infoPanel = this.createPlayersInfoPanel(game);
        this.historyPanel = this.createHistoryPanel();
        JSplitPane rightPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, this.historyPanel);
        JSplitPane leftPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, infoPanel, pawnsPanel);
        leftPanel.setOneTouchExpandable(true);
        rightPanel.setOneTouchExpandable(true);
        this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel), BorderLayout.CENTER);
        this.createBoardListener(pawnsPanel);
        this.createMenuButton();
        this.createGameButton(game);
        this.createSettingsButton(game);
        this.createHelpButton();
        this.add(this.bar, BorderLayout.PAGE_START);
        this.pawn.requestFocus();
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void configFrame() {
        this.setTitle("Zen l'initié");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800, 500));
        this.setLayout(new BorderLayout());
    }

    private void createValidButton() {
        this.valid = new JButton("go");
        this.valid.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getSelectedPawn();
                try {
                    coordinates[1] = Integer.parseInt(line.getText());
                    if (column.getText().length() == 1)
                        coordinates[2] = InputUtilities.charToInt(column.getText().charAt(0));
                    else coordinates[2] = -1;
                } catch (Exception ex) {
                    coordinates[1] = -1;
                    coordinates[2] = -1;
                }
                sendCoordinates();
            }
        });
        this.valid.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.valid.setFocusable(true);
    }

    protected void sendCoordinates() {
        if ((coordinates[0] < Player.getPAWNS_NUMBER() && coordinates[0] >= 0 || coordinates[0] == -1) && player.getPawn(coordinates[0]) != null
                && coordinates[1] < GameBoard.getDIMENSION() && coordinates[1] >= 0 && coordinates[2] < GameBoard.getDIMENSION() && coordinates[2] >= 0) {
            synchronized (getCoordinates()) {
                getCoordinates().notify();
            }
            pawn.setText("");
            if (this.level == Level.HARD) {
                line.setText("");
                column.setText("");
            }
            information.setText("");
        } else {
            addInformation(Language.getText("entries error"), true);
        }
    }

    private JPanel createPawnsPanel() {
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
        };
    }

    private JPanel createInputPanel() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        this.playerName.setAlignmentX(Component.CENTER_ALIGNMENT);
        p.add(this.playerName);
        this.playerName.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        if (this.level == Level.HARD) {
            p.add(this.playerName);
            this.addCenteredComponent(p, new JLabel(Language.getText("pawn message")));
            p.add(Box.createVerticalGlue());
            this.pawn.setMaximumSize(new Dimension(100, 20));
            p.add(this.pawn);
            this.addCenteredComponent(p, new JLabel(Language.getText("line message")));
            p.add(Box.createVerticalGlue());
            this.line.setMaximumSize(new Dimension(100, 20));
            p.add(this.line);
            this.addCenteredComponent(p, new JLabel(Language.getText("column message")));
            this.column.setMaximumSize(new Dimension(100, 20));
            p.add(Box.createVerticalGlue());
            p.add(this.column);
            this.createValidButton();
            p.add(Box.createVerticalGlue());
            p.add(this.valid);
        } else {
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(3, 3));
            AbstractAction action = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    getSelectedPawn();
                    if (coordinates[0] < Player.getPAWNS_NUMBER() && coordinates[0] >= 0 || coordinates[0] == -1) {
                        Pawn p = player.getPawn(coordinates[0]);
                        coordinates[1] = p.getLineIndex();
                        coordinates[2] = p.getColumnIndex();
                        switch (((JButton) e.getSource()).getName()) {
                            case "e":
                                coordinates[2] = p.getColumnIndex() + MatrixUtilities.countObjectLine(board, p.getLineIndex());
                                break;
                            case "w":
                                coordinates[2] = p.getColumnIndex() - MatrixUtilities.countObjectLine(board, p.getLineIndex());
                                break;
                            case "n":
                                coordinates[1] = p.getLineIndex() - MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                                break;
                            case "s":
                                coordinates[1] = p.getLineIndex() + MatrixUtilities.countObjectColumn(board, p.getColumnIndex());
                                break;
                            case "ne":
                                int i = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                                coordinates[1] = p.getLineIndex() - i;
                                coordinates[2] = p.getColumnIndex() + i;
                                break;
                            case "nw":
                                i = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                                coordinates[1] = p.getLineIndex() - i;
                                coordinates[2] = p.getColumnIndex() - i;
                                break;
                            case "se":
                                i = MatrixUtilities.countObjectDiagDesc(board, p.getLineIndex(), p.getColumnIndex());
                                coordinates[1] = p.getLineIndex() + i;
                                coordinates[2] = p.getColumnIndex() + i;
                                break;
                            case "sw":
                                i = MatrixUtilities.countObjectDiagAsc(board, p.getLineIndex(), p.getColumnIndex());
                                coordinates[1] = p.getLineIndex() + i;
                                coordinates[2] = p.getColumnIndex() - i;
                                break;
                        }
                    }
                    sendCoordinates();
                }
            };
            String[] jButtons = {"nw", "n", "ne", "w", "e", "sw", "s", "se"};
            for (int i = 0; i < jButtons.length; i++) {
                JButton button = new JButton();
                button.setName(jButtons[i]);
                button.addActionListener(action);
                try {
                    button.setIcon(new ImageIcon(ImageIO.read(new File("pictures/arrows/" + button.getName() + ".png"))
                            .getScaledInstance(Element.getSizeUnity(), Element.getSizeUnity(), Image.SCALE_DEFAULT)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

    private void getSelectedPawn() {
        try {
            coordinates[0] = Integer.parseInt(pawn.getText());
        } catch (Exception ex) {
            char letter = 'a';
            if (pawn.getText().length() == 1) letter = pawn.getText().charAt(0);
            if (letter == 'z') coordinates[0] = -1;
            else coordinates[0] = -2;
        }
    }

    private JPanel createHistoryPanel() {
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));
        historyPanel.add(new JLabel(Language.getText("history")));
        historyPanel.setMinimumSize(new Dimension(50, 100));
        return historyPanel;
    }

    private JPanel createPlayersInfoPanel(Game game) {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(6, 1));
        JLabel color1 = new JLabel() {
            @Override
            public void paintComponent(Graphics graphics) {
                graphics.setColor(game.getFirstPlayer().getColor());
                drawPlayerPawn(graphics, this.getWidth());
            }


        };
        JLabel color2 = new JLabel() {
            @Override
            public void paintComponent(Graphics graphics) {
                graphics.setColor(game.getSecondPlayer().getColor());
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
        try {
            JLabel label = new JLabel(new ImageIcon(ImageIO.read(new File("pictures/logo.png"))
                    .getScaledInstance(150, 150, Image.SCALE_DEFAULT)));
            label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label, BorderLayout.PAGE_END);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return panel;
    }

    private void drawPlayerPawn(Graphics graphics, int width) {
        graphics.fillOval((int) (width / 2 - 0.375 * Element.getSizeUnity()), 0,
                (int) (Element.getSizeUnity() * 0.75), (int) (Element.getSizeUnity() * 0.75));
        graphics.setColor(Color.BLACK);
        graphics.drawOval((int) (width / 2 - 0.375 * Element.getSizeUnity()), 0,
                (int) (Element.getSizeUnity() * 0.75), (int) (Element.getSizeUnity() * 0.75));
    }

    private void createGameButton(Game game) {
        JMenu menu = new JMenu(Language.getText("game"));
        JMenuItem save = new JMenuItem(Language.getText("save"));
        JMenuItem saveAs = new JMenuItem(Language.getText("save as"));
        JMenuItem restart = new JMenuItem(Language.getText("restart"));
        save.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.saveGame();
            }
        });
        saveAs.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                game.saveGameAs();
            }
        });
        restart.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                restartGame = true;
                synchronized (getCoordinates()) {
                    getCoordinates().notify();
                }
            }
        });
        menu.add(save);
        menu.add(saveAs);
        menu.add(restart);
        this.bar.add(menu);
        this.revalidate();
    }

    private void createMenuButton() {
        JMenu menu = new JMenu("Menu");
        JMenuItem showMenu = new JMenuItem(Language.getText("back menu"));
        JMenuItem quit = new JMenuItem(Language.getText("quit"));
        showMenu.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int menu = JOptionPane.showConfirmDialog(null, Language.getText("menu confirmation"),
                        "Menu", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (menu == JOptionPane.YES_OPTION) {
                    setVisible(false);
                    dispose();
                    backToMenu = true;
                    synchronized (getCoordinates()) {
                        getCoordinates().notify();
                    }
                }
            }
        });
        quit.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(showMenu);
        menu.add(quit);
        this.bar.add(menu);
        this.revalidate();
    }

    private void createSettingsButton(Game game) {
        JMenu menu = new JMenu(Language.getText("settings"));
        JMenu language = new JMenu(Language.getText("language")){
            @Override
            protected void paintComponent(Graphics g){
                File file;
                if (Language.getLanguage() == Language.Languages.FRENCH) file = new File("pictures/france.png");
                else file = new File("pictures/english.png");
                try {
                    this.setIcon(new ImageIcon(ImageIO.read(file).getScaledInstance(20,
                            10, Image.SCALE_DEFAULT)));
                } catch (IOException e) {
                    this.setText(Language.getText("language"));
                } finally {
                    super.paintComponent(g);
                }
            }
        };
        JMenuItem french = new JMenuItem(Language.getText("fr"));
        JMenuItem english = new JMenuItem(Language.getText("en"));
        french.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Language.setLanguage(Language.Languages.FRENCH);
                repaintFrame(game);
            }
        });
        english.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Language.setLanguage(Language.Languages.ENGLISH);
                repaintFrame(game);
            }
        });
        language.add(french);
        language.add(english);
        JMenu scheme = new JMenu(Language.getText("scheme"));
        for (SwitchScheme.Scheme s : SwitchScheme.getSchemes()) {
            JMenuItem item = new JMenuItem(Language.getText(s.name()));
            item.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SwitchScheme.switchScheme(s);
                    repaintFrame(game);
                }
            });
            scheme.add(item);
        }
        JMenuItem sound = new JMenuItem(Language.getText("sound")) {
            @Override
            public void paintComponent(Graphics graphics) {
                File file;
                if (Sound.isOn()) file = new File("pictures/soundOn.png");
                else file = new File("pictures/soundOff.png");
                try {
                    this.setIcon(new ImageIcon(ImageIO.read(file).getScaledInstance(20,
                            20, Image.SCALE_DEFAULT)));
                } catch (IOException e) {
                    this.setText("sound");
                } finally {
                    super.paintComponent(graphics);
                }
            }
        };
        sound.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.setOn(!Sound.isOn());
                sound.repaint();
            }
        });
        menu.add(scheme);
        menu.add(language);
        menu.add(sound);
        this.bar.add(menu);
        this.revalidate();
    }

    private void createHelpButton(){
        JMenu menu = new JMenu();
        menu.setIcon(new ImageIcon("pictures/help/help.png"));
        JMenuItem rules = new JMenuItem(Language.getText("rules"));
        rules.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicMenu.showRules();
            }
        });
        JMenuItem help = new JMenuItem(Language.getText("help"));
        help.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelpFrame();
            }
        });
        menu.add(rules);
        menu.add(help);
        this.bar.add(menu);
    }

    private void showHelpFrame(){
        JFrame frame = new JFrame("Zen l'Initié");
        JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html");
        pane.setEditable(false);
        try {
            byte[] encoded;
            if(Language.getLanguage() == Language.Languages.FRENCH) encoded = Files.readAllBytes(Paths.get("Languages/FrenchHelp.html"));
            else encoded = Files.readAllBytes(Paths.get("Languages/EnglishHelp.html"));
            pane.setText(new String(encoded));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.add(pane);
        frame.setBounds(new Rectangle(1000, 600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void repaintFrame(Game game) {
        GraphicalInterface.this.getContentPane().removeAll();
        GraphicalInterface.this.bar = new JMenuBar();
        GraphicalInterface.this.showFrame(game);
        GraphicalInterface.this.revalidate();
        GraphicalInterface.this.repaint();
    }

    private void createBoardListener(JPanel pawnsPanel) {
        pawnsPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (player != null) {
                    boolean found = false;
                    int i = 0;
                    while (!found && i < player.getPawns().size()) {
                        if (player.getPawns().get(i).getRectangle().contains(e.getX(), e.getY())) {
                            pawn.setText(player.getPawns().get(i).toString());
                            found = true;
                        }
                        i++;
                    }
                    if (level == Level.HARD && !found) {
                        i = 0;
                        int j;
                        while (!found && i < board.length) {
                            j = 0;
                            while (!found && j < board[i].length) {
                                if (board[i][j].getRectangle().contains(e.getX(), e.getY())) {
                                    line.setText(Integer.toString(board[i][j].getLineIndex()));
                                    column.setText(Character.toString(InputUtilities.intToChar(board[i][j].getColumnIndex())));
                                    found = true;
                                }
                                j++;
                            }
                            i++;
                        }
                    }
                }
            }
        });
    }

    private void addCenteredComponent(JPanel panel, JLabel label) {
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(label);
    }

    protected int[] getCoordinates() {
        return this.coordinates;
    }

    protected void addInformation(String text, boolean clear) {
        if (clear) this.information.setText(text);
        else this.information.setText(this.information.getText() + " " + text);
        this.information.getParent().revalidate();
        this.information.getParent().repaint();
        this.revalidate();
    }

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

    protected void setPlayer(Player player) {
        this.player = player;
        this.playerName.setText(player.getNAME());
        this.repaint();
    }

    protected boolean isBackToMenu() {
        return this.backToMenu;
    }

    protected boolean isRestartGame() {
        return this.restartGame;
    }
}
