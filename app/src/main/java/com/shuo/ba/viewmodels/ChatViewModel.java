package com.shuo.ba.viewmodels;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.shuo.ba.R;
import com.shuo.ba.activitys.MainActivity;
import com.shuo.ba.activitys.UserInfoActivity;
import com.shuo.ba.adapter.ChatListAdapter;
import com.shuo.ba.beans.MyChatBean;
import com.shuo.ba.fragment.EditMsgDialogFragmentHelper;
import com.shuo.ba.helper.HeadshotHelper;
import com.shuo.ba.helper.LoginAccountHelper;
import com.shuo.ba.helper.TimeOutNotifier;
import com.shuo.chatmodule.beans.ChatBean;
import com.shuo.chatmodule.beans.UserBean;
import com.shuo.chatmodule.chat.ChatModel;
import com.shuo.chatmodule.chat.RequestUserModel;
import com.shuo.chatmodule.constant.NetworkConstant;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends AndroidViewModel implements ChatModel.ChattingRecordGetter, RequestUserModel.RequestUserManager, DialogMsgViewModel.GetSendData {

    private UserBean userBean;

    private RequestUserModel requestUserModel;
    private ChatModel chatModel;

    private List<MyChatBean> datas;
    private ChatListAdapter chatListAdapter;

    private AppCompatActivity activity;

    private BottomSheetBehavior<View> bottomSheetBehavior;
    private RecyclerView recyclerView;
    private BottomAppBar bottomAppBar;
    private AppCompatImageView bottomIvHeadshot;

    // 超时检测器
    private TimeOutNotifier timeOutNotifier;

    // 加载对话框
    private AlertDialog progressDialog;

    // 用户名
    private String userName;

    // 是否连接成功
    private boolean isConnected = false;

    public ChatViewModel(@NonNull Application application) {
        super(application);

        datas = new ArrayList<>();
        userName = LoginAccountHelper.getCurUserName(getApplication());
    }


    // 设置activity后开始初始化
    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
        initNetworkModel();
    }

    private void initNetworkModel() {
        // 初始化超时检测线程
        timeOutNotifier = new TimeOutNotifier(5000, this::timeOut);
        // 初始化申请用户模型
        requestUserModel = new RequestUserModel(this);
        // 获取用户
        new Thread(() -> {
            Looper.prepare();
            timeOutNotifier.startNewTask("init");
            requestUserModel.loadUser(userName);
        }).start();
    }

    public void setBottomSheet(View bottomSheet) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void setBottomAppbar(BottomAppBar bottomAppbar) {
        this.bottomAppBar = bottomAppbar;
        bottomAppbar.setNavigationIcon(R.mipmap.header_1);
        bottomAppbar.setNavigationOnClickListener(this::onClickNavigationIcon);

        MenuItem refreshItem = bottomAppbar.getMenu().getItem(0);
        refreshItem.setOnMenuItemClickListener(this::refresh);

        MenuItem toTopItem = bottomAppbar.getMenu().getItem(1);
        toTopItem.setOnMenuItemClickListener((MenuItem item) -> {
            if (recyclerView == null || datas.size() == 0) {
                return true;
            }
            recyclerView.smoothScrollToPosition(0);
            return true;
        });
    }

    public void setBottomIvHeadshot(AppCompatImageView bottomIvHeadshot) {
        this.bottomIvHeadshot = bottomIvHeadshot;
    }

    public void setChatRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setLayoutManager(linearLayoutManager);
        chatListAdapter = new ChatListAdapter(datas, activity);
        recyclerView.setAdapter(chatListAdapter);
        DefaultItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // 滑到底部时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    loadHistoryMsg();
                }
            }
        });
    }

    private void loadHistoryMsg() {
        if (!isConnected) {
            toastConnectToServer();
            return;
        }
        if (chatModel == null) {
            chatModel = new ChatModel(this);
        }
        new Thread(() -> {
            Looper.prepare();
            int id = datas.size() == 0 ? -1 : datas.get(datas.size() - 1).getId();
            chatModel.getHostoryChat(id);
        }).start();
    }


    private boolean refresh(MenuItem menuItem) {
        if (isConnected) {
            return false;
        }
        // 重新初始化网络
        initNetworkModel();
        return false;
    }

    private void onClickNavigationIcon(View view) {
        if (bottomSheetBehavior == null) {
            return;
        }
        if (!isConnected) {
            toastConnectToServer();
            return;
        }
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
    }

    public boolean onBackClick() {
        // true 时退出
        if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            return false;
        }
        return true;
    }

    public void enterUserInfo() {
        Log.v("tag", "enter");
//        activity.runOnUiThread(() -> {
//            Toast.makeText(activity, "开发者还在努力开发中......", Toast.LENGTH_LONG).show();
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//        });
        Intent intent = new Intent(activity, UserInfoActivity.class);
        intent.putExtra("user", LoginAccountHelper.userToString(userBean));
        activity.startActivity(intent);
        activity.finish();
    }

    public void logout() {
        LoginAccountHelper.logOutAccount(getApplication());
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    public void sendMsg() {
        if (!isConnected) {
            toastConnectToServer();
            return;
        }
        EditMsgDialogFragmentHelper.EditMsgDialogFragment dialogFragment = EditMsgDialogFragmentHelper.getDialog(this, userBean);
        dialogFragment.show(activity.getSupportFragmentManager(), "show ");
    }

    @Override
    public void loadingUser() {
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(() -> {
            // 初始化连接服务器的对话框
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            View view = LayoutInflater.from(activity).inflate(R.layout.dialog_progress, null);
            builder.setView(view);
            builder.setCancelable(false);
            progressDialog = builder.create();
            progressDialog.show();
        });
    }

    @Override
    public void loadUserSuccess(UserBean userBean) {
        Log.v("tag", "获取用户成功! 当前用户名为" + userBean.getUserName());
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(() -> {
            Glide.with(activity).load(HeadshotHelper.getHeadshotUrl(userBean)).addListener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    bottomAppBar.setNavigationIcon(R.mipmap.header_1);
                    bottomIvHeadshot.setImageResource(R.mipmap.header_1);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    bottomAppBar.setNavigationIcon(resource);
                    bottomIvHeadshot.setImageDrawable(resource);
                    return false;
                }
            }).preload();
        });

        this.userBean = userBean;
        // 用户加载成功后再加载聊天
        chatModel = new ChatModel(ChatViewModel.this);

        new Thread(() -> {
            Looper.prepare();
            try {
                Thread.sleep(300);
                // 获取最新消息
                int id = datas.size() == 0 ? -1 : datas.get(datas.size() - 1).getId();
                chatModel.getHostoryChat(id);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void loadUserFailed(String s) {
        Log.e("tag", s);
        Log.v("tag", "连接失败");
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, "连接失败", Toast.LENGTH_LONG).show();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            isConnected = false;
        });
    }

    private void timeOut(String tag) {
        Log.v("tag", "timeOut");
        if (progressDialog != null) {
            activity.runOnUiThread(() -> {
                Toast.makeText(getApplication(), "连接失败, 可点击刷新按钮刷新", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            });
        }
    }

    private void toastConnectToServer() {
        Toast.makeText(getApplication(), "请先连接到服务器", Toast.LENGTH_LONG).show();
    }

    @Override
    public void connectingToServer() {
        Log.v("tag", "正在连接到服务器...");
    }

    @Override
    public void connectingToServerSuccess() {
        Log.v("tag", "连接成功!");

        if (activity == null) {
            return;
        }

        isConnected = true;
        timeOutNotifier.setSuccess();

        activity.runOnUiThread(() -> {
            Toast.makeText(getApplication(), "连接成功!", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        });
    }

    @Override
    public void getHistoryMsg(List<ChatBean> list) {
        activity.runOnUiThread(() -> {
            if (list == null || list.size() == 0) {
                chatListAdapter.setHasMore(false);
                return;
            }
            int i = list.size() - 1;
            while (i >= 0) {
                MyChatBean myChatBean = new MyChatBean(list.get(i));
                datas.add(myChatBean);
                i--;
            }
            chatListAdapter.notifyDataSetChanged();
        });
    }


    @Override
    public void getNewMsg(ChatBean chatBean) {
        activity.runOnUiThread(() -> {
            MyChatBean myChatBean = new MyChatBean(chatBean);
            datas.add(0, myChatBean);
            chatListAdapter.notifyItemInserted(1);
        });
    }

    @Override
    public void getSendData(ChatBean chatBean) {
        if (chatBean == null) {
            Toast.makeText(getApplication(), "发送失败", Toast.LENGTH_LONG).show();
            return;
        }
        Log.v("tag", chatBean.getContentText());
        new Thread(() -> {
            Looper.prepare();
            if (chatModel == null) {
                chatModel = new ChatModel(this);
                return;
            }
            chatBean.setBelongUser(userBean);
            chatModel.send(chatBean);
        }).start();
    }

    @Override
    public void receiveError(String s) {

    }

    @Override
    public void sendError(String s) {
        Log.e("tag", "Send Error : " + s);
    }

    @Override
    public void sendSuccess() {
        Log.v("tag", "send success");
    }

    public void close() {
        new Thread(() -> {
            if (chatModel != null) {
                chatModel.close();
            }
            isConnected = false;
        }).start();
    }

    public String getUserName() {
        return userName;
    }
}
