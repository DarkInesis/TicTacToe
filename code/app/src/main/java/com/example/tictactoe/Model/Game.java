package com.example.tictactoe.Model;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game {
    private int[][] myGameMatrix;
    private Player player0;
    private Player player1;
    private IA IA1;
    private boolean isPlayer1IA;
    private String difficulty;
    private double consecutivesWin = 0;
    private boolean turnOfPlayer0;
    private int nbTurn;

    private static class SingletonHolder {
        private static final Game instance = new Game();
    }
    public static Game getGame() {
        return SingletonHolder.instance;
    }
    /**
     * Initialize the game to zero
     *
     * @param player0    player0 (a real person)
     * @param difficulty difficulty of the IA as a string
     */
    public void newGame(Player player0, String difficulty) {
        // Initialization of the game
        this.myGameMatrix = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        this.turnOfPlayer0=(this.randomFirstPlayer()==0);
        this.difficulty = difficulty;
        this.isPlayer1IA = true;
        this.nbTurn = 0;
        // Create players (Player+IA)
        this.player0 = player0;
        this.IA1 = new IA(difficulty);
    }
    /**
     * Initialize the game to zero
     *
     * @param player0 player0 (a real person)
     * @param player1 player1 (a real person)
     */
    public void newGame(Player player0, Player player1) {
        // Initialization of the game
        this.myGameMatrix = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        this.turnOfPlayer0=(this.randomFirstPlayer()==0);
        this.isPlayer1IA = false;
        this.nbTurn = 0;
        // Create players (2 Players)
        this.player0 = player0;
        this.player1 = player1;
    }
    public void resetGame(){
        this.myGameMatrix = new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
        this.turnOfPlayer0=(this.randomFirstPlayer()==0);
    }
    public int randomFirstPlayer(){
        int entier=new Random().nextInt(1+1);
        return entier;
    }

    public void play(int line, int column) {
        if (this.turnOfPlayer0) {
            if (this.isPlayable(line, column)) {
                player0.play(line, column);
                this.myGameMatrix[line][column] = 1;
                turnOfPlayer0 = false;
                if (player0.isWin()) {
                    this.consecutivesWin += 1;
                }
                else{
                    if (this.isPlayer1IA) {
                        int delay=3*1000;
                        this.playIA(delay);
                    }
                }
            }
        } else {
                // Case of a real player
                player1.play(line, column);
                this.myGameMatrix[line][column] = -1;
                if (player1.isWin()) {
                    this.consecutivesWin = 0;
                }
            turnOfPlayer0 = true;

        }
        // If no one has win
        if (this.nbTurn == 9) {
        }
    }
    private void playIA(int delay){
        Timer timer=new Timer();
        timer.schedule(this.playIATimerTask(),delay);
        this.turnOfPlayer0=true;
    }
    private TimerTask playIATimerTask(){
        return new TimerTask() {
            @Override
            public void run() {
                int[] positionPlayedIA = IA1.play();
                myGameMatrix[positionPlayedIA[0]][positionPlayedIA[1]] = -1;
                if (IA1.isWin()) {
                    consecutivesWin = 0;
                }
            }
        };
    }

    /**
     * Tell if the play the player wants to do is playable
     *
     * @param line    line the player wants to tac
     * @param columns columns the player wants to tac
     * @return true if the case has never been played, in other case, return false
     */
    public boolean isPlayable(int line, int columns) {
        return (this.myGameMatrix[line][columns] == 0);
    }

    public int[][] getMyGameMatrix() {
        return myGameMatrix;
    }

    public boolean isWin(){
        return (player0.isWin() || player1.isWin());
    }
    public Player winnerPlayer(){
        if(player0.isWin()){
            return player0;
        }
        else{
            return player1;
        }
    }
}
