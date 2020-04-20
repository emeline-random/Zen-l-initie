package Game.View;

import Game.Controllers.Game;
import Game.bin.ArtificialPlayers.*;
import Game.bin.Element;
import Game.bin.Level;
import Game.bin.Player;
import Utilities.GameColor;
import Utilities.Language;
import Utilities.RoundRectButton;
import Utilities.Sound;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class GraphicMenu extends JFrame {

    private JPanel centerPanel;
    private JPanel pageEndPanel;
    private Game game;
    private int menu = 0;
    private boolean startGame = false;
    private boolean restart = false;
    private static final Element LOCK = new Element();

    public void showMenu() {
        SwitchScheme.switchScheme(SwitchScheme.Scheme.MENU);
        this.setLayout(new BorderLayout());
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

    private void configFrame() {
        this.setSize(930, 535);
        this.setTitle("Zen l'Initié");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
        this.setLayout(new BorderLayout());
        this.createUpperPanel();
        this.createPageEndPanel();
        this.centerPanel = new JPanel();
        this.setLocationRelativeTo(null);
        this.showGlobalMenu();
    }

    private void createPageEndPanel() {
        this.pageEndPanel = new JPanel(new BorderLayout());
        this.pageEndPanel.setOpaque(false);
        this.pageEndPanel.add(createSoundButton(), BorderLayout.LINE_START);
    }

    private void createUpperPanel() {
        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new GridBagLayout());
        JLabel spaceLabel = new JLabel() {
            @Override
            public void paintComponent(Graphics g) {
                this.setBorder(BorderFactory.createEmptyBorder(0, 0, (int) (GraphicMenu.this.getHeight() / 2.5), 0));
            }
        };
        spaceLabel.setOpaque(false);
        spaceLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 240, 0));
        JButton home = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                try {
                    this.setIcon(new ImageIcon(ImageIO.read(new File("pictures/home_logo.png"))
                            .getScaledInstance(GraphicMenu.this.getWidth() / 18,
                                    GraphicMenu.this.getHeight() / 11, Image.SCALE_DEFAULT)));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    super.paintComponent(g);
                }
            }
        };
        home.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(centerPanel);
                remove(pageEndPanel);
                Sound.play(Sound.Sounds.buttonPressed);
                showGlobalMenu();
            }
        });
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

    private void showGlobalMenu() {
        this.menu = 0;
        this.remove(this.centerPanel);
        JButton newGame = new RoundRectButton(Language.getText("new"), 10, 5, this);
        JButton resumeGame = new RoundRectButton(Language.getText("resume"), 10, 5, this);
        JButton rules = new RoundRectButton(Language.getText("see rules"), 10, 5, this);
        newGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
                showModeSelectionMenu();
            }
        });
        resumeGame.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
                getContentPane().removeAll();
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
                        GraphicMenu.this.game = (Game) file.readObject();
                        GraphicMenu.this.restart = true;
                        file.close();
                        dispose();
                    }
                } catch (ClassNotFoundException | IOException ex) {
                    JOptionPane.showMessageDialog(null, Language.getText("save game error"), "Zen l'Initié", JOptionPane.ERROR_MESSAGE);
                } catch (NullPointerException ignored) {

                } finally {
                    SwitchScheme.switchScheme(oldScheme);
                    synchronized (LOCK) {
                        LOCK.notify();
                    }
                }
            }
        });
        rules.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
                showRules();
            }
        });
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

    private JButton createSoundButton() {
        JButton sound = new JButton() {
            @Override
            public void paintComponent(Graphics graphics) {
                File file;
                if (Sound.isOn()) file = new File("pictures/soundOn.png");
                else file = new File("pictures/soundOff.png");
                try {
                    this.setIcon(new ImageIcon(ImageIO.read(file).getScaledInstance(GraphicMenu.this.getWidth() / 18,
                            GraphicMenu.this.getHeight() / 11, Image.SCALE_DEFAULT)));
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
                if (Sound.isOn()) Sound.setOn(false);
                else {
                    Sound.setOn(true);
                    Sound.play(Sound.Sounds.buttonPressed);
                }
                sound.repaint();
            }
        });
        sound.setFocusable(false);
        return sound;
    }

    public static void showRules() {
        boolean restoreScheme = false;
        if (SwitchScheme.getCurrentScheme() == SwitchScheme.Scheme.MENU){
            SwitchScheme.switchScheme(SwitchScheme.Scheme.LIGHT);
            restoreScheme = true;
        }
        JFrame rulesFrame = new JFrame("Zen l'Initié - " + Language.getText("rules"));
        addImageInRules(new File("pictures/logo.png"), rulesFrame, 200, 200);
        JPanel panel = new JPanel(new GridLayout(3, 2));
        addImageInRules(new File("pictures/help/players.png"), panel, 200, 120);
        panel.add(new JLabel(Language.getText("goal")));
        panel.add(new JLabel(Language.getText("normal move")));
        addImageInRules(new File("pictures/help/pawn.png"), panel, 160, 90);
        addImageInRules(new File("pictures/help/zen.png"), panel, 160, 90);
        panel.add(new JLabel(Language.getText("zen move")));
        rulesFrame.add(panel, BorderLayout.CENTER);
        JLabel end = new JLabel("Zen l'Initié - Breit Hoarau Emeline");
        rulesFrame.add(end, BorderLayout.PAGE_END);
        rulesFrame.revalidate();
        rulesFrame.setBounds(new Rectangle(800, 620));
        rulesFrame.setLocationRelativeTo(null);
        rulesFrame.setVisible(true);
        if (restoreScheme) SwitchScheme.switchScheme(SwitchScheme.Scheme.MENU);
    }

    private static void addImageInRules(File file, Container container, int width, int height){
        try {
            container.add(new JLabel(new ImageIcon(ImageIO.read(file).getScaledInstance(width, height, Image.SCALE_DEFAULT))), BorderLayout.PAGE_START);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showModeSelectionMenu() {
        this.menu = 1;
        this.remove(this.centerPanel);
        this.centerPanel = new JPanel();
        JButton onePlayer = new RoundRectButton("1 " + Language.getText("player"), 10, 5, this);
        JButton twoPlayers = new RoundRectButton("2 " + Language.getText("players"), 10, 5, this);
        onePlayer.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
                showConfigMenu(1);
            }
        });
        twoPlayers.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
                showConfigMenu(2);
            }
        });
        this.centerPanel.add(onePlayer);
        this.centerPanel.add(twoPlayers);
        this.centerPanel.setOpaque(false);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.revalidate();
    }

    private void showConfigMenu(int playerNumber) {
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
        button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
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
                    GraphicMenu.this.game = new Game(mode1, firstPlayer, secondPlayer, level1);
                    startGame = true;
                    dispose();
                    synchronized (LOCK) {
                        LOCK.notify();
                    }
                }
            }
        });
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
            adverse.setHorizontalAlignment(SwingConstants.CENTER);
            this.centerPanel.add(adverse);
            this.centerPanel.add(playerJComboBox);
        }
        this.centerPanel.add(level);
        this.changePageEndButton(button);
        this.add(this.centerPanel, BorderLayout.CENTER);
        this.revalidate();
    }

    private JPanel createLanguagePanel() {
        JButton fr = new JButton(new ImageIcon("pictures/france.png"));
        JButton en = new JButton(new ImageIcon("pictures/english.png"));
        fr.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
                Language.setLanguage(Language.Languages.FRENCH);
                GraphicMenu.this.changeLanguage(menu);
            }
        });
        en.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Sound.play(Sound.Sounds.buttonPressed);
                Language.setLanguage(Language.Languages.ENGLISH);
                GraphicMenu.this.changeLanguage(menu);
            }
        });
        JPanel languages = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                g.setColor(new Color(26, 14, 5));
                g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        fr.setSize(30, 20);
        en.setSize(30, 20);
        languages.setPreferredSize(new Dimension(50, 40));
        languages.add(en);
        languages.add(fr);
        languages.setOpaque(false);
        return languages;
    }

    private void changePageEndButton(JButton button) {
        if (this.pageEndPanel.getComponentCount() >= 2) this.pageEndPanel.remove(1);
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(button);
        this.pageEndPanel.add(panel, BorderLayout.CENTER);
    }

    private void changeLanguage(int i) {
        switch (i) {
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
}
