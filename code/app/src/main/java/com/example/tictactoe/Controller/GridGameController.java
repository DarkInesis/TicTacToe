package com.example.tictactoe.Controller;


import android.content.Context;

import com.example.tictactoe.Model.Game;

public class GridGameController{
    private Game model;

    public GridGameController(Context context){
        this.model=Game.getGame();
    }
    public void play(int line,int column){
        model.getGame().play(line,column);
    }
    public void reset(){
        model.resetGame();
    }
    public int[][] getGrid(){
        return model.getMyGameMatrix();
    }
}
