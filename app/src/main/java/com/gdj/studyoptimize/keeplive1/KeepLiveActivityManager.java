package com.gdj.studyoptimize.keeplive1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.ref.WeakReference;

/**
 * Comment:
 *
 * @author :DJ鼎尔东 / 1757286697@qq.cn
 * @version : Administrator1.0
 * @date : 2017/9/16
 */
public class KeepLiveActivityManager {
    private static KeepLiveActivityManager instance;
    //静态的,持有上下文 可能会出问题
    Context mContext;

    public KeepLiveActivityManager(Context context) {
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public static KeepLiveActivityManager getInstance(Context context) {
        if (instance == null) {
            instance = new KeepLiveActivityManager(context.getApplicationContext());
        }
        return instance;
    }

    public void startKeepLiveActivity() {
        Intent intent = new Intent(mContext, OnePixelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    public void finishKeepLiveActivity() {
        if (activityInstance != null && activityInstance.get() != null)
            activityInstance.get().finish();
    }

    WeakReference<Activity> activityInstance;

    public void setKeepLiveActivity(Activity activity) {
        activityInstance = new WeakReference<Activity>(activity);
    }

}
