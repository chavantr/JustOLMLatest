package com.mywings.justolm.Utilities;

/**
 * Created by Tatyabhau on 7/6/2016.
 */
public enum ItemStatus {


    PendingOrder(1), RejectedOrder(2), AcceptedOrder(3), DeliveredOrder(4), DeletedOrder(5);

    private int id;

    private ItemStatus(int id) {
        this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
