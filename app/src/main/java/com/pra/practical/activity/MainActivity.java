package com.pra.practical.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pra.practical.R;
import com.pra.practical.custom.OnSingleClickListener;
import com.pra.practical.fragment.LoginFragment;
import com.pra.practical.fragment.SplashFragment;
import com.pra.practical.helper.PreferenceHelper;
import com.pra.practical.helper.Utils;
import com.pra.practical.interFace.DialogClickListener;

public class MainActivity extends BaseActivity implements DialogClickListener {

    private ImageView mImgBack, mImgLogout;
    private TextView mTxtTitle;
    public ImageView mImgAdd;
    private FrameLayout mFlToolBar;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initComponents() {
        mFlToolBar = findViewById(R.id.toolbar_container);
        mImgBack = findViewById(R.id.img_back);
        mTxtTitle = findViewById(R.id.txt_title);
        mImgAdd = findViewById(R.id.img_add);
        mImgLogout = findViewById(R.id.img_logout);
    }


    @Override
    protected void prepareView() {
        pushFragmentDontIgnoreCurrent(new SplashFragment(), BaseActivity.FRAGMENT_JUST_ADD, false);
    }


    @Override
    protected void setActionListeners() {
        mImgBack.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                onBackPressed();
            }
        });
        mImgLogout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                logout();
            }
        });
    }

    /**
     * Common toolbar visibility method for whole application
     *
     * @param toolBarVisiblity
     * @param backVisibility
     * @param title
     * @param addVisibility
     * @param logoutVisibility
     */
    public void setTopBar(boolean toolBarVisiblity, int backVisibility, String title, int addVisibility,
                          int logoutVisibility) {
        if (toolBarVisiblity) {
            mFlToolBar.setVisibility(View.VISIBLE);
            mImgBack.setVisibility(backVisibility);
            mTxtTitle.setText(title);
            mImgAdd.setVisibility(addVisibility);
            mImgLogout.setVisibility(logoutVisibility);
        } else {
            mFlToolBar.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * logout with confirm alert
     */
    private void logout() {
        Utils.showConfirmationDialogCallBack(MainActivity.this, getString(R.string.logout_sure), this);
    }

    /**
     * logout custom dialog callBack
     */
    @Override
    public void okClick() {
        PreferenceHelper.deleteAll(MainActivity.this);
        clearBackStack();
        pushFragmentDontIgnoreCurrent(new LoginFragment(), BaseActivity.FRAGMENT_JUST_ADD);
    }
}
