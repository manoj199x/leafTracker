package com.web.leaftracker.base;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.web.leaftracker.R;
import com.web.leaftracker.Retrofit.APIClient;
import com.web.leaftracker.external.PrefManager;
import com.web.leaftracker.interfaces.getofferingsResponse;
import com.web.leaftracker.model.SupplyModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 10;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 20;
    public Context context;
    public PrefManager prefManager;
    String userJson;
    ProgressDialog progressDialog;
    Dialog dialog = null;
    public Double longitudeFromMap = null;
    private int REQUEST_CODE_UPDATE = 1201;
    ArrayList<SupplyModel> offeringsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            context = getApplicationContext();
            prefManager = new PrefManager(this);
            progressDialog = new ProgressDialog(BaseActivity.this);
            AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
            Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

            appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        Log.d(TAG,"update avail: "+appUpdateInfoTask);
                        appUpdateManager.startUpdateFlowForResult(
                                appUpdateInfo,
                                AppUpdateType.IMMEDIATE,
                                this,
                                REQUEST_CODE_UPDATE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            initializeProgressDialog();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initializeProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.progress_view);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            LottieAnimationView animation_view = dialog.findViewById(R.id.animation_view);
            addColorFilterToLottieView(animation_view);
        }
    }

    private void addColorFilterToLottieView(LottieAnimationView view) {
        view.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );
    }


    public void showProgressBar() {
        if (dialog == null) {
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void hideProgressBar() {
        if (dialog == null) {
            return;
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void gridRecycler(RecyclerView recyclerView){
        RecyclerView.LayoutManager mLayoutManagerCat = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManagerCat);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    public void initToolbar(String title) {
        Toolbar toolbar;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        assert actionBar != null;
        if (title.equalsIgnoreCase("Login")){
            actionBar.setDisplayHomeAsUpEnabled(false);
        }else{
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public void alertTheme(AlertDialog alertDialog) {
        Button positiveBtn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        positiveBtn.setBackgroundColor(getResources().getColor(R.color.white));
        Button negativeBtn = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeBtn.setTextColor(getResources().getColor(R.color.colorPrimary));
        negativeBtn.setBackgroundColor(Color.WHITE);
    }

    public void EnableRuntimePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(BaseActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            }
            if (ContextCompat.checkSelfPermission(BaseActivity.this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {


                if (ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this,
                        Manifest.permission.CAMERA)) {

                } else {
                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }
            }
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.d(TAG, "Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }

}
