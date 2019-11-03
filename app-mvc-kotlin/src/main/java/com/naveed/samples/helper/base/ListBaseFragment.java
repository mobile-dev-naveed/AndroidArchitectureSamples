package com.naveed.samples.helper.base;

import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView;

import java.util.List;

/**
 * Created by naveedali on 9/23/17.
 */

public abstract class ListBaseFragment<T> extends BaseFragment implements AdapterView.OnItemClickListener {
    //private
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerBaseAdapter mAdapter;

    protected List<T> mData;

    protected abstract void initAdapter();

}
