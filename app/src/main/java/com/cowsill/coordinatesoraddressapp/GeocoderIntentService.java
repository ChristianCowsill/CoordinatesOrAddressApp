package com.cowsill.coordinatesoraddressapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

public class GeocoderIntentService extends IntentService {

    private static final String TAG = "GeocoderIntentService";

    public GeocoderIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        boolean isAddress = intent.getBooleanExtra("addressOrCoord", false);

        if(isAddress){
            Log.i(TAG, "onHandleIntent: " + "Address");
        } else {
            Log.i(TAG, "onHandleIntent: " + "Coordinates");
        }
    }
}
