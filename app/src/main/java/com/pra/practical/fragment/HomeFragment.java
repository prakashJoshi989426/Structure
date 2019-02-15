package com.pra.practical.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pra.practical.R;
import com.pra.practical.activity.BaseActivity;
import com.pra.practical.adapter.EmptyAdapter;
import com.pra.practical.adapter.UserListAdapter;
import com.pra.practical.api.ApiError;
import com.pra.practical.api.ApiResponse;
import com.pra.practical.api.WebApiClient;
import com.pra.practical.custom.EndlessRecyclerViewScrollListener;
import com.pra.practical.custom.OnSingleClickListener;
import com.pra.practical.helper.Constants;
import com.pra.practical.helper.Utils;
import com.pra.practical.interFace.UpdateUserDetail;
import com.pra.practical.interFace.UserListItemUpdateDeleteCallBack;
import com.pra.practical.responseModel.UserListResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends BaseFragment implements UserListItemUpdateDeleteCallBack, UpdateUserDetail, SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerViewUser;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLinearLayoutManager;
    private EndlessRecyclerViewScrollListener scrollListener;

    private UserListAdapter mUserListAdapter;
    private List<UserListResponse.Data> mList;

    int PAGENUMBER = 1;// for loading more page it is used when next page then increment++ variable value
    private int mTotalItemCount = 0; // display totalcount of list
 //   boolean isLoadingMore = false; // it is used for it is currently loading more data. used for that time any another action not done
    private int mDeletedPosition = -1;
    private int mUpdatePosition = -1;


    @Override
    protected View getLayoutResourceId(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_home, null, false);
    }

    @Override
    protected void initComponents(View view) {
        // recyclerview
        mRecyclerViewUser = view.findViewById(R.id.recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(mActivity);
        Utils.intitializationRecyclerview(mRecyclerViewUser, mActivity, mLinearLayoutManager); /// RecyclerView initialization

        // swipeRefresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        mList = new ArrayList<>();
    }

    @Override
    protected void prepareView() {
        setToolBar();
        setEmptyAdapter();

        onLoadDataInitial(true);
    }

    @Override
    protected void setActionListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(this);  // pull to refresh register

        mActivity.mImgAdd.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mActivity.pushFragmentDontIgnoreCurrent(AddUpdateUserFragment.getInstance(false, null, null), BaseActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
            }
        });
        mRecyclerViewUser.setLayoutManager(mLinearLayoutManager);
        // Retain an instance so that you can call `resetState()` for fresh searches
        scrollListener = new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if (mTotalItemCount == mList.size()) {
                    return;
                } else{
                    mUserListAdapter.showLoading(true);
                    mRecyclerViewUser.post(new Runnable() {
                        @Override
                        public void run() {
                            //  isLoadingMore = true;
                            mUserListAdapter.notifyDataSetChanged();
                            PAGENUMBER++;
                            checkNetworkConnection(false);
                        }
                    });
                }
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecyclerViewUser.addOnScrollListener(scrollListener);
    }

    @Override
    protected void setToolBar() {
        mActivity.setTopBar(true, View.GONE, getString(R.string.home), View.VISIBLE, View.VISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && isVisible()) {
            setToolBar();
        }
    }


    /**
     * this method is used for when no data available at that enable pull to refresh
     * there was issue for pullToRefresh that's why use this code
     */
    private void setEmptyAdapter() {
        EmptyAdapter adapter = new EmptyAdapter();
        mRecyclerViewUser.setAdapter(adapter);
    }


    private void onLoadDataInitial(boolean isShowProgressBar) {
        PAGENUMBER = 1;  // load data from initial level from first
        checkNetworkConnection(isShowProgressBar);
    }


    /**
     * @param isProgressBar when user want to display progressBar at the center in screen at that time this should be true
     */
    private void checkNetworkConnection(boolean isProgressBar) {
        if (Utils.isOnline(mActivity, true)) { // check network connection isOnline(context,true) , true means display dialog
            userApiCalling(isProgressBar);
        } else {
            if (PAGENUMBER != 1) {
                PAGENUMBER--;
            }
            OnHideLoadMorePulltoRefreshcomponent(); // hide progreebar, pulltoRefresh, load more component
        }
    }

    private void userApiCalling(boolean isProgressBar) {
        if (isProgressBar) {
            showProgressDialog();
        }
        Call<UserListResponse> mResponseCall = WebApiClient.getInstance(mActivity).getWebApi().getUserList(PAGENUMBER);
        mResponseCall.enqueue(new ApiResponse<UserListResponse>() {
            @Override
            public void onSuccess(UserListResponse userListResponse, Headers headers) {
                OnHideLoadMorePulltoRefreshcomponent();
                if (userListResponse != null) {
                    mTotalItemCount = userListResponse.getTotal();
                    if (PAGENUMBER == 1) { // for loading initial loading data
                        mUserListAdapter = null; // issue while search data is not reset for that
                        mList = userListResponse.getData();
                    } else {  // for load more
                        mList.addAll(userListResponse.getData());
                    }
                    setData();
                } else {
                    Utils.showAlertMessage(getActivity(), Constants.IErrorMessage.IO_EXCEPTION);
                }
            }

            @Override
            public void onError(ApiError t) {
                OnHideLoadMorePulltoRefreshcomponent();
                Utils.showAlertMessage(getActivity(), "" + t.getMessage());
            }
        });
    }


    /**
     * this method is used for set adapter in list
     */
    private void setData() {
        if (mUserListAdapter == null) { // when first time load page at that time this will be called in this if
            mUserListAdapter = new UserListAdapter(mActivity, this);
            mUserListAdapter.setHasStableIds(true);
            mUserListAdapter.setItems(mList);
            mRecyclerViewUser.setAdapter(mUserListAdapter);
        } else {
            mUserListAdapter.showLoading(false);
            mUserListAdapter.setItems(mList);
            mUserListAdapter.notifyDataSetChanged();
        }
    }


    /**
     * hide all component while calling api in any case success, failure,loadmore ,pulltoRefresh any case
     */
    private void OnHideLoadMorePulltoRefreshcomponent() {
        hideProgressDialog();
        if (mSwipeRefreshLayout != null) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }

    }



    @Override
    public void update(int position) {
        mUpdatePosition = position;
        mActivity.pushFragmentDontIgnoreCurrent(AddUpdateUserFragment.getInstance(true, mList.get(position), this), BaseActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
    }

    @Override
    public void delete(int position) {
        if (Utils.isOnline(mActivity, true)) {
            mDeletedPosition = position;
            showConfirmationDialogCallBack(mActivity, getString(R.string.delete_sure));
        }

    }

    public void showConfirmationDialogCallBack(Activity mActivity, String message) {
        final Dialog alertDialog = new Dialog(mActivity);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(R.layout.popup_yes_no);
        alertDialog.setCancelable(false);
        TextView txt = (TextView) alertDialog.findViewById(R.id.txt_alert_tv);
        txt.setText(message);
        Button dialogButton = (Button) alertDialog.findViewById(R.id.button_yes_alert_btn);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (mDeletedPosition != -1) {
                    deleteApiCalling(mList.get(mDeletedPosition).getId());
                }
            }
        });
        Button dialogButtonNo = (Button) alertDialog.findViewById(R.id.button_no_alert_btn);
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void deleteApiCalling(int id) {
        showProgressDialog();
        Call<Void> mResponseCall = WebApiClient.getInstance(mActivity).getWebApi().deleteItem(id);
        mResponseCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                hideProgressDialog();
                Toast.makeText(getActivity(), "Deleted successfully", Toast.LENGTH_SHORT).show();
                if (mDeletedPosition != -1) {  //TODO: Delte API not working from Backend
                    mList.remove(mDeletedPosition);
                    setData();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                hideProgressDialog();
                Utils.showAlertMessage(getActivity(), "" + t.getMessage());
            }
        });
    }

    @Override
    public void updateUserData(UserListResponse.Data mData) {
        if (mData != null && mUpdatePosition != -1) {
            mList.get(mUpdatePosition).setFirst_name(mData.getFirst_name());
            mList.get(mUpdatePosition).setLast_name(mData.getLast_name());
            setData();
        }
    }

    @Override
    public void onRefresh() {
        if (!Utils.isOnline(getActivity(), true)) {
            mSwipeRefreshLayout.setRefreshing(false);
            return;
        }
        onLoadDataInitial(false);
    }
}
