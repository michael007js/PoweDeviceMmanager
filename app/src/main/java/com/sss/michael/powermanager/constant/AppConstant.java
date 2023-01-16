package com.sss.michael.powermanager.constant;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;

public class AppConstant {
    /**
     * 设备策略服务
     */
    public static DevicePolicyManager DPM;
    public static ComponentName CN;
    public static boolean ROOT;
    public static final int REBOOT_SHUTDOWN = 1;
    public static final int REBOOT_COLD = 2;
    public static final int REBOOT_HOT = 3;
    public static final int REBOOT_RECOVERY = 4;
    public static final int REBOOT_FAST_BOOT = 5;

}
