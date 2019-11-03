package com.naveed.samples.ui.activities

import android.os.Bundle

import com.naveed.samples.R
import com.naveed.samples.helper.base.SplashBase

class SplashActivity : SplashBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spash)
        startDelay()
    }

    override fun onDelayComplete() {
        launchActivity(MainActivity::class.java)
        finish()
    }
}
