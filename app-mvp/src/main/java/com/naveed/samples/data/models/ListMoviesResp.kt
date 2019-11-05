package com.naveed.samples.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListMoviesResp {

    @SerializedName("page")
    @Expose
    var page: Int? = null
    @SerializedName("total_results")
    @Expose
    var totalResults: Int? = null
    @SerializedName("total_pages")
    @Expose
    var totalPages: Int? = null
    @SerializedName("results")
    @Expose
    var results: List<MovieItem>? = null

}