package Game.View;


import Game.Controllers.Game;
import Game.bin.Element;
import Game.bin.Player;

public interface GameMode {

    void setGame(Game game);

    void restartGame(Game game, Element[][] board);

    void endGame(Player winner, Player looser, boolean equality);

    void movePawn(Player player, int[] move, Element[][] board);

    int[] play(Player player, Element[][] board);

    void cannotMove();

    void zenAlreadyPlaced();

    String saveAs();

    void saveAsFailure();
}
