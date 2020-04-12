package Game;

import Utilities.Language;

import javax.swing.*;

public class Graphic implements GameMode {

    private GraphicalInterface anInterface;
    private Level level;

    public Graphic(Level level){
        if (level != null){
            this.level = level;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setAnInterface(Game game) {
        this.anInterface = new GraphicalInterface(game, this.level);
    }

    @Override
    public void restartGame(Game game) {
        Element.setPairColor(game.getFirstPlayer().getColor());
        Element.setImpairColor(game.getSecondPlayer().getColor());
        game.getGameBoard().createChineseSymbols();
        this.anInterface = new GraphicalInterface(game, this.level);
        this.anInterface.createBoardListener();
        this.anInterface.setVisible(true);
        game.play();
    }

    @Override
    public void movePawn(Element[][] pawns) {
        this.anInterface.repaint();
    }

    @Override
    public void endGame(Player winner, Player looser, Game game, boolean equality) {
        if (!equality) {
            JOptionPane.showMessageDialog(null, Language.getText("congrats") + " " + winner.getNAME() + Language.getText("win message"),
                    Language.getText("end game"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, Language.getText("equality"),
                    Language.getText("end game"), JOptionPane.INFORMATION_MESSAGE);
        }
        int end = JOptionPane.showConfirmDialog(null, Language.getText("new game"),
                Language.getText("end game"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        this.anInterface.setVisible(false);
        if (end == JOptionPane.YES_OPTION) {
            game.replay();
        } else {
            JOptionPane.showMessageDialog(null, winner.getNAME() + " " + Language.getText("has") + " "
                    + winner.getPoints() + " points \n" + looser.getNAME() + " " + Language.getText("has") + " "  + looser.getPoints() + "points",
                    "End", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    private void showMenu() {
        new GraphicMenu().showMenu();
    }

    @Override
    public int[] play(Player player) {
        SwingUtilities.invokeLater(() -> this.anInterface.setPlayer(player));
        SwingUtilities.invokeLater(() -> this.anInterface.repaint());
        return this.waitResults();
    }

    @Override
    public void cannotMove() {
        SwingUtilities.invokeLater(() -> this.anInterface.addInformation(Language.getText("pawn displacement error"), false));
    }

    @Override
    public void zenAlreadyPlaced() {
        this.anInterface.addInformation(Language.getText("zen displacement error"), false);
    }

    private void checkMenu() {
        if (this.anInterface.isBackToMenu()) {
            this.showMenu();
        }
    }

    private int[] waitResults() {
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
}
