package com.mywings.justolm.Model;

import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/25/2016.
 */
public class Order {

    private String id;
    private String typeId;
    private String orderStatusId;
    private String orderStatusName;
    private String orderTime;
    private String createdAt;
    private String userId;
    private String areaId;
    private List<Items> items;
    private boolean actionDelete;
    private boolean confirmDeleted;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public boolean isActionDelete() {
        return actionDelete;
    }

    public void setActionDelete(boolean actionDelete) {
        this.actionDelete = actionDelete;
    }

    public boolean isConfirmDeleted() {
        return confirmDeleted;
    }

    public void setConfirmDeleted(boolean confirmDeleted) {
        this.confirmDeleted = confirmDeleted;
    }

}
