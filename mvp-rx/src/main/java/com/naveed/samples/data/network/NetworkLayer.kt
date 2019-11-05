package com.naveed.samples.data.network

import com.naveed.samples.data.models.ListMoviesResp
import com.naveed.samples.helper.utils.AppConstants
import io.reactivex.Observable
import io.reactivex.Single

class NetworkLayer private constructor() {
    private val unknownError = "Unknown Error Occurred"


    fun getMoviesList(listType: String, pageIndex: Int) : Observable<ListMoviesResp> {
       return MoviesServiceFactory.instance.getMoviesList(listType,AppConstants.API_KEY_IMDB, AppConstants.LANG, pageIndex, "")
    }

    fun getSearchMoviesList(query: String, pageIndex: Int) :Single<ListMoviesResp>{
        return MoviesServiceFactory.instance.getSearchAbleResultsMoviesList(AppConstants.API_KEY_IMDB, query, pageIndex)
    }


    companion object {
        private var sNetworkLayer: NetworkLayer? = null


        val instance: NetworkLayer
            get() {
                if (sNetworkLayer == null) {
                    sNetworkLayer = newInstance
                }
                return sNetworkLayer!!
            }

        private val newInstance: NetworkLayer
            @Synchronized get() = sNetworkLayer ?: NetworkLayer()
    }

}
