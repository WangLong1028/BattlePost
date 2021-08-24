package com.shuo.chatmodule.beans;

public class UserBean {
    private int id;
    private String userName;
    private String password;
    private String secureProblem;
    private String secureAnswer;
    private int userHeadshot;

    public UserBean() {
    }

    public UserBean(int id, String userName, int userHeadshot) {
        this.id = id;
        this.userName = userName;
        this.userHeadshot = userHeadshot;
    }

    public UserBean(int id, String userName, String password, String secureProblem, String secureAnswer, int userHeadshot) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.secureProblem = secureProblem;
        this.secureAnswer = secureAnswer;
        this.userHeadshot = userHeadshot;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserHeadshot() {
        return userHeadshot;
    }

    public void setUserHeadshot(int userHeadshot) {
        this.userHeadshot = userHeadshot;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecureProblem() {
        return secureProblem;
    }

    public void setSecureProblem(String secureProblem) {
        this.secureProblem = secureProblem;
    }

    public String getSecureAnswer() {
        return secureAnswer;
    }

    public void setSecureAnswer(String secureAnswer) {
        this.secureAnswer = secureAnswer;
    }
}
