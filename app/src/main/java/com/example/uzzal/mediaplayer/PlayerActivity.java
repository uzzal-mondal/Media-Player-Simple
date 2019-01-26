package com.example.uzzal.mediaplayer;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btn_next, btn_prev,  btn_pause;
    TextView songTextLabel;
    SeekBar songSeekbar;

    static MediaPlayer mediaPlayer;
    int position;

    String sName;

    ArrayList<File> mysongs;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        btn_next = (Button) findViewById(R.id.buttonNext_id);
        btn_pause = (Button) findViewById(R.id.button_pause_id);
        btn_prev = (Button) findViewById(R.id.buttonPrevious_id);

        songTextLabel = (TextView) findViewById(R.id.textSongeName_id);
        songSeekbar = (SeekBar) findViewById(R.id.seekBar_id);


        getSupportActionBar().setTitle("Now playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        updateSeekBar = new Thread(){

            @Override
            public void run() {

            int totalDuration = mediaPlayer.getDuration();
            int currentPosition = 0;

            while (currentPosition<totalDuration) try {
                sleep(500);
                currentPosition = mediaPlayer.getCurrentPosition();
                songSeekbar.setProgress(currentPosition);


            } catch (InterruptedException e) {
                e.printStackTrace();

            }

            }
        };

        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();


        mysongs = (ArrayList) bundle.getParcelableArrayList("songs");

        sName = mysongs.get(position).getName().toString();

        final String songName = i.getStringExtra("songName");

        songTextLabel.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mysongs.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        mediaPlayer.start();
        songSeekbar.setMax(mediaPlayer.getDuration());

        updateSeekBar.start();
        songSeekbar.getProgressDrawable().setColorFilter(getResources().getColor(android.R.color.white),PorterDuff.Mode.MULTIPLY);
        songSeekbar.getThumb().setColorFilter(getResources().getColor(R.color.colorAccent),PorterDuff.Mode.SRC_IN);



        songSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        btn_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                songSeekbar.setMax(mediaPlayer.getDuration());

                if(mediaPlayer.isPlaying()){
                    btn_pause.setBackgroundResource(R.drawable.icon_play);
                    mediaPlayer.pause();
                }else {

                    btn_pause.setBackgroundResource(R.drawable.icon_pause);
                    mediaPlayer.start();
                }
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();
                position = (position+1)%mysongs.size();

                Uri u = Uri.parse(mysongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);


                sName = mysongs.get(position).getName().toString();
                songTextLabel.setText(sName);

                mediaPlayer.start();
            }
        });

        btn_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mediaPlayer.stop();
                mediaPlayer.release();

                position = ((position-1)<0)?(mysongs.size()-1):(position-1);
                Uri u = Uri.parse(mysongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),u);

                sName = mysongs.get(position).getName().toString();
                songTextLabel.setText(sName);

                mediaPlayer.start();
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
