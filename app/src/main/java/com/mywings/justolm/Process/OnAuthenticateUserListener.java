package com.mywings.justolm.Process;

import com.mywings.justolm.Model.LoginResponse;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public interface OnAuthenticateUserListener {
    void onAuthenticateUserComplete(LoginResponse result, Exception exception);
}
