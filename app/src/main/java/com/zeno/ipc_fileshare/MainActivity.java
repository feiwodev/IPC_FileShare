package com.zeno.ipc_fileshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.zeno.ipc_fileshare.bean.Person;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *  文件共享 ， 可以实现跨进程通信 ，两个进程读取同一份文件 ，实现数据交流 。
 *
 *  局限性：并发读写容易出现内容不一致 ，可以通过多线程同步进行进程读写操作 。
 *
 */
public class MainActivity
        extends AppCompatActivity
{

    private TextView tvInfo ;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init() ;
    }

    private void init()
    {
        tvInfo = (TextView) findViewById(R.id.tv_info);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_write:
                writeObject();
                break;
            case R.id.btn_read:
                readObject() ;
                break;
            case R.id.btn_target:
                target() ;
                break;
            case R.id.btn_write_sp:
                writeSp() ;
                break;
            case R.id.btn_read_sp:
                readSp() ;
                break;
        }

    }

    /**
     * 读取对象数据
     */
    private void readObject()
    {
        try
        {
            // 读取对象文件
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(new File(externalStorageDirectory, "cache"))));
            Person person = (Person) inputStream.readObject();

            inputStream.close();

            if (person != null) {
                // show
                tvInfo.setText(person.toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 将对象写入文件
     */
    private void writeObject()
    {
        Person p = new Person() ;
        p.setUserName("zeno");
        p.setGender("男");
        p.setAge(20);

        try{
            // 将对象写入文件
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File(externalStorageDirectory,"cache"))) ;
            outputStream.writeObject(p);

            outputStream.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到下一个单独进程的Activity
     */
    public void target() {
        Intent intent = new Intent() ;
        intent.setAction("com.zeno.activity.REMOTE") ;
        startActivity(intent);
    }

    /**
     * 写入SharedPerference
     */
    private void writeSp() {
        SharedPreferences sp = getSharedPreferences("sp",
                Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS | Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("name","xiaozhu") ;
        edit.putString("age","20") ;
        edit.commit();
    }

    private void readSp() {
        SharedPreferences sp = getSharedPreferences("sp",
                Context.MODE_WORLD_WRITEABLE | Context.MODE_MULTI_PROCESS | Context.MODE_MULTI_PROCESS);
        String name = sp.getString("name", "");
        String age = sp.getString("age", "");
        tvInfo.setText(name+"==="+age);
    }
}
