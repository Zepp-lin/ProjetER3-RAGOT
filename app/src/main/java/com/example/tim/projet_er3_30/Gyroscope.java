package com.example.tim.projet_er3_30;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class Gyroscope extends AppCompatActivity {



    //Declarations
    private TextView mTxtAccX, mTxtAccX2;
    private TextView mTxtAccY, mTxtAccY2;

    //Acceleration vectors
    float[] gyroVector = new float[2];
    float[] gyroVectorBase = new float[2];

    boolean bSTART = false;
    float fPMG = 0, fPMD = 0;
    float fXCal = 0, fYCal = 0;
    int iRot = 0, iPM = 0;

    public static int oPMD = 255 ,oPMG = 255;


    @Override //Override the same method in mother class
    public void onCreate(Bundle savedInstanceState) {
        Sensor mAccelerometer;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gyroscope);
        SensorManager mSensorManager;
        // object mSensorManager od class  SensorManager manage sensors
        mSensorManager = (SensorManager) getSystemService(Gyroscope.SENSOR_SERVICE);
        // We use accelerometer sensor
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        //attributes are associated to text fields in the layout
        mTxtAccY = findViewById(R.id.textAccY);
        mTxtAccX = findViewById(R.id.textAccX);
        mTxtAccY2 = findViewById(R.id.textAccY2);
        mTxtAccX2 = findViewById(R.id.textAccX2);

        //attributes are the rotation and the acceleration in fonction of X and Y
        gyroVector[0] = 0;
        gyroVector[1] = 0;
    }


    /*
     *  create object able to receive information from sensor
     *  final : no derivation is possible (there is only 1 sensor)
     *  this syntax permits to declare an object of class SensorEventListener and at the same time instanciate it
     *  and declare the abstract method onSensorChanged
     */
    public final SensorEventListener mSensorListener = new SensorEventListener() {
        //Action if on sensor changes
        public void onSensorChanged(SensorEvent se) {
            if (se.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                gyroVectorBase = se.values;

                if (bSTART == true) {

                    if (gyroVectorBase[0] > 0) {
                        gyroVector[0] = (gyroVectorBase[0] - fXCal);
                    }
                    else if (gyroVectorBase[0] < 0) {
                        gyroVector[0] = (gyroVectorBase[0] + fXCal);
                    }
                    if (gyroVectorBase[1] > 0) {
                        gyroVector[1] = (gyroVectorBase[1] - fYCal);
                    }
                    else if (gyroVectorBase[1] < 0) {
                        gyroVector[1] = (gyroVectorBase[1] + fYCal);
                    }
                }
                iPM = (int) (-(gyroVector[0] * 10));
                iRot = (int) (-(gyroVector[1] * 9));

                ConvPM();
            }
            mTxtAccX2.setText(String.valueOf(iPM));
            mTxtAccY2.setText(String.valueOf(iRot));

        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) { } //Not used

    };

    //Engines power calculation class
    public void ConvPM(){
        if(iRot < -5) {
            fPMD = iPM;
            fPMG = (iPM*((90+iRot)/90));
        }

        else if(iRot > 5){
            fPMG = iPM;
            fPMD = (iPM*((90-iRot)/90));
        }

        else {
            fPMD = iPM;
            fPMG = iPM;
        }

        if (-5>iPM) {
            oPMD = (int) (255 + (iPM * 2.55));
            oPMG = (int) (255 + (iPM * 2.55));
        }

        else if(5<iPM){
            oPMD = (int) (255+(iPM * 2.55));
            oPMG = (int) (255+(iPM * 2.55));
        }

        else{
            oPMD = 255;
            oPMG = 255;
        }

        //Monitoring of the sent engines power values
        mTxtAccY.setText(String.valueOf(oPMG));
        mTxtAccX.setText(String.valueOf(oPMD));
    }

    public void onCalibration(View view) {
        bSTART = true;
        if (0<gyroVectorBase[0]) {
            fXCal = gyroVectorBase[0];
        }

        else {
            fXCal = -gyroVectorBase[0];
        }

        if (0<gyroVectorBase[1]) {
            fYCal = gyroVectorBase[1];
        }

        else {
            fYCal = -gyroVectorBase[1];
        }
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
