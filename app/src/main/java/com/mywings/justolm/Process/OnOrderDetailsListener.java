package com.mywings.justolm.Process;

import com.mywings.justolm.Model.OrderDetails;

/**
 * Created by Tatyabhau on 7/5/2016.
 */
public interface OnOrderDetailsListener {
    void onOrderDetailsComplete(OrderDetails result, Exception exception);
}
