package com.example.tim.projet_er3_30;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;


public class Bluetooth extends AppCompatActivity implements View.OnClickListener{

    public Button mButConnect =null;

    public int miMotorL = 0;
    public int miMotorR = 0;
    public int miCheckSum = 0;
    public int miBcl=0;

    public String mstrR ="";
    public String mstrL ="";
    public String mstrCheckSum ="";


    public BlueT mBluetooth;
    private Thread mThreadEnvoi = null;
    public String mstrTrame = "";

    public static TextView mTextView1 = null;
    static public Handler mHandler = new Handler() {

        public void handleMessage(Message msg) {

            String myString=(String) msg.obj;
            //Log.i("TextView", myString);
            //if ( myString !=""){
            mTextView1.setText(myString);
            //Log.i("TextView", myString);
            //}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Intent intentBluetooth = getIntent();
        //miMotorL = intentBluetooth.getIntExtra("oPMG",255);
        //miMotorR = intentBluetooth.getIntExtra("oPMD",255);

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_bluetooth);
        this.mButConnect = (Button) findViewById(R.id.button6);
        this.mButConnect.setOnClickListener(this);
        this.mTextView1 = (TextView)findViewById(R.id.textBuilding);
        this.mBluetooth= new BlueT(this, mHandler);

        this.mThreadEnvoi = new Thread(new Runnable() {
            @Override
            public void run() {

                while(true){

                    if(mBluetooth.mbtConnected == true) {

//                        miMotorR = Pad.oPMDreturn();
//                        miMotorL = Pad.oPMGreturn();

//                        miMotorR = Classic_Controler.oPMDreturn();
//                        miMotorL = Classic_Controler.oPMGreturn();

                        miMotorR = Double_Slider.oPMDreturn();
                        miMotorL = Double_Slider.oPMGreturn();

//                        miMotorR = Joystick_Controler.oPMDreturn();
//                        miMotorL = Joystick_Controler.oPMGreturn();

//                        miMotorR = Gyroscope.oPMDreturn();
//                        miMotorL = Gyroscope.oPMGreturn();





                        if(miMotorL<100){
                            mstrL="0"+Integer.toString(miMotorL);
                            if(miMotorL<10){
                                mstrL="00"+Integer.toString(miMotorL);
                            }
                        }

                        else{
                            mstrL=Integer.toString(miMotorL);
                        }

                        if(miMotorR<100){
                            mstrR="0"+Integer.toString(miMotorR);

                            if(miMotorR<10){
                                mstrR="00"+Integer.toString(miMotorR);
                            }
                        }

                        else{
                            mstrR=Integer.toString(miMotorR);
                        }

                        if(miCheckSum<1000) {
                            mstrCheckSum = "0" + Integer.toString(miCheckSum);

                            if (miCheckSum < 100) {
                                mstrCheckSum = "00" + Integer.toString(miCheckSum);

                                if (miCheckSum < 10) {
                                    mstrCheckSum = "000" + Integer.toString(miCheckSum);
                                }
                            }
                        }

                        else{
                            mstrCheckSum=Integer.toString(miCheckSum);
                        }

                        mstrTrame= mstrR+mstrL+mstrCheckSum+"\0";
                        //Log.i("Tx trame", mstrTrame);
                        mBluetooth.envoi(mstrTrame);

                        miBcl++;

                        miCheckSum = miMotorR+ miMotorL;
                    }

                    try {
                        Thread.sleep(10, 0);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        mThreadEnvoi.start();
    }

    public void onClick(View v) {

        switch(v.getId()) { // who click ?

            //Bluetooth connexion Button
            case R.id.button6:
                this.mBluetooth.connexion();
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy(); // nothing special
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
