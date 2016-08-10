package com.mywings.justolm.UserInteraction;

import android.view.View;

/**
 * Created by Tatyabhau Chavan on 5/17/2016.
 */
public interface OnNotificationListener {
    void show(String message);
    void show(String message, View id);
    boolean isConnected();
}
