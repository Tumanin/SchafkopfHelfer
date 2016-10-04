package com.applicatum.schafkopfhelfer.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;

import com.applicatum.schafkopfhelfer.R;

public class ProgressDialogHelper {

    private ProgressDialog ringProgressDialog;
    private static ProgressDialogHelper progressDialogHelper;

    private ProgressDialogHelper(){

    }

    public static ProgressDialogHelper getInstance(){
        if(progressDialogHelper == null) progressDialogHelper = new ProgressDialogHelper();
        return progressDialogHelper;
    }

    public void showProgress(Context context) {

        if (ringProgressDialog!=null) {
            ringProgressDialog.dismiss();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ringProgressDialog = new ProgressDialog(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            ringProgressDialog = new ProgressDialog(context);
            //ringProgressDialog = new ProgressDialog(context, R.style.AppTheme_Dialog);
            //ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        ringProgressDialog.setMessage("Please wait...");
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
    }

    public void showProgress(Context context, String message) {

        if (ringProgressDialog!=null) {
            ringProgressDialog.dismiss();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ringProgressDialog = new ProgressDialog(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            ringProgressDialog = new ProgressDialog(context, R.style.AppTheme_Dialog);
            ringProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        ringProgressDialog.setMessage(message);
        ringProgressDialog.setCancelable(false);
        ringProgressDialog.show();
    }

    public void dismissProgress() {
        if (ringProgressDialog!=null) {
            ringProgressDialog.dismiss();
        }
    }
}
