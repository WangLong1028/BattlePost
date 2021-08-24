package com.shuo.ba.activitys;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shuo.ba.helper.LoginAccountHelper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectStartActivity();
    }

    private void selectStartActivity() {
        String userName = LoginAccountHelper.getCurUserName(this);
        if (userName != null) {
            // 有已经登录的账号
            Intent intent = new Intent(SplashActivity.this, ChatActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        // 没有已登录的账号
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
