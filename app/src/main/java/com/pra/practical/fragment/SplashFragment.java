package com.pra.practical.fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;

import com.pra.practical.R;
import com.pra.practical.activity.BaseActivity;
import com.pra.practical.helper.Constants;
import com.pra.practical.helper.PreferenceHelper;
import com.pra.practical.helper.Utils;

public class SplashFragment extends BaseFragment {
    private Count mCount;  // using this create instance of countDownTimer

    @Override
    protected View getLayoutResourceId(LayoutInflater inflater) {
        return inflater.inflate(R.layout.fragment_splash, null, false); // inflate layout
    }

    @Override
    protected void initComponents(View view) {
        mCount = new Count(2000, 2000);  // set Count constructor

    }

    @Override
    protected void prepareView() {
        setToolBar();
        mCount.start();
    }

    @Override
    protected void setActionListeners() {

    }

    @Override
    protected void setToolBar() {
        mActivity.setTopBar(false, View.GONE, "",View.GONE,View.GONE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setToolBar();
        }
    }

    private class Count extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public Count(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            if (isAdded()) {
                if (Utils.isNotNullAndNotEmpty(PreferenceHelper.getValue(mActivity, Constants.PREF_USER, ""))) {
                    mActivity.pushFragmentDontIgnoreCurrent(new HomeFragment(), BaseActivity.FRAGMENT_JUST_ADD);
                } else {
                    mActivity.pushFragmentDontIgnoreCurrent(new LoginFragment(), BaseActivity.FRAGMENT_JUST_ADD);
                }

            }
        }
    }
}
