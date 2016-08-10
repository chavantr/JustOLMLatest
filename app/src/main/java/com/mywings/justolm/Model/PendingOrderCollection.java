package com.mywings.justolm.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/26/2016.
 */
public class PendingOrderCollection {
    public static List<PendingOrder> PENDINGORDERS;

    public static List<PendingOrder> getPENDINGORDERS() {
        return PENDINGORDERS;
    }

    static {
        PENDINGORDERS = new ArrayList<PendingOrder>();
        PENDINGORDERS.add(new PendingOrder("", "1", "Zandu Balm", "5", "Weekly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "Vicks", "5", "Weekly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "D Cold Total", "5", "Monthly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "Himalya Pain Relief", "7", "Weekly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "Disprin", "15", "Weekly"));

        PENDINGORDERS.add(new PendingOrder("", "1", "Zandu Balm Plus", "5", "Weekly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "Vicks Plus", "5", "Weekly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "D Cold Total Plus", "5", "Monthly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "Himalya Pain Relief Plus", "7", "Weekly"));
        PENDINGORDERS.add(new PendingOrder("", "1", "Disprin Plus", "15", "Weekly"));
    }
}
