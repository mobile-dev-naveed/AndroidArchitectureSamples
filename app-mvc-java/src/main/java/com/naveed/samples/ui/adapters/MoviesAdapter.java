package com.naveed.samples.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.naveed.samples.R;
import com.naveed.samples.data.models.MovieItem;
import com.naveed.samples.helper.base.RecyclerBaseAdapter;
import com.naveed.samples.helper.utils.AppConstants;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviesAdapter extends RecyclerBaseAdapter<MovieItem, MoviesAdapter.ViewHolder> {
    public boolean isVerticalList = true;
    private Context mContext;


    public MoviesAdapter(Context context, List<MovieItem> moviesList,boolean isGrid) {
        this.mDataList = moviesList;
        this.mContext = context;
        this.isVerticalList = !isGrid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(isVerticalList ? R.layout.movie_list_row : R.layout.movie_grid_row, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        holder.imageView.requestFocus();
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        MovieItem movie = mDataList.get(position);
        holder.title.setText(movie.title);
        Glide.with(mContext).load(AppConstants.IMAGE_BASE_URL + movie.posterPath).into(holder.imageView);
        holder.tvRating.setText(movie.voteAverage+ " "+mContext.getString(R.string.star) );
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemSelectedListener!=null){
                    itemSelectedListener.onItemClick(null,v,position,v.getId());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMovieName)
        public TextView title;
        @BindView(R.id.tvRating)
        public TextView tvRating;
        @BindView(R.id.imvMovie)
        public ImageView imageView;
        @BindView(R.id.cardView)
        public CardView cardView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }

    }
}