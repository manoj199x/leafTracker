package com.web.leaftracker.base;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import okhttp3.ResponseBody;
import retrofit2.Call;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.web.leaftracker.MainActivity;
import com.web.leaftracker.R;
import com.web.leaftracker.Retrofit.APIClient;
import com.web.leaftracker.home.HomeFragment;
import com.web.leaftracker.interfaces.getfactoriesResponse;
import com.web.leaftracker.interfaces.getofferingsResponse;
import com.web.leaftracker.model.FactoryModel;
import com.web.leaftracker.model.SupplyModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class BaseFragment extends Fragment {

    ArrayList<SupplyModel> offeringsList;
    ArrayList<FactoryModel> factoryList;
    Dialog dialog = null;
    public  static final  String TAG = BaseFragment.class.getSimpleName();
    public FragmentManager fragmentManager;

    public BaseFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager =  ((BaseActivity)getContext()).getSupportFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        initializeProgressDialog();
        return inflater.inflate(R.layout.fragment_base, container, false);
    }

    protected void getOfferings(getofferingsResponse callback) {
        showProgressBar();
        offeringsList = new ArrayList<>();
        Call<ResponseBody> call ;
        try {
            showProgressBar();
            call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .gettodaysofferings("Bearer ");

            Log.d(TAG, "glhglh: " + call.request());
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String response_data = response.body().string();
                        Log.d(TAG, "get MyOrder response: " + response_data);
                        JSONObject myNewJsonObj = new JSONObject(response_data);
                        if (myNewJsonObj.getBoolean("success")) {
                            JSONArray jsonArray =  myNewJsonObj.getJSONObject("orders").getJSONArray("data");
                            Log.d(TAG, "jgjhkljg: " + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int order_id =0, order_status=0;
                                String  order_confirm_id="", total_price_with_tax="";
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                order_id = jsonObject.getInt("id");
                                order_confirm_id = jsonObject.getString("order_confirm_id");
                            }
                        } else {
                            Toast.makeText(getContext(), myNewJsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        if (callback != null) {
                            callback.getofferingsResponse(offeringsList);
                        }

                    } catch (Exception e) {
                        hideProgressBar();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            hideProgressBar();
        }
    }

    protected void getFactories(getfactoriesResponse callback) {
        showProgressBar();
        factoryList = new ArrayList<>();
        Call<ResponseBody> call =null;
        try {
            showProgressBar();
            call = APIClient
                    .getInstance()
                    .getApiInterface()
                    .getFactories("Bearer ");

            Log.d(TAG, "glhglh: " + call.request());
            call.enqueue(new retrofit2.Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String response_data = response.body().string();
                        Log.d(TAG, "get MyOrder response: " + response_data);
                        JSONObject myNewJsonObj = new JSONObject(response_data);
                        if (myNewJsonObj.getBoolean("success")) {
                            JSONArray jsonArray =  myNewJsonObj.getJSONObject("orders").getJSONArray("data");
                            Log.d(TAG, "jgjhkljg: " + jsonArray.length());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                int order_id =0, order_status=0;
                                String  order_confirm_id="", total_price_with_tax="";
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                order_id = jsonObject.getInt("id");
                                order_confirm_id = jsonObject.getString("order_confirm_id");
                            }
                        } else {
                            Toast.makeText(getContext(), myNewJsonObj.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                        if (callback != null) {
                            callback.getfactoriesResponse(factoryList);
                        }

                    } catch (Exception e) {
                        hideProgressBar();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            hideProgressBar();
        }
    }

    private void initializeProgressDialog() {
        if (dialog == null) {
            dialog = new Dialog(getContext());
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
}