package com.pra.practical.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by prakash.joshi.
 */
public class EmptyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}