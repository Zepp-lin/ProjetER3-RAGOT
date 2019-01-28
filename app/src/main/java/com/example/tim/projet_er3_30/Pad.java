package com.example.tim.projet_er3_30;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Pad extends AppCompatActivity {

    //Declarations
    public TextView directionText, TextValoPMG, TextValoPMD;
    public Button butForw, butBack, butRight, butLeft;

    public static int oPMG = 255 , oPMD = 255 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pad);
        onRun();
    }

    public void onRun() {

        //Association of the attributes with their id
        butForw = findViewById(R.id.butForw);
        butBack = findViewById(R.id.butBack);
        butRight = findViewById(R.id.butRight);
        butLeft = findViewById(R.id.butLeft);
        TextValoPMG = findViewById(R.id.TextValPMG);
        TextValoPMD = findViewById(R.id.TextValPMD);
        directionText = findViewById(R.id.textDirection);

        butForw.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        directionText.setText(String.valueOf("Forward"));
                        oPMD = 510;
                        oPMG = 510;
                        break;

                    case MotionEvent.ACTION_UP:
                        directionText.setText(String.valueOf("Null"));
                        oPMD = 255;
                        oPMG = 255;
                }

                //Values monitoring
                TextValoPMD.setText("PMG : " + String.valueOf(oPMG));
                TextValoPMG.setText("PMD : " + String.valueOf(oPMD));

                return false;   //return false if the button is not pressed
            }
        });

        butBack.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        directionText.setText(String.valueOf("Backward"));
                        oPMD = 0;
                        oPMG = 0;
                        break;
                    case MotionEvent.ACTION_UP:
                        directionText.setText(String.valueOf("Null"));
                        oPMD = 255;
                        oPMG = 255;
                }

                //Values monitoring
                TextValoPMD.setText("PMG : " + String.valueOf(oPMG));
                TextValoPMG.setText("PMD : " + String.valueOf(oPMD));

                return false;   //return false if the button is not pressed
            }
        });

        butRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        directionText.setText(String.valueOf("Right"));
                        oPMG = 510;
                        oPMD = 0;
                        break;

                    case MotionEvent.ACTION_UP:
                        directionText.setText(String.valueOf("Null"));
                        oPMG = 255;
                        oPMD = 255;
                }

                //Values monitoring
                TextValoPMD.setText("PMG : " + String.valueOf(oPMG));
                TextValoPMG.setText("PMD : " + String.valueOf(oPMD));

                return false;   //return false if the button is not pressed
            }
        });

        butLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        directionText.setText(String.valueOf("Left"));
                        oPMG = 0;
                        oPMD = 510;
                        break;

                    case MotionEvent.ACTION_UP:
                        directionText.setText(String.valueOf("Null"));
                        oPMG = 255;
                        oPMD = 255;
                        break;
                }

                //Values monitoring
                TextValoPMD.setText("PMG : " + String.valueOf(oPMG));
                TextValoPMG.setText("PMD : " + String.valueOf(oPMD));

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

