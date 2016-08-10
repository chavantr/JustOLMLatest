package com.mywings.justolm.Process;

import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

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
public class ChangePassword extends AsyncTask<Void, Void, UserMessage> {


    //region Variables
    private ServiceFunctions serviceFunctions;
    private Exception exception = null;
    private String userId;
    private String oldPassword;
    private String newPassword;
    //endregion
    private OnChangePasswordListener onChangePasswordListener;

    public ChangePassword(ServiceFunctions serviceFunctions, String userId, String oldPassword, String newPassword) {
        this.serviceFunctions = serviceFunctions;
        this.userId = userId;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public void setOnUpdateListener(OnChangePasswordListener onChangePasswordListener) {
        this.onChangePasswordListener = onChangePasswordListener;
        this.doLoadInbackground();
    }

    public void doLoadInbackground() {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected UserMessage doInBackground(Void... params) {

        String response = null;

        try {
            response = serviceFunctions.changePassword(this.userId, this.oldPassword, this.newPassword);
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
        onChangePasswordListener.onChangePasswordComplete(userMessage, exception);
    }

    /**
     * @param response
     * @return userMessage
     */
    @Nullable
    private UserMessage process(String response) {
        if (null != response || !response.isEmpty()) {
            Log.d("test", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                UserMessage userMessage = UserMessage.getInstance();
                if (jsonObject.getInt(Constants.STATUS) == 200) {
                    userMessage.setMessage(jsonObject.getString(Constants.MESSAGE));
                    userMessage.setStatus(jsonObject.getInt(Constants.STATUS));
                } else {
                    userMessage.setMessage(jsonObject.getString(Constants.MESSAGE));
                    userMessage.setStatus(jsonObject.getInt(Constants.STATUS));
                    exception = new Exception(jsonObject.getString(Constants.MESSAGE));
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
