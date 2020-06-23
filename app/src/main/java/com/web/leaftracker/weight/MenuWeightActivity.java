package com.web.leaftracker.weight;

import android.os.Bundle;

import com.web.leaftracker.R;
import com.web.leaftracker.base.BaseActivity;


public class MenuWeightActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_menu);
        initToolbar("Menu");
    }
}
