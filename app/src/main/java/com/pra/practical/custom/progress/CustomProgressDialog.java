package com.pra.practical.custom.progress;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.pra.practical.R;


/**
 * this class is used for custom ProgressDialog
 */
public class CustomProgressDialog extends Dialog {
    MaterialProgressBar progress1;

    Context mContext;
    CustomProgressDialog dialog;

    public CustomProgressDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomProgressDialog show(CharSequence message) {

        dialog = new CustomProgressDialog(mContext, R.style.ProgressDialog);
        dialog.setContentView(R.layout.custom_progress_dialog);
        progress1 = (MaterialProgressBar) dialog.findViewById(R.id.progress1);
        progress1.setColorSchemeResources(R.color.colorPrimary);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        dialog.getWindow().setAttributes(lp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (dialog != null) {
            dismiss();
            dialog.show();
        }
        return dialog;
    }

    public CustomProgressDialog hide(CharSequence message) {
        if (dialog != null) {
            dialog.hide();
        }
        return dialog;
    }

    public CustomProgressDialog dismiss(CharSequence message) {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            return dialog;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
