package com.naveed.samples.presenters

import com.naveed.samples.data.network.NetworkLayer

class DataLayer {

    lateinit var networkLayer: NetworkLayer
    init {
        networkLayer = NetworkLayer.instance
    }


   // fun getMovies()
}
