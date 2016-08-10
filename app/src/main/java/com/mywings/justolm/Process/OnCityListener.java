package com.mywings.justolm.Process;

import com.mywings.justolm.Model.City;

import java.util.List;

/**
 * Created by Tatyabhau on 6/26/2016.
 */
public interface OnCityListener {

    public void onCityComplete(List<City> result,Exception exception);



}
