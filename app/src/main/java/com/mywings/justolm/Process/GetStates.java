package com.mywings.justolm.Process;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mywings.justolm.Model.State;
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
public class GetStates extends AsyncTask<Context, Void, List<State>> {


    //region
    private ServiceFunctions serviceFunctions;
    private int stateId;
    private Exception exception = null;
    private OnStateListener onStateListener;
    //endregion

    public GetStates(ServiceFunctions serviceFunctions, int stateId) {
        this.serviceFunctions = serviceFunctions;
        this.stateId = stateId;
    }

    @Override
    protected List<State> doInBackground(Context... params) {

        String response = null;
        try {
            response = serviceFunctions.getStates("" + stateId);
            Log.d("test", response);
        } catch (MissingBasicClientFunctionalityException e) {
            exception = e;
        } catch (GatheredServerClientException e) {
            exception = e;
        }
        return process(response);
    }


    @Nullable
    private List<State> process(String response) {
        List<State> states = new ArrayList<State>();
        if (null != response) {
            JSONObject jStates;
            try {
                jStates = new JSONObject(response);
                JSONArray jState = jStates.getJSONArray("data");
                if (null != jState) {
                    for (int i = 0; i < jState.length(); i++) {
                        State state = State.getInstance();
                        state.setId(Integer.parseInt(jState.getJSONObject(i).getString(Constants.STATE_ID)));
                        state.setName(jState.getJSONObject(i).getString(Constants.STATE_NAME));
                        state.setCountryId(stateId);
                        states.add(state);
                    }
                } else {
                    exception = new Exception(jStates.getString(Constants.MESSAGE));
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
        return states;
    }


    @Override
    protected void onPostExecute(List<State> states) {
        super.onPostExecute(states);
        onStateListener.onStateComplete(states, exception);
    }

    public void setOnStateListener(OnStateListener onStateListener, Context context) {
        this.onStateListener = onStateListener;
        this.doLoadInbackground(context);
    }

    private void doLoadInbackground(Context context) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
    }
}
