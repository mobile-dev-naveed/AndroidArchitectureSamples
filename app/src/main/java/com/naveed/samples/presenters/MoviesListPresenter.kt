package com.naveed.samples.presenters


import android.annotation.SuppressLint
import com.naveed.samples.data.models.MovieItem
import com.naveed.samples.data.network.NetworkLayer
import com.naveed.samples.view.MoviesView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MoviesListPresenter @Inject constructor() : BasePresenter<MoviesView>(), MoviesPresenter {

    lateinit var moviesView : MoviesView

    override fun onAttach(mvpView: MoviesView) {
        moviesView = mvpView
    }


    var pageIndex = 1
    var loadMore = false

    var mMoviesList: MutableList<MovieItem> = ArrayList()



    val types = arrayOf("Popular", "Top rated", "Upcoming", "Now playing")
    val typesKeys = arrayOf("popular", "top_rated", "upcoming", "now_playing")

    var typeSelected: String = types[0]


    @SuppressLint("CheckResult")
    override fun requestMovies(query: String) {
        if (loadMore) {
            pageIndex++
            if (query.isNullOrEmpty()) {
                // Call Using Observable
                NetworkLayer.instance.getMoviesList(typeSelected, pageIndex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ moviesList ->
                            moviesList.results?.let {
                                mMoviesList.addAll(it)
                            }
                            moviesView.updateMoviesList()
                        }, { error ->
                            moviesView.onError(error.message!!)
                        })

            } else {
                // call using Single
                NetworkLayer.instance.getSearchMoviesList(query, pageIndex)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({ moviesList ->
                            moviesList.results?.let {
                                mMoviesList.addAll(it)
                            }
                            moviesView.updateMoviesList()
                        }, { error ->
                            moviesView.onError(error.message!!)
                        })
            }
        }
    }


    fun clearMoviesList() {
        pageIndex = 1
        loadMore = true
        mMoviesList.clear()
        moviesView.updateMoviesList()
    }

}
