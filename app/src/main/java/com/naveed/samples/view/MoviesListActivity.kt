package com.naveed.samples.view

import android.app.SearchManager
import android.content.Context

import androidx.core.content.ContextCompat
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView

import com.naveed.samples.R
import com.naveed.samples.helper.base.BaseActivity
import com.naveed.samples.helper.utils.EndlessRecyclerViewScrollListener
import com.naveed.samples.helper.utils.NetworkConnection
import butterknife.ButterKnife
import com.naveed.samples.dagger.DaggerMovieListComponent
import com.naveed.samples.dagger.MovieListComponent
import com.naveed.samples.presenters.MoviesListPresenter
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MoviesListActivity : BaseActivity(),MoviesView{


    internal var typeSelectedTemp = "popular"
    private var moviesAdapter: MoviesAdapter? = null

    private val isGrid = false
    private var query = ""

    @Inject
    lateinit var moviePresenter:MoviesListPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        initDagger()

        setRecyclerAdapter()
        setUpSpinner()
        addListeners()
    }

    fun initDagger(){
        DaggerMovieListComponent.builder().build().inject(this)
        moviePresenter.onAttach(this)
    }

    private fun setRecyclerAdapter() {
        moviesAdapter = MoviesAdapter(this, moviePresenter.mMoviesList, isGrid)
        setAsList()
        recyclerView!!.adapter = moviesAdapter
        moviesAdapter!!.setItemSelectedListener { parent, view, position, id ->
            val bundle = Bundle()
            bundle.putParcelable("data", moviePresenter.mMoviesList[position])
            launchActivity(MovieDetailActivity::class.java, bundle)
        }

    }

    private fun setUpSpinner() {
        val dataAdapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, moviePresenter.types)
        spFilter!!.adapter = dataAdapter
        spFilter!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                typeSelectedTemp = moviePresenter.typesKeys[position]
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
               requestData()
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
                this@MoviesListActivity.query = s
                moviePresenter.clearMoviesList()
                requestData()
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                //Log.d("search","search text "+s);
                return false
            }
        })
        searchView.setOnCloseListener {
            this@MoviesListActivity.query = ""
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
                this@MoviesListActivity.query = ""
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
        if (moviePresenter.typeSelected != typeSelectedTemp) {
           moviePresenter.clearMoviesList()
        }
        moviePresenter.typeSelected = typeSelectedTemp
        requestData()
    }

    // Api calls
    private fun requestData() {
        //moviePresenter.clearMoviesList()
        if(NetworkConnection.isConnection(this)) {
            showProgressDialog(R.string.please_wait)
            moviePresenter.requestMovies(query)
        }else onError(getString(R.string.no_internet))
    }

    override fun updateMoviesList() {
        moviesAdapter!!.notifyDataSetChanged()
        dismissProgress()
    }
    override fun onError(error: String) {
        showToast(error)
        dismissProgress()
    }


}
