package com.naveed.samples.presenters

import com.naveed.samples.apputils.events.SingleDataEvent
import com.naveed.samples.data.models.ListMoviesResp
import com.naveed.samples.data.models.MovieItem
import com.naveed.samples.data.network.EventBusKeys
import com.naveed.samples.data.network.NetworkLayer
import com.naveed.samples.view.MoviesView
import org.greenrobot.eventbus.Subscribe

class MoviesListPresenter constructor(view: MoviesView): BasePresenter(), MoviesPresenter{

    var pageIndex = 1
    var loadMore = false

    var mMoviesList:MutableList<MovieItem> = ArrayList()
    val moviesView = view


    val types = arrayOf("Popular", "Top rated", "Upcoming", "Now playing")
    val typesKeys = arrayOf("popular", "top_rated", "upcoming", "now_playing")

    var typeSelected :String = types[0]


    override fun requestMovies(query: String) {
        if (loadMore) {
            pageIndex++
            if (query.isNullOrEmpty())
                NetworkLayer.instance.getMoviesList(typeSelected, pageIndex)
            else NetworkLayer.instance.getSearchMoviesList(query, pageIndex)
        }
    }


    fun clearMoviesList(){
        pageIndex = 1
        loadMore = true
        mMoviesList.clear()
        moviesView.updateMoviesList()
    }


    @Subscribe
    fun onApiResponse(event: SingleDataEvent<*>) {
        if (event.eventId == EventBusKeys.MOVIE_LIST) {
            if (event.status) {
                val resp = event.data as ListMoviesResp
                if (pageIndex == resp.totalPages!!.toInt()) {
                    loadMore = false
                }
                mMoviesList.addAll(resp.results!!)
                moviesView.updateMoviesList()
            } else {
                moviesView.onError(event.message)
            }

        }
    }


}
