package com.gdj.studyoptimize.http;

import android.graphics.BitmapFactory;
import android.net.http.HttpResponseCache;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gdj.studyoptimize.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * http请求是可以缓存的
 */
public class OptimizeHttpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimize_http);
    }

    public void openCache(View v) {
        try {
            //android系统默认的httpReponseCache(网络请求响应缓存)是关闭的
            //开启缓存之后会在擦车目录下面创建http的文件夹,HttpResponseCache会缓存所有的返回信息
            File cacheDir = new File(getCacheDir(), "http");//缓存目录
            long maxSize = 10 * 1024 * 1024;//缓存大小,单位byte
            HttpResponseCache.install(cacheDir, maxSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void request(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try { //虚拟机不支持loadhost,自己访问自己没意义,服务器要么写ip,要么写模拟器地址
                    HttpURLConnection conn = (HttpURLConnection) new URL("http://static.xfx.zhimadi.cn/7d/89/7d897624083819a69834b6593e998be9.jpg").openConnection();
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();
                    if (conn.getResponseCode() == 200) {
                        // InputStream is = conn.getInputStream();
                        //  BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    public void requestBitmap(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                //缓存图片
                try {
                    BitmapFactory.decodeStream((InputStream)
                            new URL("http://static.xfx.zhimadi.cn/7d/89/7d897624083819a69834b6593e998be9.jpg").getContent());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    public void deleteCache(View v) {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            try {
                cache.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
//三级缓存  内存缓存,外部缓存, 算法 LruCache  Sqlite缓存+加密