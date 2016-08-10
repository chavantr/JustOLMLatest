package com.mywings.justolm.Process;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mywings.justolm.Model.City;
import com.mywings.justolm.Model.Country;
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
public class GetCity extends AsyncTask<Context, Void, List<City>> {


    //region
    private ServiceFunctions serviceFunctions;
    private int stateId;
    private Exception exception = null;
    private OnCityListener onCityListener;
    //endregion


    public GetCity(ServiceFunctions serviceFunctions, int stateId) {
        this.serviceFunctions = serviceFunctions;
        this.stateId = stateId;
    }

    @Override
    protected List<City> doInBackground(Context... params) {
        String response = null;

        try {
            response = serviceFunctions.getCities("" + stateId);

        } catch (MissingBasicClientFunctionalityException e) {
            exception = e;
        } catch (GatheredServerClientException e) {
            exception = e;
        }
        return process(response);
    }


    public void setOnCityListener(OnCityListener onCityListener, Context context) {
        this.onCityListener = onCityListener;
        this.doLoadInbackground(context);
    }


    private void doLoadInbackground(Context context) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
    }



    @Nullable
    private List<City> process(String response) {
        List<City> cities = new ArrayList<City>();
        if (null != response) {
            JSONObject jcountries;
            try {
                jcountries = new JSONObject(response);
                JSONArray jCountry = jcountries.getJSONArray(Constants.DATA);
                if (null != jCountry) {
                    for (int i = 0; i < jCountry.length(); i++) {
                        City city = City.getInstance();
                        city.setId(Integer.parseInt(jCountry.getJSONObject(i).getString(Constants.CITY_ID)));
                        city.setName(jCountry.getJSONObject(i).getString(Constants.CITY_NAME));
                        city.setStateId(stateId);
                        cities.add(city);
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
        return cities;
    }

    @Override
    protected void onPostExecute(List<City> cities) {
        super.onPostExecute(cities);
        onCityListener.onCityComplete(cities, exception);
    }
}
