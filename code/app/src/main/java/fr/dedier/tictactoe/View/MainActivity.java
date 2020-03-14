package fr.dedier.tictactoe.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.dedier.tictactoe.Controller.MenuDifficultyController;
import fr.dedier.tictactoe.R;
public class MainActivity extends AppCompatActivity implements View.OnClickListener   {
    private MenuDifficultyController controller;

    private Button easyButton;
    private Button mediumButton;
    private Button hardButton;
    private Button twoVStwobutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.controller = new MenuDifficultyController(this);
        easyButton=findViewById(R.id.easybutton);
        mediumButton=findViewById(R.id.mediumbutton);
        hardButton=findViewById(R.id.hardbutton);
        easyButton.setOnClickListener(this);
        mediumButton.setOnClickListener(this);
        hardButton.setOnClickListener(this);
        easyButton.setTag(0);
        mediumButton.setTag(1);
        hardButton.setTag(2);
        twoVStwobutton=findViewById(R.id.twoVStwobutton);
        twoVStwobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.initGame();
                goToGameActivity();
            }
        });
    }
    private void goToGameActivity() {
        Intent gameActivity = new Intent(this, GameActivity.class);
        startActivity(gameActivity);
    }
    @Override
    public void onClick(View v) {
        int tag = (int) v.getTag();
        if ( tag<=2 &&tag>=0){
            String difficulty="";
            switch (tag){
                case 0:
                {
                    difficulty="EASY";
                    break;
                }
                case 1:
                {
                    difficulty="MEDIUM";
                    break;
                }
                case 2:
                {
                    difficulty="HARD";
                    break;
                }
            }
            controller.initGameWithIA(difficulty);
            goToGameActivity();
        }

    }
}
