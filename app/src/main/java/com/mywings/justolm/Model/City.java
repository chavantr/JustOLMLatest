package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau on 6/26/2016.
 */
public class City {

    private static City ourInstance;
    private int id;
    private String name;
    private int stateId;


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

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }


    public static synchronized City getInstance() {
       // if (null == ourInstance) {
            ourInstance = new City();
       // }
        return ourInstance;
    }

    private City() {
    }
}
