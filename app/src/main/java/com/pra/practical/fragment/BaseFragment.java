package com.pra.practical.fragment;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.pra.practical.activity.MainActivity;
import com.pra.practical.custom.progress.CustomProgressDialog;
import com.pra.practical.helper.PreferenceHelper;
/**
 * this class cointains for all fragment in Base you can initialization common instance
 * also some abstract method
 */
public abstract class BaseFragment extends Fragment {

    MainActivity mActivity;
    private View mView;
    public CustomProgressDialog mProgressDialog;


    public PreferenceHelper mPreferenceHelper;
    public long APITIMESTAMP = 100;

    public int FILTER_REQUEST_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) this.getActivity();
        mProgressDialog = new CustomProgressDialog(mActivity);
        mPreferenceHelper = new PreferenceHelper(mActivity);
    }

    /*@Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation animation = super.onCreateAnimation(transit, enter, nextAnim);

        // HW layer support only exists on API 11+
        if (Build.VERSION.SDK_INT >= 11) {
            if (animation == null && nextAnim != 0) {
                animation = AnimationUtils.loadAnimation(getActivity(), nextAnim);
            }
            if (animation != null) {
                getView().setLayerType(View.LAYER_TYPE_HARDWARE, null);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        if (getView() != null)
                            getView().setLayerType(View.LAYER_TYPE_NONE, null);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
        return animation;
    }*/





    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mView == null) {
            mView = getLayoutResourceId(inflater);
            initComponents(mView);
            setActionListeners();
            prepareView();
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    private class UiOpearation extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }


    protected abstract View getLayoutResourceId(LayoutInflater inflater);

    protected abstract void initComponents(View view);

    protected abstract void prepareView();

    protected abstract void setActionListeners();

    protected abstract void setToolBar();


    /**
     * show progress Dialog for application
     */
    public void showProgressDialog() {
        if (mProgressDialog.isShowing()) {
            hideProgressDialog();
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show("");
        }
    }


    /**
     * Hide progress Dialog for whole application
     */
    public void hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss("");
        }
    }

    public void freeMemory() {
        hideProgressDialog();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

}
