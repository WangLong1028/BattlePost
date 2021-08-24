package com.shuo.ba.viewmodels;

import android.app.Activity;
import android.app.Application;
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
import com.shuo.chatmodule.login.SignUpModel;
import com.wildma.pictureselector.PictureSelector;

public class SignUpViewModel extends AndroidViewModel implements SignUpModel.SignUpResultCallback {

    // 注册用的model
    private SignUpModel signUpModel;

    private MutableLiveData<String> userName = new MutableLiveData<>();
    private MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> secureProblem = new MutableLiveData<>();
    private MutableLiveData<String> secureAnswer = new MutableLiveData<>();

    private Activity activity;

    private String headshotPath;

    public SignUpViewModel(@NonNull Application application) {
        super(application);

        signUpModel = new SignUpModel(this);
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void selectHeadshot() {
        if (activity == null) {
            return;
        }
        PictureSelector
                .create(activity, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(true, 64, 64, 1, 1);
    }

    public void signUp() {
        if (userName.getValue() == null) {
            Toast.makeText(getApplication(), R.string.please_input_user_name, Toast.LENGTH_LONG).show();
            return;
        }
        if (password.getValue() == null) {
            Toast.makeText(getApplication(), R.string.please_input_password, Toast.LENGTH_LONG).show();
            return;
        }
        if (secureProblem.getValue() == null) {
            Toast.makeText(getApplication(), R.string.please_input_secure_problem, Toast.LENGTH_LONG).show();
            return;
        }
        if (secureAnswer.getValue() == null) {
            Toast.makeText(getApplication(), R.string.please_input_secure_answer, Toast.LENGTH_LONG).show();
            return;
        }

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                signUpModel.SignUp(userName.getValue(), password.getValue(), secureProblem.getValue(), secureAnswer.getValue(),  headshotPath);
            }
        }.start();
    }

    @Override
    public void signUpSuccess() {
        LoginAccountHelper.setLoginAccount(userName.getValue(), activity);
        Intent intent = new Intent(activity, ChatActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @Override
    public void signUpFailed(String s) {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(() -> {
            Toast.makeText(getApplication(), s, Toast.LENGTH_LONG).show();
        });
    }

    public void setHeadshotPath(String path) {
        this.headshotPath = path;
    }

    @Override
    public void uploadingHeadshot() {

    }

    @Override
    public void uploadHeadshotSuccess() {

    }

    @Override
    public void uploadHeadshotFailed() {

    }

    public MutableLiveData<String> getUserName() {
        return userName;
    }

    public MutableLiveData<String> getPassword() {
        return password;
    }

    public MutableLiveData<String> getSecureProblem() {
        return secureProblem;
    }

    public MutableLiveData<String> getSecureAnswer() {
        return secureAnswer;
    }
}
