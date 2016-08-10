package com.mywings.justolm.Process;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.NetworkUtils.GatheredServerClientException;
import com.mywings.justolm.NetworkUtils.MissingBasicClientFunctionalityException;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public class ForgotPassword extends AsyncTask<String, Void, UserMessage> {


    //region Variables
    private ServiceFunctions serviceFunctions;
    private Exception exception = null;
    //endregion


    private OnForgotPasswordListener onForgotPasswordListener;

    public ForgotPassword(ServiceFunctions serviceFunctions) {
        this.serviceFunctions = serviceFunctions;
    }

    public void setOnUpdateListener(OnForgotPasswordListener onForgotPasswordListener, String email) {
        this.onForgotPasswordListener = onForgotPasswordListener;
        this.doLoadInbackground(email);
    }

    public void doLoadInbackground(String email) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, email);
    }

    @Override
    protected UserMessage doInBackground(String... params) {

        String response = null;

        try {
            response = serviceFunctions.forgotPassword(params[0]);
        } catch (MissingBasicClientFunctionalityException e) {
            exception = e;
        } catch (GatheredServerClientException e) {
            exception = e;
        }

        return process(response);
    }

    @Override
    protected void onPostExecute(UserMessage userMessage) {
        super.onPostExecute(userMessage);
        onForgotPasswordListener.onForgotPasswordSent(userMessage, exception);
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
                if (jsonObject.getInt(Constants.STATUS) == 200) {
                    userMessage.setMessage(jsonObject.getString(Constants.MESSAGE));
                    userMessage.setStatus(jsonObject.getInt(Constants.STATUS));
                } else {
                    exception = new Exception(jsonObject.getString(Constants.MESSAGE));
                    userMessage.setMessage(jsonObject.getString(Constants.MESSAGE));
                    userMessage.setStatus(jsonObject.getInt(Constants.STATUS));
                }
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
}
