package com.gdj.studyoptimize.keeplive;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gdj.studyoptimize.R;

/**
 * Comment:双进程
 *
 * @author :DJ鼎尔东 / 1757286697@qq.cn
 * @version : Administrator1.0
 * @date : 2017/9/17
 */
public class LocalService extends Service {
    private MyBind binder;
    private MyServiceConnection conn;

    @Override
    public void onCreate() {
        super.onCreate();
        if (binder == null) {
            binder = new MyBind();
        }
        conn = new MyServiceConnection();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LocalService.this.bindService(new Intent( LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //(R.drawable.ic_bitmap, "神秘博士", System.currentTimeMillis());
        builder.setTicker("神秘博士");
        builder.setContentIntent(PendingIntent.getService(this, 0, intent, 0))
                .setContentTitle("神秘博士12级")
                .setAutoCancel(true)
                .setContentText("mtian")
                .setWhen(System.currentTimeMillis());

        //notification.t
        //把service设置为前台运行,避免收集系统自动杀掉该服务
        startForeground(startId, builder.build());

        return START_STICKY;//尽量好好存活
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class MyBind extends RemoteConnection.Stub {


        @Override
        public String getProgressName() throws RemoteException {
            return "LocalService";
        }
    }

    //建立连接
    class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.w("LocalServiceConnection", "建立连接成功");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.w("LocalServiceConnection", "Remote远程服务被干掉了,连接断开");

            //启动被干掉的
            LocalService.this.startService(new Intent(LocalService.this, RemoteService.class));
            LocalService.this.bindService(new Intent(LocalService.this, RemoteService.class), conn, Context.BIND_IMPORTANT);
        }
    }
}
