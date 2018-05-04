package com.leoberteck.whattheword.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;

import com.leoberteck.whattheword.R;

public final class DialogUtils {

    public static void showNoServerConnectionDialog(Context context
            , DialogInterface.OnClickListener positiveButtonClick){
        new AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(R.string.no_connection_to_server)
                .setPositiveButton(R.string.ok, positiveButtonClick)
                .show();
    }

    public static void showConfirmationDialog(Context context
            , @StringRes int title
            , @StringRes int message
            , DialogInterface.OnClickListener positiveButtonClick
            , DialogInterface.OnClickListener negativeButtonClick){
        new AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(R.string.yes, positiveButtonClick)
            .setNegativeButton(R.string.no, negativeButtonClick)
            .show();
    }
}
