package com.shuo.ba.beans;

import com.shuo.chatmodule.beans.ChatBean;
import com.shuo.chatmodule.beans.UserBean;

public class MyChatBean extends ChatBean {
    private String userName;
    private int headshot;

    public MyChatBean(ChatBean chatBean) {
        this(chatBean.getId(), chatBean.getContentText(), chatBean.getBelongUser());
    }

    private MyChatBean(int id, String contentText, UserBean belongUser) {
        super(contentText, belongUser);
        this.userName = belongUser.getUserName();
        this.headshot = belongUser.getUserHeadshot();
        setId(id);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getHeadshot() {
        return headshot;
    }

    public void setHeadshot(int headshot) {
        this.headshot = headshot;
    }
}
