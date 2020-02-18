package com.example.tictactoe.Controller;

import android.content.Context;
import com.example.tictactoe.Model.Game;
import com.example.tictactoe.Model.Player;

public class MenuDifficultyController{
    private Game model;
    public MenuDifficultyController(Context context){
        this.model=Game.getGame();
    }
    public void initGameWithIA(String difficulty){
        Player player0=new Player();
        Game.getGame().newGame(player0,difficulty);
    }
    public void initGame(){
        Player player0=new Player();
        Player player1=new Player();
        Game.getGame().newGame(player0,player1);
    }
}