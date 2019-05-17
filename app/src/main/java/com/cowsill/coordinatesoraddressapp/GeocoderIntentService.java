package com.cowsill.coordinatesoraddressapp;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class GeocoderIntentService extends IntentService {

    private static final String TAG = "GeocoderIntentService";

    public GeocoderIntentService(){
        super(TAG);
    }
    Geocoder mGeocoder;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        
        boolean isAddress = intent.getBooleanExtra("addressOrCoord", false);
        mGeocoder = new Geocoder(this);
        if(isAddress){
            getCoordinates(intent);
        } else {
            getAddress(intent);
        }
    }
    
    // If sent coordinates, we get the address
    private void getAddress(Intent intent){

        // Get coordinates from intent
        double latitude = intent.getDoubleExtra(Constants.LATITUDE, 0);
        double longtitude = intent.getDoubleExtra(Constants.LONGTITUDE, 0);

        List<Address> addresses = null;

        // Get array of addresses
        try{
            addresses = mGeocoder.getFromLocation(
                    latitude,
                    longtitude,
                    1
            );
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch (IllegalArgumentException illegalArgumentException){
            illegalArgumentException.printStackTrace();
        }

        // Handle case where no address found
        if(addresses == null || addresses.size() == 0){
            String errorMessage = getString(R.string.error_no_valid_address);
            Log.i(TAG, "getAddress: " + errorMessage);
        } else {
            Address address = addresses.get(0);
            String addressString = address.getAddressLine(0);
            Log.i(TAG, "getAddress: " + addressString);
        }

    }
    
    // If sent address, we get coordinates
    private void getCoordinates(Intent intent){

        // Get address from intent
        String addressFromIntent = intent.getStringExtra(Constants.ADDRESS);

        // Get array of addresses
        List<Address> addresses = null;
        try{
            addresses = mGeocoder.getFromLocationName(
                    addressFromIntent,
                    1
            );
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }catch (IllegalArgumentException illegalArgumentException){
            illegalArgumentException.printStackTrace();
        }

        // Handle case where no address found
        if(addresses == null || addresses.size() == 0){
            String errorMessage = getString(R.string.error_no_valid_coords);
            Log.i(TAG, "getAddress: " + errorMessage);
        } else {
            Address address = addresses.get(0);
            String latitudeString = String.valueOf(address.getLatitude());
            String longtitudeString = String.valueOf(address.getLongitude());
            Log.i(TAG, "getCoordinates: " + latitudeString + ", " + longtitudeString);
        }
    }
}
