package com.mywings.justolm.Process;

import com.mywings.justolm.Model.UserMessage;

/**
 * Created by Tatyabhau on 8/9/2016.
 */
public interface OnUpdateOrderListener {
    void onUpdateComplete(UserMessage userMessage, Exception exception);
}
