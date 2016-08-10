package com.mywings.justolm.Process;

import android.content.Context;
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
 * Created by Tatyabhau on 7/7/2016.
 */
public class DeleteOrder extends AsyncTask<Context, Void, UserMessage> {


    private ServiceFunctions serviceFunctions;
    private String userId;
    private String orderId;
    private Exception exception;
    private OnDeleteListener onDeleteListener;

    public DeleteOrder(ServiceFunctions serviceFunctions, String userId, String orderId) {
        this.serviceFunctions = serviceFunctions;
        this.userId = userId;
        this.orderId = orderId;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener, Context context) {
        this.onDeleteListener = onDeleteListener;
        this.doLoadInbackground(context);
    }

    private void doLoadInbackground(Context context) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
    }

    @Override
    protected UserMessage doInBackground(Context... params) {

        String response = null;

        try {
            response = serviceFunctions.deleteOrder(userId, orderId);
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
        onDeleteListener.onDeleteComplete(userMessage, exception);
    }
}
