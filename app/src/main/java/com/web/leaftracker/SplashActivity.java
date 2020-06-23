package com.web.leaftracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.web.leaftracker.auth.LoginActivity;
import com.web.leaftracker.base.BaseActivity;
import com.web.leaftracker.home.MainActivity;

public class SplashActivity extends BaseActivity {
    String TAG = SplashActivity.class.getSimpleName();
    int app_version;
    TextView versionTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);
            versionTxt = findViewById(R.id.versionTxt);
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            app_version = pInfo.versionCode;
            versionTxt.setText(pInfo.versionName);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isPermissionGranted()) {
                        /*if (Constants.isInternetOn(context)) {
                            getVersionCode();
                        } else {
                            mainMethod();
                        }*/
                        mainMethod();
                    }
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public void getVersionCode() {
        try {
            final Call<ResponseBody> call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .getVersion();

            Log.d(TAG, "version Request : " + call.request());
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String response_data = response.body().string();
                        Log.d(TAG, "version response: " + response_data);
                        JSONObject myNewJsonObj = new JSONObject(response_data);
                        if (Integer.parseInt(myNewJsonObj.getString("version").trim()) > app_version) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashActivity.this, R.style.MyAlertDialogStyle);
                            builder.setTitle("Confirm")
                                    .setIcon(R.drawable.app_logo)
                                    .setTitle("Update to latest")
                                    .setMessage("Please update your app to get the latest features!")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(Constants.APP_LINK));
                                                startActivity(intent);
                                                finish();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }

                                    });
                            android.app.AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                            alertTheme(alertDialog);
                        }else{
                            mainMethod();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d(TAG, "Error");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            mainMethod();
        }
    }*/

    private void mainMethod() {
        try {
            if (prefManager.isLoggedIn()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("", "Permission is granted");
                return true;
            } else {
                Log.d("", "Permission is revoked");
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1
                );
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.d("", "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("", "Permission is granted");
            //mainMethod();
            /*if (Constants.isInternetOn(context)) {
                getVersionCode();
            } else {
                mainMethod();
            }*/
            mainMethod();
        }
    }
}
