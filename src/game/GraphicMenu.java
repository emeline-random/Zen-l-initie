package Game;

import ArtificialPlayers.ArtificialPlayer;
import ArtificialPlayers.FirstLevel;
import ArtificialPlayers.SecondLevel;
import Utilities.GameColor;
import Utilities.Language;
import Utilities.RoundRectButton;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class GraphicMenu extends JFrame {

    private JPanel centerPanel;
    private JPanel pageEndPanel;
    private Game game;
    private int menu = 0;
    private boolean startGame = false;
    private boolean restart = false;
    private static final Element LOCK = new Element();
    private static final Color LIGHTCOLOR = new Color(230, 216, 202);
    private static final Color DARKCOLOR = new Color(26, 14, 5);
    private static final Font FONT = new Font("Arial Rounded MT Bold", Font.PLAIN, 17);

    public void showMenu() {
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        SwingUtilities.invokeLater(this::configFrame);
        try {
            synchronized (LOCK) {
                LOCK.wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (this.startGame) this.game.beginGame();
        else if (this.restart) this.game.restartGame();
        else showMenu();
    }

    private void showGlobalMenu() {
        this.menu = 0;
        this.remove(this.centerPanel);
        this.remove(this.pageEndPanel);
        JButton newGame = new RoundRectButton(Language.getText("new"), 10);
        JButton resumeGame = new RoundRectButton(Language.getText("resume"), 10);
        JButton rules = new RoundRectButton(Language.getText("see rules"), 10);
        newGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameModeSelection();
            }
        });
        resumeGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getContentPane().removeAll();
                try {
                    FileInputStream stream = new FileInputStream("game.ser");
                    ObjectInputStream file = new ObjectInputStream(stream);
                    GraphicMenu.this.game = (Game) file.readObject();
                    GraphicMenu.this.restart = true;
                    file.close();
                    dispose();
                } catch (ClassNotFoundException | IOException ex) {
                    JOptionPane.showMessageDialog(null, Language.getText("save game error"), "Zen l'Initié", JOptionPane.ERROR_MESSAGE);
                } finally {
                    synchronized (LOCK) {
                        LOCK.notify();
                    }
                }
            }
        });
        rules.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRules();
            }
        });
        this.centerPanel = new JPanel();
        newGame.setBackground(DARKCOLOR);
        newGame.setForeground(LIGHTCOLOR);
        resumeGame.setBackground(DARKCOLOR);
        resumeGame.setForeground(LIGHTCOLOR);
        rules.setForeground(LIGHTCOLOR);
        rules.setBackground(DARKCOLOR);
        rules.setFont(FONT);
        newGame.setFont(FONT);
        resumeGame.setFont(FONT);
        this.centerPanel.add(newGame);
        this.centerPanel.add(resumeGame);
        this.pageEndPanel = new JPanel();
        this.pageEndPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        this.pageEndPanel.add(rules);
        this.pageEndPanel.setOpaque(false);
        this.centerPanel.setOpaque(false);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.add(this.pageEndPanel, BorderLayout.PAGE_END);
        this.setVisible(true);
    }

    private void gameModeSelection() {
        this.menu = 1;
        this.remove(this.centerPanel);
        this.centerPanel = new JPanel();
        JButton onePlayer = new RoundRectButton("1 " + Language.getText("player"), 10);
        JButton twoPlayers = new RoundRectButton("2 " + Language.getText("players"), 10);
        onePlayer.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfigurationMenu(1);
            }
        });
        twoPlayers.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showConfigurationMenu(2);
            }
        });
        onePlayer.setBackground(DARKCOLOR);
        twoPlayers.setBackground(DARKCOLOR);
        twoPlayers.setForeground(LIGHTCOLOR);
        onePlayer.setForeground(LIGHTCOLOR);
        onePlayer.setFont(FONT);
        twoPlayers.setFont(FONT);
        this.centerPanel.add(onePlayer);
        this.centerPanel.add(twoPlayers);
        this.centerPanel.setOpaque(false);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.revalidate();
    }

    private void showConfigurationMenu(int playerNumber) {
        this.menu = playerNumber + 1;
        this.remove(this.centerPanel);
        this.remove(this.pageEndPanel);
        this.centerPanel = new JPanel();
        JLabel mode = new JLabel("mode");
        mode.setFont(FONT);
        mode.setHorizontalAlignment(SwingConstants.CENTER);
        JCheckBoxMenuItem graphicBox = new JCheckBoxMenuItem(Language.getText("graphic"), true);
        JCheckBoxMenuItem consoleBox = new JCheckBoxMenuItem(Language.getText("console"), false);
        JCheckBox level = new JCheckBox(Language.getText("displacement help"));
        DefaultComboBoxModel<ArtificialPlayer> playersModel = new DefaultComboBoxModel<>(new ArtificialPlayer[]{new FirstLevel(), new SecondLevel()});
        JComboBox<ArtificialPlayer> playerJComboBox = new JComboBox<>(playersModel);

        JLabel player1 = new JLabel(Language.getText("player1"));
        player1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        player1.setHorizontalAlignment(SwingConstants.CENTER);
        player1.setFont(FONT);
        JLabel player2 = new JLabel(Language.getText("player2"));
        player2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        player2.setHorizontalAlignment(SwingConstants.CENTER);
        player2.setFont(FONT);
        JTextField name1 = new JTextField(Language.getText("name"));
        JTextField name2 = new JTextField(Language.getText("name"));

        DefaultComboBoxModel<GameColor> model1 = new DefaultComboBoxModel<>(GameColor.getColors());
        DefaultComboBoxModel<GameColor> model2 = new DefaultComboBoxModel<>(GameColor.getColors());
        JComboBox<GameColor> colors1 = new JComboBox<>(model1);
        JComboBox<GameColor> colors2 = new JComboBox<>(model2);

        JButton button = new RoundRectButton(Language.getText("start"), 10);
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    if (playerNumber == 2) secondPlayer = new Player(name2.getText(), (GameColor) colors2.getSelectedItem());
                    else secondPlayer = (ArtificialPlayer) playerJComboBox.getSelectedItem();
                    GraphicMenu.this.game = new Game(mode1, firstPlayer, secondPlayer, level1);
                    startGame = true;
                    dispose();
                    synchronized (LOCK) {
                        LOCK.notify();
                    }
                }
            }
        });
        button.setFont(FONT);
        graphicBox.setBackground(LIGHTCOLOR);
        consoleBox.setBackground(LIGHTCOLOR);
        graphicBox.setBorderPainted(false);
        consoleBox.setBorderPainted(false);
        level.setBackground(LIGHTCOLOR);
        if (playerNumber == 2) this.centerPanel.setLayout(new GridLayout(4, 3));
        else this.centerPanel.setLayout(new GridLayout(3, 3));
        this.centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        this.centerPanel.add(mode);
        this.centerPanel.add(graphicBox);
        this.centerPanel.add(consoleBox);
        this.centerPanel.add(player1);
        this.centerPanel.add(name1);
        this.centerPanel.add(colors1);
        if (playerNumber == 2) {
            this.centerPanel.add(player2);
            this.centerPanel.add(name2);
            this.centerPanel.add(colors2);
            this.centerPanel.add(new JLabel());
        } else {
            JLabel adverse = new JLabel(Language.getText("adverse level"));
            adverse.setFont(FONT);
            adverse.setHorizontalAlignment(SwingConstants.CENTER);
            this.centerPanel.add(adverse);
            this.centerPanel.add(playerJComboBox);
        }
        this.centerPanel.add(level);
        this.centerPanel.setBackground(LIGHTCOLOR);
        button.setBackground(DARKCOLOR);
        button.setForeground(LIGHTCOLOR);
        button.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 25));
        this.pageEndPanel = new JPanel();
        this.pageEndPanel.add(button);
        this.pageEndPanel.setOpaque(false);
        if (playerNumber == 1) this.pageEndPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));
        else this.pageEndPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.add(this.pageEndPanel, BorderLayout.PAGE_END);
        this.revalidate();
    }

    private void showRules() {
        JOptionPane.showMessageDialog(this, "come later", "Zen l'Initié - " + Language.getText("rules"), JOptionPane.INFORMATION_MESSAGE);
    }

    public void configFrame() {
        this.setSize(800, 520);
        try {
            this.setIconImage(ImageIO.read(new File("pictures/logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setContentPane(new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                try {
                    g.drawImage(ImageIO.read(new File("pictures/background.png")).getScaledInstance(GraphicMenu.this.getWidth(),
                            GraphicMenu.this.getHeight(), Image.SCALE_DEFAULT), 0, 0, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        JPanel upperPanel = new JPanel();
        this.centerPanel = new JPanel();
        this.pageEndPanel = new JPanel();
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        upperPanel.setLayout(new GridBagLayout());
        JLabel spaceLabel = new JLabel();
        spaceLabel.setOpaque(false);
        spaceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 240, 0));
        JButton home = new JButton();
        home.setBackground(DARKCOLOR);
        try {
            home.setIcon(new ImageIcon(ImageIO.read(new File("pictures/home_logo.png"))
                    .getScaledInstance(30, 30, Image.SCALE_DEFAULT)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        home.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(centerPanel);
                remove(pageEndPanel);
                showGlobalMenu();
            }
        });
        home.setFocusPainted(false);
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
        this.pageEndPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 10, 0));
        this.add(upperPanel, BorderLayout.PAGE_START);
        this.setLocationRelativeTo(null);
        this.showGlobalMenu();
    }

    private JPanel createLanguagePanel(){
        JButton fr = new JButton(new ImageIcon("pictures/france.png"));
        JButton en = new JButton(new ImageIcon("pictures/english.png"));
        fr.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Language.setLanguage("fr");
                GraphicMenu.this.changeLanguage(menu);
            }
        });
        en.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Language.setLanguage("en");
                GraphicMenu.this.changeLanguage(menu);
            }
        });
        fr.setBorderPainted(false);
        en.setBorderPainted(false);
        fr.setContentAreaFilled(false);
        en.setContentAreaFilled(false);
        JPanel languages = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                g.setColor(DARKCOLOR);
                g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        fr.setSize(30, 20);
        en.setSize(30, 20);
        languages.setPreferredSize(new Dimension(90, 40));
        languages.add(fr);
        languages.add(en);
        languages.setOpaque(false);
        return languages;
    }

    private void changeLanguage(int i){
        switch (i){
            case 0:
                this.showGlobalMenu();
                break;
            case 1:
                this.gameModeSelection();
                break;
            case 2:
                this.showConfigurationMenu(1);
                break;
            case 3:
                this.showConfigurationMenu(2);
                break;
        }
    }
}
