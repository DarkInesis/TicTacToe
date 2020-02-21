package com.example.tictactoe.Model;

import java.util.Random;

public class IA extends Player {
    private boolean[][] actionsPlayed;
    private String difficulty;
    public IA(String difficulty)
    {
        super();
        this.difficulty=difficulty;
        this.actionsPlayed= new boolean[][] {{false,false,false}, {false,false,false}, {false,false,false}};
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
        int[][] gameMatrix=Game.getGame().getMyGameMatrix().getValue();
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
    public boolean isWin() {
        // Case of the player tac the central case
        if (this.actionsPlayed[1][1]){
            // Tests diagonals
            if((this.actionsPlayed[0][0] && this.actionsPlayed[2][2]) ||(this.actionsPlayed[0][2] && this.actionsPlayed[2][0]))
            {
                return true;
            }
        }
        boolean[] lineCompleted={true,true,true};
        for(int line=0;line<3;line++)
        {
            if(this.actionsPlayed[line]==lineCompleted){
                return true;
            }
        }
        for(int column=0;column<3;column++){
            // Test columns
            if(this.actionsPlayed[0][column]&& this.actionsPlayed[1][column] && this.actionsPlayed[2][column])
            {
                return true;
            }
        }
        return false;
    }
}
