package fr.dedier.tictactoe;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class PlayAudio extends Service {
    private static final String LOGCAT = null;
    MediaPlayer objPlayer;

    public void onCreate(){
        super.onCreate();
        objPlayer = MediaPlayer.create(getApplicationContext(),R.raw.reset);
        objPlayer.setLooping(true);
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        objPlayer.start();
        return START_STICKY;
    }

    public void onStop(){
        objPlayer.pause();
    }

    public void onPause(){
        objPlayer.pause();
    }
    public void onDestroy(){
        objPlayer.stop();
        objPlayer.release();
    }
    @Override
    public IBinder onBind(Intent objIndent) {
        return null;
    }
}