package com.mywings.justolm.Process;

import com.mywings.justolm.Model.UserMessage;

/**
 * Created by Tatyabhau on 7/5/2016.
 */
public interface OnInitOrderListener {
    void onInitOrderComplete(UserMessage result, Exception exception);
}
