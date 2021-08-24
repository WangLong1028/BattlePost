package com.shuo.chatmodule.chat;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.shuo.chatmodule.beans.UserBean;
import com.shuo.chatmodule.constant.NetworkConstant;
import com.shuo.chatmodule.helper.SocketHelper;

import java.io.IOException;
import java.net.Socket;

public class RequestUserModel {

    private RequestUserManager requestUserManager;

    public RequestUserModel(RequestUserManager requestUserManager) {
        this.requestUserManager = requestUserManager;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loadUser(String userName) {
        if (requestUserManager == null) {
            return;
        }

        requestUserManager.loadingUser();
        try {
            Socket socket = new Socket(NetworkConstant.SERVER_IP, NetworkConstant.SERVER_PORT);
            // 获取用户信息
            SocketHelper.sendMsg(socket, NetworkConstant.REQUEST_HEADER_REQUEST_USER + NetworkConstant.REQUEST_SEPARATOR + userName);
            String userData = SocketHelper.getMsg(socket);
            decodeData(userData);
        } catch (IOException e) {
            requestUserManager.loadUserFailed(NetworkConstant.CONNECT_ERROR);
        }
    }

    private void decodeData(String userData) {
        if (requestUserManager == null) {
            return;
        }

        if (userData == null) {
            requestUserManager.loadUserFailed(NetworkConstant.CONNECT_ERROR);
            return;
        }
        String[] userDataInfo = userData.split(NetworkConstant.DATA_SEPARATOR);
        if (userDataInfo.length != 6) {
            requestUserManager.loadUserFailed(userData);
            return;
        }

        int userId = Integer.parseInt(userDataInfo[0]);
        String userName = userDataInfo[1];
        String password = userDataInfo[2];
        String secureProblem = userDataInfo[3];
        String secureAnswer = userDataInfo[4];

//        int userId = 1;
//        String userName = "reimage";
//        String password = "e1261505825";
//        String secureProblem = "手机型号";
//        String secureAnswer = "S20+";
//        int headshot = 0;

        UserBean userBean = new UserBean(userId, userName, password, secureProblem, secureAnswer, 0);
        requestUserManager.loadUserSuccess(userBean);
    }


    public interface RequestUserManager {
        void loadingUser();

        void loadUserSuccess(UserBean userBean);

        void loadUserFailed(String errorMsg);
    }
}
