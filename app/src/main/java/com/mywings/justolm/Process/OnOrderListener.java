package com.mywings.justolm.Process;

import com.mywings.justolm.Model.Order;

import java.util.List;

/**
 * Created by Tatyabhau on 7/5/2016.
 */
public interface OnOrderListener {
    void onOrderComplete(List<Order> result, boolean isadmin, Exception exception);
}
