package com.naveed.samples.data.network

import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import com.naveed.samples.helper.utils.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


object MoviesServiceFactory {

    internal var iMoviesApi: IMoviesApi? = null
    val instance: IMoviesApi
        get() {
            if (iMoviesApi == null) {
                val gson = GsonBuilder()
                        .setLenient()
                        .create()

                val okHttpClient = OkHttpClient.Builder().readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .build()

                val retrofit = Retrofit.Builder()
                        .baseUrl(AppConstants.BASE_URL)
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .build()

                iMoviesApi = retrofit.create(IMoviesApi::class.java)
            }
            return iMoviesApi!!
        }


}
