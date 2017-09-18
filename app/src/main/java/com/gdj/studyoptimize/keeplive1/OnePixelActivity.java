package com.gdj.studyoptimize.keeplive1;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Comment: 一个像素的activity来 宝  活
 *
 * @author :DJ鼎尔东 / 1757286697@qq.cn
 * @version : Administrator1.0
 * @date : 2017/9/16
 */
public class OnePixelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);

        WindowManager.LayoutParams params = window.getAttributes();

        params.height = 1;
        params.width = 1;
        params.x = 0;
        params.y = 0;
        window.setAttributes(params);

        //注意主题样式

        KeepLiveActivityManager.getInstance(this).setKeepLiveActivity(this);


    }
}
