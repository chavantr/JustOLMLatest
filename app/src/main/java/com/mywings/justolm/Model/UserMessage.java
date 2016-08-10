package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public class UserMessage {


    //region Fields
    private int status;
    private String message;
    //endregion
    private static UserMessage ourInstance;

    public static synchronized UserMessage getInstance() {
        if (null == ourInstance) {
            ourInstance = new UserMessage();
        }
        return ourInstance;
    }

    private UserMessage() {

    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
