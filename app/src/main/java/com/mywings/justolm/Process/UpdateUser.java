package com.mywings.justolm.Process;

import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.mywings.justolm.Model.Registration;
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
public class UpdateUser extends AsyncTask<Registration, Void, UserMessage> {

    //region Variables
    private ServiceFunctions serviceFunctions;
    private Exception exception = null;
    //endregion
    private OnUpdateProfileListener onUpdateProfileListener;

    public UpdateUser(ServiceFunctions serviceFunctions) {
        this.serviceFunctions = serviceFunctions;
    }

    public void setOnUpdateProfileListener(OnUpdateProfileListener onUpdateProfileListener, Registration registration) {
        this.onUpdateProfileListener = onUpdateProfileListener;
        this.doLoadInbackground(registration);
    }

    public void doLoadInbackground(Registration registration) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, registration);
    }

    @Override
    protected UserMessage doInBackground(Registration... params) {
        String response = null;
        try {
            response = serviceFunctions.updateProfile(params[0]);
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
        onUpdateProfileListener.onProfileUpdateComplete(userMessage, exception);
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
}
