package fr.dedier.tictactoe.Model;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;
import java.util.Random;

public class Game extends ViewModel {
    private MutableLiveData<int[][]> myGameMatrix;
    private MutableLiveData<String> turnOf;
    private MutableLiveData<String> whoWin;

    private Player player0;
    private Player player1;
    private IA IA1;
    private boolean isPlayer1IA;
    private String difficulty;

    private int[] score;
    private double consecutiveWin;
    private boolean turnOfPlayer0;
    private int nbTurn;
    private boolean IACanPlay=true;
    private long dateLastPlay=System.nanoTime();
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
        myGameMatrix = new MutableLiveData<>();
        myGameMatrix.setValue(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        this.turnOfPlayer0 = (this.randomFirstPlayer() == 0);
        this.difficulty = difficulty;
        this.isPlayer1IA = true;
        this.nbTurn = 0;
        this.score = new int[]{0, 0};
        // Create players (Player+IA)
        this.player0 = player0;
        this.IA1 = new IA(this.difficulty);

        this.turnOf = new MutableLiveData<>();
        if (!this.turnOfPlayer0) // Case the IA plays first
        {
            IACanPlay=false;
            Runnable startIArunnable=new Runnable() {
                @Override
                public void run() {
                    playIA();
                    IACanPlay=true;
                }
            };
            new Handler().postDelayed(startIArunnable,(long) 1 * 1000);
        }
        this.setTurnOf();

        this.whoWin = new MutableLiveData<>();
    }

    /**
     * Initialize the game to zero
     *
     * @param player0 player0 (a real person)
     * @param player1 player1 (a real person)
     */
    public void newGame(Player player0, Player player1) {
        // Initialization of the game
        myGameMatrix = new MutableLiveData<>();
        myGameMatrix.setValue(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        this.turnOfPlayer0 = (this.randomFirstPlayer() == 0);
        this.isPlayer1IA = false;
        this.nbTurn = 0;
        this.score = new int[]{0, 0};
        // Create players (2 Players)
        this.player0 = player0;
        this.player1 = player1;
        this.whoWin = new MutableLiveData<>();
        this.turnOf = new MutableLiveData<>();
        this.setTurnOf();
    }

    public void resetGame() {
        // Reset game
        myGameMatrix.setValue(new int[][]{{0, 0, 0}, {0, 0, 0}, {0, 0, 0}});
        // Reset players
        this.nbTurn = 0;
        this.player0 = new Player();
        if (this.isPlayer1IA) {
            this.IA1 = new IA(difficulty);
        } else {
            this.player1 = new Player();
        }
        // Init game
        this.setWhoWin("");
        this.turnOfPlayer0 = (this.randomFirstPlayer() == 0);
        if (this.isPlayer1IA) {
            if (!this.turnOfPlayer0) // Cas ou l'IA joue en premier
            {
                this.playIA();
            }
        }
        this.setTurnOf();
    }

    /*#################################################################
    #                            Manage Play                          #
    ##################################################################*/
    public void playManager(final int line, final int column){
        Runnable runPlay=new Runnable() {
            @Override
            public void run() {
                play(line,column);
            }
        };
        long delay= (long) 0.5* 1000 * 1000 * 1000;
        if((System.nanoTime()-dateLastPlay)<delay){
            new Handler().postDelayed(runPlay,delay);
            dateLastPlay=System.nanoTime();
        }
        else{
            play(line, column);
        }
    }
    public void play(int line, int column) {
        if (this.isPlayable(line, column)) {
            if (this.turnOfPlayer0) {
                player0.play(line, column);
                this.changeMyGameMatrix(line, column, 1);
                turnOfPlayer0 = false;
                this.setTurnOf();
                this.nbTurn += 1;
                if (this.isPlayer1IA) {
                    if (!this.isWin() && this.nbTurn != 9 && IACanPlay) {
                        IACanPlay=false;
                        Runnable runnable=new Runnable() {
                            @Override
                            public void run() {
                                playIA();
                                // If someone has win
                                if (isWin()) {
                                    if (player0.isWin()) {
                                        score[0] += 1;
                                    } else {
                                        score[1] += 1;
                                    }
                                    setWhoWin(whoWin());
                                }
                                // If no one has win
                                else if (nbTurn == 9) {
                                    setWhoWin("nul");
                                }
                                IACanPlay=true;
                            }
                        };
                        new Handler().postDelayed(runnable,1*1000);
                    }
                }
            } else if (!this.isPlayer1IA) {
                // Case of a real player
                player1.play(line, column);
                this.changeMyGameMatrix(line, column, -1);
                turnOfPlayer0 = true;
                this.setTurnOf();
                this.nbTurn += 1;
            }
            // If someone has win
            if (this.isWin()) {
                if (player0.isWin()) {
                    score[0] += 1;
                } else {
                    score[1] += 1;
                }
                setWhoWin(this.whoWin());
            }
            // If no one has win
            else if (this.nbTurn == 9) {
                setWhoWin("nul");
            }
        }
    }

    /*#################################################################
    #                            Play IA                              #
    ##################################################################*/
    private void playIA() {
        int[] positionPlayedIA = IA1.play();
        changeMyGameMatrix(positionPlayedIA[0], positionPlayedIA[1], -1);
        this.turnOfPlayer0 = true;
        this.setTurnOf();
        this.nbTurn += 1;
    }
    /*#################################################################
    #                       XXXX                                       #
    ##################################################################*/

    /**
     * Tell if the play the player wants to do is playable
     *
     * @param line    line the player wants to tac
     * @param columns columns the player wants to tac
     * @return true if the case has never been played, in other case, return false
     */
    private boolean isPlayable(int line, int columns) {
        return (Objects.requireNonNull(this.myGameMatrix.getValue())[line][columns] == 0);
    }

    private boolean isWin() {
        if (isPlayer1IA) {
            return (player0.isWin() || IA1.isWin());
        } else {
            return (player0.isWin() || player1.isWin());
        }
    }

    private String whoWin() {
        if (player0.isWin()) {
            return "Joueur 1";
        } else if (isPlayer1IA) {
            if (IA1.isWin()) {
                return "Ordinateur";
            }
        } else {
            if (player1.isWin()) {
                return "Joueur 2";
            }
        }
        return "Personne";
    }

    /*#################################################################
    #                       Getters                                   #
    ##################################################################*/

    public int[] getScore() {
        return this.score;
    }
    public int getNbTurn(){
        return this.nbTurn;
    }
    /*#################################################################
    #                       MutableLiveData                           #
    ##################################################################*/

    public MutableLiveData<String> getTurnOf() {
        if (turnOf == null) {
            turnOf = new MutableLiveData<>();
        }
        return turnOf;
    }

    public MutableLiveData<int[][]> getMyGameMatrix() {
        if (myGameMatrix == null) {
            myGameMatrix = new MutableLiveData<>();
        }
        return myGameMatrix;
    }

    public MutableLiveData<String> getWhoWin() {
        if (whoWin == null) {
            whoWin = new MutableLiveData<>();
        }
        return whoWin;
    }
    /*------------------ Set MutuableLiveData -----------------------*/

    private void setTurnOf() {
        if (turnOfPlayer0) {
            this.turnOf.setValue("Joueur 1");
        } else if (isPlayer1IA) {
            this.turnOf.setValue("Ordinateur");
        } else {
            this.turnOf.setValue("Joueur 2");
        }
    }

    private void setWhoWin(String winnerName) {
        this.whoWin.setValue(winnerName);
    }

    private void changeMyGameMatrix(int line, int columm, int value) {
        int[][] myGameTemp = this.myGameMatrix.getValue();
        assert myGameTemp != null;
        myGameTemp[line][columm] = value;
        this.myGameMatrix.setValue(myGameTemp);
    }

    /*#################################################################
    #                               Utils                             #
    ##################################################################*/

    private int randomFirstPlayer() {
        return new Random().nextInt(1 + 1);
    }
}
