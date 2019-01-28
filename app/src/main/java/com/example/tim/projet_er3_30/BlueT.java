package com.example.tim.projet_er3_30;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.*;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


public class BlueT {

    private static final String TAG = "BTT";

    private BluetoothAdapter mbtAdapt; //BT adapter of the phone
    private Activity mActivity; //main activity who instantiate blueT -> association
    private BluetoothDevice[]mPairedDevices;// table of known devices

    private int mDeviceSelected = -1; //the device choosen by the phone
    private String[] mstrDeviceName;
    public boolean mbtConnected = false;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");  // dummy UUID
    private BluetoothSocket mSocket;
    private Handler mHandler;
    public Boolean mbtActif;
    public String mstrRecu = " ";


    public BlueT(Activity Activity) {

        this.mActivity = Activity;
        this.Verif();
    }

    public BlueT(Activity Activity, Handler Handler) {

        this.mActivity = Activity;
        this.mHandler = Handler;
        this.Verif();

        Thread mThreadReception =null;	//thread that receives data from device
        mThreadReception = new Thread(new Runnable() { //create Thread for reception
            @Override
            public void run() {

                while(true) {

                    //Log.i(TAG, "etat="+mbtActif);
                    if(mbtAdapt != null) {
                        //Log.i(TAG, "etat="+mbtAdapt);

                        if(mbtAdapt.isEnabled()) {

                            mbtActif = true;
                            //Log.i(TAG, "etat="+mbtActif);
                        }

                        else {

                            mbtActif = false;
                            //Log.i(TAG, "etat="+mbtActif);
                        }
                    }

                    // reception of data when connected
                    if(mbtConnected == true)  {

                        mstrRecu = reception();

                        if (!mstrRecu.equals("")) { // if there is something -> send message to the handler of the activity

                            Message msg = mHandler.obtainMessage();
                            msg.obj = mstrRecu;
                            mHandler.sendMessage(msg);
                            //Log.i("Recu", mstrRecu);
                        }
                        //else
                        //Log.i("mstrRecu", "vide");
                    }

                    try {
                        Thread.sleep(2, 0); // this has to be lower than the period of the robot
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                        //Log.i("IT", "mstrRecu");
                    }
                }
            }
        });

        mThreadReception.start(); //start thread
    }

    //Verification of BT adapter
    public void Verif() {

        mbtAdapt = BluetoothAdapter.getDefaultAdapter(); // recover BT information on adapter

        if(mbtAdapt == null) {
            Log.i(TAG, "Not presentt");
        }

        else {
            Log.i(TAG, "Present");
        }
    }

    //Connection to device
    public void connexion() {

        this.Device_Connu(); //recover informations for each connected devices
        AlertDialog.Builder adBuilder = new AlertDialog.Builder(mActivity);//pop up off knoxn devices
        //        adBuilder.setTitle("device");
        //miDeviceDelected = mDeviceSelected;

        adBuilder.setSingleChoiceItems(mstrDeviceName, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                mDeviceSelected = which;
                dialog.dismiss();
                tryconnect(); //connection to the chosen device
            }
        });

        AlertDialog adb = adBuilder.create();
        adb.show();
    }

    //Recover all known devices
    public void Device_Connu() {

        Set<BluetoothDevice> Devices; //liste of mDevices
        int iBlc = 0;				//used by connection
        Devices = mbtAdapt.getBondedDevices(); //recover the devices in a tab
        iBlc = Devices.size(); // number of known devices
        this.mstrDeviceName = new String[iBlc]; //table will be given to pop up menu
        iBlc = 0;

        for(BluetoothDevice dev : Devices) {
            this.mstrDeviceName[iBlc] = dev.getName();
            iBlc = iBlc + 1;
        }

        this.mPairedDevices = (BluetoothDevice[]) Devices.toArray(new BluetoothDevice[0]); //cast of set in array.
    }

    public void tryconnect() {

        try {
            this.mSocket =this.mPairedDevices[this.mDeviceSelected].createRfcommSocketToServiceRecord(MY_UUID); //connection to vhchoosen device via Socket, mUUID: id of BT on device of the target
            this.mSocket.connect();
            Toast.makeText(this.mActivity, "Connected", Toast.LENGTH_SHORT).show();
            this.mbtConnected = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.mActivity, "Try again", Toast.LENGTH_SHORT).show();
            try {
                mSocket.close();
            }
            catch(Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    // false -> error; true -> ok
    public Boolean envoi(String strOrdre) {
        OutputStream OutStream;	//mSocket for communication

        try	{
            OutStream = this.mSocket.getOutputStream(); //open output stream

            byte[] trame = strOrdre.getBytes();

            OutStream.write(trame); //send frame via output stream
            OutStream.flush();
            Log.i(TAG, "Send");
        }

        catch(Exception e2) {
            Log.i(TAG, "Error");
            tryconnect();
            try {
                this.mSocket.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            this.mbtConnected = false;
        }

        return this.mbtConnected;
    }

    private String reception() {
        InputStream InStream;		//mSocket for communication

        byte mbBuffer[] = new byte[200]; // large buffer !
        byte myByte;
        int iPos=0;
        String mstrData = "";

        try {
            InStream = this.mSocket.getInputStream();// input stream
            /*
            if(InStream.available() > 0 ) {
                // inBLu = number of characters
                // the following part has to be improved
                iNbLu=InStream.read(mbBuffer,iPos,199); // be aware -> a complete frame is not received
                mstrData = new String(mbBuffer,0,iNbLu); //create a string using byte received
            }
            */

            while (InStream.available() > 0 ) {

                myByte = (byte) InStream.read();
                mbBuffer[iPos] = myByte;

                if (myByte == 0) {
                    break;
                }

                iPos++;
            }

            mstrData = new String(mbBuffer, 0,iPos); //create a string using byte received
        }
        catch (Exception e) {
            Log.i(TAG, "Error");
            try {
                mSocket.close();
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }

            this.mbtConnected = false;
        }

        return mstrData;
    }

}
