package com.sss.michael.powermanager.activity;

import android.content.Intent;
import android.view.View;

import com.sss.michael.powermanager.R;
import com.sss.michael.powermanager.base.BaseActivity;
import com.sss.michael.powermanager.callback.OnClickCallBack;
import com.sss.michael.powermanager.databinding.ActivityFunctionBinding;

public class FunctionActivity extends BaseActivity<ActivityFunctionBinding> {

    @Override
    protected int setLayout() {
        return R.layout.activity_function;
    }

    @Override
    protected void init() {

        binding.itvClickWifiPassword.setOnClickListener(new OnClickCallBack() {
            @Override
            protected void onViewClick(View v, boolean root) {
                if (root){
                    startActivity(new Intent(getContext(),WifiPasswordPreviewActivity.class));
                }
            }
        });
    }
}