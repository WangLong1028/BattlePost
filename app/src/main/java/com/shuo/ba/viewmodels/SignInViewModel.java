package com.shuo.ba.viewmodels;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.shuo.ba.R;
import com.shuo.ba.activitys.ChatActivity;
import com.shuo.ba.helper.LoginAccountHelper;
import com.shuo.chatmodule.beans.UserBean;
import com.shuo.chatmodule.login.LoginModel;


public class SignInViewModel extends AndroidViewModel implements LoginModel.LoginResultCallback {

    // 登录Model;
    private LoginModel loginModel;

    private MutableLiveData<String> userName = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();

    private Context context;
    private Activity activity;

    public SignInViewModel(@NonNull Application application) {
        super(application);

        this.context = application;
        this.loginModel = new LoginModel(this);
    }

    public void setActivity(Activity activity){
        this.activity = activity;
    }

    public void login() {
        if (getUserName().getValue() == null) {
            Toast.makeText(context, R.string.please_input_user_name, Toast.LENGTH_LONG).show();
            return;
        }
        if (getPassword().getValue() == null){
            Toast.makeText(context, R.string.please_input_password, Toast.LENGTH_LONG).show();
        }
        // 登录
        //loginModel.login(userName.getValue(), password.getValue());
        new Thread(()->{
            Looper.prepare();
            loginModel.login(userName.getValue(), password.getValue());
        }).start();
    }

    public MutableLiveData<String> getUserName() {
        if (userName == null) {
            userName = new MutableLiveData<>();
        }
        return userName;
    }

    public MutableLiveData<String> getPassword() {
        if (password == null) {
            password = new MutableLiveData<>();
        }
        return password;
    }

    @Override
    public void logSuccess() {

        Log.v("tag", "success");
        activity.runOnUiThread(()->{
            Toast.makeText(activity, R.string.sign_in_success, Toast.LENGTH_LONG).show();
        });
        LoginAccountHelper.setLoginAccount(userName.getValue(), activity);
        Intent intent = new Intent(activity, ChatActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void logFailed(String s) {
        Log.e("tag", s);
        activity.runOnUiThread(()->{
            Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        });
    }
}
