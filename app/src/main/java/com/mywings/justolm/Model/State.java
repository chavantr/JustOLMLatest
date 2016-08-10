package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau on 6/25/2016.
 */
public class State {

    private static State ourInstance;
    private int id;
    private String name;
    private int countryId;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }


    public static synchronized State getInstance() {

        //if (null == ourInstance) {
            ourInstance = new State();
        //}
        return ourInstance;
    }

    private State() {
    }

}
