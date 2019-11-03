package com.naveed.samples.helper.base;

import android.os.Bundle;

import java.util.List;

public abstract class ListBaseActivity<T> extends BaseActivity {


    protected List<T> mDataList;
    protected RecyclerBaseAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


}
