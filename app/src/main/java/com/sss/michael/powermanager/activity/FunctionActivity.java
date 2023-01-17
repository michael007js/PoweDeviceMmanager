package com.sss.michael.powermanager.activity;

import android.content.Intent;
import android.provider.Settings;
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
        binding.itvClickAndroidSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);

            }
        });
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