package com.shuo.ba.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginAccountHelper {
    public static void setLoginAccount(String userName, Context context) {
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        sp.edit().putString("user_name", userName).apply();
    }

    public static void logOutAccount(Context context){
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        sp.edit().remove("user_name").apply();
    }

    public static String getCurUserName(Context context){
        SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        String userName = sp.getString("user_name", null);
        if (userName == null){
            return null;
        }
        return userName;
    }

    /**
     * 加密，把一个字符串在原有的基础上+1
     *
     * @param data 需要解密的原字符串
     * @return 返回解密后的新字符串
     */
    private static String encode(String data) {
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //遍历
        for (int i = 0; i < b.length; i++) {
            b[i] += 1;//在原有的基础上+1
        }
        return new String(b);
    }

    /**
     * 解密：把一个加密后的字符串在原有基础上-1
     * @param data 加密后的字符串
     * @return 返回解密后的新字符串
     */
    private static String decode(String data) {
        //把字符串转为字节数组
        byte[] b = data.getBytes();
        //遍历
        for(int i=0;i<b.length;i++) {
            b[i] -= 1;//在原有的基础上-1
        }
        return new String(b);
    }
}
