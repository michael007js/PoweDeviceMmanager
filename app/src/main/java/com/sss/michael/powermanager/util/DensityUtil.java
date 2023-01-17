package com.sss.michael.powermanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class DensityUtil {
    public DensityUtil() {
    }

    public static int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(1, dpVal, Resources.getSystem().getDisplayMetrics());
    }

    public static int sp2px(float spVal) {
        return (int)TypedValue.applyDimension(2, spVal, Resources.getSystem().getDisplayMetrics());
    }

    public static float px2dp(float pxVal) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return pxVal / scale;
    }

    public static float px2sp(float pxVal) {
        return pxVal / Resources.getSystem().getDisplayMetrics().scaledDensity;
    }

    public static int getScreenWidthPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenWidthPixels(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager)context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    public static int getScreenHeightPixels(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static int getScreenHeightPixels(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager)context.getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
}
