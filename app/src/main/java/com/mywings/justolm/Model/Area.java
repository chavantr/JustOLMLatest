package com.mywings.justolm.Model;

/**
 * Created by Tatyabhau on 6/26/2016.
 */
public class Area {

    private static Area ourInstance;
    private int id;
    private String name;
    private int cityId;

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

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }


    public static synchronized Area getInstance() {
  //      if (null == ourInstance) {
            ourInstance = new Area();
    //    }
        return ourInstance;
    }

    private Area() {
    }
}
