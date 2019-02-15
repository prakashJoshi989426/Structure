package com.pra.practical.custom.loadmorerecycleview;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.pra.practical.R;

import java.util.List;
/**
 * Created by prakash.joshi
 */
public abstract class FooterLoaderAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected boolean isShowLoader = false;
    private static final int VIEWTYPE_ITEM = 1;
    private static final int VIEWTYPE_LOADER = 2;
    protected List<T> mItems;
    protected LayoutInflater mInflater;

    public FooterLoaderAdapter(Activity mActivity) {
        mInflater = LayoutInflater.from(mActivity);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEWTYPE_LOADER) {

            // Your Loader XML view here
            View view = mInflater.inflate(R.layout.view_progressbar_load_more_list, viewGroup, false);

            // Your LoaderViewHolder class
            return new LoaderViewHolder(view);
        } else if (viewType == VIEWTYPE_ITEM) {
            return getYourItemViewHolder(viewGroup);
        }

        throw new IllegalArgumentException("Invalid ViewType: " + viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        // Loader ViewHolder
        if (viewHolder instanceof LoaderViewHolder) {
            LoaderViewHolder loaderViewHolder = (LoaderViewHolder)viewHolder;
            if (isShowLoader) {
                loaderViewHolder.itemView.findViewById(R.id.progressbar).setVisibility(View.VISIBLE);
            } else {
                loaderViewHolder.itemView.findViewById(R.id.progressbar).setVisibility(View.GONE);
            }

            return;
        }

        bindYourViewHolder(viewHolder, position);

    }

    @Override
    public int getItemCount() {
        // If no items are present, there's no need for loader
        if (mItems == null || mItems.size() == 0) {
            return 0;
        }
        // +1 for loader
        return mItems.size() + 1;
    }


    @Override
    public int getItemViewType(int position) {
        // loader can't be at position 0
        // loader can only be at the last position
        if (position != 0 && position == getItemCount() - 1) {
            return VIEWTYPE_LOADER;
        }
        return VIEWTYPE_ITEM;
    }

    public void showLoading(boolean isShowLoader) {
        this.isShowLoader = isShowLoader;

    }

    public void setItems(List<T> items) {
        mItems = items;
    }

    public abstract RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent);
    public abstract void bindYourViewHolder(RecyclerView.ViewHolder holder, int position);
}
