package com.web.leaftracker.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;
import com.web.leaftracker.R;
import com.web.leaftracker.Widget.CircleImageView;
import com.web.leaftracker.auth.LoginActivity;
import com.web.leaftracker.profile.BankActivity;
import com.web.leaftracker.profile.ProfileActivity;
import com.web.leaftracker.base.BaseActivity;
import com.web.leaftracker.external.Constants;
import com.web.leaftracker.external.ServiceManager;
import com.web.leaftracker.weight.MenuWeightActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;


public class MainActivity extends BaseActivity implements LocationListener {
    String TAG = MainActivity.class.getName();
    public static double fusedLatitude = 26.190058;
    public static double fusedLongitude = 91.7684965;
    public static Location MyLocation = null;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static String Location_Address = "";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    LocationManager locationManager;
    public static String latitude = "", longitude = "";

    Toolbar toolbar;
    Boolean isExitOK = Boolean.FALSE;
    NavigationView nav_view;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle toggle;
    LinearLayout view_profile, home, logout;
    Context context;
    String loginType;
    TextView laginAs;
    Button changelogin, recapture;
    AlertDialog alertDialog;
    AlertDialog alertDialog1;
    CircleImageView person_image;
    TextView name, regis_no, GpsError;
    LinearLayout factoryLyt, vendorLyt, capturePhoto,menu_weight;
    Switch timeSwitch;
    ProgressBar progressBar;
    String ImageName;
    Button submit_btn;
    EditText vehicleNoTxt, teaWeightTxt, GpsLocation;
    String vehicleNo, teaWeight;
    String imageString;
    byte[] imageByte = null;
    String checkData;
    public static LinearLayout layoutGps;
    TextView stg_scan;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 102;
    private static final int REQUEST_CODE_QR_SCAN = 101;
    String stg_id;
    LinearLayout image_upload_lyt,bank_detail;
    ImageView tripImage;
    String trip_id ="";
    String Login_type="";
    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initData();
        initNavigation();
        initListner();

        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_from_left, R.anim.slide_to_right)
                .replace(R.id.frameContainer,new HomeFragment(),
                        Constants.TAGHomeFragment).commit();
    }

    private void initListner() {
        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.clearSession();
                closeDrawer();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
        menu_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                Intent intent = new Intent(MainActivity.this, MenuWeightActivity.class);
                startActivity(intent);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer();
            }
        });
        bank_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDrawer();
                Intent intent = new Intent(MainActivity.this, BankActivity.class);
                startActivity(intent);
            }
        });

        person_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefManager.getKeyIsOnTrip()) {
                    Toast.makeText(context, "You have not started your trip yet!", Toast.LENGTH_SHORT).show();
                } else {
                    /*if (ValidateData()) {
                        if (new ServiceManager(context).hasInternet()) {
                            if (ImageName != null) {
                                //onlineSubmitData();
                            } else {
                                Toast.makeText(context, "Please upload image!", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            Toast.makeText(context, Constants.no_internet, Toast.LENGTH_SHORT).show();
                        }
                    }*/
                }
            }
        });


    }

   /* public void startTrip(View v){
        if (new ServiceManager(context).hasInternet()) {
            Log.d(TAG, "jhjghjg: " + isChecked);
            vehicleNo = vehicleNoTxt.getText().toString();
            vehicleNoTxt.setError(null);
            if (isChecked){
                if (TextUtils.isEmpty(vehicleNo)) {
                    setErrorInputLayout(vehicleNoTxt, getString(R.string.err_invalid_vehicle_no));
                    timeSwitch.setChecked(prefManager.getKeyIsOnTrip());
                }else {
                    getDateTime(isChecked);
                }
            }else{
                getDateTime(isChecked);
            }

        } else {
            Toast.makeText(context, Constants.no_internet, Toast.LENGTH_SHORT).show();
            timeSwitch.setChecked(prefManager.getKeyIsOnTrip());
        }
    }*/

    public boolean haveCameraPermission() {
        boolean havePermission = true;
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                havePermission = false;
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        MainActivity.this, Manifest.permission.CAMERA)) {

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        } catch (Exception e) {
        } finally {
            return havePermission;
        }
    }


    public static void setErrorInputLayout(EditText editText, String msg) {
        editText.requestFocus();
        editText.setError(msg);
    }

    private void checkCameraPermission() {
        try {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            } else {
                startCamera();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCamera() {
        try {
            Long tsLong = System.currentTimeMillis() / 1000;
            ImageName = tsLong.toString();
            File sub = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            if (!sub.exists()) {
                sub.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            File sub1 = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name));
            if (sub1.exists()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                    //dateTime = sdf.format(new Date());
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/" + getString(R.string.app_name) + "/" + ImageName + ".jpg")));
                    startActivityForResult(cameraIntent, 1001);
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cameraIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        startActivityForResult(cameraIntent, 1001);
                    } catch (Exception ee) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }
    private void closeDrawer() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        }
    }

    private void initData() {
        try {
            /*Intent intent = getIntent();
            prefManager.setKeyCurrentLogin(intent.getStringExtra("LoginType"));*/
            //tripImage = (ImageView) findViewById(R.id.tripImage);
            bank_detail = findViewById(R.id.bank_detail);
            menu_weight = findViewById(R.id.menu_weight);
            layoutGps = (LinearLayout) findViewById(R.id.layoutGps);
            submit_btn = (Button) findViewById(R.id.submit_btn);
            person_image = (CircleImageView) findViewById(R.id.profile);
            regis_no = (TextView) findViewById(R.id.regis_no);
            name = (TextView) findViewById(R.id.name);
            view_profile = (LinearLayout) findViewById(R.id.view_profile);
            home = (LinearLayout) findViewById(R.id.home);
            logout = (LinearLayout) findViewById(R.id.logout);
            factoryLyt = (LinearLayout) findViewById(R.id.factoryLyt);
            vendorLyt = (LinearLayout) findViewById(R.id.vendorLyt);
            timeSwitch = (Switch) findViewById(R.id.timeSwitch);
            vehicleNoTxt = (EditText) findViewById(R.id.vehicleNo);
            teaWeightTxt = (EditText) findViewById(R.id.teaWeight);
            GpsLocation = (EditText) findViewById(R.id.GpsLocation);
            loginType = getIntent().getStringExtra("login_type");
            if (loginType.equalsIgnoreCase("1")){
                initToolbar("Vendor Dashboard");
                vendorLyt.setVisibility(View.VISIBLE);
                factoryLyt.setVisibility(View.GONE);
            }else if(loginType.equalsIgnoreCase("2")){
                initToolbar("Factory Dashboard");
                vendorLyt.setVisibility(View.GONE);
                factoryLyt.setVisibility(View.VISIBLE);
            }else{
                initToolbar("HG Dashboard");
                vendorLyt.setVisibility(View.GONE);
            }

            //name.setText(prefManager.getKeyName());
            //regis_no.setText(prefManager.getKeyRegistration());

            /*Log.d(TAG, "ghg: " + prefManager.getKeyStg() + " " + prefManager.getKeyCollector() + " " + prefManager.getKeyBlf());

            if (Integer.parseInt(prefManager.getKeyStg()) == 1 && Integer.parseInt(prefManager.getKeyCollector()) == 0 && Integer.parseInt(prefManager.getKeyBlf()) == 0) {
                changelogin.setVisibility(View.GONE);
            } else if (Integer.parseInt(prefManager.getKeyStg()) == 0 && Integer.parseInt(prefManager.getKeyCollector()) == 1 && Integer.parseInt(prefManager.getKeyBlf()) == 0) {
                changelogin.setVisibility(View.GONE);
            } else if (Integer.parseInt(prefManager.getKeyStg()) == 0 && Integer.parseInt(prefManager.getKeyCollector()) == 0 && Integer.parseInt(prefManager.getKeyBlf()) == 1) {
                changelogin.setVisibility(View.GONE);
            } else {
                changelogin.setVisibility(View.VISIBLE);
            }

            Log.d(TAG, "is swich data: " + prefManager.getKeyIsOnTrip());
            if (prefManager.getKeyIsOnTrip()) {
                timeSwitch.setChecked(true);
                //tripImage.setImageResource(R.drawable.green_dot);
                teaWeightTxt.setVisibility(View.VISIBLE);
                stg_scan.setVisibility(View.VISIBLE);
                image_upload_lyt.setVisibility(View.VISIBLE);
                vehicleNoTxt.setVisibility(View.GONE);
                submit_btn.setVisibility(View.VISIBLE);

            } else {
                timeSwitch.setChecked(false);
                //tripImage.setImageResource(R.drawable.red_dot);
                vehicleNoTxt.setVisibility(View.VISIBLE);
                teaWeightTxt.setVisibility(View.GONE);
                stg_scan.setVisibility(View.GONE);
                image_upload_lyt.setVisibility(View.GONE);
                submit_btn.setVisibility(View.GONE);
            }
            changeLogin();*/
            ServiceManager.CheckPermission(this);
            if (!CheckGpsStatus()) {
                layoutGps.setVisibility(View.VISIBLE);
            } else {
                layoutGps.setVisibility(View.GONE);
            }
            if (checkPlayServices()) {
                startFusedLocation();
                registerRequestUpdate(this);
            }
            getLocation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean CheckGpsStatus() {
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            return false;
        }
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
                    GpsLocation.setText(loc1.getLatitude() + " , " + loc1.getLongitude());
                    latitude = Double.toString(loc1.getLatitude());
                    longitude = Double.toString(loc1.getLongitude());
                    GpsError.setText("");
                    MyLocation = loc1;
                } else {
                    MyLocation = loc;
                    GpsLocation.setText(loc.getLatitude() + " , " + loc.getLongitude());
                    latitude = Double.toString(loc.getLatitude());
                    longitude = Double.toString(loc.getLongitude());
                    GpsError.setText("");
                }
                try {
                    Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                    Location_Address = addresses.get(0).getAddressLine(0);
                    GpsLocation.setText(GpsLocation.getText() + "\n" + addresses.get(0).getAddressLine(0));
                    GpsError.setText("");
                    Log.d(TAG, "location: " + loc.getLatitude());
                    Log.d(TAG, "location1: " + GpsLocation.getText() + "\n " + addresses.get(0).getAddressLine(0));
                } catch (Exception e) {
                    Location_Address = "";
                }


            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Error", e.toString());
            }

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, new android.location.LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    MyLocation = location;
                    GpsLocation.setText(location.getLatitude() + " , " + location.getLongitude());
                    latitude = Double.toString(location.getLatitude());
                    longitude = Double.toString(location.getLongitude());
                    //Toast.makeText(MainActivity.this, "Get Location From GPS", Toast.LENGTH_SHORT).show();
                    Log.d("", "Get Location From GPS  Lat:" + latitude + "  Lng:  " + longitude);
                    try {
                        Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        Location_Address = addresses.get(0).getAddressLine(0);
                        GpsLocation.setText(GpsLocation.getText() + "\n" + addresses.get(0).getAddressLine(0));
                        GpsError.setText("");
                    } catch (Exception e) {
                        Location_Address = "";
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

                    GpsError.setText("Waiting for GPS Location.\nPlease Enable GPS.");
                }
            });
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void startFusedLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnectionSuspended(int cause) {
                        }

                        @Override
                        public void onConnected(Bundle connectionHint) {
                            try {
                                registerRequestUpdate(MainActivity.this);
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
    }

    public void stopFusedLocation() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    public void registerRequestUpdate(final LocationListener listener) {
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

    public boolean isGoogleApiClientConnected() {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

/*    private void changeLogin() {
        Log.d(TAG, "hjjhj: " + prefManager.getKeyCurrentLogin());
        laginAs.setText(prefManager.getKeyCurrentLogin());
        if (prefManager.getKeyCurrentLogin().equalsIgnoreCase(Constants.stgLogin)) {
            stgLyt.setVisibility(View.VISIBLE);
            collectorLyt.setVisibility(View.GONE);
        } else if (prefManager.getKeyCurrentLogin().equalsIgnoreCase(Constants.collectorLogin)) {
            stgLyt.setVisibility(View.GONE);
            collectorLyt.setVisibility(View.VISIBLE);
        }
    }*/


    public void initToolbar(String title) {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    //set navigation menu
    private void initNavigation() {
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.closeDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        if (isExitOK) {
            super.onBackPressed();
        } else {
            isExitOK = true;
            Toast.makeText(this, "Press BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    isExitOK = false;
                }
            }, 1000);
        }
    }




    @Override
    public void onLocationChanged(Location location) {

    }



}
