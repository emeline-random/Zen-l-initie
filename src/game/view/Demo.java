package game.view;

import game.bin.Level;
import game.bin.Player;
import game.bin.artificialPlayers.ArtificialPlayer;
import game.bin.artificialPlayers.SecondLevel;
import game.controller.Game;
import utilities.GameColor;
import utilities.Language;

import javax.swing.*;

/**
 * Java class that allows to present main features of the menu and the game in graphic mode.
 * When you call the showDemo method you can't do anything else and you need to wait the end of it but
 * creating a Demo object won't call the showDemo() method by itself.
 */
public class Demo extends GraphicMenu {

    /**
     * Allows to show the demo in the current language.
     */
    public void showDemo() {
        this.showFirstMenuSelection();
        /*creates objects for both "false" games*/
        Graphic graphic = new Graphic(Level.EASY);
        Player firstPlayer = new Player("Jean", GameColor.WHITE);
        Player secondPlayer = new Player("Margot", GameColor.BLUE);
        Player thirdPlayer = new SecondLevel().createPlayer(GameColor.WHITE);
        /*creates the first false game and performs a displacement*/
        Game game = new Game(graphic, firstPlayer, secondPlayer, Level.EASY);
        this.performFirstMove(game, graphic, firstPlayer, secondPlayer);
        JOptionPane.showMessageDialog(graphic.getAnInterface(), Language.getText("demo7"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        graphic.getAnInterface().dispose();
        this.showSecondMenuSelection();
        /*creates the second false game and performs 2 displacements*/
        Game game1 = new Game(graphic, firstPlayer, thirdPlayer, Level.EASY);
        this.performFirstMove(game1, graphic, firstPlayer, thirdPlayer);
        JOptionPane.showMessageDialog(graphic.getAnInterface(), Language.getText("demo10"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        game1.getGameBoard().movePawn(10, 10, 7, 7);
        graphic.movePawn(firstPlayer, new int[]{11, 7, 7}, game.getGameBoard().getBoard());
        JOptionPane.showMessageDialog(graphic.getAnInterface(), Language.getText("demo11"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        graphic.getAnInterface().dispose();
        new GraphicMenu().showMenu();
    }

    /**
     * Allows to simulate a displacement in graphic mode.
     * @param game the game that is simulate
     * @param graphic the view of the game
     * @param firstPlayer the first player of the game
     * @param secondPlayer the second player of the game;
     */
    private void performFirstMove(Game game, Graphic graphic, Player firstPlayer, Player secondPlayer){
        graphic.getAnInterface().setPlayer(firstPlayer);
        graphic.getAnInterface().getPawn().setText("4");
        JOptionPane.showMessageDialog(graphic.getAnInterface(), Language.getText("demo6"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        game.getGameBoard().movePawn(8, 7, 6, 5);
        graphic.movePawn(firstPlayer, new int[]{4, 6, 5}, game.getGameBoard().getBoard());
        graphic.getAnInterface().setPlayer(secondPlayer);
    }

    /**
     * Allows to show the use of the menu and to show how to create games while displaying explanations.
     */
    private void showFirstMenuSelection() {
        super.configFrame();
        JOptionPane.showMessageDialog(this, Language.getText("demo1"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        super.showModeSelectionMenu();
        JOptionPane.showMessageDialog(this, Language.getText("demo2"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        super.showConfigMenu(1);
        JOptionPane.showMessageDialog(this, Language.getText("demo3"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        super.showConfigMenu(2);
        JOptionPane.showMessageDialog(this, Language.getText("demo4"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        JComboBox<GameColor> box = (JComboBox<GameColor>) this.centerPanel.getComponent(8);
        box.setSelectedIndex(3);
        JCheckBox box3 = (JCheckBox) this.centerPanel.getComponent(10);
        box3.setSelected(true);
        JOptionPane.showMessageDialog(this, Language.getText("demo5"), "Zen l'initié - Demo", JOptionPane.WARNING_MESSAGE);
        this.dispose();
    }

    /**
     * Allows to show how to create a one player game while displaying explanations.
     */
    private void showSecondMenuSelection() {
        super.configFrame();
        super.showConfigMenu(1);
        JOptionPane.showMessageDialog(this, Language.getText("demo8"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        JComboBox<ArtificialPlayer> box1 = (JComboBox<ArtificialPlayer>) this.centerPanel.getComponent(7);
        box1.setSelectedIndex(1);
        JCheckBox box2 = (JCheckBox) this.centerPanel.getComponent(8);
        box2.setSelected(true);
        JOptionPane.showMessageDialog(this, Language.getText("demo9"), "Zen l'initié - Demo", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }
}
