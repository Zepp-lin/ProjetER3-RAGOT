package com.example.tim.projet_er3_30;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Classic_Controler extends AppCompatActivity {

    //Declarations
    public SeekBar Seek;
    public TextView buttonsText, textPMG, textPMD;
    public Button button_left, button_right;

    int iRot = 0;
    float fPtot = 0;

    static int oPMD = 255, oPMG = 255;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classic_controler);
        onRun();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onRun(){

        //Association of the attributes with their id
        Seek = findViewById(R.id.seekAcc);
        button_left = findViewById(R.id.button_left);
        button_right = findViewById(R.id.button_right);
        buttonsText = findViewById(R.id.testText_buttons);
        textPMD = findViewById(R.id.textPMD);
        textPMG = findViewById(R.id.textPMG);

        //Set initial value of Seek
        Seek.setProgress(100);

        //Set interval
        Seek.setMax(200);
        Seek.setMin(0);


        Seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //Catch the value of the seek-bar
                fPtot = Integer.valueOf(progress);

                if ((fPtot < 110) && (fPtot > 90)) {
                    oPMD = 255;
                    oPMG = 255;
                }
                else{

                    //Engines power calculation
                    if (0 > fPtot) {
                        if (iRot >= -90) {
                            oPMG = 255;
                            oPMD = (int) (fPtot * 2.55);
                        } else if (iRot <= 90) {
                            oPMG = (int) (fPtot * 2.55);
                            oPMD = 255;
                        } else if (iRot == 0) {
                            oPMD = (int) (fPtot * 2.55);
                            oPMG = (int) (fPtot * 2.55);
                        }
                    } else if (0 < fPtot) {
                        if (iRot == -90) {
                            oPMG = 255;
                            oPMD = (int) (fPtot * 2.55);
                        } else if (iRot == 90) {
                            oPMG = (int) (fPtot * 2.55);
                            oPMD = 255;
                        } else if (iRot == 0) {
                            oPMD = (int) (fPtot * 2.55);
                            oPMG = (int) (fPtot * 2.55);
                        }
                    } else {
                        oPMD = 0;
                        oPMG = 0;
                    }
                }

                //Monitoring of the sent engines power values
                textPMG.setText("PMG : " + String.valueOf(oPMG));
                textPMD.setText("PMD : " + String.valueOf(oPMD));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Seek.setProgress(100);
                oPMD = 255;
                oPMG = 255;
            }
        });

        button_left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        buttonsText.setText(String.valueOf("Left"));
                        iRot = -90;
                        oPMG = 255;
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonsText.setText(String.valueOf("Null"));
                        iRot = 0;
                        oPMG = (int) (fPtot * 2.55);
                }

                //Monitoring of the sent engines power values
                textPMG.setText("PMG : " + String.valueOf(oPMG));
                textPMD.setText("PMD : " + String.valueOf(oPMD));

                return false;   //return false if the button is not pressed
            }
        });

        button_right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent){

                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        buttonsText.setText(String.valueOf("Right"));
                        iRot = 90;
                        oPMD = 255;
                        break;

                    case MotionEvent.ACTION_UP:
                        buttonsText.setText(String.valueOf("Null"));
                        iRot = 0;
                        oPMD = (int) (fPtot * 2.55);
                }

                //Monitoring of the sent engines power values
                textPMG.setText("PMG : " + String.valueOf(oPMG));
                textPMD.setText("PMD : " + String.valueOf(oPMD));

                return false;   //return false if the button is not pressed
            }
        });
    }

    //Pass the values to Bluetooth
    public static int oPMGreturn(){
        return oPMG;
    }
    public static int oPMDreturn(){
        return oPMD;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
