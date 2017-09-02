package com.gdj.studyoptimize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片优化
 * 图片存在的几种形式:
 * file,
 * 流,大小和File差不多,可以通过把图片变成Base64的字符串传进去,以便进行加密
 * bitmap
 */
public class OptimizeBitmapActivity extends AppCompatActivity {

    private File imageFile;
    private File sdFile;

    /**
     * 图片压缩
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimize_bitmap);

        sdFile = Environment.getExternalStorageDirectory();

        //本地图片
        imageFile = new File(sdFile, "cccc.jpg");
    }

    /**DensityDpi    图片存在的几种形式：**/

    /**
     * BitmapFactory.Options:参数:
     * inDensity:bitmap的像素密度
     * inTargetDensity：bitmap最终的像素密度
     *
     * decodeResourceStream方法中会根据当前设备屏幕像素的密度做了缩放适配的操作
     */
    /***BitmapFactory中的api主要有三类:
     * 加载(decodeFile文件,decodeResource资源,decodeStream流)
     *
     */
    /**
     * 质量压缩--通过算法抠掉了图片中的一些点附近相近的像素,来达到降低质量减少文件大小的目的
     * 减小了图片的质量,只能实现对file的影响,对价在这个图片出来的bitmap内存是无法节省的
     * 因为bitmap在内存中的大小是按照像素计算的,也就是width*height,
     * 对于质量压缩,并不会改变图片的真是的像素
     * <p>
     * 应用场景:将图片压缩后保存到本地,或者将图片上传到服务器中
     */
    public void qualitCompress(View v) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // BitmapFactory.decodeResource(getResources(), R.drawable.ic_bitmap);

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        //压缩图片
        compressImageToFile(bitmap, new File(sdFile, "qualitCompress.jpeg"));

    }

    //降低图片的质量来进行压缩,可以用来----传输
    public static void compressImageToFile(Bitmap bitmap, File file) {
        int quality = 50;   //0~100
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 尺寸压缩
     * 通过减少单位尺寸的像素值,真正意义上的降低像素,
     * 缓存缩略图,头像
     * AA
     * CD--->压缩后
     * AB
     * AA
     */
    public void sizeCompress(View v) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        // BitmapFactory.decodeResource(getResources(), R.drawable.ic_bitmap);

        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        //压缩图片
        sizeCompress(bitmap, new File(sdFile, "sizeCompress.jpeg"));

    }


    public static void sizeCompress(Bitmap bitmap, File file) {
        //压缩尺寸倍数,值越大,图片的尺寸就越小
        int ratio = 4;
        /** 颜色0~255   也有0~128*/
        Bitmap result = Bitmap.createBitmap(bitmap.getWidth() / ratio, bitmap.getHeight() / ratio, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);

        RectF rect = new RectF(0, 0, (bitmap.getWidth() / ratio), (bitmap.getHeight() / ratio));

        canvas.drawBitmap(bitmap, null, rect, null);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        result.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //采样率压缩
    public void sampleCompress(View v) {
        File saveFile = new File(getExternalCacheDir(), "采样率压缩.jpg");
        File f = new File("/storage/sdcard0/DCIM/Camera/.....jpg");
        sampleCompress(f.getAbsolutePath(), saveFile);
    }

    //采样率压缩,通过设置采样率来把图片加载到内存当中,内存中的图片就已经被压缩过了
    public static void sampleCompress(String filePath, File file) {
        //数值越高,图片像素越低
        int inSampleSize = 8;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;//位true的时候不会真正的加载图片,得到的只是图片的宽高而已
        //采样率
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //把压缩后的数据存放在baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        try {
            if (file.exists()) {
                file.delete();
            } else {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //优化:::绕过安卓Bitmap  API层, 来自己编码实现修复使用哈夫曼算法
    //终极压缩


}