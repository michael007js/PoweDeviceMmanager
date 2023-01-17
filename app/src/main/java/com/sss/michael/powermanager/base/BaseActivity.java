package com.sss.michael.powermanager.base;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.blankj.utilcode.util.ShellUtils;
import com.blankj.utilcode.util.Utils;
import com.sss.michael.powermanager.callback.OnDialogCallBack;
import com.sss.michael.powermanager.constant.AppConstant;
import com.sss.michael.powermanager.util.CheckRoot;
import com.sss.michael.powermanager.util.DialogUtil;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<B extends ViewDataBinding> extends AppCompatActivity {
    protected AlertDialog dialog;
    protected B binding;
    protected boolean isInit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置在最近任务中是否隐藏
        ActivityManager systemService = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTasks = systemService.getAppTasks();
        int size = appTasks.size();
        if (size > 0) {
            appTasks.get(0).setExcludeFromRecents(true);

        }

        setContentView(setLayout());
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppConstant.ROOT = CheckRoot.isDeviceRooted();
        if (AppConstant.ROOT) {
            if (!isInit) {
                isInit = true;
                if (binding == null) {
                    binding = DataBindingUtil.setContentView(BaseActivity.this, setLayout());
                }
                init();
            }
        } else {
            ShellUtils.execCmdAsync("su", true, new Utils.Consumer<ShellUtils.CommandResult>() {
                @Override
                public void accept(ShellUtils.CommandResult commandResult) {


                }
            });
            if (!isInit) {
                if (binding == null) {
                    binding = DataBindingUtil.setContentView(BaseActivity.this, setLayout());
                }
                init();
            }
        }
        changeUiState();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected Context getContext() {
        return this;
    }

    protected abstract int setLayout();

    protected abstract void init();

    protected void changeUiState() {

    }


    /**
     * 开关管理权限
     */
    protected void switchAdminDevicePermission() {
        if (AppConstant.DPM.isAdminActive(AppConstant.CN)) {
            DialogUtil.showDialog(this, dialog, "确定要清除设备管理权限吗？", new OnDialogCallBack() {
                @Override
                public void onConfirm() {
                    AppConstant.DPM.removeActiveAdmin(AppConstant.CN);
                    if (AppConstant.DPM.isAdminActive(AppConstant.CN)) {
                        Toast.makeText(getContext(), "清除权限成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "清除权限失败", Toast.LENGTH_SHORT).show();
                    }
                    changeUiState();
                }

                @Override
                public void onCancel() {

                }
            });
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, AppConstant.CN);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "开启一键锁可以保证程序的正常使用");
            startActivity(intent);
            changeUiState();
        }
    }


}
