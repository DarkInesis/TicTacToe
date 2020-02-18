package com.example.tictactoe.Model;

import java.util.Random;

public class IA extends Player {
    private boolean[][] actionsPlayed;
    private String difficulty;
    public IA(String difficulty)
    {
        super();
        this.difficulty=difficulty;
        this.actionsPlayed= new boolean[][] {{true,true,true}, {true,true,true}, {true,true,true}};
    }
    public int[] play() {
        switch (this.difficulty.toUpperCase()){
            case "EASY":
            {
                return this.playEasy();
            }
            case  "MEDIUM":
            {
                return this.playMedium();
            }
            case "HARD":
            {
                return this.playHard();
            }
        }
        return null;
    }
    private int[] playEasy(){
        int[][] gameMatrix=Game.getGame().getMyGameMatrix();
        Random rand=new Random();
        int line;
        int column;
        do{
            line= rand.nextInt(3);
            column=rand.nextInt(3);
        }while(gameMatrix[line][column]!=0);
        // Update of actionsPlayed
        this.actionsPlayed[line][column]= true;
        int[] position={line,column};
        return position;
    }
    private int[] playMedium(){
        //temp
        return this.playEasy();
    }
    private int[] playHard(){
        //temp
        return this.playEasy();
    }
}
