package com.ahmed.audiovideoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, MediaPlayer.OnCompletionListener {

    private VideoView myVideoView;
    private Button btnPlayVideo, btnPlayMusic, btnPauseMusic;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private SeekBar volumeSeekBar;
    private AudioManager audioManager;
    private SeekBar moveBackAndForthSeekBar;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myVideoView = findViewById(R.id.myVideoView);
        btnPlayVideo = findViewById(R.id.btnPlayVideo);
        btnPlayMusic = findViewById(R.id.btnPlayMusic);
        btnPauseMusic = findViewById(R.id.btnPauseMusic);
        volumeSeekBar = findViewById(R.id.seekBarVolume);
        moveBackAndForthSeekBar = findViewById(R.id.seekBarMove);

        btnPlayVideo.setOnClickListener(MainActivity.this);
        btnPlayMusic.setOnClickListener(MainActivity.this);
        btnPauseMusic.setOnClickListener(MainActivity.this);


        mediaController = new MediaController(MainActivity.this);
        mediaPlayer = MediaPlayer.create(this, R.raw.music);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        int maxiVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int miniVol = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int curVol = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        volumeSeekBar.setMax(maxiVol);
        volumeSeekBar.setProgress(curVol);

        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if (fromUser) {

                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);

                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        moveBackAndForthSeekBar.setOnSeekBarChangeListener(MainActivity.this);
        moveBackAndForthSeekBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.setOnCompletionListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnPlayVideo:
                Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mow);

                myVideoView.setVideoURI(videoUri);
                myVideoView.setMediaController(mediaController);
                mediaController.setAnchorView(myVideoView);

                myVideoView.start();
                break;

            case R.id.btnPlayMusic:
                mediaPlayer.start();
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        moveBackAndForthSeekBar.setProgress(mediaPlayer.getCurrentPosition());
                    }
                }, 0, 1000);
                break;
            case R.id.btnPauseMusic:
                Log.i("How","Pause is clicked");
                mediaPlayer.pause();
                timer.cancel();
                break;

        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            mediaPlayer.seekTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        timer.cancel();
        Toast.makeText(this,"Music has ended.",Toast.LENGTH_LONG).show();
    }
}