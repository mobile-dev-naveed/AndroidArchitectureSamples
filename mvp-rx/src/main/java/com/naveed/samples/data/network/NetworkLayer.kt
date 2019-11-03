package com.naveed.samples.data.network

import org.greenrobot.eventbus.EventBus

import com.naveed.samples.apputils.events.SingleDataEvent
import com.naveed.samples.data.models.ListMoviesResp
import com.naveed.samples.helper.utils.AppConstants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkLayer private constructor() {
    private val unknownError = "Unknown Error Occurred"

    //
    fun getMoviesList(listType: String, pageIndex: Int) {
        val call = MoviesServiceFactory.instance.getMoviesList(listType, AppConstants.API_KEY_IMDB, AppConstants.LANG, pageIndex, "")
        call.enqueue(object : Callback<ListMoviesResp> {
            override fun onResponse(call: Call<ListMoviesResp>, response: Response<ListMoviesResp>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    postEventSingleResponse(true, EventBusKeys.MOVIE_LIST, "", resp)
                } else {
                    postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, unknownError, null)

                }
            }

            override fun onFailure(call: Call<ListMoviesResp>, t: Throwable) {
                postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, t.message!!, null)
            }
        })
    }

    fun getSearchMoviesList(query: String, pageIndex: Int) {
        val call = MoviesServiceFactory.instance.getSearchAbleResultsMoviesList(AppConstants.API_KEY_IMDB, query, pageIndex)
        call.enqueue(object : Callback<ListMoviesResp> {
            override fun onResponse(call: Call<ListMoviesResp>, response: Response<ListMoviesResp>) {
                if (response.isSuccessful) {
                    val resp = response.body()
                    postEventSingleResponse(true, EventBusKeys.MOVIE_LIST, "", resp)
                } else {
                    postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, unknownError, null)

                }
            }

            override fun onFailure(call: Call<ListMoviesResp>, t: Throwable) {
                postEventSingleResponse(false, EventBusKeys.MOVIE_LIST, t.message!!, null)
            }
        })
    }


    fun postEventSingleResponse(status: Boolean, responseId: Int, message: String, data: Any?) {
        val eventBusData = SingleDataEvent(status, responseId, message, data)
        EventBus.getDefault().post(eventBusData)
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
