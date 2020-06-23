package com.web.leaftracker.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.web.leaftracker.R;
import com.web.leaftracker.base.BaseActivity;
import com.web.leaftracker.external.Constants;
import com.web.leaftracker.external.ServiceManager;
import com.web.leaftracker.home.MainActivity;

import java.util.List;
import java.util.Locale;


public class RegisterActivity extends BaseActivity implements LocationListener {
    String TAG = RegisterActivity.class.getName();
    EditText  nameTxt, phoneTxt, emailTxt, locationTxt, addressTxt, stateTxt, distTxt, pinTxt,
            passwordTxt, conPasswordTxt;

    String registration = "", name = "",  phone = "", email = "", latitude = "", longitude = "", location = "", address = "",
            state = "", dist = "",  password = "", conPassword = "";
    Button registerBtn;
    TextView registerLink;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean isValidEmail = true;
    String loginType = "";
    AlertDialog alertDialog;
    public static Location MyLocation = null;
    public static String Location_Address = "";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    LocationManager locationManager;
    RelativeLayout imgLyt;
    static final int REQUEST_CODE_QR_SCAN = 101;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initToolbar("Register");
        initView();
        initData();
        initListner();
    }

    private void initView() {
        try{
            imgLyt = (RelativeLayout) findViewById(R.id.imgLyt);
            nameTxt = (EditText) findViewById(R.id.nameTxt);
            phoneTxt = (EditText) findViewById(R.id.phoneTxt);
            emailTxt = (EditText) findViewById(R.id.emailTxt);
            locationTxt = (EditText) findViewById(R.id.locationTxt);
            addressTxt = (EditText) findViewById(R.id.addressTxt);
            stateTxt = (EditText) findViewById(R.id.stateTxt);
            distTxt = (EditText) findViewById(R.id.distTxt);
            passwordTxt = (EditText) findViewById(R.id.passwordTxt);
            conPasswordTxt = (EditText) findViewById(R.id.conPasswordTxt);
            registerBtn = (Button) findViewById(R.id.registerBtn);
            registerLink = (TextView) findViewById(R.id.registerLink);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initListner() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ValidateRegisterData()) {
                    if (new ServiceManager(context).hasInternet()) {
                        //doRegister();
                    } else {
                        Toast.makeText(context, Constants.no_internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        imgLyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (haveCameraPermission()) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean haveCameraPermission() {
        boolean havePermission = true;
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                havePermission = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        RegisterActivity.this, Manifest.permission.CAMERA)) {

                } else {
                    ActivityCompat.requestPermissions(RegisterActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return havePermission;
        }
    }


    public boolean ValidateRegisterData() {
        try{
            name = nameTxt.getText().toString();
            phone = phoneTxt.getText().toString();
            email = emailTxt.getText().toString();
            location = locationTxt.getText().toString();
            address = addressTxt.getText().toString();
            state = stateTxt.getText().toString();
            dist = distTxt.getText().toString();
            password = passwordTxt.getText().toString();
            conPassword = conPasswordTxt.getText().toString();

            nameTxt.setError(null);
            phoneTxt.setError(null);
            emailTxt.setError(null);
            locationTxt.setError(null);
            addressTxt.setError(null);
            stateTxt.setError(null);
            distTxt.setError(null);
            pinTxt.setError(null);
            passwordTxt.setError(null);
            conPasswordTxt.setError(null);


            if (TextUtils.isEmpty(name)) {
                setErrorInputLayout(nameTxt, getString(R.string.err_invalid_name));
                return false;
            }
            if (TextUtils.isEmpty(phone)) {
                setErrorInputLayout(phoneTxt, getString(R.string.err_invalid_phone_no));
                return false;
            } else if (phone.length() < 10) {
                setErrorInputLayout(phoneTxt, getString(R.string.err_invalid_phone_no));
                return false;
            }
            if (TextUtils.isEmpty(password)) {
                setErrorInputLayout(passwordTxt, getString(R.string.err_invalid_login_pass));
                return false;
            } else if (password.length() < 4) {
                setErrorInputLayout(passwordTxt, getString(R.string.err_invalid_login_pass));
                return false;
            }
            if (TextUtils.isEmpty(conPassword)) {
                setErrorInputLayout(conPasswordTxt, getString(R.string.err_invalid_pass_confirm));
                return false;
            } else if (conPassword.length() < 4) {
                setErrorInputLayout(conPasswordTxt, getString(R.string.err_invalid_pass_confirm));
                return false;
            }

            if (!password.equalsIgnoreCase(conPassword)) {
                setErrorInputLayout(conPasswordTxt, getString(R.string.err_invalid_pass_confirm));
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }

    public static void setErrorInputLayout(EditText editText, String msg) {
        editText.requestFocus();
        editText.setError(msg);
    }

    private void initData() {
        try{
            ServiceManager.CheckPermission(this);
            if (checkPlayServices()) {
                startFusedLocation();
                registerRequestUpdate(this);
            }
            getLocation();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                Toast.makeText(getApplicationContext(),
                        "This device is supported. Please download google play services", Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void startFusedLocation() {
        try{
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnectionSuspended(int cause) {
                            }

                            @Override
                            public void onConnected(Bundle connectionHint) {
                                try {
                                    registerRequestUpdate(RegisterActivity.this);
                                } catch (Exception e) {
                                }
                            }
                        }).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {

                            @Override
                            public void onConnectionFailed(ConnectionResult result) {

                            }
                        }).build();
                mGoogleApiClient.connect();
            } else {
                mGoogleApiClient.connect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void registerRequestUpdate(final LocationListener listener) {
        try{
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(1000); // every second

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, listener);
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (!isGoogleApiClientConnected()) {
                            mGoogleApiClient.connect();
                        }
                        registerRequestUpdate(listener);
                    }
                }
            }, 1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isGoogleApiClientConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }
    public void getLocation() {
        try {
            if (checkPlayServices()) {
                startFusedLocation();
                registerRequestUpdate(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            try {
                Location loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                if (loc == null) {
                    Location loc1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    locationTxt.setText(loc1.getLatitude() + " , " + loc1.getLongitude());
                    latitude = Double.toString(loc1.getLatitude());
                    longitude = Double.toString(loc1.getLongitude());
                    MyLocation = loc1;
                } else {
                    MyLocation = loc;
                    locationTxt.setText(loc.getLatitude() + " , " + loc.getLongitude());
                    latitude = Double.toString(loc.getLatitude());
                    longitude = Double.toString(loc.getLongitude());
                }
                try {
                    Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                    address = addresses.get(0).getAddressLine(0);
                    locationTxt.setText(locationTxt.getText() + "\n" + addresses.get(0).getAddressLine(0));
                    Log.d(TAG, "location: " + loc.getLatitude());
                    Log.d(TAG, "location1: " + locationTxt.getText() + "\n " + addresses.get(0).getAddressLine(0));
                } catch (Exception e) {
                    address = "";
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Error", e.toString());
            }

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    MyLocation = location;
                    locationTxt.setText(location.getLatitude() + " , " + location.getLongitude());
                    latitude = Double.toString(location.getLatitude());
                    longitude = Double.toString(location.getLongitude());
                    //Toast.makeText(MainActivity.this, "Get Location From GPS", Toast.LENGTH_SHORT).show();
                    Log.d("", "Get Location From GPS  Lat:" + latitude + "  Lng:  " + longitude);
                    try {
                        Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        address = addresses.get(0).getAddressLine(0);
                        locationTxt.setText(locationTxt.getText() + "\n" + addresses.get(0).getAddressLine(0));
                    } catch (Exception e) {
                        address = "";
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
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    /*public void doRegister() {
        try {
            Log.d(TAG, "jfjgj: " + isStg + " " + isCollector + " " + isBlf);
            ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.VISIBLE);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            Log.d("Path ", Utils.RegisterPath);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utils.RegisterPath,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                            Log.d("Response Result", response);
                            try {
                                JSONObject myNewJsonObj = new JSONObject(response);
                                if (myNewJsonObj.getBoolean("success")) {
                                    saveMyProfile(myNewJsonObj.getString("name"), myNewJsonObj.getString("email"), myNewJsonObj.getString("phone"),
                                            myNewJsonObj.getString("registration_no"), myNewJsonObj.getString("fathername"), myNewJsonObj.getString("address"),
                                            myNewJsonObj.getString("dist"), myNewJsonObj.getString("state"), myNewJsonObj.getString("pin"), myNewJsonObj.getString("isStg"),
                                            myNewJsonObj.getString("isCollector"), myNewJsonObj.getString("isBlf"));


                                } else {
                                    String msg = "Unable to login";
                                    msg = myNewJsonObj.getString("error");

                                    Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                Log.d("JSONException Result", e.toString());
                                Toast.makeText(RegisterActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Log.d("JSONException Result", e.toString());
                                Toast.makeText(RegisterActivity.this, "Unable to login", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    ((ProgressBar) findViewById(R.id.progress)).setVisibility(View.GONE);
                    try {
                        Log.d("error 1 ", "" + error.getLocalizedMessage());
                        Log.d("error ", "" + error.getMessage());
                        Toast.makeText(getApplicationContext(), "Unable to register", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("name", name);
                    params.put("registration_no", registration);
                    params.put("fathername", fatherName);
                    params.put("phone_no", phone);
                    params.put("email", email);
                    params.put("latitude", latitude);
                    params.put("longitude", longitude);
                    params.put("address", address);
                    params.put("dist", dist);
                    params.put("state", state);
                    params.put("pin", pin);
                    params.put("password", password);
                    params.put("isStg", Integer.toString(isStg));
                    params.put("isCollector", Integer.toString(isCollector));
                    params.put("isBlf", Integer.toString(isBlf));
                    params.put("Content-Type", "application/json");
                    return params;
                }

            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    3000,
                    3,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(stringRequest);
        } catch (Exception e) {
        }
    }*/

    public void saveMyProfile(String name, String email, String phone, String registration_no, String fathername, String address,
                              String dist, String state, String pin, String isStg, String isCollector, String isBlf) {
        try {
            Log.d(TAG, "llgf: " + isStg + " " + isCollector + " " + isBlf);

            if (Integer.parseInt(isStg) == 1 && Integer.parseInt(isCollector) == 0 && Integer.parseInt(isBlf) == 0) {
                prefManager.createLogin(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
                prefManager.setKeyCurrentLogin(Constants.stgLogin);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                //intent.putExtra("LoginType", Constants.stgLogin);
                startActivity(intent);
                finish();
            } else if (Integer.parseInt(isStg) == 0 && Integer.parseInt(isCollector) == 1 && Integer.parseInt(isBlf) == 0) {
                prefManager.createLogin(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
                prefManager.setKeyCurrentLogin(Constants.collectorLogin);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                //intent.putExtra("LoginType", Constants.collectorLogin);
                startActivity(intent);
                finish();
            } else if (Integer.parseInt(isStg) == 0 && Integer.parseInt(isCollector) == 0 && Integer.parseInt(isBlf) == 1) {
                prefManager.createLogin(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
                prefManager.setKeyCurrentLogin(Constants.blfLogin);
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                //intent.putExtra("LoginType", Constants.blfLogin);
                startActivity(intent);
                finish();
            } else {
                //showPopUp(name, email, phone, registration_no, fathername, address, dist, state, pin, isStg, isCollector, isBlf);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onLocationChanged(Location location) {

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
                AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
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
            /*String[] ResultArray = result.trim().split("[\\r\\n]+");
            final String phone_no = ResultArray[1].substring(ResultArray[1].indexOf(":") + 1, ResultArray[1].length());
            Log.d(TAG, "jgjj1: " + phone_no);*/

            AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
            alertDialog.setTitle("Scan result");
            alertDialog.setMessage(result);
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
    }
}

