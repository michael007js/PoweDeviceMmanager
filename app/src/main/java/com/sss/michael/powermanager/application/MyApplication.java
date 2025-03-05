package com.sss.michael.powermanager.application;

import android.app.Application;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.PowerManager;

import com.sss.michael.powermanager.constant.AppConstant;
import com.sss.michael.powermanager.receiver.MyAdmin;
import com.sss.michael.powermanager.util.CheckRoot;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (AppConstant.DPM == null) {
            AppConstant.DPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        }
        if (AppConstant.CN == null) {
            AppConstant.CN = new ComponentName(this, MyAdmin.class);
        }
        if (AppConstant.PM == null) {
            AppConstant.PM = (PowerManager) getSystemService(Context.POWER_SERVICE);
        }
        AppConstant.ROOT = CheckRoot.isDeviceRooted();
    }
}
