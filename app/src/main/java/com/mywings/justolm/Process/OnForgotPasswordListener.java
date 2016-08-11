package com.mywings.justolm.Process;

import com.mywings.justolm.Model.UserMessage;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public interface OnForgotPasswordListener {
    void onForgotPasswordSent(UserMessage result, Exception exception);
}
