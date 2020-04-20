package Game.View;

import Game.Controllers.Game;
import Game.bin.ChineseSymbol;
import Game.bin.Level;
import Game.bin.Element;
import Game.bin.Player;
import Utilities.Language;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Graphic implements GameMode {

    private GraphicalInterface anInterface;
    private Game game;
    private final Level level;

    public Graphic(Level level) {
        if (level != null) {
            this.level = level;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    public void setAnInterface(Element[][] board, ChineseSymbol[][] symbols) {
        this.anInterface = new GraphicalInterface(this.game, this.level, board, symbols);
    }

    @Override
    public void restartGame(Game game, Element[][] board) {
        this.game = game;
        Element.setPairColor(game.getFirstPlayer().getColor());
        Element.setImpairColor(game.getSecondPlayer().getColor());
        this.anInterface = new GraphicalInterface(game, this.level, board, game.createSymbols());
        this.anInterface.setVisible(true);
        game.play();
    }

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

    @Override
    public void movePawn(Player player, int[] move, Element[][] board) {
        this.anInterface.addHistory(player, move);
        this.anInterface.repaint();
    }

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

    @Override
    public void cannotMove() {
        SwingUtilities.invokeLater(() -> this.anInterface.addInformation(Language.getText("pawn displacement error"), false));
    }

    @Override
    public void zenAlreadyPlaced() {
        this.anInterface.addInformation(Language.getText("zen displacement error"), false);
    }

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

    @Override
    public void saveAsFailure() {
        JOptionPane.showMessageDialog(this.anInterface, Language.getText("save error"), "Zen l'initié",
                JOptionPane.ERROR_MESSAGE);
    }

    private void checkMenu() {
        if (this.anInterface.isBackToMenu()) {
            new GraphicMenu().showMenu();
        } else if (this.anInterface.isRestartGame()) {
            game.replay();
        }
    }
}
