package com.example.tim.projet_er3_30;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log; //Keep for debugging
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView; //Keep for the runOnUiThread part


public class Joystick_Controler extends AppCompatActivity implements Joystick_View.JoystickListener {

    //Declarations
    //public TextView txtXPercent, txtYPercent; //needed for runOnUiThread
    static int oPMD = 255, oPMG = 255;
    float fAngle, fSpeedPercent;

    @Override
    protected void onCreate (Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_joystick);
        onRun();
    }

    public void onRun(){

        Joystick_View joystick = new Joystick_View(this);
        setContentView(joystick);

        //txtXPercent = findViewById(R.id.textXPercent); //TextView used to monitor the values of fXPercent and fYPercent
        //txtYPercent = findViewById(R.id.textYPercent); //(commented because runOnUiThread is not working)
    }

    @Override
    public void onJoystickMoved (float fXPercent, float fYPercent, int id) {

        //Log.d("Main Method", "X percent : " + fXPercent * 100 + " Y Percent : " + (-1 * fYPercent) * 100); //Ignore if you are not debugging
        //run_Thread(fXPercent, fYPercent); // Launch the runOnUiThread (declared at the end of the code)

        float fZoneRange = (float) 15;

        fXPercent = fXPercent * 100;
        fYPercent = (-1 * fYPercent) * 100;

        oPMD = 255 ; //Default values when
        oPMG = 255 ; //the robot is not moving

        fAngle = (float) Math.abs( Math.atan(fYPercent / fXPercent) );
        fSpeedPercent = (float) Math.sqrt( Math.pow(fXPercent, 2) + Math.pow(fYPercent, 2) );
        if (fSpeedPercent > 1) {    //Forcing the value of fSpeedPercent to 1 in case it goes over
            fSpeedPercent = 1;      //(occure when the joystick is pulled over its base)
        }

        /*------------------------------Straight directions------------------------------*/
        if(((-fZoneRange < fXPercent) && (fXPercent < fZoneRange)) || ((-fZoneRange < fYPercent) && (fYPercent < fZoneRange))) {
            //Log.d("Main Method", "STRAIGHT DIRECTIONS"); //Ignore if you are not debugging

            /*Forward*/
            if ((-fZoneRange < fXPercent) && (fXPercent < fZoneRange) && (fYPercent > 0)) {

                oPMD = 255 + (int) (255 * fSpeedPercent);
                oPMG = oPMD;
                //Log.d("Main Method", "FORWARD "); //Ignore if you are not debugging
            }

            /*Backward*/
            if ((-fZoneRange < fXPercent) && (fXPercent < fZoneRange) && (fYPercent < 0)) {

                oPMD = 255 - (int) (255 * fSpeedPercent);
                oPMG = oPMD;
                //Log.d("Main Method", "BACKWARD "); //Ignore if you are not debugging
            }

            /*Left*/
            if ((-fZoneRange < fYPercent) && (fYPercent < fZoneRange) && (fXPercent < 0)) {

                oPMD = 255 + (int) (255 * fSpeedPercent);
                oPMG = 0;
                //Log.d("Main Method", "LEFT "); //Ignore if you are not debugging
            }

            /*Right*/
            if ((-fZoneRange < fYPercent) && (fYPercent < fZoneRange) && (fXPercent > 0)) {

                oPMD = 0;
                oPMG = 255 + (int) (255 * fSpeedPercent);
                //Log.d("Main Method", "RIGHT "); //Ignore if you are not debugging
            }
        }
        /*--------------------Linear directions (turning while moving)--------------------*/
        else {

            //Log.d("Main Method", "LINEAR DIRECTIONS"); //Ignore if you are not debugging

            /*Going Right and Forward*/
            if ((fYPercent > 0) && (fXPercent > 0)) {

                oPMD = 255 + (int) (255 * fSpeedPercent * (fAngle / (Math.PI / 2)));
                oPMG = 255 + (int) (fSpeedPercent * 255);
                //Log.d("Main Method", "FORW + RIGHT "); //Ignore if you are not debugging
            }

            /*Going Left and Forward*/
            if ((fYPercent > 0) && (fXPercent < 0)) {

                oPMD = 255 + (int) (fSpeedPercent * 255);
                oPMG = 255 + (int) (255 * fSpeedPercent * (fAngle / (Math.PI / 2)));
                //Log.d("Main Method", "FORW + LEFT "); //Ignore if you are not debugging
            }

            /*Going Right and Backward*/
            if ((fYPercent < 0) && (fXPercent > 0)) {

                oPMD = 255 - (int) (255 * fSpeedPercent * (fAngle / (Math.PI / 2)));
                oPMG = 255 - (int) (255 * fSpeedPercent);
                //Log.d("Main Method", "BACK + RIGHT "); //Ignore if you are not debugging
            }

            /*Going Left and Backward*/
            if ((fYPercent < 0) && (fXPercent < 0)) {

                oPMD = 255 - (int) (255 * fSpeedPercent);
                oPMG = 255 - (int) (255 * fSpeedPercent * (fAngle / (Math.PI / 2)));
                //Log.d("Main Method", "BACK + LEFT "); //Ignore if you are not debugging
            }
        }

        //Ignore if you are not debugging
        Log.d("Main Method", "iPowerR : " + oPMD);
        Log.d("Main Method", "iPowerL : " + oPMG);
        Log.d("Main Method", "fSpeedPercent : " + fSpeedPercent);
        Log.d("Main Method", "fAngle : " + fAngle);
        Log.d("Main Method", "");


    }

    //Pass the values to Bluetooth
    public static int oPMGreturn(){
        return oPMG;
    }
    public static int oPMDreturn(){ return oPMD; }

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

    //Need to be debug but not essential for the joystick to be functional
    /*private void run_Thread(final float fXPercent, final float fYPercent){
        runOnUiThread (new Runnable() {
            public void run() {
                txtXPercent.setText("X : " + String.valueOf(fXPercent) + "%"); //The error seems to came from the .setText
                txtYPercent.setText("Y : " + String.valueOf(fYPercent) + "%"); //"Attempt to invoke virtual method
            }                                                                  //'void android.widget.TextView.setText(java.lang.CharSequence)'
        });                                                                    // on a null object reference"
    }*/


}
