package com.shuo.ba.viewmodels;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.shuo.ba.R;
import com.shuo.chatmodule.beans.ChatBean;
import com.shuo.chatmodule.beans.UserBean;
import com.shuo.chatmodule.constant.NetworkConstant;

public class DialogMsgViewModel extends AndroidViewModel {

    private GetSendData getSendData;

    private DialogFragment dialog;

    private MutableLiveData<String> editContentText = new MutableLiveData<>();


    public DialogMsgViewModel(@NonNull Application application) {
        super(application);
    }

    public void setDialog(DialogFragment dialog) {
        this.dialog = dialog;
    }

    public void setGetSendData(GetSendData getSendData) {
        this.getSendData = getSendData;
    }


    public void setIvHeadshot(AppCompatImageView ivHeadshot, UserBean userBean) {
        Glide.with(getApplication()).load("http://" + NetworkConstant.SERVER_IP + ":" + NetworkConstant.SERVER_PORT + "/headshot/" + userBean.getId()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                ivHeadshot.setImageResource(R.mipmap.header_1);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                ivHeadshot.setImageDrawable(resource);
                return false;
            }
        }).preload();
    }

    public void send() {
        if (editContentText.getValue() == null) {
            Toast.makeText(getApplication(), R.string.please_input_msg, Toast.LENGTH_LONG).show();
            return;
        }
        if (getSendData != null) {
            ChatBean chatBean = new ChatBean();
            chatBean.setContentText(editContentText.getValue());
            getSendData.getSendData(chatBean);
        }
        dialog.dismiss();
    }

    public void exit(){
        dialog.dismiss();
    }

    public void clearData(){
        if (editContentText != null) {
            editContentText.setValue(null);
        }
    }

    public MutableLiveData<String> getEditContentText() {
        return editContentText;
    }

    public interface GetSendData {
        void getSendData(ChatBean chatBean);
    }
}
