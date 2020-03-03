package com.example.tictactoe.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;

import com.example.tictactoe.Controller.GridGameController;
import com.example.tictactoe.Model.Game;
import com.example.tictactoe.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private AdView bottomPub;
    private GridGameController controller;
    private Button backToMenuButton;
    private TextView playerNameText;
    private ImageButton[] tableButtonGrid;
    private Button ressetButton;
    private Game gameModel;
    private AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
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
        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setNegativeButton("Menu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                backToMenu();
            }
        });
        alertDialogBuilder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.reset();
            }
        });
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
                playerNameText.setText(controller.getTurnOf());
            }
        };
        final Observer<String> winnerObserver = new Observer<String>() {
            @Override
            public void onChanged(String winner) {
                if (!winner.isEmpty()) {
                    String message = "";
                    if (winner == "nul") {
                        message = "Match nul";
                    } else {
                        message = winner + " a gagné";
                    }
                    alertDialogBuilder.setMessage(message);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
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

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        gameModel.getMyGameMatrix().observe(this, gridObserver);
        gameModel.getTurnOf().observe(this, nameOfPlayerObserver);
        gameModel.getWhoWin().observe(this, winnerObserver);

    }

    private void initGameLayout() {
        playerNameText = findViewById(R.id.playerNameText);
        ressetButton = findViewById(R.id.resetGamebutton);
        ressetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.reset();
            }
        });
        tableButtonGrid = new ImageButton[]{findViewById(R.id.position11), findViewById(R.id.position12), findViewById(R.id.position13),
                findViewById(R.id.position21), findViewById(R.id.position22), findViewById(R.id.position23),
                findViewById(R.id.position31), findViewById(R.id.position32), findViewById(R.id.position33)};
        for (int i = 0; i < 9; i++) {
            tableButtonGrid[i].setOnClickListener(this);
            tableButtonGrid[i].setTag(i);
        }

    }

    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        int column = tag % 3;
        int line = (tag - tag % 3) / 3;
        controller.play(line, column);
        int[][] grid = controller.getGrid();
    }

    private void updateGridLayout() {
        int[][] grille = controller.getGrid();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int indice = i * 3 + j;
                ImageButton image = tableButtonGrid[indice];
                if (grille[i][j] == 1) {
                    image.setImageResource(R.drawable.tic);
                } else if (grille[i][j] == -1) {
                    image.setImageResource(R.drawable.tac);
                } else {
                    image.setImageDrawable(null);
                }
            }
        }
    }

    private void backToMenu() {
        finish();
    }
}
