package com.sss.michael.powermanager.activity;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.Utils;
import com.sss.michael.powermanager.R;
import com.sss.michael.powermanager.base.BaseActivity;
import com.sss.michael.powermanager.callback.OnDialogCallBack;
import com.sss.michael.powermanager.receiver.MyAdmin;
import com.sss.michael.powermanager.util.DialogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class MainActivity extends BaseActivity {
    /**
     * 设备策略服务
     */
    private DevicePolicyManager dpm;
    private ComponentName componentName;
    private Button devicePermission, requestRoot;
    private Button shutdown, rebootCold, rebootHot, rebootRecovery, rebootFastBoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        devicePermission = findViewById(R.id.device_permission);
        requestRoot = findViewById(R.id.request_root);
        shutdown = findViewById(R.id.shutdown);
        rebootCold = findViewById(R.id.reboot_cold);
        rebootHot = findViewById(R.id.reboot_hot);
        rebootRecovery = findViewById(R.id.reboot_recovery);
        rebootFastBoot = findViewById(R.id.reboot_fastboot);
        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyAdmin.class);


        //设置在最近任务中是否隐藏
        ActivityManager systemService = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTasks = systemService.getAppTasks();
        int size = appTasks.size();
        if (size > 0) {
            appTasks.get(0).setExcludeFromRecents(true);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        switchUiState();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

    }

    private void switchUiState() {
        devicePermission.setText(dpm.isAdminActive(componentName) ? "设备管理权限已获取" : "获取设备管理权限");
        if (DeviceUtils.isDeviceRooted() && ShellUtils.execCmd("echo root", true).result == 0) {
            requestRoot.setText("Root权限已获取");
            requestRoot.setEnabled(false);
            shutdown.setEnabled(true);
            rebootCold.setEnabled(true);
            rebootHot.setEnabled(true);
            rebootRecovery.setEnabled(true);
            rebootFastBoot.setEnabled(true);
        } else {
            requestRoot.setText("检查Root权限");
            requestRoot.setEnabled(true);
            shutdown.setEnabled(false);
            rebootCold.setEnabled(false);
            rebootHot.setEnabled(false);
            rebootRecovery.setEnabled(false);
            rebootFastBoot.setEnabled(false);
        }

    }

    /**
     * 开关管理权限
     */
    public void switchAdmin(View view) {
        if (dpm.isAdminActive(componentName)) {
            DialogUtil.showDialog(this, dialog, "确定要清除设备管理权限吗？", new OnDialogCallBack() {
                @Override
                public void onConfirm() {
                    dpm.removeActiveAdmin(componentName);
                    if (dpm.isAdminActive(componentName)) {
                        Toast.makeText(MainActivity.this, "清除权限成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "清除权限失败", Toast.LENGTH_SHORT).show();
                    }
                    switchUiState();
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            ComponentName mDeviceAdminSample = new ComponentName(this, MyAdmin.class);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启一键锁可以保证程序的正常使用");
            startActivity(intent);
            switchUiState();
        }
    }

    /**
     * 一键锁屏
     */
    public void lockScreen(View view) {
        if (dpm.isAdminActive(componentName)) {
            dpm.lockNow();// 锁屏
            dpm.resetPassword("", 0);// 设置屏蔽密码
            // 清除Sdcard上的数据
            // dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
            // 恢复出厂设置
            // dpm.wipeData(0);
        } else {
            DialogUtil.showDialog(this, dialog, "缺少设备管理权限，是否去授予该权限？", new OnDialogCallBack() {
                @Override
                public void onConfirm() {
                    switchAdmin(null);
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    /**
     * 卸载当前软件
     */
    public void uninstallSoftware(View view) {
        if (dpm.isAdminActive(componentName)) {
            DialogUtil.showDialog(this, dialog, "是否需要连带清除设备管理权限？", new OnDialogCallBack() {
                @Override
                public void onConfirm() {
                    dpm.removeActiveAdmin(componentName);
                    if (dpm.isAdminActive(componentName)) {
                        Toast.makeText(MainActivity.this, "清除权限成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "清除权限失败", Toast.LENGTH_SHORT).show();
                    }
                    switchUiState();
                    Intent uninstall_intent = new Intent();
                    uninstall_intent.setAction(Intent.ACTION_DELETE);
                    uninstall_intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(uninstall_intent);
                }

                @Override
                public void onCancel() {
                    Intent uninstall_intent = new Intent();
                    uninstall_intent.setAction(Intent.ACTION_DELETE);
                    uninstall_intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(uninstall_intent);
                }
            });
        } else {
            Intent uninstall_intent = new Intent();
            uninstall_intent.setAction(Intent.ACTION_DELETE);
            uninstall_intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(uninstall_intent);
        }

    }

    public void requestRoot(View view) {
        ShellUtils.execCmdAsync("su", true, new Utils.Consumer<ShellUtils.CommandResult>() {
            @Override
            public void accept(ShellUtils.CommandResult commandResult) {
                LogUtils.e(commandResult.toString());
            }
        });
    }

    public void shutdown(View view) {
        DialogUtil.showDialog(this, dialog, "是否要关机？", new OnDialogCallBack() {
            @Override
            public void onConfirm() {
                ShellUtils.execCmd(new String[]{"su", "-c", "reboot -p"}, true);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void rebootCold(View view) {
        DialogUtil.showDialog(this, dialog, "是否要重启设备？", new OnDialogCallBack() {
            @Override
            public void onConfirm() {
                ShellUtils.execCmd(new String[]{"su", "-c", "reboot "}, true);
//                DangerousUtils.reboot();
            }

            @Override
            public void onCancel() {
            }
        });
    }


    public void rebootHot(View view) {
        DialogUtil.showDialog(this, dialog, "是否要重启android系统？", new OnDialogCallBack() {
            @Override
            public void onConfirm() {
                ShellUtils.execCmd(new String[]{"su", "-c", "su -c killall system_server"}, true);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void rebootRecovery(View view) {
        DialogUtil.showDialog(this, dialog, "是否要重启到recovery模式？", new OnDialogCallBack() {
            @Override
            public void onConfirm() {
                ShellUtils.execCmd(new String[]{"su", "-c", "reboot recovery"}, true);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    public void rebootFastBoot(View view) {
        DialogUtil.showDialog(this, dialog, "是否要重启到fastboot模式？", new OnDialogCallBack() {
            @Override
            public void onConfirm() {
                ShellUtils.execCmd(new String[]{"su", "-c", "reboot bootloader"}, true);
            }

            @Override
            public void onCancel() {
            }
        });

    }

    /**
     * https://blog.csdn.net/Sugar_wolf/article/details/127806122?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-3-127806122-blog-123650861.pc_relevant_aa&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EYuanLiJiHua%7EPosition-3-127806122-blog-123650861.pc_relevant_aa
     * https://blog.csdn.net/lb245557472/article/details/84068519
     * https://github.com/mzlogin/awesome-adb?login=from_csdn
     * <p>
     * 查看连接过的 WiFi 密码
     * cat /data/misc/wifi/*.conf   如果 Android O 或以后，WiFi 密码保存的地址有变化，是在 WifiConfigStore.xml 里面  cat /data/misc/wifi/WifiConfigStore.xml
     * <p>
     * 查看设备 IP 地址
     * ifconfig | grep Mask  或  ifconfig wlan0  或  netcfg
     * <p>
     * 查看设备 CPU 信息
     * cat /proc/cpuinfo
     * <p>
     * 查看设备内存信息
     * cat /proc/meminfo
     * <p>
     * 查看设备更多硬件与系统属性
     * cat /system/build.prop
     */

    public void clearBackground(View view) {
        getTopPackageNameFor21(this);

if (true){
    return;
}
        ShellUtils.execCmdAsync(new String[]{ "ps"}, true, new Utils.Consumer<ShellUtils.CommandResult>() {
            @Override
            public void accept(ShellUtils.CommandResult commandResult) {
                LogUtils.e(commandResult.toString());
            }
        });
    }


    public static String getTopPackageNameFor21(Context mContext) {
        String topPackageName = "";
        android.app.usage.UsageStatsManager usm = (android.app.usage.UsageStatsManager) mContext.getSystemService("usagestats");
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        long startTime = calendar.getTimeInMillis() - 60*1000;

        //Log.logD(" Range start:" + dateFormat.format(startTime) );
        //Log.logD(" Range   end:" + dateFormat.format(endTime));
        android.app.usage.UsageEvents uEvents = usm.queryEvents(startTime,endTime);
        android.app.usage.UsageEvents.Event e = new android.app.usage.UsageEvents.Event();
        while (uEvents.hasNextEvent()){

            if (uEvents.getNextEvent(e)){
                topPackageName = e.getPackageName();
                LogUtils.e(  topPackageName);
            }
        }

        return topPackageName;
    }


    private static  String getRunningApk() {
        long startTime = System.currentTimeMillis();
        String pgkProcessAppMap = "";
        Set<String> rProcess = new HashSet<String>();
        String cmd = "ps";
        try {
            java.lang.Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = null;
            int index = 0;
            while ((line = in.readLine()) != null) {
                LogUtils.e(line);
                if (index > 0) {
                    StringTokenizer st = new StringTokenizer(line);
                    int lenght = st.countTokens();
                    if (lenght > 0) {
                        String uid = st.nextToken();//0 index USER
                        if (uid.startsWith("u0_") ) {
                            String processInfo = "";
                            for (int i = 0; i < (lenght - 1); i++) {
                                if (!(i == (lenght - 2))) {
                                    st.nextToken();
                                } else {
                                    processInfo = st.nextToken();
                                }
                            }

                            if (!TextUtils.isEmpty(processInfo)) {
                                if (processInfo.contains(":")) {
                                    String a[] = processInfo.split(":");
                                    rProcess.add(a[0]);
                                } else {
                                    rProcess.add(processInfo);
                                }
                            }

                        }
                    }
                }
                index++;
            }
        } catch (IOException e) {
            LogUtils.e("getRunningApk err="+e.toString());
        }
        for(String pro : rProcess){
            pgkProcessAppMap += pro + ",";
        }
        if (pgkProcessAppMap.contains(",") && pgkProcessAppMap.length() > 0 ) {
            pgkProcessAppMap = pgkProcessAppMap.substring(0, pgkProcessAppMap.length()-1);
        }
        long endTime = System.currentTimeMillis();
        LogUtils.e("do_exec pgkProcessAppMap = " + pgkProcessAppMap + "\t time = " + (endTime - startTime));
        return pgkProcessAppMap;
    }

    /**
     * https://blog.csdn.net/guangdeshishe/article/details/116758070
     */
}