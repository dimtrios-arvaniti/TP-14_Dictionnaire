package com.example.dim.tp14.utils;

import android.content.Context;
import android.widget.Toast;

public class CommonUtils {

    public static void showToastShort(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
