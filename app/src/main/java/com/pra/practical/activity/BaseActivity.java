package com.pra.practical.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.pra.practical.R;
import com.pra.practical.custom.progress.CustomProgressDialog;

import java.util.ArrayList;

/**
 * this class used as abstract class for Activity onCreate - Cointains - also some abstract method
 * also include fragment structure
 *  Created by prakash.joshi.
 */
public abstract class BaseActivity extends AppCompatActivity {

    // fragment tag
    public static int counter;
    public static final int FRAGMENT_JUST_REPLACE = 0;
    public static final int FRAGMENT_JUST_ADD = 1;
    public static final int FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE = 2;
    public static final int FRAGMENT_ADD_TO_BACKSTACK_AND_ADD = 3;

    // Variable
    private long mLastClickTimeListViewItem = 0;
    Fragment fragmentName;

    FragmentManager fragmentManager;

    public CustomProgressDialog mProgressDialog;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResourceId());

        fragmentManager = getSupportFragmentManager();
        initComponents();
        prepareView();
        setActionListeners();
    }

    protected abstract void initComponents();

    protected abstract int getLayoutResourceId();

    protected abstract void prepareView();

    protected abstract void setActionListeners();


    public void hideStatusBar() {
        try {
            ((View) findViewById(android.R.id.title).getParent())
                    .setVisibility(View.GONE);
        } catch (Exception e) {
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
    }

    public void showStatusBar() {
        try {
            ((View) findViewById(android.R.id.title).getParent())
                    .setVisibility(View.VISIBLE);
        } catch (Exception e) {
        }
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * @param id            id of fragment_container in which we need to add the frmgment
     * @param args          bundle to pass to the new fragment
     * @param fragment      fragment to add
     * @param shouldAnimate flag to denote if animation is required or not
     * @param shouldAdd     need to add to backstack or not
     */
    public void pushFragments(int id, Bundle args, Fragment fragment,
                              boolean shouldAnimate, boolean shouldAdd) {
        pushFragments(id, args, fragment, shouldAnimate, shouldAdd, false);
    }


    /**
     * @param id              id of fragment_container in which we need to add the frmgment
     * @param args            bundle to pass to the new fragment
     * @param fragment        fragment to add
     * @param shouldAnimate   flag to denote if animation is required or not
     * @param shouldAdd       need to add to backstack or not
     * @param ignoreIfCurrent ignore if current fragment is same as the new fragment
     */
    public void pushFragments(final int id, final Bundle args,
                              final Fragment fragment, boolean shouldAnimate,
                              final boolean shouldAdd, final boolean ignoreIfCurrent) {
        pushFragments(id, args, fragment, shouldAnimate, shouldAdd, ignoreIfCurrent, false);
    }


    /**
     * @param id              id of fragment_container in which we need to add the frmgment
     * @param args            bundle to pass to the new fragment
     * @param fragment        fragment to add
     * @param shouldAnimate   flag to denote if animation is required or not
     * @param shouldAdd       need to add to backstack or not
     * @param ignoreIfCurrent ignore if current fragment is same as the new fragment
     * @param justAdd         parameter to use add intead of replace
     */
    public void pushFragments(final int id, final Bundle args,
                              final Fragment fragment, boolean shouldAnimate,
                              final boolean shouldAdd, final boolean ignoreIfCurrent, final boolean justAdd) {
        pushFragments(id, args, fragment, null, shouldAnimate, shouldAdd, ignoreIfCurrent, justAdd);
    }


    /**
     * @param id              id of fragment_container in which we need to add the frmgment
     * @param args            bundle to pass to the new fragment
     * @param fragment        fragment to add
     * @param shouldAnimate   flag to denote if animation is required or not
     * @param shouldAdd       need to add to backstack or not
     * @param ignoreIfCurrent ignore if current fragment is same as the new fragment
     * @param justAdd         parameter to use add intead of replace
     * @param fragmentParent  the parent in which we need to add fragment as child
     */

    @SuppressLint("RestrictedApi")
    public void pushFragments(final int id, final Bundle args,
                              final Fragment fragment, final Fragment fragmentParent, boolean shouldAnimate,
                              final boolean shouldAdd, final boolean ignoreIfCurrent, final boolean justAdd) {

        try {
            hideKeyboard(BaseActivity.this);

            // assert fragment != null;
            if (fragment.getArguments() == null) {
                fragment.setArguments(args);
            }

         /*   FragmentManager fragmentManager;

            if (fragmentParent == null) {
                fragmentManager = getSupportFragmentManager();
            } else {
                fragmentManager = fragmentParent.getChildFragmentManager();
            }*/

            FragmentTransaction ft = fragmentManager.beginTransaction();
            if (shouldAdd) {
                ft.addToBackStack(fragment.getClass().getCanonicalName());
            }
            // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            if (shouldAnimate) {
                ft.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
            }

          /*  if (getSupportFragmentManager().findFragmentById(R.id.rootLayout) != null) {
                ft.hide(getSupportFragmentManager().findFragmentById(R.id.rootLayout));
            }*/
            if (fragmentManager.findFragmentById(R.id.fragment_container) != null) {
                ft.hide(fragmentManager.findFragmentById(R.id.fragment_container));
            }
           /* int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) != null) {
                    ft.hide(getSupportFragmentManager().findFragmentById(R.id.fragment_container));
                }
            } else {

                if (getSupportFragmentManager().findFragmentById(R.id.rootLayout) != null) {
                    ft.hide(getSupportFragmentManager().findFragmentById(R.id.rootLayout));
                }
            }*/

            if (justAdd) {
                fragmentName = fragment;
                ft.add(id, fragment, fragment.getClass().getCanonicalName());
            } else {
                ft.replace(R.id.fragment_container, fragment);
            }
            if (isFinishing()) {
                ft.commit();
            } else {
                ft.commitAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void popStack() {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    public void clearBackStack() {
        FragmentManager fm = getSupportFragmentManager();
        int count = fm.getBackStackEntryCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                fm.popBackStack();
                //fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public void clearBackStackNoAnimation() {
        sDisableFragmentAnimations = true;
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        sDisableFragmentAnimations = false;
    }







    /**
     * This method for push fragment with bundle
     *
     * @param bundle
     * @param fragment
     * @param fragmentTransactionType
     */
    public void pushFragmentDontIgnoreCurrent(Bundle bundle, Fragment fragment, int fragmentTransactionType) {
        switch (fragmentTransactionType) {
            case FRAGMENT_JUST_REPLACE:
                pushFragments(R.id.fragment_container, bundle, fragment, true, false, false, false);
                break;
            case FRAGMENT_JUST_ADD:
                pushFragments(R.id.fragment_container, bundle, fragment, true, false, false, true);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE:
                pushFragments(R.id.fragment_container, bundle, fragment, true, true, false, false);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_ADD:
                pushFragments(R.id.fragment_container, bundle, fragment, true, true, false, true);
                break;
        }
    }

    public boolean sDisableFragmentAnimations = false;

    public void pushFragmentDontIgnoreCurrent(Fragment fragment, int fragmentTransactionType) {
        switch (fragmentTransactionType) {
            case FRAGMENT_JUST_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, true, false, false, false);
                break;
            case FRAGMENT_JUST_ADD:
                pushFragments(R.id.fragment_container, null, fragment, true, false, false, true);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, true, true, false, false);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_ADD:
                pushFragments(R.id.fragment_container, null, fragment, true, true, false, true);
                break;
        }
    }

    public void pushFragmentDontIgnoreCurrent(Fragment fragment, int fragmentTransactionType, boolean shouldAnimate) {
        switch (fragmentTransactionType) {
            case FRAGMENT_JUST_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, shouldAnimate, false, false, false);
                break;
            case FRAGMENT_JUST_ADD:
                pushFragments(R.id.fragment_container, null, fragment, shouldAnimate, false, false, true);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE:
                pushFragments(R.id.fragment_container, null, fragment, shouldAnimate, true, false, false);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_ADD:
                pushFragments(R.id.fragment_container, null, fragment, shouldAnimate, true, false, true);
                break;
        }
    }


    /**
     * this method will be for Child Fragment
     *
     * @param bundle                  Bundle Data
     * @param fragment                name of child fragment
     * @param fragmentTransactionType type of transaction
     */
    public void pushChildFragmentDontIgnoreCurrent(int containerId, Bundle bundle, Fragment fragment, int fragmentTransactionType) {
        switch (fragmentTransactionType) {
            case FRAGMENT_JUST_REPLACE:
                pushFragments(containerId, bundle, fragment, true, false, false, false);
                break;
            case FRAGMENT_JUST_ADD:
                pushFragments(containerId, bundle, fragment, true, false, false, true);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_REPLACE:
                pushFragments(containerId, bundle, fragment, true, true, false, false);
                break;
            case FRAGMENT_ADD_TO_BACKSTACK_AND_ADD:
                pushFragments(containerId, bundle, fragment, true, true, false, true);
                break;
        }
    }


    public boolean isCurrentFragment(Fragment f) {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        return currentFragment != null && f != null && currentFragment.getClass().equals(f.getClass());
    }


    public Fragment currentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment_container);
    }


    public void pushFragments(Bundle args, Fragment fragment,
                              boolean shouldAnimate, boolean shouldAdd, boolean currentfragment) {

        pushFragments(R.id.fragment_container, args, fragment, shouldAnimate, shouldAdd,
                currentfragment);

    }

    public void pushFragments(Bundle bundle, Fragment fragment,
                              boolean shouldAdd) {
        pushFragments(bundle, fragment, false, shouldAdd, false);
    }

    public void pushFragments(Fragment fragment) {
        pushFragments(null, fragment, false, false, false);
    }







    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

}