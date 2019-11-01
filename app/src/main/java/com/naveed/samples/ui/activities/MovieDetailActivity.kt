package com.naveed.samples.ui.activities

import android.os.Bundle

import com.bumptech.glide.Glide

import com.naveed.samples.R
import com.naveed.samples.data.models.MovieItem
import com.naveed.samples.helper.base.BaseActivity
import com.naveed.samples.helper.utils.AppConstants
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetailActivity : BaseActivity() {

    private var movieItem: MovieItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)
        ButterKnife.bind(this)
        if (intent.extras != null) {
            val bundle = intent.extras!!.getBundle("bundle")
            movieItem = bundle!!.getParcelable("data")
            loadDetails()
        }

    }


    private fun loadDetails() {
        if (movieItem == null)
            return

        tvMovieName!!.text = "Movie :  ${movieItem!!.title!!}"
        Glide.with(this).load(AppConstants.IMAGE_BASE_URL + movieItem!!.backdropPath!!).into(imvMovie!!)
        Glide.with(this).load(AppConstants.IMAGE_BASE_URL + movieItem!!.posterPath!!).into(imvMoviePoster!!)
        tvRating!!.text = "Rating : " + movieItem!!.voteAverage + " " + getString(R.string.star)
        tvRatingCount!!.text = "Rating Count : " + movieItem!!.voteCount!!
        tvReleaseDate!!.text = "Release Date : " + movieItem!!.releaseDate!!
        tvOverView!!.text = "Synopsis :\n" + movieItem!!.overview!!


    }


}
