package com.mywings.justolm.Process;

import com.mywings.justolm.Model.UserMessage;

/**
 * Created by Tatyabhau on 7/7/2016.
 */
public interface OnDeleteListener {
    void onDeleteComplete(UserMessage userMessage, Exception exception);
}
