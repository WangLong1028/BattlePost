package com.shuo.ba.activitys;

import android.os.Bundle;
import android.transition.Slide;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.shuo.ba.R;
import com.shuo.ba.databinding.ActivitySignInBinding;
import com.shuo.ba.viewmodels.SignInViewModel;

public class SignInActivity extends AppCompatActivity {

    private ActivitySignInBinding activitySignInBinding;
    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide());

        activitySignInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        signInViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SignInViewModel.class);

        activitySignInBinding.setSignInVm(signInViewModel);
        activitySignInBinding.setLifecycleOwner(this);

        signInViewModel.setActivity(this);
    }


}
