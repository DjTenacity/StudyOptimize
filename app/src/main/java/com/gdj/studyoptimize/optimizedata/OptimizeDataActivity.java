package com.gdj.studyoptimize.optimizedata;

import android.content.ClipData;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gdj.studyoptimize.R;
import com.gdj.studyoptimize.entity.Basic;
import com.gdj.studyoptimize.entity.Car;
import com.gdj.studyoptimize.entity.FlatBufferBuilder;
import com.gdj.studyoptimize.entity.Items;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/***/
public class OptimizeDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimize_data);

        serialize();
    }

    private void serialize() {
        //-------序列化
        FlatBufferBuilder builder = new FlatBufferBuilder();

        int id1 = builder.createString("兰博基尼");
        //准备Car对象
        int car1 = Car.createCar(builder, 10001L, 88888L, id1);

        int id2 = builder.createString("奔驰");
        int car2 = Car.createCar(builder, 10001L, 88888L, id2);

        int id3 = builder.createString("奥迪A8");
        int car3 = Car.createCar(builder, 10001L, 88888L, id3);
        //偏移量

        int[] cars = new int[3];
        cars[0] = car1;
        cars[1] = car2;
        cars[2] = car3;

        int carList = Basic.createCarListVector(builder, cars);

        int name = builder.createString("jack");
        int email = builder.createString("jack@qq.com");
        int basic = Basic.createBasic(builder, 101, name, email, 100L, true, 100, carList);

        int basicOffset = Items.createBasicVector(builder, new int[]{basic});


        Items.startItems(builder);
        Items.addItemId(builder, 1000L);
        Items.addTimestemp(builder, 2016);
        Items.addBasic(builder, basicOffset);


        int rootItems = Items.endItems(builder);

        Items.finishItemsBuffer(builder, rootItems);


        //-----------保存数据到文件----------
        File sdCard = Environment.getExternalStorageDirectory();
        //保存的路径
        File file = new File(sdCard, "Item.text");

        if (file.exists()) {
            file.delete();
        }
        ByteBuffer data = builder.dataBuffer();
        FileOutputStream out = null;
        FileChannel channel = null;
        try {
            out = new FileOutputStream(file);
            channel = out.getChannel();

            while (data.hasRemaining()) {
                channel.write(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) out.close();
                if (channel != null) channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //----------反序列化--------------
        FileInputStream fis = null;
        FileChannel readChannel = null;

        try {
            fis = new FileInputStream(file);
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            readChannel = fis.getChannel();
            int readBytes = 0;
            while ((readBytes = readChannel.read(byteBuffer)) != -1) {
                Log.w("flatbuffer", readBytes + "读取数据个数");
            }
            //把指针回到最初的状态,准备从byteBuffer当中读取数据
            byteBuffer.flip();
            //解析出二进制为items对象
            Items items = Items.getRootAsItems(byteBuffer);
            //读取数据测试,,看看是否和保存的一致
            Log.e("FLATBUFFER", "items.id" + items.ItemId() + "temp" + items.timestemp());
            Basic basic2 = items.basic(0);
            Log.e("FLATBUFFER_Basic", "basic2.name" + basic2.name());

            for (int i = 0; i < basic2.carListLength(); i++) {
                Car car = basic2.carList(i);
                Log.w("FLATBUFFER__Car", "car.num" + car.number() + car.describle());

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (readChannel != null) readChannel.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
