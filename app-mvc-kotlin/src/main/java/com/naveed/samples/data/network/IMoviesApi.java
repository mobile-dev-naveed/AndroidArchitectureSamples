package com.naveed.samples.data.network;


import com.naveed.samples.data.models.ListMoviesResp;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IMoviesApi {


    @GET("movie/{type}")
    Call<ListMoviesResp> getMoviesList(@Path("type") String type,
                                       @Query("api_key") String apiKey,
                                       @Query("language") String language,
                                       @Query("page") int page,
                                       @Query("region") String region);

    @GET("search/movie")
    Call<ListMoviesResp> getSearchAbleResultsMoviesList(@Query("api_key") String apiKey,
                                       @Query("query") String language,
                                       @Query("page") int page);



}


