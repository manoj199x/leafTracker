package com.web.leaftracker.external;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.view.View;

import com.web.leaftracker.home.MainActivity;


public class NetworkChangeReceiver extends BroadcastReceiver {


    private Context _context;
    @Override
    public void onReceive(Context context, Intent intent) {

        this._context=context;
        /*if(checkInternet(context))
        {
            try{
                LoginActivity.No_Internet_Layout.setVisibility(View.GONE);
            }catch (Exception e){}
            try{
                doSync();
            }catch (Exception e){}
        }else{
            try{
                LoginActivity.No_Internet_Layout.setVisibility(View.VISIBLE);
            }catch (Exception e){}
        }*/

        if(CheckGpsStatus(context))
        {
            try{
                MainActivity.layoutGps.setVisibility(View.GONE);
            }catch (Exception e){}
        }else{
            try{
                MainActivity.layoutGps.setVisibility(View.VISIBLE);
            }catch (Exception e){}
        }

    }

    boolean checkInternet(Context context) {
        ServiceManager serviceManager = new ServiceManager(context);
        if (serviceManager.hasInternet()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean CheckGpsStatus(Context context){
        boolean returnVal=false;
        try {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            returnVal=  locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(returnVal){
                try {
                    Location loc1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    MainActivity.latitude = Double.toString(loc1.getLatitude());
                    MainActivity.longitude = Double.toString(loc1.getLongitude());
                }catch (SecurityException e){}catch (Exception e){}
            }
        }catch (Exception e){
            return  false;
        }
        return returnVal;
    }

}
