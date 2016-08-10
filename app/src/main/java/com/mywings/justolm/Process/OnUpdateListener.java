package com.mywings.justolm.Process;

import com.mywings.justolm.Model.LoginResponse;
import com.mywings.justolm.Model.UserMessage;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public interface OnUpdateListener {
    public void onUpdateComplete(LoginResponse result, Exception exception);
}
