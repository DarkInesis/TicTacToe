package fr.dedier.tictactoe.View;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import fr.dedier.tictactoe.R;

public class LoadingActivity extends AppCompatActivity {
    ProgressBar loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        this.loadingBar=findViewById(R.id.loadingBar);
        final Timer timer=new Timer();
        final int[] compteur = {0};
        final Intent menu = new Intent(this, MainActivity.class );
        TimerTask loadingTask=new TimerTask() {
            @Override
            public void run() {
                compteur[0] +=1;
                loadingBar.setProgress(compteur[0]);
                if (compteur[0] ==100){
                    timer.cancel();
                    startActivity(menu);
                    finish();
                }
            }
        };
        timer.schedule(loadingTask,0,40);
    }
}
