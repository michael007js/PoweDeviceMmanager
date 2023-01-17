package com.sss.michael.powermanager.activity;

import android.content.Intent;
import android.view.View;
import android.view.WindowManager;

import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.Utils;
import com.sss.michael.powermanager.R;
import com.sss.michael.powermanager.base.BaseActivity;
import com.sss.michael.powermanager.callback.OnClickCallBack;
import com.sss.michael.powermanager.callback.OnDialogCallBack;
import com.sss.michael.powermanager.constant.AppConstant;
import com.sss.michael.powermanager.databinding.ActivityLauncherBinding;
import com.sss.michael.powermanager.util.DialogUtil;
import com.sss.michael.powermanager.util.ShellUtils;
import com.sss.michael.powermanager.view.LayoutPowerRebootBtnView;

public class LaunchActivity extends BaseActivity<ActivityLauncherBinding> {

    @Override
    protected int setLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void init() {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = ScreenUtils.getScreenWidth();
        layoutParams.height = ScreenUtils.getAppScreenHeight()-BarUtils.getNavBarHeight();
        getWindow().setAttributes(layoutParams);


        binding.rebootCold.setData(AppConstant.REBOOT_COLD, new LayoutPowerRebootBtnView.OnLayoutPowerRebootBtnViewCallBack() {
            @Override
            public void onItemClick(int mode) {
                DialogUtil.showDialog(getContext(), dialog, "是否要重启设备？", new OnDialogCallBack() {
                    @Override
                    public void onConfirm() {
                        ShellUtils.execCmdAsync(new String[]{"su", "-c", "reboot"}, false,true, new Utils.Consumer<ShellUtils.CommandResult>() {
                            @Override
                            public void accept(ShellUtils.CommandResult commandResult) {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
        binding.shutdown.setData(AppConstant.REBOOT_SHUTDOWN, new LayoutPowerRebootBtnView.OnLayoutPowerRebootBtnViewCallBack() {
            @Override
            public void onItemClick(int mode) {
                DialogUtil.showDialog(getContext(), dialog, "是否要关机？", new OnDialogCallBack() {
                    @Override
                    public void onConfirm() {
                        ShellUtils.execCmdAsync(new String[]{"su", "-c", "reboot -p"}, false,true, new Utils.Consumer<ShellUtils.CommandResult>() {
                            @Override
                            public void accept(ShellUtils.CommandResult commandResult) {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
        binding.rebootHot.setData(AppConstant.REBOOT_HOT, new LayoutPowerRebootBtnView.OnLayoutPowerRebootBtnViewCallBack() {
            @Override
            public void onItemClick(int mode) {
                DialogUtil.showDialog(getContext(), dialog, "是否要重启android系统？", new OnDialogCallBack() {
                    @Override
                    public void onConfirm() {
                        ShellUtils.execCmdAsync(new String[]{"su", "-c", "killall system_server"},false, true, new Utils.Consumer<ShellUtils.CommandResult>() {
                            @Override
                            public void accept(ShellUtils.CommandResult commandResult) {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
        binding.rebootRecovery.setData(AppConstant.REBOOT_RECOVERY, new LayoutPowerRebootBtnView.OnLayoutPowerRebootBtnViewCallBack() {
            @Override
            public void onItemClick(int mode) {
                DialogUtil.showDialog(getContext(), dialog, "是否要重启到recovery模式？", new OnDialogCallBack() {
                    @Override
                    public void onConfirm() {
                        ShellUtils.execCmdAsync(new String[]{"su", "-c", "reboot recovery"},false, true, new Utils.Consumer<ShellUtils.CommandResult>() {
                            @Override
                            public void accept(ShellUtils.CommandResult commandResult) {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
        binding.rebootFastboot.setData(AppConstant.REBOOT_FAST_BOOT, new LayoutPowerRebootBtnView.OnLayoutPowerRebootBtnViewCallBack() {
            @Override
            public void onItemClick(int mode) {
                DialogUtil.showDialog(getContext(), dialog, "是否要重启到fastboot模式？", new OnDialogCallBack() {
                    @Override
                    public void onConfirm() {
                        ShellUtils.execCmdAsync(new String[]{"su", "-c", "reboot bootloader"},false, true, new Utils.Consumer<ShellUtils.CommandResult>() {
                            @Override
                            public void accept(ShellUtils.CommandResult commandResult) {
                                finish();
                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                    }
                });
            }
        });
        binding.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.tvLockScreen.setOnClickListener(new OnClickCallBack() {
            @Override
            protected void onViewClick(View v, boolean root) {
                if (AppConstant.DPM.isAdminActive(AppConstant.CN)) {
                    AppConstant.DPM.lockNow();// 锁屏
                    finish();
                } else {
                    DialogUtil.showDialog(getContext(), dialog, "缺少设备管理权限，是否去授予该权限？", new OnDialogCallBack() {
                        @Override
                        public void onConfirm() {
                            switchAdminDevicePermission();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
            }
        });
        binding.ivQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), QuestionActivity.class).putExtra("action", 1));
            }
        });

        binding.ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FunctionActivity.class));
            }
        });

    }

}
