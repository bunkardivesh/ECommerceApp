package com.ecom.ecommerceapp.utility;

import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class MakeSnakBar {

    public static void makeSnakbar(String msg, int color, View mLayout, Context context)
    {
        Snackbar snackbar =  Snackbar.make(mLayout, msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view1 = snackbar.getView();
        view1.setBackgroundColor(ContextCompat.getColor(context, color));
    }

}
