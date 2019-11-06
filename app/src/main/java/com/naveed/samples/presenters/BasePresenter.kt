package com.naveed.samples.presenters

import org.greenrobot.eventbus.EventBus

abstract class BasePresenter<T> {


    abstract fun onAttach(view: T)

}