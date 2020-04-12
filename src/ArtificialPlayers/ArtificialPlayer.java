package ArtificialPlayers;

import Game.Element;
import Game.Game;
import Game.Player;
import Utilities.GameColor;

public abstract class ArtificialPlayer extends Player{

    public ArtificialPlayer(String name, GameColor color) {
        super(name, color);
    }

    public abstract int[] play(Element[][] board, Player adverse, Game game);

    public abstract Player createPlayer(GameColor color);

}
