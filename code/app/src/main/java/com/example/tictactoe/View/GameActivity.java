package com.example.tictactoe.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageButton;

import com.example.tictactoe.Controller.GridGameController;
import com.example.tictactoe.R;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private GridGameController controller;
    private Button backToMenuButton;
    private TextView playerNameText;
    private ImageButton[] tableButtonGrid;
    private Button ressetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        backToMenuButton = findViewById(R.id.menuButton);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        this.initGameLayout();
        this.controller=new GridGameController(this);
        }
    private void initGameLayout(){
        playerNameText = findViewById(R.id.playerNameText);
        ressetButton=findViewById(R.id.resetGamebutton);
        ressetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.reset();
                updateGrid();
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
        controller.play(line,column);
        updateGrid();
    }
    private void updateGrid(){
        int[][] grille=controller.getGrid();
        for (int i=0;i<3;i++){
            for (int j=0;j<3;j++){
                int indice=i*3+j;
                ImageButton image=tableButtonGrid[indice];
                if(grille[i][j]==1){
                    image.setImageResource(R.drawable.tic);
                }
                else if(grille[i][j]==-1){
                    image.setImageResource(R.drawable.tac);
                }
                else{
                    image.setImageDrawable(null);
                }
            }
        }
    }
}
