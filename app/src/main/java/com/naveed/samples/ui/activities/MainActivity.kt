package com.naveed.samples.ui.activities

import android.app.SearchManager
import android.content.Context

import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Spinner
import android.widget.Toast

import org.greenrobot.eventbus.Subscribe

import java.util.ArrayList

import com.naveed.samples.R
import com.naveed.samples.apputils.events.SingleDataEvent
import com.naveed.samples.data.models.ListMoviesResp
import com.naveed.samples.data.models.MovieItem
import com.naveed.samples.data.network.EventBusKeys
import com.naveed.samples.data.network.NetworkController
import com.naveed.samples.helper.base.BaseActivity
import com.naveed.samples.helper.utils.EndlessRecyclerViewScrollListener
import com.naveed.samples.helper.utils.NetworkConnection
import com.naveed.samples.ui.adapters.MoviesAdapter
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var types = arrayOf("Popular", "Top rated", "Upcoming", "Now playing")
    var typesKeys = arrayOf("popular", "top_rated", "upcoming", "now_playing")
    internal var typeSelected = "popular"
    internal var typeSelectedTemp = "popular"
    private var moviesAdapter: MoviesAdapter? = null
    private val mMoviesList = ArrayList<MovieItem>()

    private var loadMore = true
    private var pageIndex = 1
    private val isGrid = false
    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        setRecyclerAdapter()
        setUpSpinner()
        addListeners()
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

    private fun setUpSpinner() {
        val dataAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, types)
        spFilter!!.adapter = dataAdapter
        spFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                typeSelectedTemp = typesKeys[position]
                onClickedFetchBtn()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }

    private fun addListeners(){
        btnFetch.setOnClickListener {
            onClickedFetchBtn()
        }
    }

    private fun setAsList() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView!!.layoutManager = layoutManager
        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                if (loadMore) {
                    pageIndex++
                    requestMovies()
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

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                this@MainActivity.query = s
                requestData()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                //Log.d("search","search text "+s);
                return false
            }
        })
        searchView.setOnCloseListener {
            this@MainActivity.query = ""
            filterView!!.visibility = View.VISIBLE
            requestData()
            false
        }
        val searchMenuItem = menu.findItem(R.id.filter)
        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                filterView!!.visibility = View.GONE
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                filterView!!.visibility = View.VISIBLE
                this@MainActivity.query = ""
                searchView.setQuery("", false)
                requestData()
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun setMenuItemIcon(item: MenuItem, resourceId: Int) {
        item.icon = ContextCompat.getDrawable(this, resourceId)
    }


    protected fun onClickedFetchBtn() {
        if (typeSelected != typeSelectedTemp) {
            mMoviesList.clear()
            moviesAdapter!!.notifyDataSetChanged()
            pageIndex = 1
        }
        typeSelected = typeSelectedTemp
        requestMovies()
    }

    // Api calls
    private fun requestData() {
        mMoviesList.clear()
        pageIndex = 1
        moviesAdapter!!.notifyDataSetChanged()
        if (query.isEmpty()) {
            requestMovies()
        } else {
            searchMovies(query)
        }
    }

    private fun requestMovies() {
        if (NetworkConnection.isConnection(this)) {
            showProgressDialog(R.string.please_wait)
            NetworkController.instance.getMoviesList(typeSelected, pageIndex)
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    private fun searchMovies(query: String) {
        if (NetworkConnection.isConnection(this)) {
            showProgressDialog(R.string.please_wait)
            NetworkController.instance.getSearchMoviesList(query, pageIndex)
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
