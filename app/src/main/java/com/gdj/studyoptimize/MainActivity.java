package com.gdj.studyoptimize;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gdj.studyoptimize.keeplive.JobHandleService;
import com.gdj.studyoptimize.keeplive.LocalService;
import com.gdj.studyoptimize.keeplive.RemoteService;
import com.gdj.studyoptimize.keeplive1.MyService;
import com.gdj.studyoptimize.keeplive1.OnePixelActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        //OnePixelActivity

    }

    public void OnePixel(View view) {

        startActivity(new Intent(this, OnePixelActivity.class));
        finish();
        //如果是这样的话,手机会回到手机桌面,但是无法进行操作,除非按了返回键
        //但是....我们要做的就是锁屏后而已,锁屏界面在上面盖住了
        //监听锁屏广播,锁了-----启动这个activity,就不会影响 用户的操作了
        //监听锁屏的   开启-----结束掉这个activity
//        要监听锁屏的广播---动态注册。
//        ScreenListener.begin(new xxxListener
//                onScreenOff() );
    }

    public void keeplive(View v) {
        //LocalService可能会被干掉,所以这里相互绑定
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
        startService(new Intent(this, JobHandleService.class));

    }


}
