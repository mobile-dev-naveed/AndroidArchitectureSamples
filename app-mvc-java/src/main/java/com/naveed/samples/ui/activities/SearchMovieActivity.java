package com.naveed.samples.ui.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import com.naveed.samples.R;
import com.naveed.samples.apputils.events.SingleDataEvent;
import com.naveed.samples.data.models.ListMoviesResp;
import com.naveed.samples.data.models.MovieItem;
import com.naveed.samples.data.network.EventBusKeys;
import com.naveed.samples.data.network.NetworkController;
import com.naveed.samples.helper.base.BaseActivity;
import com.naveed.samples.helper.utils.EndlessRecyclerViewScrollListener;
import com.naveed.samples.helper.utils.NetworkConnection;
import com.naveed.samples.ui.adapters.MoviesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchMovieActivity extends BaseActivity {

    private MoviesAdapter moviesAdapter;
    private List<MovieItem> mMoviesList = new ArrayList<>();

    @BindView(R.id.recyclerView)
    protected RecyclerView recyclerView;
    @BindView(R.id.filterView)
    protected RelativeLayout filterView;
    @BindView(R.id.spFilter)
    protected Spinner spFilter;

    private boolean loadMore = true;
    private int pageIndex = 1;
    private boolean isGrid = false;
    private String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setRecyclerAdapter();
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            this.query = query;
            searchMovies(query);
        }

    }

    private void setRecyclerAdapter() {
        moviesAdapter = new MoviesAdapter(this, mMoviesList, isGrid);
        //moviesAdapter.isVerticalList = true;
        setAsList();
        recyclerView.setAdapter(moviesAdapter);
        moviesAdapter.setItemSelectedListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("data", mMoviesList.get(position));
                launchActivity(MovieDetailActivity.class, bundle);
            }
        });

    }


    private void setAsGrid() {

        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(manager);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (loadMore) {
                    pageIndex++;
                    searchMovies(query);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);


    }

    private void setAsList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (loadMore) {
                    pageIndex++;
                    searchMovies(query);
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.filter).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //menu.it
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMenuItemIcon(MenuItem item, int resourceId) {
        item.setIcon(ContextCompat.getDrawable(this, resourceId));
    }


    // Api calls


    private void searchMovies(String query) {
        if (NetworkConnection.isConnection(this)) {
            showProgressDialog(R.string.please_wait);
            NetworkController.getInstance().getSearchMoviesList(query, pageIndex);
        } else {
            Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }


    // api requests
    @Subscribe
    public void onApiResponse(SingleDataEvent event) {
        if (event.getEventId() == EventBusKeys.MOVIE_LIST) {
            dismissProgress();
            if (event.getStatus()) {
                ListMoviesResp resp = (ListMoviesResp) event.data;
                if (pageIndex == resp.totalPages.intValue()) {
                    loadMore = false;
                }
                mMoviesList.addAll(resp.results);
                moviesAdapter.notifyDataSetChanged();
            } else {
                showToast(event.getMessage());
            }

        }
    }


}
