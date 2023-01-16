package com.sss.michael.powermanager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.sss.michael.powermanager.R;
import com.sss.michael.powermanager.callback.OnClickCallBack;
import com.sss.michael.powermanager.constant.AppConstant;
import com.sss.michael.powermanager.databinding.LayoutPowerRebootBtnBinding;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

public class LayoutPowerRebootBtnView extends LinearLayout {
    private LayoutPowerRebootBtnBinding btnBinding;

    public LayoutPowerRebootBtnView(Context context) {
        this(context, null);
    }

    public LayoutPowerRebootBtnView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LayoutPowerRebootBtnView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        btnBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_power_reboot_btn, this, true);
    }

    public void setData(final int type, final OnLayoutPowerRebootBtnViewCallBack onLayoutPowerRebootBtnViewCallBack) {
        if (type == AppConstant.REBOOT_SHUTDOWN) {
            btnBinding.ivIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_power_shutdown));
            btnBinding.ivIcon.setImageResource(R.mipmap.power_shutdown);
            btnBinding.tvName.setText("关机");
            btnBinding.tvDesc.setText("正常关机");
        } else if (type == AppConstant.REBOOT_COLD) {
            btnBinding.ivIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_power_cold));
            btnBinding.ivIcon.setImageResource(R.mipmap.power_reboot);
            btnBinding.tvName.setText("冷重启");
            btnBinding.tvDesc.setText("正常重启");
        } else if (type == AppConstant.REBOOT_HOT) {
            btnBinding.ivIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_power_hot));
            btnBinding.ivIcon.setImageResource(R.mipmap.power_hot_reboot);
            btnBinding.tvName.setText("热重启");
            btnBinding.tvDesc.setText("重启当前Android系统（不会初始化硬件，这可能会引发一些未知的问题）");
        } else if (type == AppConstant.REBOOT_RECOVERY) {
            btnBinding.ivIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_power_recovery));
            btnBinding.ivIcon.setImageResource(R.mipmap.power_recovery);
            btnBinding.tvName.setText("Recovery");
            btnBinding.tvDesc.setText("重启到Recovery模式（俗称卡刷模式）");
        } else if (type == AppConstant.REBOOT_FAST_BOOT) {
            btnBinding.ivIcon.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.bg_power_fastboot));
            btnBinding.ivIcon.setImageResource(R.mipmap.power_fastboot);
            btnBinding.tvName.setText("Fastboot");
            btnBinding.tvDesc.setText("重启到Fastboot模式（俗称USB线刷模式）");
        }

        btnBinding.llParent.setOnClickListener(new OnClickCallBack() {
            @Override
            protected void onViewClick(View v, boolean root) {
                if (root) {
                    if (onLayoutPowerRebootBtnViewCallBack != null) {
                        onLayoutPowerRebootBtnViewCallBack.onItemClick(type);
                    }
                }
            }
        });
    }

    public interface OnLayoutPowerRebootBtnViewCallBack {
        void onItemClick(int mode);
    }
}
