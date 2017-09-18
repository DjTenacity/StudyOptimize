package com.gdj.studyoptimize.keeplive1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Comment:
 *
 * @author :DJ鼎尔东 / 1757286697@qq.cn
 * @version : Administrator1.0
 * @date : 2017/9/16
 */
public class MyService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenListener listener = new ScreenListener(this);

        listener.begin(new ScreenListener.ScreenStateListener() {
            @Override
            public void onScreenOn() {
                //开屏  finish这个一个像素的Activity
                KeepLiveActivityManager.getInstance(MyService.this).startKeepLiveActivity();
            }

            @Override
            public void onScreenOff() {
                //锁屏  启动一个像素的activity
                KeepLiveActivityManager.getInstance(MyService.this).finishKeepLiveActivity();

            }

            @Override
            public void onUserPresent() {

            }
        });
    }
}
