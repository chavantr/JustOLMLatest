package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau Chavan on 5/27/2016.
 */
public class OrderDetails {

    private String prescribeName;
    private String schedule;
    private String period;
    private String qty;
    private String scheduler;


    public OrderDetails(String period, String prescribeName, String qty, String schedule, String scheduler) {
        this.period = period;
        this.prescribeName = prescribeName;
        this.qty = qty;
        this.schedule = schedule;
        this.scheduler = scheduler;
    }


    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPrescribeName() {
        return prescribeName;
    }

    public void setPrescribeName(String prescribeName) {
        this.prescribeName = prescribeName;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }


}
