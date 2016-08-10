package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau Chavan on 5/26/2016.
 */
public class PendingOrder {


    //region Variables
    private String index;
    private String preName;
    private String qty;
    private String scheduler;
    private String period;
    //endregion
    
    public PendingOrder(String index, String period, String preName, String qty, String scheduler) {
        this.index = index;
        this.period = period;
        this.preName = preName;
        this.qty = qty;
        this.scheduler = scheduler;
    }


    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }


}
