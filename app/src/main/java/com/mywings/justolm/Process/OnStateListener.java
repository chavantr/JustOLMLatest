package com.mywings.justolm.Process;

import com.mywings.justolm.Model.State;

import java.util.List;

/**
 * Created by Tatyabhau on 6/26/2016.
 */
public interface OnStateListener {
    void onStateComplete(List<State> result, Exception exception);
}
