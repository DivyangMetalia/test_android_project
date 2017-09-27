package com.elluminati.charge.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by elluminati on 21-Sep-17.
 */

public class Utils {
    private static ProgressDialog progressDialog;

    public static void showProgressDialog(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    public static void hideProgressbar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
}
