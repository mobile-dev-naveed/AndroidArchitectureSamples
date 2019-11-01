package com.naveed.samples.helper.base;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

/**
 * Created by naveedali on 10/22/17.
 */

public abstract class RecyclerBaseAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements Filterable {

    protected List<T> mDataList;
    protected Context mContext;


    protected AdapterView.OnItemClickListener itemSelectedListener;


    public AdapterView.OnItemClickListener getItemSelectedListener() {
        return itemSelectedListener;
    }

    public void setItemSelectedListener(AdapterView.OnItemClickListener itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public void setCopyAllData(List<T> data) {

    }
}
