package com.pra.practical.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.pra.practical.R;
import com.pra.practical.activity.BaseActivity;
import com.pra.practical.api.ApiError;
import com.pra.practical.api.ApiResponse;
import com.pra.practical.api.WebApiClient;
import com.pra.practical.custom.OnSingleClickListener;
import com.pra.practical.helper.Constants;
import com.pra.practical.helper.Utils;
import com.pra.practical.helper.Validation;
import com.pra.practical.interFace.UpdateUserDetail;
import com.pra.practical.requestModel.CreateUserRequestModel;
import com.pra.practical.responseModel.CrerateUserResponseModel;
import com.pra.practical.responseModel.UpdateUserResponseModel;
import com.pra.practical.responseModel.UserListResponse;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddUpdateUserFragment extends BaseFragment {

    private EditText mEdtFirstName, mEdtjob;
    private TextView mTxtAddUpdateUser;

    private boolean isBundleFromEdit = false;
    private UserListResponse.Data mBundleUserData = null;
    static UpdateUserDetail mUpdateUserDetail;

    public static AddUpdateUserFragment getInstance(boolean isEdit, UserListResponse.Data mData,
                                                    UpdateUserDetail updateUserDetail) {
        AddUpdateUserFragment fragmentAddMember = new AddUpdateUserFragment();
        Bundle mBundle = new Bundle();
        mBundle.putBoolean(Constants.BUNDLE_FROM_EDIT, isEdit);
        mBundle.putParcelable(Constants.BUNDLE_USER, mData);
        mUpdateUserDetail = updateUserDetail;
        fragmentAddMember.setArguments(mBundle);
        return fragmentAddMember;
    }


    @Override
    protected View getLayoutResourceId(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_add_update, null, false);
    }

    @Override
    protected void initComponents(View view) {
        mEdtFirstName = view.findViewById(R.id.edt_first_name);
        mEdtjob = view.findViewById(R.id.edt_job);
        mTxtAddUpdateUser = view.findViewById(R.id.txt_add_update_user);
    }

    @Override
    protected void prepareView() {
        getBundleData();
        setToolBar();
        if (isBundleFromEdit) {
            mEdtFirstName.setText(mBundleUserData.getFirst_name());
            mEdtjob.setText(mBundleUserData.getLast_name());
            mTxtAddUpdateUser.setText(R.string.update_user);
        }
    }

    private void getBundleData() {
        Bundle mBundle = getArguments();
        if (mBundle != null) {
            if (mBundle.containsKey(Constants.BUNDLE_FROM_EDIT)) {
                isBundleFromEdit = mBundle.getBoolean(Constants.BUNDLE_FROM_EDIT);
            }
            if (mBundle.containsKey(Constants.BUNDLE_USER)) {
                mBundleUserData = mBundle.getParcelable(Constants.BUNDLE_USER);
            }
        }
    }

    @Override
    protected void setActionListeners() {
        mTxtAddUpdateUser.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                addUpdateUser();
            }
        });
    }

    private void addUpdateUser() {
        BaseActivity.hideKeyboard(mActivity);
        if (isValidData()) {
            if (Utils.isOnline(mActivity, true)) {
                if (isBundleFromEdit) {
                    editUserApiCalling();
                } else {
                    addUserApiCalling();
                }

            }
        }
    }

    private void addUserApiCalling() {
        showProgressDialog();
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel(mEdtFirstName.getText().toString()
                , mEdtjob.getText().toString());
        Call<CrerateUserResponseModel> mResponse = WebApiClient.getInstance(mActivity).getWebApi().createUser(createUserRequestModel);
        mResponse.enqueue(new Callback<CrerateUserResponseModel>() {
            @Override
            public void onResponse(Call<CrerateUserResponseModel> call, Response<CrerateUserResponseModel> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    if (response.code() == 200 || response.code() == 201) {
                        Toast.makeText(getActivity(), R.string.added_user, Toast.LENGTH_SHORT).show();
                        mActivity.onBackPressed();
                    } else {
                        Utils.showAlertMessage(getActivity(), Constants.IErrorMessage.IO_EXCEPTION);
                    }
                } else {
                    Utils.showAlertMessage(getActivity(), Constants.IErrorMessage.IO_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<CrerateUserResponseModel> call, Throwable t) {
                hideProgressDialog();
                Utils.showAlertMessage(getActivity(), "" + t.getMessage());
            }
        });
    }

    private void editUserApiCalling() {
        showProgressDialog();
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel(mEdtFirstName.getText().toString()
                , mEdtjob.getText().toString());
        Call<UpdateUserResponseModel> mResponse = WebApiClient.getInstance(mActivity).getWebApi().updateUser(mBundleUserData.getId(), createUserRequestModel);
        mResponse.enqueue(new ApiResponse<UpdateUserResponseModel>() {
            @Override
            public void onSuccess(UpdateUserResponseModel updateUserResponseModel, Headers headers) {
                hideProgressDialog();
                if (updateUserResponseModel != null) {
                    Toast.makeText(getActivity(), R.string.edit_user, Toast.LENGTH_SHORT).show();
                    UserListResponse.Data mdata = new UserListResponse.Data();
                    mdata.setFirst_name(updateUserResponseModel.getName());
                    mdata.setLast_name(updateUserResponseModel.getJob());
                    mUpdateUserDetail.updateUserData(mdata);
                    mActivity.onBackPressed();
                } else {
                    Utils.showAlertMessage(getActivity(), Constants.IErrorMessage.IO_EXCEPTION);
                }
            }

            @Override
            public void onError(ApiError t) {
                hideProgressDialog();
                Utils.showAlertMessage(getActivity(), "" + t.getMessage());
            }
        });

    }

    private boolean isValidData() {
        String firstname = mEdtFirstName.getText().toString().trim();
        String lastname = mEdtjob.getText().toString().trim();
        if (!Validation.isRequiredField(firstname)) {
            mEdtFirstName.requestFocus();
            mEdtFirstName.setError(getResources().getString(R.string.enter_first_name));
            return false;
        } else if (!Validation.isRequiredField(lastname)) {
            mEdtjob.requestFocus();
            mEdtjob.setError(getResources().getString(R.string.enter_job_name));
            return false;
        }
        return true;
    }

    /**
     * set toopBar
     */
    @Override
    protected void setToolBar() {
        if (isBundleFromEdit) {
            mActivity.setTopBar(true, View.VISIBLE, "Edit user", View.GONE, View.GONE);
        } else {
            mActivity.setTopBar(true, View.VISIBLE, "Add user", View.GONE, View.GONE);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setToolBar();
        }
    }
}
