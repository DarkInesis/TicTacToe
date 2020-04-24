package fr.dedier.tictactoe.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import fr.dedier.tictactoe.Controller.GridGameController;
import fr.dedier.tictactoe.Model.Game;
import fr.dedier.tictactoe.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private AdView bottomPub;
    private GridGameController controller;
    private Button backToMenuButton;
    private TextView scoreText;
    private ImageButton[] tableButtonGrid;
    private Game gameModel;
    private AlertDialog.Builder alertDialogBuilder;
    private InterstitialAd mInterstitialAd;
    private String winnerActivity;
    private Dialog dialogBox;
    private Button retryButtonPopUp;
    private Button menuButtonPopUp;
    private TextView messageEndGame;
    private ImageView winnerImage;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        this.scoreText = findViewById(R.id.scoreText);
        backToMenuButton = findViewById(R.id.menuButton);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
            }
        });
        this.initGameLayout();
        this.controller = new GridGameController(this);
        // init builder pop-up
        initPopUp();
        // Get the ViewModel.
        gameModel = controller.getModel();
        // Create the observer which updates the UI.
        final Observer<int[][]> gridObserver = new Observer<int[][]>() {
            @Override
            public void onChanged(@Nullable final int[][] grid) {
                updateGridLayout();
            }
        };
        final Observer<String> nameOfPlayerObserver = new Observer<String>() {
            @Override
            public void onChanged(String s) {
                int[] score = controller.getScore();
                String scoreString = score[0] + " : " + score[1];
                scoreText.setText(scoreString);
            }
        };
        final Observer<String> winnerObserver = new Observer<String>() {
            @Override
            public void onChanged(String winner) {
                if (!winner.isEmpty()) {
                    winnerActivity = winner;
                    String message;
                    if (winner.equals("nul")) {
                        message = getString(R.string.nulMessage);
                        winnerImage.setImageResource(0);
                    } else {
                        if(winner=="Joueur 1"){
                            winnerImage.setImageResource(R.drawable.ic_icone_planete);
                            message = getString(R.string.winnerMessage);
                        }
                        else if(winner=="Joueur 2"){
                            winnerImage.setImageResource(R.drawable.ic_icone_vaisseau);
                            message = getString(R.string.winnerMessage);
                        }
                        else{
                            winnerImage.setImageResource(R.drawable.ic_icone_vaisseau);
                            message = getString(R.string.looserMessage);
                        }

                    }
                    messageEndGame.setText(message);
                    dialogBox.show();
                }
            }
        };
        // Loading pubs
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        bottomPub = findViewById(R.id.BottomPub);
        AdRequest adRequest = new AdRequest.Builder().build();
        bottomPub.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-8051616064667371/8621823685");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        gameModel.getMyGameMatrix().observe(this, gridObserver);
        gameModel.getTurnOf().observe(this, nameOfPlayerObserver);
        gameModel.getWhoWin().observe(this, winnerObserver);
    }

    private void initGameLayout() {
        tableButtonGrid = new ImageButton[]{findViewById(R.id.position11), findViewById(R.id.position12), findViewById(R.id.position13),
                findViewById(R.id.position21), findViewById(R.id.position22), findViewById(R.id.position23),
                findViewById(R.id.position31), findViewById(R.id.position32), findViewById(R.id.position33)};
        for (int i = 0; i < 9; i++) {
            tableButtonGrid[i].setOnClickListener(this);
            tableButtonGrid[i].setTag(i);
            tableButtonGrid[i].setImageDrawable(null);
        }

    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        int column = tag % 3;
        int line = (tag - tag % 3) / 3;
        controller.play(line, column);
    }

    private void updateGridLayout() {
        int[][]grille= controller.getGrid();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                    ImageButton image = tableButtonGrid[i * 3 + j];
                    if (image.getDrawable() == null) {
                        float x_dest=image.getX();
                        float y_dest=image.getY();
                        Path path=new Path();
                        path.moveTo(0,y_dest);
                        path.lineTo(x_dest,y_dest);
                        ObjectAnimator animator=ObjectAnimator.ofFloat(image,View.X,View.Y,path);
                        animator.setDuration(800);
                        if (grille[i][j] == 1) {
                            image.setImageResource(R.drawable.ic_icone_planete);
                            animator.start();
                        } else if (grille[i][j] == -1) {
                            image.setImageResource(R.drawable.ic_icone_vaisseau);
                            image.setRotation(90);
                            image.animate().rotationBy(-90f).setDuration(800);
                            animator.start();
                        }
                        else if (grille[i][j] == 0) {
                            image.setImageDrawable(null);
                        }
                    }
                    else if(grille[i][j]==0){
                        image.setImageDrawable(null);
                    }
            }
        }
    }
    private void initPopUp(){
        dialogBox=new Dialog(this);
        dialogBox.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogBox.setCancelable(false);
        dialogBox.setContentView(R.layout.pop_up_game_finished);

        messageEndGame=dialogBox.findViewById(R.id.messageEndGame);
        winnerImage=dialogBox.findViewById(R.id.winnerImage);
        // Init buttons
        retryButtonPopUp=dialogBox.findViewById(R.id.retryButtonPopUp);
        menuButtonPopUp=dialogBox.findViewById(R.id.menuButtonPopUp);
        retryButtonPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (winnerActivity.equals("nul")) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                    // Reload an other Ad
                    mInterstitialAd.loadAd(new AdRequest.Builder().build());
                }
                controller.reset();
                dialogBox.cancel();
            }
        });
        menuButtonPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMenu();
                dialogBox.cancel();
            }
        });


    }
    private void backToMenu() {
        finish();
    }
}
