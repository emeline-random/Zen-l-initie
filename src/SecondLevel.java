package ArtificialPlayers;

import Game.Element;
import Game.Game;
import Game.Player;
import Utilities.GameColor;
import Utilities.Language;

import java.io.Serializable;

public class SecondLevel extends ArtificialPlayer implements Serializable {

    public SecondLevel(String name, GameColor color) {
        super(name, color);
        this.setArtificialPlayer(true);
    }

    public SecondLevel(){
        super("", GameColor.WHITE);
    }

    @Override
    public int[] play(Element[][] board, Player adverse, Game game) {
        return new int[0];
    }

    @Override
    public Player createPlayer(GameColor color) {
        return new SecondLevel("ord", GameColor.getRandomColor(color));
    }

    @Override
    public String toString(){
        return Language.getText("level2");
    }
}
