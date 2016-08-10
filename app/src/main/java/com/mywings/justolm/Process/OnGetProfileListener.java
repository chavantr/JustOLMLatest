package com.mywings.justolm.Process;

import com.mywings.justolm.Model.LoginResponse;

/**
 * Created by Tatyabhau on 7/14/2016.
 */
public interface OnGetProfileListener {
    public void onGetProfileComplete(LoginResponse loginResponse, Exception exception);
}
