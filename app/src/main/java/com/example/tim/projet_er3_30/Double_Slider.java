package com.example.tim.projet_er3_30;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


public class Double_Slider extends AppCompatActivity {

    //Declarations
    public SeekBar SeekAcc, SeekRot;
    public TextView seekTextAcc, seekTextRot, Txt_RoueGauches, Txt_RoueDroites;

    float fPMG = 0, fPMD = 0;
    float fPtot = 0, fRot = 0;

    public static int oPMD = 255 ,oPMG = 255;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_double_slider);
        onRun();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onRun(){

        //Association of the attributes with their id
        SeekAcc = findViewById(R.id.seekAcc);
        seekTextAcc = findViewById(R.id.testText_seekAcc);
        SeekRot = findViewById(R.id.seekRot);
        seekTextRot = findViewById(R.id.testText_seekRot);
        Txt_RoueGauches = findViewById(R.id.textRoueGauches);
        Txt_RoueDroites= findViewById(R.id.textRoueDroites);

        //Set initial value of seek-bars
        SeekAcc.setProgress(100);
        SeekRot.setProgress(90);

        //Set intervals
        SeekAcc.setMax(200);
        SeekAcc.setMin(0);
        SeekRot.setMax(180);
        SeekRot.setMin(0);

        //Rotation
        SeekRot.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                fRot = Integer.valueOf(progress)-90;
                seekTextRot.setText(String.valueOf(fRot));
                ConvPM();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        //Acceleration
        SeekAcc.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                fPtot = Integer.valueOf(progress) - 100;
                seekTextAcc.setText(String.valueOf(fPtot));
                ConvPM();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

    }

    //Engines power calculation class
    public void ConvPM(){
        if(fRot < -10) {
            fPMD = fPtot;
            fPMG = (fPtot*((90+fRot)/90));
        }

        else if(fRot > 10){
            fPMG = fPtot;
            fPMD = (fPtot*((90-fRot)/90));
        }

        else {
            fPMD = fPtot;
            fPMG = fPtot;
        }

        if (-10>fPtot) {
            oPMD = (int) (255 + (fPMD * 2.55));
            oPMG = (int) (255 +(fPMG * 2.55));
        }

        else if(10<fPtot){
            oPMD = (int) (255+(fPMD * 2.55));
            oPMG = (int) (255+(fPMG * 2.55));

        }
        else{
            oPMD = 255;
            oPMG = 255;
        }

        Txt_RoueGauches.setText(String.valueOf(oPMG));
        Txt_RoueDroites.setText(String.valueOf(oPMD));
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
