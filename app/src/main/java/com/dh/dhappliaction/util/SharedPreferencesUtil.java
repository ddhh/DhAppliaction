package com.dh.dhappliaction.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * Created by 端辉 on 2016/5/25.
 */
public class SharedPreferencesUtil {

    public static void editor(Context context,String user,String user_id){
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences= context.getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        editor.putString("user",user);
        editor.putString("user_id", user_id);
        //提交当前数据
        editor.commit();
        //使用toast信息提示框提示成功写入数据
    }

    public static String getUser_id(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String user_id =sharedPreferences.getString("user_id", "");
        return user_id;
    }

    public static String getUser(Context context){
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        // 使用getString方法获得value，注意第2个参数是value的默认值
        String user =sharedPreferences.getString("user", "");
        return user;
    }

}
