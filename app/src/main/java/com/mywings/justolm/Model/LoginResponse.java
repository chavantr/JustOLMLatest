package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public class LoginResponse {

    //region
    private int status;
    private String message;
    private UserInfo userInfo;
    //endregion


    private static LoginResponse ourInstance;

    public static LoginResponse getInstance() {

        if (null == ourInstance) {
            ourInstance = new LoginResponse();
        }

        return ourInstance;
    }

    public LoginResponse() {

    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
