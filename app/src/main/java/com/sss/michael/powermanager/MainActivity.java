package com.sss.michael.powermanager;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sss.michael.powermanager.callback.OnDialogCallBack;

public class MainActivity extends BaseActivity {
    /**
     * 设备策略服务
     */
    private DevicePolicyManager dpm;
    private ComponentName componentName;
    private Button devicePermission, requestRoot;
    private Button shutdown, rebootCold, rebootHot,rebootRecovery,rebootFastBoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicePermission = findViewById(R.id.device_permission);
        requestRoot = findViewById(R.id.request_root);
        shutdown = findViewById(R.id.shutdown);
        rebootCold = findViewById(R.id.reboot_cold);
        rebootHot = findViewById(R.id.reboot_hot);
        rebootRecovery = findViewById(R.id.reboot_recovery);
        rebootFastBoot = findViewById(R.id.reboot_fastboot);
        dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        componentName = new ComponentName(this, MyAdmin.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switchUiState();
    }

    private void switchUiState() {
        devicePermission.setText(dpm.isAdminActive(componentName) ? "设备管理权限已获取" : "获取设备管理权限");
        if (ShellUtils.checkRootPermission()){
            requestRoot.setText( "Root权限已获取" );
            requestRoot.setEnabled(false);
            shutdown.setEnabled(true);
            rebootCold.setEnabled(true);
            rebootHot.setEnabled(true);
            rebootRecovery.setEnabled(true);
            rebootFastBoot.setEnabled(true);
        }else {
            requestRoot.setText( "检查Root权限");
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


    //                try{
//                    Log.v(TAG, "root Runtime->shutdown");
//                    //Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","shutdown"});  //关机
//                    Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","reboot -p"});  //关机
//                    proc.waitFor();
//                }catch(Exception e){
//                    e.printStackTrace();
//                }
    public void requestRoot(View view) {
        ShellUtils.checkRootPermission();
    }

    public void shutdown(View view) {
        DialogUtil.showDialog(this, dialog, "是否要关机？", new OnDialogCallBack() {
            @Override
            public void onConfirm() {
                ShellUtils.execCommand(new String[]{"su","-c","reboot -p"},true);
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
                ShellUtils.execCommand(new String[]{"su", "-c", "reboot "},true);
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
                ShellUtils.execCommand(new String[]{"su", "-c", "su -c killall system_server"},true);
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
                ShellUtils.execCommand(new String[]{"su", "-c", "reboot recovery"},true);
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
                ShellUtils.execCommand(new String[]{"su", "-c", "reboot bootloader"},true);
            }

            @Override
            public void onCancel() {
            }
        });

    }
}