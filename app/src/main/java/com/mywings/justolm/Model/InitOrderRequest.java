package com.mywings.justolm.Model;

import java.util.List;

/**
 * Created by Tatyabhau on 7/6/2016.
 */
public class InitOrderRequest {

    private static InitOrderRequest ourInstance;
    private String orderTime;
    private String orderDate;
    private String orderType;
    private String userId;
    private List<Items> items;

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }




    private InitOrderRequest() {
    }

    public static synchronized InitOrderRequest getInstance() {

        if (null == ourInstance) {
            ourInstance = new InitOrderRequest();
        }

        return ourInstance;
    }
}
