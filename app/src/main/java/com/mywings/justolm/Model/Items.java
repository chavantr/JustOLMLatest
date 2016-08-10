package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau on 7/6/2016.
 */
public class Items {

    private String id;
    private String itemName;
    private String itemPhoto;
    private String quantity;
    private String period;
    private String schedular;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemNAme) {
        this.itemName = itemNAme;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public void setItemPhoto(String itemPhoto) {
        this.itemPhoto = itemPhoto;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSchedular() {
        return schedular;
    }

    public void setSchedular(String schedular) {
        this.schedular = schedular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
