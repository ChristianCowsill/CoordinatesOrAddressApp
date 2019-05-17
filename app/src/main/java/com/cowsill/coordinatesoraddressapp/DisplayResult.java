package com.cowsill.coordinatesoraddressapp;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayResult extends AppCompatActivity {

    ConstraintLayout mAddressLayout;
    ConstraintLayout mCoordinatesLayout;
    TextView mTvAddress;
    TextView mTvLatitude;
    TextView mTvLongtitude;
    boolean mIsAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_result);

        Intent sentIntent = getIntent();
        mIsAddress = sentIntent.getBooleanExtra(Constants.IS_ADDRESS, false);

        // If TRUE, then we are now receiving coordinates
        if(mIsAddress){
            createCoordinatesLayout(sentIntent);
        // Otherwise, we are getting address
        } else {
            createAddressLayout(sentIntent);
        }


    }

    private void createCoordinatesLayout(Intent intent){

        // Instantiate UI items and make visible
        mCoordinatesLayout = findViewById(R.id.coordinatesLayout);
        mTvLatitude = findViewById(R.id.tvLatitude);
        mTvLongtitude = findViewById(R.id.tvLongtitude);
        mCoordinatesLayout.setVisibility(View.VISIBLE);

        mTvLatitude.setText(intent.getStringExtra(Constants.LATITUDE));
        mTvLongtitude.setText(intent.getStringExtra(Constants.LONGTITUDE));
    }

    private void createAddressLayout(Intent intent){

        // Instantiate UI items and make visible
        mAddressLayout = findViewById(R.id.addressLayout);
        mTvAddress = findViewById(R.id.tvAddress);
        mAddressLayout.setVisibility(View.VISIBLE);

        mTvAddress.setText(intent.getStringExtra(Constants.ADDRESS));
    }
}
