package fr.dedier.tictactoe.Controller;


import fr.dedier.tictactoe.Model.Game;

public class GridGameController {
    private Game model;

    public GridGameController() {
        this.model = Game.getGame();
    }

    public void play(int line, int column) {
        model.getGame().play(line, column);
    }

    public void reset() {
        model.resetGame();
    }

    public int[][] getGrid() {
        return model.getMyGameMatrix().getValue();
    }

    public Game getModel() {
        return model;
    }

    public String getWhoWin() {
        return model.getWhoWin().getValue();
    }

    public String getTurnOf() {
        return model.getTurnOf().getValue();
    }

    public int[] getScore() {
        return model.getScore();
    }
}
