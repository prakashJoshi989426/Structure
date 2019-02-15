package com.pra.practical.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.pra.practical.R;
import com.pra.practical.interFace.DialogClickListener;
import com.squareup.picasso.Picasso;



public class Utils {

    public static Dialog alertDialog;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static void setImageFromUrlForProfilepic(Context context, ImageView imageView, String url) {
        if (url.length() == 0) {
            url = "temp";
        }
        Picasso.get().
                load(url).
                placeholder(R.drawable.ic_default_user).
                error(R.drawable.ic_default_user).
                into(imageView);
    }

    public static void intitializationRecyclerview(RecyclerView recyclerView, Context context, LinearLayoutManager linearLayoutManager) {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    public static boolean isOnline(Context context, boolean isDialogEnabled) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        if (isDialogEnabled) {
            showAlertMessage(context, context.getResources().getString(R.string.error_no_internet_connection));
        }
        return false;
    }


    public static void showAlertMessage(Context context, String message) {
        try {
            final Dialog alertDialog = new Dialog(context);
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDialog.setContentView(R.layout.popup_yes);
            alertDialog.setCancelable(false);
            TextView txt = (TextView) alertDialog.findViewById(R.id.txt_alert_tv);
            txt.setText(message);
            Button dialogButton = (Button) alertDialog.findViewById(R.id.button_ok_alert_btn);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showConfirmationDialogCallBack(Activity mActivity, String message, final DialogClickListener mDialogClickListener) {
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
                if (mDialogClickListener != null) {
                    alertDialog.dismiss();
                    mDialogClickListener.okClick();
                }
            }
        });
        Button dialogButtonNo = (Button) alertDialog.findViewById(R.id.button_no_alert_btn);
        dialogButtonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogClickListener != null) {
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    public static void syso(String sout) {
        System.out.println("" + sout);
    }

    public static boolean isNotNullAndNotEmpty(String s) {
        if (s != null && !s.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


}
