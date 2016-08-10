package com.mywings.justolm.Process;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

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
public class GetCountries extends AsyncTask<Context, Void, List<Country>> {


    //region
    private ServiceFunctions serviceFunctions;
    private Exception exception = null;
    private OnCountryListener onCountryListener;
    //endregion


    public GetCountries(ServiceFunctions serviceFunctions) {
        this.serviceFunctions = serviceFunctions;
    }

    @Override
    protected List<Country> doInBackground(Context... params) {

        String response = null;


        try {
            response = serviceFunctions.getCountries();

        } catch (MissingBasicClientFunctionalityException e) {
            exception = e;
        } catch (GatheredServerClientException e) {
            exception = e;
        }
        return process(response);
    }

    @Override
    protected void onPostExecute(List<Country> countries) {
        super.onPostExecute(countries);
        onCountryListener.onCountryComplete(countries, exception);
    }

    public void setOnCountryListener(OnCountryListener onCountryListener, Context context) {
        this.onCountryListener = onCountryListener;
        this.doLoadInbackground(context);
    }

    private void doLoadInbackground(Context context) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
    }

    @Nullable
    private List<Country> process(String response) {
        List<Country> countries = new ArrayList<>();
        if (null != response) {
            JSONObject jcountries;
            try {
                jcountries = new JSONObject(response);
                JSONArray jCountry = jcountries.getJSONArray("data");
                if (null != jCountry) {
                    for (int i = 0; i < jCountry.length(); i++) {
                        Country country = new Country();
                        country.setId(Integer.parseInt(jCountry.getJSONObject(i).getString(Constants.COUNTRY_ID)));
                        country.setName(jCountry.getJSONObject(i).getString(Constants.COUNTRY_NAME));
                        countries.add(country);
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
        return countries;
    }

}
