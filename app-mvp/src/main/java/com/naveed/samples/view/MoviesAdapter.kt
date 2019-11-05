package com.naveed.samples.view

import android.content.Context
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.bumptech.glide.Glide

import com.naveed.samples.R
import com.naveed.samples.data.models.MovieItem
import com.naveed.samples.helper.base.RecyclerBaseAdapter
import com.naveed.samples.helper.utils.AppConstants
import kotlinx.android.synthetic.main.movie_list_row.view.*

class MoviesAdapter(context: Context, moviesList: List<MovieItem>, isGrid: Boolean) : RecyclerBaseAdapter<MovieItem, MoviesAdapter.ViewHolder>() {
    var isVerticalList = true

    init {
        this.mDataList = moviesList
        this.isVerticalList = !isGrid
        mContext  = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(if (isVerticalList) R.layout.movie_list_row else R.layout.movie_grid_row, parent, false)
        return ViewHolder(itemView)
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        holder.imageView!!.requestFocus()
        super.onViewAttachedToWindow(holder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (_, _, _, voteAverage, title, _, posterPath) = mDataList[position]
        holder.title.text = title
        if (posterPath!=null)
        Glide.with(this.mContext).load("${AppConstants.IMAGE_BASE_URL}$posterPath!!}").into(holder.imageView)
        holder.tvRating.text = voteAverage.toString() + " " + mContext.getString(R.string.star)
        holder.cardView.setOnClickListener { v ->
            if (itemSelectedListener != null) {
                itemSelectedListener.onItemClick(null, v, position, v.id.toLong())
            }
        }


    }

    override fun getItemCount(): Int {
        return mDataList.size
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.tvMovieName
        val tvRating: TextView = view.tvRating
        val imageView: ImageView = view.imvMovie
        val cardView: CardView = view.cardView
    }
}