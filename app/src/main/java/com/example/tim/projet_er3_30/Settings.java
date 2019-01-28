package com.example.tim.projet_er3_30;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import android.widget.ArrayAdapter;

import android.content.Intent;


public class Settings extends AppCompatActivity {

    //Declarations
    public Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        addItemsOnSpinner();
    }

    public void onClickSubmit (View view) {

        if (String.valueOf(spinner.getSelectedItem()) == "Classic") {
            Intent myIntentClassic = new Intent(Settings.this,Classic_Controler.class);
            startActivityForResult(myIntentClassic,0);
        }

        else if (String.valueOf(spinner.getSelectedItem()) == "Double Slider") {
            Intent myIntentSliders = new Intent(Settings.this, Double_Slider.class);
            startActivityForResult(myIntentSliders,0);
        }

        else if (String.valueOf(spinner.getSelectedItem()) == "Joystick") {
            Intent myIntentJoystick = new Intent(Settings.this, Joystick_Controler.class);
            startActivityForResult(myIntentJoystick,0);
        }

        else if (String.valueOf(spinner.getSelectedItem()) == "Pad") {
            Intent myIntentPad = new Intent(Settings.this, Pad.class);
            startActivityForResult(myIntentPad,0);
        }

        else if (String.valueOf(spinner.getSelectedItem()) == "Gyroscope") {
            Intent myIntentGyroscope = new Intent(Settings.this, Gyroscope.class);
            startActivityForResult(myIntentGyroscope,0);
        }
    }

    private void addItemsOnSpinner () {

        //Assotiate the spinner to its Id
        spinner = findViewById(R.id.spinner);

        List<String> list = new ArrayList<String>();
        list.add("Pad");
        list.add("Classic");
        list.add("Double Slider");
        list.add("Joystick");
        list.add("Gyroscope");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void onClickBluetooth(View view){

        Intent myIntent = new Intent(Settings.this,Bluetooth.class);
        startActivityForResult(myIntent,0);
    }

}
