package Game;


public interface GameMode {

    void restartGame(Game game);

    void movePawn(Element[][] pawns);

    void endGame(Player winner, Player looser, Game game, boolean equality);

    int[] play(Player player);

    void cannotMove();

    void zenAlreadyPlaced();

}
