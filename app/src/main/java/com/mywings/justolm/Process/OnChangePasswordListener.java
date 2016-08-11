package com.mywings.justolm.Process;

import com.mywings.justolm.Model.UserMessage;

/**
 * Created by Tatyabhau on 5/29/2016.
 */
public interface OnChangePasswordListener {
     void onChangePasswordComplete(UserMessage result, Exception exception);
}
