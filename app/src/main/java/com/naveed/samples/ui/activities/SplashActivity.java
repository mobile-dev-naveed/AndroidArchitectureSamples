package com.naveed.samples.ui.activities;

import android.os.Bundle;

import com.naveed.samples.R;
import com.naveed.samples.helper.base.SplashBase;

public class SplashActivity extends SplashBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);
        startDelay();
    }

    @Override
    protected void onDelayComplete() {
        launchActivity(MainActivity.class);
        finish();
    }
}
