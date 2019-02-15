package com.pra.practical.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pra.practical.R;
import com.pra.practical.custom.CircularImageView;
import com.pra.practical.custom.OnSingleClickListener;
import com.pra.practical.custom.loadmorerecycleview.FooterLoaderAdapter;
import com.pra.practical.helper.Utils;
import com.pra.practical.interFace.UserListItemUpdateDeleteCallBack;
import com.pra.practical.responseModel.UserListResponse;

/**
 * this class contain custom adapter with progressBar
 */

public class UserListAdapter extends FooterLoaderAdapter<UserListResponse.Data> {

    private Activity mActivity;
    UserListItemUpdateDeleteCallBack listCallBack;

    public UserListAdapter(Activity mActivity, UserListItemUpdateDeleteCallBack listCallBack) {
        super(mActivity);
        this.mActivity = mActivity;
        this.listCallBack = listCallBack;
    }


    @Override
    public RecyclerView.ViewHolder getYourItemViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_user, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(itemView);
        if (mItems.size() == 0) {
            myViewHolder.mView.setVisibility(View.GONE);
        } else {
            myViewHolder.mView.setVisibility(View.VISIBLE);
        }
        return new MyViewHolder(itemView);
    }


    @Override
    public void bindYourViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {
            MyViewHolder viewHolder = (MyViewHolder) holder;
            UserListResponse.Data mUserDataModel = mItems.get(position);
            viewHolder.mCardViewParent.setVisibility(View.VISIBLE);
            viewHolder.mTxtName.setText(mUserDataModel.getFirst_name() + " " + mUserDataModel.getLast_name());
            Utils.setImageFromUrlForProfilepic(mActivity, viewHolder.mImagProfile, mUserDataModel.getAvatar());


            viewHolder.mBtnDelete.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (listCallBack != null) {
                        listCallBack.delete(position);
                    }

                }
            });

            viewHolder.mBtnUpdate.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (listCallBack != null) {
                        listCallBack.update(position);
                    }

                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardViewParent;
        private CircularImageView mImagProfile;
        private TextView mTxtName;
        View mView;
        private Button mBtnUpdate, mBtnDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImagProfile = (CircularImageView) itemView.findViewById(R.id.img_profile_pic);
            mCardViewParent = (CardView) itemView.findViewById(R.id.card_view_parent);
            mTxtName = (TextView) itemView.findViewById(R.id.txt_name);
            mBtnUpdate = (Button) itemView.findViewById(R.id.btn_update);
            mBtnDelete = (Button) itemView.findViewById(R.id.btn_delete);
        }
    }
}
