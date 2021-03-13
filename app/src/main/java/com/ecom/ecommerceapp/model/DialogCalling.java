package com.ecom.ecommerceapp.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecom.ecommerceapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class DialogCalling{



    public static void showFailedAlertBoxWithFinish(final Activity context, final String v) {

        new AlertDialog.Builder(context).setMessage(v)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        context.finish();

                    }
                }).show();
    }

    public static void showAlertBoxWithFinish(final Activity context, final String v) {

        new AlertDialog.Builder(context).setMessage(v)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                        context.finish();
                    }

                }).show();
    }

}
