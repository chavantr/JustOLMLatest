package com.mywings.justolm.Process;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mywings.justolm.Model.LoginResponse;
import com.mywings.justolm.Model.Registration;
import com.mywings.justolm.Model.UserInfo;
import com.mywings.justolm.NetworkUtils.GatheredServerClientException;
import com.mywings.justolm.NetworkUtils.MissingBasicClientFunctionalityException;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public class RegisterUser extends AsyncTask<Registration, Void, LoginResponse> {


    //region Variables
    private Context context;
    private ServiceFunctions serviceFunctions;

    private Exception exception = null;

    //endregion
    private OnUpdateListener onUpdateListener;

    public RegisterUser(Context context, ServiceFunctions serviceFunctions) {
        this.context = context;
        this.serviceFunctions = serviceFunctions;
    }

    public void setOnUpdateListener(OnUpdateListener onUpdateListener, Registration registration) {
        this.onUpdateListener = onUpdateListener;
        this.doLoadInbackground(registration);
    }

    public void doLoadInbackground(Registration registration) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, registration);
    }

    @Override
    protected LoginResponse doInBackground(Registration... params) {

        String response = null;

        try {
            response = serviceFunctions.registration(params[0]);
        } catch (MissingBasicClientFunctionalityException e) {
            exception = e;
        } catch (GatheredServerClientException e) {
            exception = e;
        }

        return process(response, params[0]);
    }

    @Override
    protected void onPostExecute(LoginResponse userMessage) {
        super.onPostExecute(userMessage);
        onUpdateListener.onUpdateComplete(userMessage, exception);
    }

    /**
     * @param response
     * @return userMessage
     */
    @Nullable
    private LoginResponse process(String response, Registration user) {
        if (null != response) {
            Log.d("Registration", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                LoginResponse loginResponse = LoginResponse.getInstance();
                loginResponse.setMessage(jsonObject.getString(Constants.MESSAGE));
                loginResponse.setStatus(jsonObject.getInt(Constants.STATUS));
                JSONObject userInfo = jsonObject.getJSONObject(Constants.DATA);
                if (userInfo.has(Constants.USER_ID)) {
                    UserInfo info = UserInfo.getInstance();
                    info.setId(userInfo.getInt(Constants.USER_ID));
                    info.setFirstName(userInfo.getString(Constants.FIRST_NAME));
                    info.setMiddleName(userInfo.getString(Constants.MIDDLE_NAME));
                    info.setLastName(userInfo.getString(Constants.LAST_NAME));
                    info.setDob(userInfo.getString(Constants.DOB));
                    info.setGender(userInfo.getString(Constants.GENDER));
                    info.setProfession(userInfo.getString(Constants.PROFESSION));
                    info.setAddress(userInfo.getString(Constants.ADDRESS));
                    info.setCountryCode(String.valueOf(userInfo.getString(Constants.COUNTRY_ID)));
                    info.setState(String.valueOf(userInfo.getString(Constants.STATE_ID)));
                    info.setCity(String.valueOf(userInfo.getString(Constants.CITY_ID)));
                    info.setArea(String.valueOf(userInfo.getString(Constants.AREA_ID)));
                    info.setZip(userInfo.getString(Constants.ZIP));
                    info.setEmail(userInfo.getString(Constants.EMAIL));
                    info.setMobile(userInfo.getString(Constants.MOBILE));
                    if (userInfo.getString(Constants.IS_ADMIN).equalsIgnoreCase("0")) {
                        info.setAdmin(false);
                    } else {
                        info.setAdmin(true);
                    }
                    loginResponse.setUserInfo(info);
                }
                return loginResponse;
            } catch (JSONException e) {
                exception = e;
                return null;
            }
        } else {
            exception = new Exception("Internal server error.");
            return null;
        }
    }


    private void writefile(Registration user) {
        try {
            File file = new File("mnt/sdcard/user.ser");
            FileOutputStream fos = context.openFileOutput(file.getAbsolutePath(), Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(user);
            os.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
