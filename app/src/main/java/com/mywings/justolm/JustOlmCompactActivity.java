package com.mywings.justolm;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.mywings.justolm.LocalUtils.JustOLMHelper;
import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Model.Registration;
import com.mywings.justolm.Model.UserInfo;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.UserInteraction.OnNotificationListener;
import com.mywings.justolm.Utilities.DateTimeUtils;
import com.mywings.justolm.Utilities.InitHelper;
import com.mywings.justolm.Utilities.JustOLMShared;
import com.mywings.justolm.Utilities.NetworkUtil;
import com.mywings.justolm.Utilities.ValidationHelper;

import java.util.Comparator;

/**
 * Created by Tatyabhau Chavan on 5/17/2016.
 */
public abstract class JustOlmCompactActivity extends AppCompatActivity implements OnNotificationListener {

    public ValidationHelper validationHelper;
    public DateTimeUtils dateTimeUtils;
    public InitHelper initHelper;
    public ServiceFunctions serviceFunctions;
    public JustOLMHelper justOLMHelper;
    public JustOLMShared justOLMShared;
    private NetworkUtil networkUtil;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkUtil = new NetworkUtil(getApplicationContext());
        validationHelper = new ValidationHelper();
        dateTimeUtils = DateTimeUtils.getInstance();
        initHelper = InitHelper.getInstance();
        serviceFunctions = ServiceFunctions.getInstance(this);
        justOLMHelper = JustOLMHelper.getInstance(this, "justlm", null, 1);
        justOLMShared = JustOLMShared.getInstance(this);

    }

    @Override
    public void show(String message) {

    }

    public void hideSoftInput() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    @Override
    public void show(String message, View id) {

        Snackbar.make(id, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.action_ok, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();

    }

    @Override
    public boolean isConnected() {
        if (!networkUtil.isConnected()) {
            show(getString(R.string.internect_connectivity), getGroup());
        }
        return networkUtil.isConnected();
    }

    /**
     *
     */
    public Dialog logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.acton_logout));
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                justOLMShared.setStringValue("username", null);
                justOLMShared.setStringValue("password", null);
                logoutScreen();
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }


    public void show() {
        try {
            progressDialog = ProgressDialog.show(this, null, "Please wait...",
                    true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        progressDialog.dismiss();
    }


    /**
     * @return
     */
    public ViewGroup getGroup() {
        final ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        return viewGroup;
    }

    private void logoutScreen() {
        justOLMShared.clearPreference();
        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    public UserInfo getCookies() {
        UserInfo userInfo = UserInfo.getInstance();
        userInfo.setId(justOLMShared.getIntegerValue("userId"));
        userInfo.setFirstName(justOLMShared.getStringValue("firstname"));
        userInfo.setMiddleName(justOLMShared.getStringValue("middlename"));
        userInfo.setLastName(justOLMShared.getStringValue("lastname"));
        userInfo.setDob(justOLMShared.getStringValue("dob"));
        userInfo.setGender(justOLMShared.getStringValue("gender"));
        userInfo.setProfession(justOLMShared.getStringValue("profession"));
        userInfo.setAddress(justOLMShared.getStringValue("address"));
        userInfo.setCountryCode(justOLMShared.getStringValue("country"));
        userInfo.setState(justOLMShared.getStringValue("state"));
        userInfo.setCity(justOLMShared.getStringValue("city"));
        userInfo.setArea(justOLMShared.getStringValue("area"));


        userInfo.setZip(justOLMShared.getStringValue("zip"));
        userInfo.setCountryName(justOLMShared.getStringValue("countryName"));
        userInfo.setStateName(justOLMShared.getStringValue("stateName"));
        userInfo.setCityName(justOLMShared.getStringValue("cityName"));
        userInfo.setAreaName(justOLMShared.getStringValue("areaName"));

        userInfo.setEmail(justOLMShared.getStringValue("email"));

        userInfo.setMobile(justOLMShared.getStringValue("mobile"));
        userInfo.setAdmin(Boolean.parseBoolean(justOLMShared.getStringValue("isadmin")));

        return userInfo;
    }

    public com.mywings.justolm.Model.Registration getRegCookies() {
        Registration registration = Registration.getInstance();
        registration.setId(justOLMShared.getIntegerValue("userId"));
        registration.setFirstName(justOLMShared.getStringValue("firstname"));
        registration.setMiddleName(justOLMShared.getStringValue("middlename"));
        registration.setLastName(justOLMShared.getStringValue("lastname"));
        registration.setDateOfBirth(justOLMShared.getStringValue("dob"));
        registration.setGender(justOLMShared.getStringValue("gender"));
        registration.setProfession(justOLMShared.getStringValue("profession"));
        registration.setAddress(justOLMShared.getStringValue("address"));
        registration.setCountry(justOLMShared.getStringValue("country"));
        registration.setState(justOLMShared.getStringValue("state"));
        registration.setCity(justOLMShared.getStringValue("city"));
        registration.setArea(justOLMShared.getStringValue("area"));
        registration.setPinCode(justOLMShared.getStringValue("zip"));
        registration.setCountryName(justOLMShared.getStringValue("countryName"));
        registration.setStateName(justOLMShared.getStringValue("stateName"));
        registration.setCityName(justOLMShared.getStringValue("cityName"));
        registration.setAreaName(justOLMShared.getStringValue("areaName"));
        registration.setEmail(justOLMShared.getStringValue("email"));
        registration.setMobileNumber(justOLMShared.getStringValue("mobile"));
        return registration;
    }

    public static class IdComparator implements Comparator<Order> {

        @Override
        public int compare(Order lhs, Order rhs) {

            return (Integer.parseInt(lhs.getId()) > Integer.parseInt(rhs.getId())) ? -1 :
                    (Integer.parseInt(lhs.getId()) < Integer.parseInt(rhs.getId())) ? 1 : 0;


        }
    }
}
