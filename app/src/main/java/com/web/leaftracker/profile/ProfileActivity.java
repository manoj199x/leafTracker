package com.web.leaftracker.profile;

import android.os.Bundle;
import android.widget.TextView;

import com.web.leaftracker.R;
import com.web.leaftracker.Widget.CircleImageView;
import com.web.leaftracker.base.BaseActivity;


/**
 * Created by USER on 07-Jan-19.
 */

public class ProfileActivity extends BaseActivity {
    String TAG = ProfileActivity.class.getName();
    CircleImageView person_image;
    TextView person_name, regin_number, name, email, mobile, address, dist, state, pin, login_as;
    String loginType = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
        initToolbar("My Profile");
        initData();
    }

    private void initView() {
        try{
            person_image = (CircleImageView) findViewById(R.id.person_image);
            person_name = (TextView) findViewById(R.id.person_name);
            regin_number = (TextView) findViewById(R.id.regin_number);
            name = (TextView) findViewById(R.id.name);
            email = (TextView) findViewById(R.id.email);
            mobile = (TextView) findViewById(R.id.mobile);
            address = (TextView) findViewById(R.id.address);
            dist = (TextView) findViewById(R.id.dist);
            state = (TextView) findViewById(R.id.state);
            pin = (TextView) findViewById(R.id.pin);
            login_as = (TextView) findViewById(R.id.login_as);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initData() {
        try{
            person_name.setText(prefManager.getKeyName());
            regin_number.setText(prefManager.getKeyRegistration());
            name.setText(prefManager.getKeyName());
            email.setText(prefManager.getKeyEmail());
            mobile.setText(prefManager.getKeyMobile());
            address.setText(prefManager.getKeyAddress());
            dist.setText(prefManager.getKeyDist());
            state.setText(prefManager.getKeyState());
            pin.setText(prefManager.getKeyPin());
            login_as.setText(loginType);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
