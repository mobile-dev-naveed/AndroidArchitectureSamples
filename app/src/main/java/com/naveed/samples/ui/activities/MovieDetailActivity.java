package com.naveed.samples.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.naveed.samples.R;
import com.naveed.samples.data.models.MovieItem;
import com.naveed.samples.helper.base.BaseActivity;
import com.naveed.samples.helper.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends BaseActivity {

    private MovieItem movieItem;

    @BindView(R.id.imvMovie)
    protected ImageView imvMovie;
    @BindView(R.id.imvMoviePoster)
    protected ImageView imvMoviePoster;
    @BindView(R.id.tvMovieName)
    protected TextView tvMovieName;

    @BindView(R.id.tvRating)
    protected TextView tvRating;
    @BindView(R.id.tvRatingCount)
    protected TextView tvRatingCount;
    @BindView(R.id.tvReleaseDate)
    protected TextView tvReleaseDate;
    @BindView(R.id.tvOverView)
    protected TextView tvOverView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras().getBundle("bundle");
            movieItem = bundle.getParcelable("data");
            loadDetails();
        }

    }


    private void loadDetails() {
        if (movieItem == null)
            return;

        tvMovieName.setText("Movie : " + movieItem.title);
        Glide.with(this).
                load(AppConstants.IMAGE_BASE_URL + movieItem.backdropPath).
                into(imvMovie);
        Glide.with(this).
                load(AppConstants.IMAGE_BASE_URL + movieItem.posterPath).
                into(imvMoviePoster);
        tvRating.setText("Rating : " + movieItem.voteAverage + " " + getString(R.string.star));
        tvRatingCount.setText("Rating Count : " + movieItem.voteCount);
        tvReleaseDate.setText("Release Date : " + movieItem.releaseDate);
        tvOverView.setText("Synopsis :\n" + movieItem.overview);


    }


}
