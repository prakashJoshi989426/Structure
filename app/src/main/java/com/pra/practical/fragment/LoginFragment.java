package com.pra.practical.fragment;

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
import com.pra.practical.helper.PreferenceHelper;
import com.pra.practical.helper.Utils;
import com.pra.practical.helper.Validation;
import com.pra.practical.requestModel.RegisterLoginRequestModel;
import com.pra.practical.responseModel.RegisterLoginResponseModel;

import okhttp3.Headers;
import retrofit2.Call;

public class LoginFragment extends BaseFragment {

    private EditText mEdtEmail, mEdtPassword;
    private TextView mTxtLogin, mTxtRegister;

    @Override
    protected View getLayoutResourceId(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_login, null, false);
    }

    @Override
    protected void initComponents(View view) {
        mEdtEmail = view.findViewById(R.id.edt_email);
        mEdtPassword = view.findViewById(R.id.edt_password);
        mTxtLogin = view.findViewById(R.id.txt_sign_in);
        mTxtRegister = view.findViewById(R.id.btn_new_user);
    }

    @Override
    protected void prepareView() {
        setToolBar();
    }

    @Override
    protected void setActionListeners() {
        mTxtRegister.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                mActivity.pushFragmentDontIgnoreCurrent(new RegisterFragment(), BaseActivity.FRAGMENT_ADD_TO_BACKSTACK_AND_ADD);
            }
        });

        mTxtLogin.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                login();
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setToolBar();
        }
    }

    @Override
    protected void setToolBar() {
        mActivity.setTopBar(false, View.GONE, "",View.GONE,View.GONE);
    }


    private void login() {
        BaseActivity.hideKeyboard(mActivity);
        if (isValidData()) {
            if (Utils.isOnline(mActivity, true)) {
                showProgressDialog();
                loginApiCalling();
            }
        }
    }

    private boolean isValidData() {
        String email = mEdtEmail.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();
        if (!Validation.isRequiredField(email)) {
            mEdtEmail.requestFocus();
            mEdtEmail.setError(getResources().getString(R.string.enter_email));
            return false;
        } else if (!Validation.isEmail(email)) {
            mEdtEmail.requestFocus();
            mEdtEmail.setError(getResources().getString(R.string.valid_email));
            return false;
        } else if (!Validation.isRequiredField(password)) {
            mEdtPassword.requestFocus();
            mEdtPassword.setError(getResources().getString(R.string.enter_password));
            return false;
        }
        return true;
    }


    private void loginApiCalling() {
        String email = mEdtEmail.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();

        final RegisterLoginRequestModel registerRequestModel = new RegisterLoginRequestModel(email, password);
        Call<RegisterLoginResponseModel> mResponseCall = WebApiClient.getInstance(mActivity).getWebApi().login(registerRequestModel);
        mResponseCall.enqueue(new ApiResponse<RegisterLoginResponseModel>() {
            @Override
            public void onSuccess(RegisterLoginResponseModel registerLoginResponseModel, Headers headers) {
                hideProgressDialog();
                if (registerLoginResponseModel != null) {
                    PreferenceHelper.setValue(mActivity, Constants.PREF_USER, registerLoginResponseModel.getToken());
                    Toast.makeText(mActivity, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    mActivity.pushFragmentDontIgnoreCurrent(new HomeFragment(), BaseActivity.FRAGMENT_JUST_ADD);
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
}
