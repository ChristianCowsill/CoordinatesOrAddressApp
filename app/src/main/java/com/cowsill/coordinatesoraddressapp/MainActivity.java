package com.cowsill.coordinatesoraddressapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    EditText etLatitude;
    EditText etLongtitude;
    EditText etAddress;
    ToggleButton tbSwitchQuery;
    Button btnGetResult;
    boolean isAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        etLatitude = findViewById(R.id.edLatitude);
        etLongtitude = findViewById(R.id.etLongtitude);
        etAddress = findViewById(R.id.etAddress);
        tbSwitchQuery = findViewById(R.id.tbSwitchQuery);
        btnGetResult = findViewById(R.id.btnGetResult);

        // Initialize boolean and enabled values of edittexts
        isAddress = true;
        etAddress.setEnabled(true);
        etLatitude.setEnabled(false);
        etLongtitude.setEnabled(false);

        tbSwitchQuery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){ // COORDINATES
                    if(buttonView.isPressed()){
                        changeEnabledStatus(etAddress);
                        changeEnabledStatus(etLatitude);
                        changeEnabledStatus(etLongtitude);
                        isAddress = false;
                    }
                } else{ // ADDRESS
                    if(buttonView.isPressed()){
                        changeEnabledStatus(etAddress);
                        changeEnabledStatus(etLatitude);
                        changeEnabledStatus(etLongtitude);
                        isAddress = true;
                    }
                }
            }
        });

        btnGetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentService();
            }
        });

    }

    private void startIntentService() {

        Intent startServiceIntent = new Intent(getApplicationContext(), GeocoderIntentService.class);
        startServiceIntent.putExtra("addressOrCoord", isAddress);
        if(isAddress){
            if(!etAddress.getText().toString().isEmpty()){
                String address = etAddress.getText().toString();
                startServiceIntent.putExtra("address", address);
                startService(startServiceIntent);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.error_no_address),
                        Toast.LENGTH_SHORT
                ).show();
            }
        } else {
            if(!(etLatitude.getText().toString().isEmpty() || etLongtitude.getText().toString().isEmpty())){
                double latitude = Double.parseDouble(etLatitude.getText().toString());
                double longtitude = Double.parseDouble(etLongtitude.getText().toString());
                startServiceIntent.putExtra("latitude", latitude);
                startServiceIntent.putExtra("longtitude", longtitude);
                startService(startServiceIntent);
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.error_no_lat_long),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

    }

    private void changeEnabledStatus(EditText editText){

        if(editText.isEnabled()){
            editText.setEnabled(false);
        } else {
            editText.setEnabled(true);
        }

        editText.setText("");
    }
}
