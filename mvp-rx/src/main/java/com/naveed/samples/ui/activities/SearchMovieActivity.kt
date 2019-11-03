package com.naveed.samples.ui.activities

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast

import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList

import com.naveed.samples.R
import com.naveed.samples.apputils.events.SingleDataEvent
import com.naveed.samples.data.models.ListMoviesResp
import com.naveed.samples.data.models.MovieItem
import com.naveed.samples.data.network.EventBusKeys
import com.naveed.samples.data.network.NetworkLayer
import com.naveed.samples.helper.base.BaseActivity
import com.naveed.samples.helper.utils.EndlessRecyclerViewScrollListener
import com.naveed.samples.helper.utils.NetworkConnection
import com.naveed.samples.ui.adapters.MoviesAdapter
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_main.*

class SearchMovieActivity : BaseActivity() {

    private var moviesAdapter: MoviesAdapter? = null
    private val mMoviesList = ArrayList<MovieItem>()

//    @BindView(R.id.recyclerView)
//    protected var recyclerView: RecyclerView? = null
//    @BindView(R.id.filterView)
//    protected var filterView: RelativeLayout? = null
//    @BindView(R.id.spFilter)
//    protected var spFilter: Spinner? = null

    private var loadMore = true
    private var pageIndex = 1
    private val isGrid = false
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setRecyclerAdapter()
        val intent = intent
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            this.query = query
            searchMovies(query)
        }

    }

    private fun setRecyclerAdapter() {
        moviesAdapter = MoviesAdapter(this, mMoviesList, isGrid)
        //moviesAdapter.isVerticalList = true;
        setAsList()
        recyclerView!!.adapter = moviesAdapter
        moviesAdapter!!.setItemSelectedListener { parent, view, position, id ->
            val bundle = Bundle()
            bundle.putParcelable("data", mMoviesList[position])
            launchActivity(MovieDetailActivity::class.java, bundle)
        }

    }


    private fun setAsGrid() {

        val manager = GridLayoutManager(this, 2)
        recyclerView!!.layoutManager = manager
        val scrollListener = object : EndlessRecyclerViewScrollListener(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (loadMore) {
                    pageIndex++
                    searchMovies(query)
                }
            }
        }
        recyclerView!!.addOnScrollListener(scrollListener)


    }

    private fun setAsList() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (loadMore) {
                    pageIndex++
                    searchMovies(query)
                }
            }
        }
        recyclerView!!.addOnScrollListener(scrollListener)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.filter).actionView as SearchView
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName))

        //menu.it
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            //            case R.id.filter:
            //                //Toast.makeText(getApplicationContext(), "Filter Selected", Toast.LENGTH_LONG).show();
            //                if (filterView.getVisibility() == View.VISIBLE) {
            //                    filterView.setVisibility(View.GONE);
            //                    //setMenuItemIcon(item, R.drawable.ic_filter);
            //                } else {
            //                    filterView.setVisibility(View.VISIBLE);
            //                    //setMenuItemIcon(item, R.drawable.ic_filter_selected);
            //                }
            //
            //                return true;
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setMenuItemIcon(item: MenuItem, resourceId: Int) {
        item.icon = ContextCompat.getDrawable(this, resourceId)
    }


    // Api calls


    private fun searchMovies(query: String) {
        if (NetworkConnection.isConnection(this)) {
            showProgressDialog(R.string.please_wait)
            NetworkLayer.instance.getSearchMoviesList(query, pageIndex)
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }


    // api requests
    @Subscribe
    fun onApiResponse(event: SingleDataEvent<*>) {
        if (event.eventId == EventBusKeys.MOVIE_LIST) {
            dismissProgress()
            if (event.status) {
                val resp = event.data as ListMoviesResp
                if (pageIndex == resp.totalPages!!.toInt()) {
                    loadMore = false
                }
                mMoviesList.addAll(resp.results!!)
                moviesAdapter!!.notifyDataSetChanged()
            } else {
                showToast(event.message)
            }

        }
    }


}
