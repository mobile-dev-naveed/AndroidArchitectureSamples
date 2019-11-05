package com.naveed.samples.data.network


import com.naveed.samples.data.models.ListMoviesResp
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMoviesApi {


    @GET("movie/{type}")
    fun getMoviesList(@Path("type") type: String,
                      @Query("api_key") apiKey: String,
                      @Query("language") language: String,
                      @Query("page") page: Int,
                      @Query("region") region: String): Observable<ListMoviesResp>

    @GET("search/movie")
    fun getSearchAbleResultsMoviesList(@Query("api_key") apiKey: String,
                                       @Query("query") language: String,
                                       @Query("page") page: Int): Single<ListMoviesResp>


}


