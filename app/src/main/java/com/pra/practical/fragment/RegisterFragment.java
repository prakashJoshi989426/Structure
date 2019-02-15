package com.pra.practical.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pra.practical.R;
import com.pra.practical.activity.BaseActivity;
import com.pra.practical.api.WebApiClient;
import com.pra.practical.custom.OnSingleClickListener;
import com.pra.practical.helper.Constants;
import com.pra.practical.helper.Utils;
import com.pra.practical.helper.Validation;
import com.pra.practical.requestModel.RegisterLoginRequestModel;
import com.pra.practical.responseModel.RegisterLoginResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends BaseFragment {


    private EditText mEdtEmail, mEdtPassword, mEdtConfirmPassword;
    private TextView mTxtRegister;

    @Override
    protected View getLayoutResourceId(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_register, null, false);
    }

    @Override
    protected void initComponents(View view) {
        mEdtEmail = view.findViewById(R.id.edt_email);
        mEdtPassword = view.findViewById(R.id.edt_password);
        mEdtConfirmPassword = view.findViewById(R.id.edt_confirm_password);
        mTxtRegister = view.findViewById(R.id.txt_register);
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
                registerUser();
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
        mActivity.setTopBar(false,View.GONE,"",View.GONE,View.GONE);
    }


    private void registerUser() {
        BaseActivity.hideKeyboard(mActivity);
        if (isValidData()) {
            if (Utils.isOnline(mActivity, true)) {
                showProgressDialog();
                registerApiCalling();
            }
        }
    }

    private boolean isValidData() {
        String email = mEdtEmail.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();
        String confirmPassword = mEdtConfirmPassword.getText().toString().trim();
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
        } else if (!Validation.isRequiredField(confirmPassword)) {
            mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setError(getResources().getString(R.string.enter_confirm_password));
            return false;
        } else if (!password.equals(confirmPassword)) {
            mEdtConfirmPassword.requestFocus();
            mEdtConfirmPassword.setError(getResources().getString(R.string.password_confirm));
            return false;
        }
        return true;
    }


    private void registerApiCalling() {
        String email = mEdtEmail.getText().toString().trim();
        String password = mEdtPassword.getText().toString().trim();
        String confirmPassword = mEdtConfirmPassword.getText().toString().trim();
        final RegisterLoginRequestModel registerRequestModel = new RegisterLoginRequestModel(email, password);
        Call<RegisterLoginResponseModel> mResponseCall = WebApiClient.getInstance(mActivity).getWebApi().register(registerRequestModel);

        mResponseCall.enqueue(new Callback<RegisterLoginResponseModel>() {
            @Override
            public void onResponse(Call<RegisterLoginResponseModel> call, Response<RegisterLoginResponseModel> response) {
                hideProgressDialog();
                if (response.body() != null) {
                    if (response.code() == 200 || response.code() == 201) {
                        hideProgressDialog();
                        Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                        mActivity.clearBackStack();
                        mActivity.pushFragmentDontIgnoreCurrent(new LoginFragment(), BaseActivity.FRAGMENT_JUST_ADD);
                    } else {
                        Utils.showAlertMessage(getActivity(), Constants.IErrorMessage.IO_EXCEPTION);
                    }
                } else {
                    Utils.showAlertMessage(getActivity(), Constants.IErrorMessage.IO_EXCEPTION);
                }
            }

            @Override
            public void onFailure(Call<RegisterLoginResponseModel> call, Throwable t) {
                hideProgressDialog();
                Utils.showAlertMessage(getActivity(), "" + t.getMessage());
            }
        });

    }




}
