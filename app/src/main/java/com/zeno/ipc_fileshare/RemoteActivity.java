package com.zeno.ipc_fileshare;

import android.content.Context;
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

public class RemoteActivity
        extends AppCompatActivity
{

    private TextView tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);

        initView();

        initDatas();
    }

    private void initDatas()
    {
        try
        {
            // 得到SD卡目录对象
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            // 得到保存的对象输入流
            ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(
                    new FileInputStream(new File(externalStorageDirectory, "cache"))));
            // 读取文件对象 ， 转换成目标对象
            Person person = (Person) inputStream.readObject();

            // 关闭流
            inputStream.close();
            if (person != null) {
                // show
                tvShow.setText(person.toString());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initView()
    {
        tvShow = (TextView) findViewById(R.id.tv_show);
    }


    public void onClick(View view)
    {
        switch (view.getId()) {
            case R.id.btn_write:
                writeData();
                break;
            case R.id.btn_read_sp:
                readSp() ;
                break;
            case R.id.btn_write_sp:
                writeSp();
                break;
        }
    }

    private void writeData()
    {
        Person p = new Person() ;
        p.setUserName("xiaojiu");
        p.setGender("女");
        p.setAge(16);

        try{

            // 将对象写入文件
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    new FileOutputStream(new File(externalStorageDirectory, "cache")));
            outputStream.writeObject(p);

        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跨进程读取SharedPreferences
     */
    private void readSp() {
        try{
            // 通过包名得到另一个进程的上下文
            Context context = createPackageContext("com.zeno.ipc_fileshare",
                    Context.CONTEXT_IGNORE_SECURITY);
            // 得到SP对象
            SharedPreferences sp = context.getSharedPreferences("sp",
                    Context.MODE_WORLD_READABLE | Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
            // 取到数据
            String name = sp.getString("name", "");
            String age = sp.getString("age","") ;
            tvShow.setText(name+"==="+age);


        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void writeSp() {
        try{
            // 通过包名得到另一个进程的上下文
            Context context = createPackageContext("com.zeno.ipc_fileshare",Context.CONTEXT_IGNORE_SECURITY) ;
            SharedPreferences sp = context.getSharedPreferences("sp",
                    Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE | Context.MODE_MULTI_PROCESS);
            // 得到编辑对象
            SharedPreferences.Editor edit = sp.edit();
            // 装填数据
            edit.putString("name","xiaojiu") ;
            edit.putString("age","20") ;
            // 写入
            edit.commit();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
