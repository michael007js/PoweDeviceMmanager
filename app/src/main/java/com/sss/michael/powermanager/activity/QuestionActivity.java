package com.sss.michael.powermanager.activity;

import com.sss.michael.powermanager.R;
import com.sss.michael.powermanager.base.BaseActivity;
import com.sss.michael.powermanager.databinding.ActivityQuestionBinding;

public class QuestionActivity extends BaseActivity<ActivityQuestionBinding> {


    @Override
    protected int setLayout() {
        return R.layout.activity_question;
    }

    @Override
    protected void init() {
        int action = getIntent().getIntExtra("action", 0);
        if (action == 1) {
            binding.tvDesc.setText(
                    "1.所有的功能都是基于Root后，如果您的设备没有Root,那么本软件的很大一部分功能将无法使用" +
                            "\n" +"\n" +
                            "2.如果您的设备已经Root了但是点击之后没有反应，请确保在面具、super su等Root管理软件中将本程序授权并已经设置为Root用户"
            );
        }
    }
}