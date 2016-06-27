package com.example.ravikiran.spidertask3;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.StaticLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    ViewFlipper flip;
    Boolean slideShowRunning=false;
    int slideShowDuration=0;
    Button slideShow;

    MediaPlayer play1,play2,play3;
    Spinner songSelect;
    Button play;
    Button stop;


    Boolean sensorEnabled=false;
    SensorManager sm;
    Sensor s;
    Button enable;
    Button disable;


    Handler h=new Handler(){
        @Override
        public void handleMessage(Message m){
            flip=(ViewFlipper)findViewById(R.id.flipper);
            flip.startFlipping();
            viewflipperListener();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        slideShow=(Button)findViewById(R.id.slide_show);

        play1=MediaPlayer.create(this,R.raw.got_theme);
        play2=MediaPlayer.create(this,R.raw.tdk_theme);
        play3=MediaPlayer.create(this,R.raw.potc_theme);
        songSelect=(Spinner)findViewById(R.id.spinner);
        play=(Button)findViewById(R.id.play_song);
        stop=(Button)findViewById(R.id.stop_song);
        stop.setEnabled(false);

        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        s=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        enable=(Button)findViewById(R.id.enable);
        disable=(Button)findViewById(R.id.disable);
        disable.setEnabled(false);

        timer();
    }

    public void startSlideShow(View v){

        Runnable r1= new Runnable() {
            @Override
            public void run() {
                slideShowRunning=true;

                h.sendEmptyMessage(0);

            }
        };
        Thread t1=new Thread(r1);
        t1.start();




    }

    public void viewflipperListener(){
        flip.getInAnimation().setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (flip.getDisplayedChild() == 7) {
                    flip.stopFlipping();
                    slideShowRunning = false;
                }


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }


        });
}

    public void timer(){

        final TextView timeview=(TextView)findViewById(R.id.timer);
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = slideShowDuration / 3600;
                int minutes = (slideShowDuration % 3600) / 60;
                int seconds = slideShowDuration % 60;
                String time = String.format("%d:%02d:%02d", hours, minutes, seconds);
                timeview.setText(time);
                if (slideShowRunning == true){
                    slideShowDuration++;
                }
                handler.postDelayed(this, 1000);

            }
        });
    }
    public void playSong(View view){

            if(songSelect.getSelectedItem().toString().equals("Game of thrones theme song")){
                play1.start();
            }
            else if(songSelect.getSelectedItem().toString().equals("The Dark Knight theme song")){
                play2.start();
            }
            else {
                play3.start();
            }
        play.setEnabled(false);
        stop.setEnabled(true);


    }
    public void stopSong(View View){


            if(play1.isPlaying()==true){
                play1.pause();
            }
            else if(play2.isPlaying()==true){
                play2.pause();
            }
            else{
                play3.pause();
            }
        play.setEnabled(true);
        stop.setEnabled(false);

    }

    public void enableSensor(View view){
       // sensorEnabled=true;
        slideShowRunning=false;
        sm.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL);
        flip=(ViewFlipper)findViewById(R.id.flipper);
        flip.stopFlipping();
        Toast.makeText(MainActivity.this, "sensor enabled", Toast.LENGTH_SHORT).show();
        enable.setEnabled(false);
        disable.setEnabled(true);
        slideShow.setEnabled(false);

    }

    public void disableSensor(View view){
        //sensorEnabled=false;
        sm.unregisterListener(this);
        Toast.makeText(MainActivity.this, "sensor disabled", Toast.LENGTH_SHORT).show();
        enable.setEnabled(true);
        disable.setEnabled(false);
        slideShow.setEnabled(true);

    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.values[0]==0){
            flip=(ViewFlipper)findViewById(R.id.flipper);
            flip.showNext();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
