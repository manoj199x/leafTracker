package com.web.leaftracker.factory;

import android.os.Bundle;

import com.web.leaftracker.R;
import com.web.leaftracker.base.BaseActivity;

public class FactoryHomeActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_menu);
        initToolbar("Menu");
    }
}
