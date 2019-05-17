package com.cowsill.coordinatesoraddressapp;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;

import java.io.IOException;
import java.util.List;

public class GeocoderIntentService extends IntentService {

    private static final String TAG = "GeocoderIntentService";

    public GeocoderIntentService(){
        super(TAG);
    }
    Geocoder mGeocoder;
    boolean mIsAddress;
    Intent mDisplayResultIntent;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        
        mIsAddress = intent.getBooleanExtra("addressOrCoord", false);
        mGeocoder = new Geocoder(this);
        if(mIsAddress){
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
        String addressString = null;

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
            addressString = address.getAddressLine(0);
            mDisplayResultIntent = new Intent(this, DisplayResult.class);
            mDisplayResultIntent.putExtra("isAddress", mIsAddress); // isAddress is FALSE
            mDisplayResultIntent.putExtra("address", addressString);
            mDisplayResultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mDisplayResultIntent);
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
            mDisplayResultIntent = new Intent(this, DisplayResult.class);
            mDisplayResultIntent.putExtra(Constants.IS_ADDRESS, mIsAddress); // isAddress is TRUE
            mDisplayResultIntent.putExtra(Constants.LATITUDE, latitudeString);
            mDisplayResultIntent.putExtra(Constants.LONGTITUDE, longtitudeString);
            mDisplayResultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(mDisplayResultIntent);
        }
    }
}
