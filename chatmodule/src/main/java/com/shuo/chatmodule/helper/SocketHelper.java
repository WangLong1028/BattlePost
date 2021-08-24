package com.shuo.chatmodule.helper;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.shuo.chatmodule.constant.NetworkConstant;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketHelper {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getMsg(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            int len = socket.getInputStream().read(bytes);
            return new String(bytes, StandardCharsets.UTF_8).substring(0, len);
        } catch (Exception e) {
            return null;
        }
    }

    public static String decodeMsg(String msg) {
        for (int i = 0; i < msg.length(); i++) {
            if (msg.charAt(i) == '\0') {
                return msg.substring(0, i);
            }
        }
        return msg;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean sendMsg(Socket socket, String msg) {
        try {
            if (msg == null) {
                return false;
            }
            if (socket == null) {
                return false;
            }
            socket.getOutputStream().write(msg.getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static boolean uploadHeadshotPic(Socket socket, int id, String path) {
        try {
            if(socket == null){
                return false;
            }
            sendMsg(socket, NetworkConstant.REQUEST_HEADER_POST_HEADSHOT_PIC);

            File file = new File(path);

            String[] fileInfo = file.getName().split("\\.");
            sendMsg(socket, id + "." + fileInfo[fileInfo.length - 1]);

            FileInputStream fileInputStream = new FileInputStream(file);
            sendMsg(socket, "" + file.length());
            byte[] bytes = new byte[1024];
            int data;
            while ((data = fileInputStream.read(bytes)) != -1) {
                socket.getOutputStream().write(bytes, 0, data);
            }
            fileInputStream.close();

            String msg = getMsg(socket);
            if (msg == null) {
                return false;
            }
            return msg.equals(NetworkConstant.UPLOAD_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}