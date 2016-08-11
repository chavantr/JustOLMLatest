package com.mywings.justolm.Process;

import com.mywings.justolm.Model.Area;

import java.util.List;

/**
 * Created by Tatyabhau on 6/26/2016.
 */
public interface OnAreaListener {
     void onAreaComplete(List<Area> result,Exception exception);
}
