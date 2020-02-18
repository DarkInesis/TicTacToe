package com.example.tictactoe.Model;

public class Player {
    private boolean[][] actionsPlayed;
    public Player(){
        this.actionsPlayed=new boolean[][]{{false, false, false}, {false, false, false}, {false, false, false}};
    }

    public boolean[][] getActionsPlayed() {
        return actionsPlayed;
    }
    public void play(int line,int column){
        this.actionsPlayed[line][column]=true;
    }

    /**
     * Test is the game is win by the player
     * @return true if the player win, false in other case
     */
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
