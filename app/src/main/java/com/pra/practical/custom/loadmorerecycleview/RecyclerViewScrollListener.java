package com.pra.practical.custom.loadmorerecycleview;


import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * this class is used for Common Scrolling to manage Load more functionality
 */
public class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private static final String TAG = "RecylcerScrollListener";
    private int mScrollThreshold = 40;
    private int scrolledDistance = 0;
    private static final int HIDE_THRESHOLD = 20;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    public boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private boolean infiniteScrollingEnabled = true;
    private boolean controlsVisible = true;
    RecycleViewListener mRecycleViewListener;

    public RecyclerViewScrollListener(RecycleViewListener mRecycleViewListener) {
        this.mRecycleViewListener = mRecycleViewListener;
    }

    // So TWO issues here.
    // 1. When the data is refreshed, we need to change previousTotal to 0. -  it is solved using onDataCleared() method call in screen while refreshing
    // 2. When we switch fragments and it loads itself from some place, for some  - issue only for available in gridlayoutManager in Recyclerview
    // reason gridLayoutManager returns stale data and hence re-assigning it every time.
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        visibleItemCount = recyclerView.getChildCount();
        if (manager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();
            totalItemCount = gridLayoutManager.getItemCount();
        } else if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
            totalItemCount = linearLayoutManager.getItemCount();
        }
        if (infiniteScrollingEnabled) {
            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false;
                    previousTotal = totalItemCount;
                }
            }
            if (!loading && (totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold)) {
                // End has been reached
                // do something
                mRecycleViewListener.onLoadMore();
                loading = true;
            }
        }
        if (firstVisibleItem == 0) {
            if (!controlsVisible) {
                mRecycleViewListener.onScrollUp();
                controlsVisible = true;
            }
            return;
        }

        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            mRecycleViewListener.onScrollDown();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            mRecycleViewListener.onScrollUp();
            controlsVisible = true;
            scrolledDistance = 0;
        }

        if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
            scrolledDistance += dy;
        }
    }


    public void setScrollThreshold(int scrollThreshold) {
        mScrollThreshold = scrollThreshold;
    }

    public void stopInfiniteScrolling() {
        infiniteScrollingEnabled = false;
    }

    public void onDataCleared() {
        previousTotal = 0;
    }

    public boolean isLoadingMore() {
        return loading;
    }


}

