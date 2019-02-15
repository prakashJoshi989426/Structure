package com.pra.practical.custom;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long MIN_CLICK_INTERVAL = 1000;
    private long mLastClickTime;

    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < MIN_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        onSingleClick(v);
    }
}
