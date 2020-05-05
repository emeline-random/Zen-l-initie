package game.view;

import game.controller.Game;
import game.bin.ChineseSymbol;
import game.bin.Level;
import game.bin.Element;
import game.bin.Player;
import utilities.Language;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Allows to create a view to play in graphic mode. A game must have a view (that implements GameMode) to be
 * displayed and to work correctly. It allows to interact with the users.
 */
public class Graphic implements GameMode {

    /**
     * The JFrame linked to this object
     */
    private GraphicalInterface anInterface;
    /**
     * The game displayed in the view
     */
    private Game game;
    /**
     * The level of the game displayed
     */
    private final Level level;

    /**
     * Allows to create a graphic view with a precised level.
     * @throws IllegalArgumentException if the level is null
     * @param level the level that the game will be played in
     */
    public Graphic(Level level) {
        if (level != null) {
            this.level = level;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Allows to set the current game and the pair and impair colors
     * of the board depending on both players chosen colors.
     * @param game the displayed game
     */
    @Override
    public void setGame(Game game) {
        this.game = game;
        Element.setPairColor(game.getFirstPlayer().getCOLOR());
        Element.setImpairColor(game.getSecondPlayer().getCOLOR());
    }

    /**
     * Allows to create the JFrame corresponding to the game displayed in this view.
     * @param board the board to display in the frame
     * @param symbols the symbols to display in the frame
     */
    public void setAnInterface(Element[][] board, ChineseSymbol[][] symbols) {
        this.anInterface = new GraphicalInterface(this.game, this.level, board, symbols);
    }

    /**
     * Allows to restart a saved game by creating the Frame corresponding
     * to the state of the game and setting the pair and impair colors
     * depending on the colors chosen by both players, then calls game.play().
     * @param game the game to restart
     * @param board the board to display
     */
    @Override
    public void restartGame(Game game, Element[][] board) {
        this.game = game;
        Element.setPairColor(game.getFirstPlayer().getCOLOR());
        Element.setImpairColor(game.getSecondPlayer().getCOLOR());
        this.anInterface = new GraphicalInterface(game, this.level, board, game.createSymbols());
        this.anInterface.setVisible(true);
        game.play();
    }

    /**
     * Allows to show a message depending on who won and asks the players if they want to
     * replay this game. If the answer is true the replay() method of the game is called, otherwise
     * the graphic menu is displayed.
     * @param winner the winner of the game
     * @param looser the looser of the game
     * @param equality true if there's an equality, else otherwise
     */
    @Override
    public void endGame(Player winner, Player looser, boolean equality) {
        if (!equality) {
            JOptionPane.showMessageDialog(null, Language.getText("congrats") + " " +
                            winner.getNAME() + " " + Language.getText("win message"),
                    Language.getText("end game"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, Language.getText("equality"),
                    Language.getText("end game"), JOptionPane.INFORMATION_MESSAGE);
        }
        int end = JOptionPane.showConfirmDialog(null, Language.getText("new game"),
                Language.getText("end game"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        this.anInterface.setVisible(false);
        if (end == JOptionPane.YES_OPTION) {
            this.game.replay();
        } else {
            JOptionPane.showMessageDialog(null, winner.getNAME() + " " + Language.getText("has") + " "
                            + winner.getPoints() + " points \n" + looser.getNAME() + " " + Language.getText("has") + " " + looser.getPoints() + "points",
                    "End", JOptionPane.INFORMATION_MESSAGE);
            new GraphicMenu().showMenu();
        }
    }

    /**
     * Allows to actualize the view of the board by repainting the frame and
     * adding a line in the history panel.
     * @param player the player who just played
     * @param move the move done in format [pawn, line, column]
     * @param board the board that will be display
     */
    @Override
    public void movePawn(Player player, int[] move, Element[][] board) {
        this.anInterface.addHistory(player, move);
        this.anInterface.repaint();
    }

    /**
     * Allows to wait until the player entered the coordinates of its move
     * and validates it or clicks on the replay or menu button.
     * @param player the player that should play
     * @param board the board of Element objects on which the game is taking place
     * @return the coordinates of the move in format [pawn, line, column]
     */
    @Override
    public int[] play(Player player, Element[][] board) {
        SwingUtilities.invokeLater(() -> this.anInterface.setPlayer(player));
        SwingUtilities.invokeLater(() -> this.anInterface.repaint());
        try {
            synchronized (this.anInterface.getCoordinates()) {
                this.anInterface.getCoordinates().wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.checkMenu();
        return this.anInterface.getCoordinates();
    }

    /**
     * Shows a message that says that the move is incorrect.
     */
    @Override
    public void cannotMove() {
        SwingUtilities.invokeLater(() -> this.anInterface.addInformation(Language.getText("pawn displacement error"), false));
    }

    /**
     * Shows a message that says that the Zen was already at this position.
     */
    @Override
    public void zenAlreadyPlaced() {
        this.anInterface.addInformation(Language.getText("zen displacement error"), false);
    }

    /**
     * Display a JFileChooser object so that the user can easily chose the place he
     * wants to save his game in. If the path doesn't end with .ser, this extension
     * is added.
     * @return the chosen path
     */
    @Override
    public String saveAs() {
        String path = null;
        JFileChooser chooser = new JFileChooser(".");
        chooser.setFileFilter(new FileNameExtensionFilter("ser files", "ser"));
        int save = chooser.showSaveDialog(null);
        if (save == JFileChooser.APPROVE_OPTION) {
            path = chooser.getSelectedFile().getAbsolutePath();
            if (!path.endsWith(".ser")) path = path + ".ser";
        }
        return path;
    }

    /**
     * Shows a message saying that the save of the game has not been performed due to a problem with the file/path.
     */
    @Override
    public void saveAsFailure() {
        JOptionPane.showMessageDialog(this.anInterface, Language.getText("save error"), "Zen l'initié",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Allows to get the interface (to create a demo)
     * @return the interface
     */
    public GraphicalInterface getAnInterface() {
        return anInterface;
    }

    /**
     * Allows to check if the player clicked on the replay button or the menu button
     * and if he does, calls the appropriate method.
     */
    private void checkMenu() {
        if (this.anInterface.isBackToMenu()) {
            new GraphicMenu().showMenu();
        } else if (this.anInterface.isRestartGame()) {
            this.game.replay();
        }
    }
}
