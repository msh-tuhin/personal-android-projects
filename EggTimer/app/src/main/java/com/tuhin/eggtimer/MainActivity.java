package com.tuhin.eggtimer;

import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private long toTime = 30000;
    private long minutes = 0;
    private long seconds = 30;

    SeekBar time;
    TextView myText;
    Button controlButton;
    CountDownTimer a;
    MediaPlayer mPlayer;

    public void buttonClicked(View view){

        if(controlButton.getText() == "GO"){
            controlButton.setText("STOP");
            a = new CountDownTimer(toTime + 100, 1000){

                public void onTick(long mili){
                    seconds--;
                    if(seconds == -1 ){
                        if(minutes != 0)
                        minutes--;
                        seconds = 59;
                    }
                    myText.setText(text(minutes, seconds));

                }
                public void onFinish(){
                    mPlayer.start();
                    controlButton.setText("GO");
                    controlButton.setClickable(false);
                }
            }.start();
        } else {
            controlButton.setText("GO");
            toTime = minutes * 60000 + seconds * 1000;
            a.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayer = MediaPlayer.create(this, R.raw.airhorn);
        time = (SeekBar) findViewById(R.id.seekBar);
        myText = (TextView) findViewById(R.id.textView);
        controlButton = (Button) findViewById(R.id.controlButton);
        controlButton.setText("GO");

        time.setMax(600000);
        time.setProgress(30000);

        time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                myText.setText(text(progress/60000, (progress%60000)/1000));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                toTime = time.getProgress();
                minutes =  toTime/60000;
                seconds = (toTime%60000)/1000;

                myText.setText(text(minutes, seconds));
                if(!controlButton.isClickable()){
                    controlButton.setClickable(true);
                }
            }
        });
    }
    String text(long minutes, long seconds){
        String minuteText, secondText;
        if(minutes<10){
            minuteText = "0" + String.valueOf(minutes);
        }
        else minuteText = String.valueOf(minutes);
        if(seconds<10){
            secondText = "0" + String.valueOf(seconds);
        } else {
            secondText = String.valueOf(seconds);
        }
        return minuteText + ":" + secondText;
    }
}
