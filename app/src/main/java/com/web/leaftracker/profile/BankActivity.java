package com.web.leaftracker.profile;

import android.os.Bundle;

import com.web.leaftracker.R;
import com.web.leaftracker.base.BaseActivity;

public class BankActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bank);
        initToolbar("Bank Details");
    }
}
