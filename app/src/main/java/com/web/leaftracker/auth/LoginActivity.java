package com.web.leaftracker.auth;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.web.leaftracker.R;
import com.web.leaftracker.Retrofit.APIClient;
import com.web.leaftracker.base.BaseActivity;
import com.web.leaftracker.external.Constants;
import com.web.leaftracker.external.ServiceManager;
import com.web.leaftracker.home.MainActivity;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class LoginActivity extends BaseActivity implements LocationListener {
    static String TAG = LoginActivity.class.getName();
    EditText user_id, password;
    public static double fusedLatitude = 26.190058;
    public static double fusedLongitude = 91.7684965;
    ImageView imgLogin;
    static final int REQUEST_CODE_QR_SCAN = 101;
    Button loginBtn;
    TextView registerLink;
    String loginType = "";
    AlertDialog alertDialog;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getLocation();
        initToolbar("Login");
        initData();
        initListner();

    }


    public static void setFusedLatitude(double lat) {
        fusedLatitude = lat;
    }

    public static void setFusedLongitude(double lon) {
        fusedLongitude = lon;
    }

    public double getFusedLatitude() {
        return fusedLatitude;
    }

    public double getFusedLongitude() {
        return fusedLongitude;
    }

    private void initListner() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();

            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }



    private void initData() {
        registerLink = (TextView) findViewById(R.id.registerLink);
        user_id = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.password);
        loginBtn = (Button) findViewById(R.id.loginBtn);
    }

    public void doLogin() {
        try {
            Log.d(TAG, "Validate login: " + ValidateLoginData());

            /*if (ValidateLoginData()) {

            }*/
            if (new ServiceManager(this).hasInternet()) {
                String LoginName = user_id.getText().toString().trim();
                String Password = password.getText().toString().trim();
                Intent intent = null;
                intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("login_type",LoginName);
                startActivity(intent);
                finish();
                //doLogin(LoginName, Password);

            } else {
                Toast.makeText(context, Constants.no_internet, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void doLogin(final String username, final String password) {

        try {
            showProgressBar();
            Call<ResponseBody> call = null;
            call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .login(username, password);
            Log.d(TAG, "call data login: " + call.request());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String response_data = response.body().string();
                        Log.d(TAG, "login response: " + response_data);
                        JSONObject myNewJsonObj = new JSONObject(response_data);
                        if (myNewJsonObj.getBoolean("success")) {
                            saveMyProfile(myNewJsonObj.getString("name"), myNewJsonObj.getString("email"), myNewJsonObj.getString("phone"),
                                    myNewJsonObj.getString("registration_no"), myNewJsonObj.getString("fathername"), myNewJsonObj.getString("address"),
                                    myNewJsonObj.getString("dist"), myNewJsonObj.getString("state"), myNewJsonObj.getString("pin"), myNewJsonObj.getString("isStg"),
                                    myNewJsonObj.getString("isCollector"), myNewJsonObj.getString("isBlf"));


                        } else {
                            Toast.makeText(context, myNewJsonObj.getString("error"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        hideProgressBar();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveMyProfile(String name, String email, String phone, String registration_no, String fathername, String address,
                              String dist, String state, String pin, String isStg, String isCollector, String isBlf) {
        try {
            Log.d(TAG, "llgf: " + isStg + " " + isCollector + " " + isBlf);

            if (Integer.parseInt(isStg) == 1 && Integer.parseInt(isCollector) == 0 && Integer.parseInt(isBlf) == 0) {
                prefManager.createLogin(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
                prefManager.setKeyCurrentLogin(Constants.stgLogin);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //intent.putExtra("LoginType", Constants.stgLogin);
                startActivity(intent);
                finish();
            } else if (Integer.parseInt(isStg) == 0 && Integer.parseInt(isCollector) == 1 && Integer.parseInt(isBlf) == 0) {
                prefManager.createLogin(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
                prefManager.setKeyCurrentLogin(Constants.collectorLogin);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                //intent.putExtra("LoginType", Constants.collectorLogin);
                startActivity(intent);
                finish();
            } else if (Integer.parseInt(isStg) == 0 && Integer.parseInt(isCollector) == 0 && Integer.parseInt(isBlf) == 1) {
                prefManager.createLogin(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
                prefManager.setKeyCurrentLogin(Constants.blfLogin);
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                 /*intent.putExtra("LoginType", Constants.blfLogin);*/
                startActivity(intent);
                finish();
            } else {
                //showPopUp(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean ValidateLoginData() {
        String LoginName = user_id.getText().toString().trim();
        String Password = password.getText().toString().trim();
        user_id.setError(null);
        password.setError(null);

        if (TextUtils.isEmpty(LoginName)) {
            setErrorInputLayout(user_id, getString(R.string.err_invalid_phone_no));
            return false;
        } else if (LoginName.length() < 10) {
            setErrorInputLayout(user_id, getString(R.string.err_invalid_phone_no));
            return false;
        }
        if (TextUtils.isEmpty(Password)) {
            setErrorInputLayout(password, getString(R.string.err_invalid_login_pass));
            return false;
        } else if (Password.length() < 4) {
            setErrorInputLayout(password, getString(R.string.err_invalid_login_pass));
            return false;
        }

        return true;
    }

    public static void setErrorInputLayout(EditText editText, String msg) {
        editText.requestFocus();
        editText.setError(msg);
    }


    @Override
    public void onLocationChanged(Location location) {
        try {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            Log.d(TAG, "COULD NOT GET A GOOD RESULT.");
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if (result != null) {
                AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
                alertDialog.setTitle("Scan Error");
                alertDialog.setMessage("QR Code could not be scanned");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return;

        }
        if (requestCode == REQUEST_CODE_QR_SCAN) {
            if (data == null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            Log.d(TAG, "Have scan result in your app activity :" + result);
            String[] ResultArray = result.trim().split("[\\r\\n]+");
            final String phone_no = ResultArray[1].substring(ResultArray[1].indexOf(":") + 1, ResultArray[1].length());
            Log.d(TAG, "jgjj1: " + phone_no);

            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            user_id.setText(phone_no);
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }

}

