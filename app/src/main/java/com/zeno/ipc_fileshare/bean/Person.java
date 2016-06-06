package com.zeno.ipc_fileshare.bean;

import java.io.Serializable;

/**
 * Created by Zeno on 2016/5/31.
 */
public class Person implements Serializable
{


    private String userName ;
    private int age ;
    private String gender ;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    @Override
    public String toString()
    {
        return "Person{" +
                "userName='" + userName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                '}';
    }
}
