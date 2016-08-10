package com.mywings.justolm.Process;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.mywings.justolm.Model.InitOrderRequest;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.NetworkUtils.GatheredServerClientException;
import com.mywings.justolm.NetworkUtils.MissingBasicClientFunctionalityException;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tatyabhau on 7/6/2016.
 */
public class InitOrder extends AsyncTask<InitOrderRequest, Void, UserMessage> {

    private Exception exception;
    private ServiceFunctions serviceFunctions;
    private OnInitOrderListener onInitOrderListener;

    public InitOrder(ServiceFunctions serviceFunctions) {
        this.serviceFunctions = serviceFunctions;
    }

    public void setOnInitOrderListener(OnInitOrderListener onInitOrderListener, InitOrderRequest request) {
        this.onInitOrderListener = onInitOrderListener;
        this.doLoadInbackground(request);
    }

    private void doLoadInbackground(InitOrderRequest request) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, request);
    }

    @Override
    protected UserMessage doInBackground(InitOrderRequest... params) {
        String response = null;
        try {
            response = serviceFunctions.initOrder(params[0]);
        } catch (MissingBasicClientFunctionalityException e) {
            e.printStackTrace();
            exception = e;
        } catch (GatheredServerClientException e) {
            e.printStackTrace();
            exception = e;
        }
        return process(response);
    }

    /**
     * @param response
     * @return userMessage
     */
    @Nullable
    private UserMessage process(String response) {
        if (null != response || !response.isEmpty()) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                UserMessage userMessage = UserMessage.getInstance();
                userMessage.setMessage(jsonObject.getString(Constants.MESSAGE));
                userMessage.setStatus(jsonObject.getInt(Constants.STATUS));
                return userMessage;
            } catch (JSONException e) {
                exception = e;
                return null;
            }
        } else {
            exception = new Exception("Internal server error.");
            return null;
        }
    }

    @Override
    protected void onPostExecute(UserMessage userMessage) {
        super.onPostExecute(userMessage);
        onInitOrderListener.onInitOrderComplete(userMessage, exception);
    }
}
