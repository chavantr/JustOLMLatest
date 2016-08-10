package com.mywings.justolm.Process;

import com.mywings.justolm.Model.Country;

import java.util.List;

/**
 * Created by Tatyabhau on 6/25/2016.
 */
public interface OnCountryListener {
    void onCountryComplete(List<Country> result,Exception exception);
}
