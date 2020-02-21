package com.example.tictactoe.Model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends ViewModel {
    private MutableLiveData<int[][]> myGameMatrix;
    private MutableLiveData<String> turnOf;
    private MutableLiveData<String> whoWin;

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

    /*#################################################################
    #                       Manage game                               #
    ##################################################################*/

    /**
     * Initialize the game to zero
     *
     * @param player0    player0 (a real person)
     * @param difficulty difficulty of the IA as a string
     */
    public void newGame(Player player0, String difficulty) {
        // Initialization of the game
        myGameMatrix = new MutableLiveData<int[][]>();
        myGameMatrix.setValue(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        this.turnOfPlayer0=(this.randomFirstPlayer()==0);
        this.difficulty = difficulty;
        this.isPlayer1IA = true;
        this.nbTurn = 0;
        // Create players (Player+IA)
        this.player0 = player0;
        this.IA1 = new IA(this.difficulty);

        this.turnOf=new MutableLiveData<String>();
        if(!this.turnOfPlayer0) // Cas ou l'IA joue en premier
        {
            this.playIA();
        }
        this.setTurnOf();

        this.whoWin=new MutableLiveData<String>();
    }
    /**
     * Initialize the game to zero
     *
     * @param player0 player0 (a real person)
     * @param player1 player1 (a real person)
     */
    public void newGame(Player player0, Player player1) {
        // Initialization of the game
        myGameMatrix = new MutableLiveData<int[][]>();
        myGameMatrix.setValue(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        this.turnOfPlayer0=(this.randomFirstPlayer()==0);
        this.isPlayer1IA = false;
        this.nbTurn = 0;
        // Create players (2 Players)
        this.player0 = player0;
        this.player1 = player1;

        this.turnOf=new MutableLiveData<String>();
    }
    public void resetGame(){
/*
        if(this.isPlayer1IA){
            this.newGame(new Player(),this.difficulty);
        }
        else{
            this.newGame(new Player(),new Player());
        }
*/
        myGameMatrix.setValue(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        this.turnOfPlayer0=(this.randomFirstPlayer()==0);
        this.turnOf=new MutableLiveData<String>();
        if(!this.turnOfPlayer0) // Cas ou l'IA joue en premier
        {
            this.playIA();
        }
        this.setTurnOf();
        this.nbTurn = 0;
        this.whoWin=new MutableLiveData<String>();
        this.player0=new Player();
        if(this.isPlayer1IA){
            this.IA1= new IA(difficulty);
        }
        else{
            this.player1=new Player();
        }
    }

    /*#################################################################
    #                            Manage Play                          #
    ##################################################################*/

    public void play(int line, int column) {
        if(this.isPlayable(line, column)) {
            if (this.turnOfPlayer0) {
                player0.play(line, column);
                this.changeMyGameMatrix(line, column, 1);
                turnOfPlayer0 = false;
                this.setTurnOf();
                if (!this.isWin()) {
                    if (this.isPlayer1IA) {
                        //int delay = 3 * 1000;
                        //this.playIA(delay);
                        this.playIA();
                    }
                }
            }else if (!this.isPlayer1IA) {
                // Case of a real player
                player1.play(line, column);
                this.changeMyGameMatrix(line, column, -1);
                turnOfPlayer0 = true;
                this.setTurnOf();
            }
            // If someone has win
            if (this.isWin()){
                setWhoWin(this.whoWin());
            }
            // If no one has win
            if (this.nbTurn == 9) {
            }
        }
    }

    /*#################################################################
    #                            Play IA                              #
    ##################################################################*/

    private void playIA(int delay){
        Timer timer=new Timer();
        timer.schedule(this.playIATimerTask(),delay);
        this.turnOfPlayer0=true;
        this.setTurnOf();
    }
    private void playIA(){
        int[] positionPlayedIA = IA1.play();
        changeMyGameMatrix(positionPlayedIA[0],positionPlayedIA[1],-1);
        if (IA1.isWin()) {
            consecutivesWin = 0;
        }
        this.turnOfPlayer0=true;
        this.setTurnOf();
    }
    private TimerTask playIATimerTask(){
        return new TimerTask() {
            @Override
            public void run() {
                int[] positionPlayedIA = IA1.play();
                changeMyGameMatrix(positionPlayedIA[0],positionPlayedIA[1],-1);
                if (IA1.isWin()) {
                    consecutivesWin = 0;
                }
            }
        };
    }

    /*#################################################################
    #                       XXXX                           #
    ##################################################################*/

    /**
     * Tell if the play the player wants to do is playable
     *
     * @param line    line the player wants to tac
     * @param columns columns the player wants to tac
     * @return true if the case has never been played, in other case, return false
     */
    public boolean isPlayable(int line, int columns) {
        return (this.myGameMatrix.getValue()[line][columns] == 0);
    }
    public boolean isWin(){
        if (isPlayer1IA){ return (player0.isWin() || IA1.isWin()); }
        else{ return (player0.isWin() || player1.isWin()); }
    }
    public String whoWin(){
        if (player0.isWin()){ return "Joueur 1";}
        else if(isPlayer1IA) {if (IA1.isWin()){return "Ordinateur";}}
        else{if(player1.isWin()){return "Joueur 2";}}
        return "Personne";
    }

    /*#################################################################
    #                       MutableLiveData                           #
    ##################################################################*/

    public MutableLiveData<String> getTurnOf(){
        if(turnOf==null){
            turnOf=new MutableLiveData<String>();
        }
        return turnOf;
    }
    public MutableLiveData<int[][]> getMyGameMatrix() {
        if (myGameMatrix == null) {
            myGameMatrix = new MutableLiveData<int[][]>();
        }
        return myGameMatrix;
    }

    public MutableLiveData<String> getWhoWin() {
        if(whoWin==null){
            whoWin=new MutableLiveData<String>();
        }
        return whoWin;
    }
    /*------------------ Set MutuableLiveData -----------------------*/

    private void setTurnOf(){
        if(turnOfPlayer0){
            this.turnOf.setValue("Joueur 1");
        }
        else if(isPlayer1IA){
            this.turnOf.setValue("Ordinateur");
        }
        else{
            this.turnOf.setValue("Joueur 2");
        }
    }
    private void setWhoWin(String winnerName)
    {
        this.whoWin.setValue(winnerName);
    }
    private void changeMyGameMatrix(int line,int columm,int value){
        int[][] myGameTemp=this.myGameMatrix.getValue();
        myGameTemp[line][columm]=value;
        this.myGameMatrix.setValue(myGameTemp);
    }

    /*#################################################################
    #                               Utils                             #
    ##################################################################*/

    public int randomFirstPlayer(){
        int entier=new Random().nextInt(1+1);
        return entier;
    }

}
