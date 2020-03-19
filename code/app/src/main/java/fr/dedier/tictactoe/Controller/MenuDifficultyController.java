package fr.dedier.tictactoe.Controller;

import fr.dedier.tictactoe.Model.Game;
import fr.dedier.tictactoe.Model.Player;

public class MenuDifficultyController {
    private Game model;

    public MenuDifficultyController() {
        this.model = Game.getGame();
    }

    public void initGameWithIA(String difficulty) {
        Player player0 = new Player();
        Game.getGame().newGame(player0, difficulty);
    }

    public void initGame() {
        Player player0 = new Player();
        Player player1 = new Player();
        Game.getGame().newGame(player0, player1);
    }
}