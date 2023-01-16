package com.sss.michael.powermanager.callback;

import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.sss.michael.powermanager.constant.AppConstant;

public abstract class OnClickCallBack implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        if (!AppConstant.ROOT) {
            ToastUtils.showShort("当前设备没有root!");
        }
        onViewClick(v, AppConstant.ROOT);
    }

    protected abstract void onViewClick(View v, boolean root);

}
