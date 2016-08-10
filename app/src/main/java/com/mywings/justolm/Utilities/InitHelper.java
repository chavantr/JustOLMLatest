package com.mywings.justolm.Utilities;

import android.content.Context;

import com.mywings.justolm.Model.UpdateOrderAdmin;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.Process.ChangePassword;
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.ForgotPassword;
import com.mywings.justolm.Process.GetAreas;
import com.mywings.justolm.Process.GetCity;
import com.mywings.justolm.Process.GetCountries;
import com.mywings.justolm.Process.GetOrders;
import com.mywings.justolm.Process.GetProfile;
import com.mywings.justolm.Process.GetStates;
import com.mywings.justolm.Process.InitOrder;
import com.mywings.justolm.Process.LoginUser;
import com.mywings.justolm.Process.RegisterUser;
import com.mywings.justolm.Process.UpdateOrder;
import com.mywings.justolm.Process.UpdateUser;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public class InitHelper {

    private static InitHelper ourInstance;

    private InitHelper() {
    }

    public static InitHelper getInstance() {
        if (null == ourInstance) {
            ourInstance = new InitHelper();
        }
        return ourInstance;
    }

    public LoginUser loginUser(ServiceFunctions serviceFunctions, String username, String password) {
        return new LoginUser(serviceFunctions, username, password);
    }

    public UpdateUser updateUser(ServiceFunctions serviceFunctions) {
        return new UpdateUser(serviceFunctions);
    }

    public RegisterUser registerUser(ServiceFunctions serviceFunctions, Context context) {
        return new RegisterUser(context, serviceFunctions);
    }

    public ForgotPassword forgotPassword(ServiceFunctions serviceFunctions) {
        return new ForgotPassword(serviceFunctions);
    }

    public ChangePassword changePassword(ServiceFunctions serviceFunctions, String userId, String oldPassword, String newPassword) {
        return new ChangePassword(serviceFunctions, userId, oldPassword, newPassword);
    }

    public GetCountries getCountries(ServiceFunctions serviceFunctions) {
        return new GetCountries(serviceFunctions);
    }

    public GetStates getStates(ServiceFunctions serviceFunctions, int id) {
        return new GetStates(serviceFunctions, id);
    }

    public GetCity getCities(ServiceFunctions serviceFunctions, int id) {
        return new GetCity(serviceFunctions, id);
    }

    public GetAreas getAreas(ServiceFunctions serviceFunctions, int id) {
        return new GetAreas(serviceFunctions, id);
    }

    public InitOrder initOrder(ServiceFunctions serviceFunctions) {
        return new InitOrder(serviceFunctions);
    }

    public GetOrders getOrders(ServiceFunctions serviceFunctions, String userId, String statusId, boolean isadmin) {
        return new GetOrders(serviceFunctions, userId, statusId, isadmin);
    }

    public DeleteOrder deleteOrder(ServiceFunctions serviceFunctions, String userId, String orderId) {
        return new DeleteOrder(serviceFunctions, userId, orderId);
    }

    public GetProfile getProfile(ServiceFunctions serviceFunctions, String userId) {
        return new GetProfile(serviceFunctions, userId);
    }

    public UpdateOrder updateOrder(ServiceFunctions serviceFunctions, UpdateOrderAdmin updateOrderAdmin) {
        return new UpdateOrder(serviceFunctions, updateOrderAdmin);
    }

}
