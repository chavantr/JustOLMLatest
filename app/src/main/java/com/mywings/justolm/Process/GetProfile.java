package com.mywings.justolm.Process;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.mywings.justolm.LoginActivity;
import com.mywings.justolm.Model.LoginResponse;
import com.mywings.justolm.Model.UserInfo;
import com.mywings.justolm.MyProfile;
import com.mywings.justolm.NetworkUtils.GatheredServerClientException;
import com.mywings.justolm.NetworkUtils.MissingBasicClientFunctionalityException;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.Utilities.Constants;
import com.mywings.justolm.Utilities.JustOLMShared;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public class GetProfile extends AsyncTask<Context, Void, LoginResponse> {


    //region Variables
    private OnGetProfileListener onGetProfileListener;
    private ServiceFunctions serviceFunctions;
    private String userId;
    private Exception exception = null;
    private MyProfile login;
    private JustOLMShared justOLM;
    //endregion

    public GetProfile(ServiceFunctions serviceFunctions, String userId) {
        this.serviceFunctions = serviceFunctions;
        this.userId = userId;
    }


    public void setOnGetProfileListener(OnGetProfileListener onGetProfileListener,Context context) {
        this.onGetProfileListener = onGetProfileListener;
        this.doLoadInbackground(context);
    }

    private void doLoadInbackground(Context context) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
    }


    @Override
    protected LoginResponse doInBackground(Context... params) {

        String response = null;

        login = (MyProfile) params[0];

        justOLM = login.justOLMShared;

        try {
            response = serviceFunctions.getProfile(userId);
            Log.d("test",response);
        } catch (MissingBasicClientFunctionalityException e) {
            exception = e;
        } catch (GatheredServerClientException e) {
            exception = e;
        }

        return process(response);
    }

    @Override
    protected void onPostExecute(LoginResponse loginResponse) {
        super.onPostExecute(loginResponse);
        onGetProfileListener.onGetProfileComplete(loginResponse, exception);
    }

    private LoginResponse process(String response) {
        LoginResponse loginResponse = LoginResponse.getInstance();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(response);
            loginResponse.setStatus(jsonObject.getInt(Constants.STATUS));
            loginResponse.setMessage(jsonObject.getString(Constants.MESSAGE));
            if (loginResponse.getStatus() == 200) {
                JSONObject userInfo = jsonObject.getJSONObject(Constants.DATA);
                UserInfo info = UserInfo.getInstance();
                info.setId(userInfo.getInt(Constants.USER_ID));
                info.setFirstName(userInfo.getString(Constants.FIRST_NAME));
                info.setMiddleName(userInfo.getString(Constants.MIDDLE_NAME));
                info.setLastName(userInfo.getString(Constants.LAST_NAME));
                info.setDob(userInfo.getString(Constants.DOB));
                info.setGender(userInfo.getString(Constants.GENDER));
                info.setProfession(userInfo.getString(Constants.PROFESSION));
                info.setAddress(userInfo.getString(Constants.ADDRESS));
                info.setCountryCode(userInfo.getString(Constants.COUNTRY_ID));
                info.setState(userInfo.getString(Constants.STATE_ID));
                info.setCity(userInfo.getString(Constants.CITY_ID));
                info.setArea(userInfo.getString(Constants.AREA_ID));
                info.setZip(userInfo.getString(Constants.ZIP));
                info.setEmail(userInfo.getString(Constants.EMAIL));
                info.setMobile(userInfo.getString(Constants.MOBILE));
                if (userInfo.getString(Constants.IS_ADMIN).equalsIgnoreCase("0")) {
                    info.setAdmin(false);
                } else {
                    info.setAdmin(true);
                }
                loginResponse.setUserInfo(info);
                setCookies(info);
            } else {
                loginResponse.setUserInfo(null);
                exception = new Exception(loginResponse.getMessage());
            }
        } catch (JSONException e) {
            exception = e;
        }
        return loginResponse;
    }

    private void setCookies(UserInfo userInfo) {

        justOLM.setStringValue("firstname", userInfo.getFirstName());
        justOLM.setStringValue("middlename", userInfo.getMiddleName());
        justOLM.setStringValue("lastname", userInfo.getLastName());
        justOLM.setStringValue("dob", userInfo.getDob());
        justOLM.setStringValue("gender", userInfo.getGender());
        justOLM.setStringValue("profession", userInfo.getProfession());
        justOLM.setStringValue("address", userInfo.getAddress());
        justOLM.setStringValue("country", userInfo.getCountryCode());
        justOLM.setStringValue("state", userInfo.getState());
        justOLM.setStringValue("city", userInfo.getCity());
        justOLM.setStringValue("area", userInfo.getArea());
        justOLM.setStringValue("zip", userInfo.getZip());
        justOLM.setStringValue("email", userInfo.getEmail());
        justOLM.setStringValue("mobile", userInfo.getMobile());
        justOLM.setStringValue("isadmin", String.valueOf(userInfo.isAdmin()));
    }
}
