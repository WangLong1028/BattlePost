package com.shuo.ba.activitys;

import android.os.Bundle;
import android.os.Looper;
import android.transition.Slide;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.shuo.ba.R;
import com.shuo.ba.databinding.ActivityChatBinding;
import com.shuo.ba.viewmodels.ChatViewModel;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private ChatViewModel chatViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        getWindow().setEnterTransition(new Slide());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        chatViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(ChatViewModel.class);

        binding.setChat(chatViewModel);
        binding.setLifecycleOwner(this);

        chatViewModel.setActivity(this);

        setViews();
    }

    private void setViews(){
        chatViewModel.setBottomSheet(binding.bottomSheet);
        chatViewModel.setBottomAppbar(binding.bottomAppbar);
        chatViewModel.setChatRecyclerView(binding.chatRecyclerView);
        chatViewModel.setBottomIvHeadshot(binding.bottomSheetHeader);

        binding.bottomItemUserInfo.setOnItemClickListener(chatViewModel::enterUserInfo);
        binding.bottomItemSignOut.setOnItemClickListener(chatViewModel::logout);
    }

    @Override
    public void onBackPressed() {
        if (!chatViewModel.onBackClick()){
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        //chatViewModel.close();
        new Thread(()->{
            Looper.prepare();
            Glide.get(this).clearDiskCache();
        }).start();
        super.onStop();
    }
}
