package com.naveed.samples.helper.base

import android.os.Bundle
import android.os.Handler

/**
 * Created by naveedali on 10/8/17.
 */

abstract class SplashBase : BaseActivity() {
    private val activityClass: Class<*>? = null
    private lateinit var mHandler: Handler

    private var runnable: Runnable = Runnable { onDelayComplete() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mHandler = Handler()

    }

    protected fun startDelay() {
        mHandler.postDelayed(runnable, TIME_DELAY.toLong())
    }

    protected abstract fun onDelayComplete()

    companion object {
        private const val TIME_DELAY = 2500
    }

}
