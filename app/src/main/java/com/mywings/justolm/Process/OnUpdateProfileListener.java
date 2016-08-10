package com.mywings.justolm.Process;

import com.mywings.justolm.Model.UserMessage;

/**
 * Created by Tatyabhau on 7/2/2016.
 */
public interface OnUpdateProfileListener {

    void onProfileUpdateComplete(UserMessage userMessage, Exception exception);

}
