package Game;

import Utilities.InputUtilities;
import Utilities.Language;
import Utilities.MatrixUtilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class GraphicalInterface extends JFrame {

    private Element[][] board;
    private Level level;
    transient private ChineseSymbol[][] symbols;
    private Player player;
    private JTextField pawn;
    private JTextField line;
    private JTextField column;
    private JLabel playerName;
    private JLabel information;
    private int[] coordinates;
    private JButton valid;
    private boolean backToMenu = false;//TODO faire ça propre en fonction du level choisi et pas tout recréer à chaque fois
    private JMenuBar bar;
    private final Color BACKGROUND = Color.white;//new Color(230, 216, 202);//GameColor.WHITE;
    private final static int MENU_SIZE = 60;

    public GraphicalInterface(Game game, Level level) {
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
            this.board = game.getGameBoard().getBoard();
            this.symbols = game.getGameBoard().getSymbols();
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
        this.createPawnsPanel();
        this.createInputPanel();
        this.createBoardListener();
        this.createQuitButton(game);
        this.createMenuButton();
        this.add(this.bar, BorderLayout.PAGE_START);
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
                try {
                    coordinates[0] = Integer.parseInt(pawn.getText());
                } catch (Exception ex) {
                    char letter = 'a';
                    if (pawn.getText().length() == 1) letter = pawn.getText().charAt(0);
                    if (letter == 'z') coordinates[0] = -1;
                    else coordinates[0] = -2;
                }
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
        this.valid.setFocusable(true);
    }

    protected void sendCoordinates() {
        if ((coordinates[0] < Player.getPAWNS_NUMBER() && coordinates[0] >= 0 || coordinates[0] == -1) && player.pawnAlive(coordinates[0])
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

    private void createPawnsPanel() {
        JPanel pawnsPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (this.getHeight() < this.getWidth()) {
                    Element.sizeUnity = (int) ((this.getHeight() * 0.95 - MENU_SIZE) / GameBoard.getDIMENSION());
                } else {
                    Element.sizeUnity = (int) ((this.getWidth() * 0.95 - MENU_SIZE) / GameBoard.getDIMENSION());
                }
                Pawn.setDiameter(Element.sizeUnity - Element.sizeUnity / 4);
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
                    g.drawLine(Element.sizeUnity, (i + 1) * Element.sizeUnity,
                            Element.sizeUnity * (board.length + 1), (i + 1) * Element.sizeUnity);
                    g.drawLine((i + 1) * Element.sizeUnity, Element.sizeUnity,
                            (i + 1) * Element.sizeUnity, Element.sizeUnity * (board.length + 1));
                }
                String[] strings = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"};
                for (int i = 0; i < GameBoard.getDIMENSION(); i++) {
                    g.drawString(strings[i], (int) ((i + 1.25) * Element.sizeUnity), (int) (Element.sizeUnity * 0.75));
                    g.drawString(Integer.toString(i), (int) (Element.sizeUnity * 0.5), (int) ((i + 1.75) * Element.sizeUnity));
                }
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(Element.sizeUnity * (GameBoard.getDIMENSION() + 2), Element.sizeUnity * (GameBoard.getDIMENSION() + 2));
            }
        };
        pawnsPanel.setBackground(this.BACKGROUND);
        this.add(pawnsPanel, BorderLayout.CENTER);
    }

    protected void createQuitButton(Game game) {
        JMenuItem item = new JMenuItem(Language.getText("save menu"));
        item.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int quit = JOptionPane.showConfirmDialog(null, Language.getText("quit confirmation"),
                        Language.getText("quit"), JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (quit == JOptionPane.YES_OPTION) {
                    dispose();
                    game.saveAndCloseGame();
                }
            }
        });
        item.setForeground(this.BACKGROUND);
        item.setBackground(new Color(26, 14, 5));
        this.bar.add(item);
        this.revalidate();
    }

    protected void createMenuButton() {
        JMenuItem item = new JMenuItem("Menu");
        item.addActionListener(new AbstractAction() {
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
        item.setForeground(this.BACKGROUND);
        item.setBackground(new Color(26, 14, 5));
        this.bar.add(item);
        this.revalidate();
    }

    protected void createBoardListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (player != null) {
                    boolean found = false;
                    int i = 0;
                    while (!found && i < player.getPawns().size()) {
                        if (player.getPawns().get(i).getRectangle().contains(e.getX(), e.getY() - MENU_SIZE)) {
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
                                if (board[i][j].getRectangle().contains(e.getX(), e.getY() - MENU_SIZE)) {
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

    private void createInputPanel() {
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
                    try {
                        coordinates[0] = Integer.parseInt(pawn.getText());
                    } catch (Exception ex) {
                        char letter = 'a';
                        if (pawn.getText().length() == 1) letter = pawn.getText().charAt(0);
                        if (letter == 'z') coordinates[0] = -1;
                        else coordinates[0] = -2;
                    }
                    if (coordinates[0] < Player.getPAWNS_NUMBER() && coordinates[0] >= 0 || coordinates[0] == -1) {
                        Pawn p = GameBoard.findPawn(coordinates[0], player);
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
                            .getScaledInstance(Element.sizeUnity, Element.sizeUnity, Image.SCALE_DEFAULT)));
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
        try {
            JLabel label = new JLabel(new ImageIcon(ImageIO.read(new File("pictures/logo.png"))
                    .getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
            label.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            p.add(label);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.setBackground(this.BACKGROUND);
        this.add(p, BorderLayout.LINE_END);
    }

    public int[] getCoordinates() {
        return this.coordinates;
    }

    public void addInformation(String text, boolean clear) {
        if (clear) this.information.setText(text);
        else this.information.setText(this.information.getText() + text);
    }

    public void setPlayer(Player player) {
        this.player = player;
        this.playerName.setText(player.getNAME());
        this.repaint();
    }

    public boolean isBackToMenu() {
        return this.backToMenu;
    }
}
