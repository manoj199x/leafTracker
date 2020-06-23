package com.web.leaftracker.external;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class ServiceManager {

    private Context _context;

    public ServiceManager(Context context){
        this._context = context;
    }
    public boolean hasInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo netInfo = connectivity.getActiveNetworkInfo();
            //doSync();

            return (netInfo != null && netInfo.isConnected());

        }
        return false;
    }

    public static void CheckPermission(Activity nActivity) {
        try {
            if (ContextCompat.checkSelfPermission(nActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(nActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(nActivity, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(nActivity, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        } catch (Exception e) {
        }
        try {
            if (ContextCompat.checkSelfPermission(nActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(nActivity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            }
        } catch (Exception e) {
        }
    }

    public static void loadAnimation(View view) {
        YoYo.with(Techniques.ZoomIn)
                .duration(400)
                .repeat(0)
                .playOn(view);
        //view.animation = AnimationUtils.loadAnimation(view.context, R.anim.popup_show)
    }
}
