package com.mywings.justolm.Process;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mywings.justolm.Model.Area;
import com.mywings.justolm.NetworkUtils.GatheredServerClientException;
import com.mywings.justolm.NetworkUtils.MissingBasicClientFunctionalityException;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau on 6/26/2016.
 */
public class GetAreas extends AsyncTask<Context, Void, List<Area>> {

    //region
    private ServiceFunctions serviceFunctions;
    private int cityId;
    private Exception exception = null;
    private OnAreaListener onAreaListener;

    public GetAreas(ServiceFunctions serviceFunctions, int cityId) {
        this.serviceFunctions = serviceFunctions;
        this.cityId = cityId;
    }

    //endregion

    public void setOnAreaListener(OnAreaListener onAreaListener, Context context) {
        this.onAreaListener = onAreaListener;
        this.doLoadInbackground(context);
    }

    private void doLoadInbackground(Context context) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
    }

    @Override
    protected List<Area> doInBackground(Context... params) {
        String response = null;
        try {
            response = serviceFunctions.getAreas("" + cityId);
            Log.d("test", response);
        } catch (MissingBasicClientFunctionalityException e) {
            exception = e;
        } catch (GatheredServerClientException e) {
            exception = e;
        }
        return process(response);
    }

    @Override
    protected void onPostExecute(List<Area> areas) {
        super.onPostExecute(areas);
        onAreaListener.onAreaComplete(areas, exception);
    }

    @Nullable
    private List<Area> process(String response) {
        List<Area> areas = new ArrayList<>();
        if (null != response) {
            JSONObject jcountries;
            try {
                jcountries = new JSONObject(response);
                if (jcountries.getInt("status") == 200) {
                    JSONArray jCountry = jcountries.getJSONArray(Constants.DATA);
                    if (null != jCountry) {
                        for (int i = 0; i < jCountry.length(); i++) {
                            Area area = Area.getInstance();
                            area.setId(Integer.parseInt(jCountry.getJSONObject(i).getString(Constants.AREA_ID)));
                            area.setName(jCountry.getJSONObject(i).getString(Constants.AREA_NAME));
                            area.setCityId(cityId);
                            areas.add(area);
                        }
                    } else {
                        exception = new Exception(jcountries.getString(Constants.MESSAGE));
                        return null;
                    }
                } else {
                    exception = new Exception(jcountries.getString(Constants.MESSAGE));
                    return null;
                }
            } catch (JSONException e) {
                exception = e;
                return null;
            }
        } else {
            exception = new Exception("Internal server error.");
            return null;
        }
        return areas;
    }
}
