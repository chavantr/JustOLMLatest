package com.mywings.justolm.NetworkUtils;

import android.content.Context;
import android.util.Log;

import com.mywings.justolm.Model.InitOrderRequest;
import com.mywings.justolm.Model.Registration;
import com.mywings.justolm.Model.UpdateOrderAdmin;
import com.mywings.justolm.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.security.KeyManagementException;

/**
 * Created by Tatyabhau on 5/28/2016.
 */
public class ServiceFunctions<T> {


    private static ServiceFunctions ourInstance;
    private Context context;

    private ServiceFunctions(Context context) {
        this.context = context;
    }

    public static ServiceFunctions getInstance(Context context) {

        if (null == ourInstance) {
            ourInstance = new ServiceFunctions(context);
        }
        return ourInstance;
    }

    public String login(String username, String password) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_LOGIN);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.EMAIL).value(username.trim())
                    .key(Constants.PASSWORD).value(password.trim())
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);


    }


    public String forgotPassword(String email) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_FORGOT_PASSWORD);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.EMAIL).value(email.trim())
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);


    }


    public String deleteOrder(String userId, String orderId) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {
        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_DELETE_ORDER);
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.USER_ID).value(userId)
                    .key(Constants.ORDER_ID).value(orderId)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.setJSONString(jsonStringer.toString());
        Log.d("test", jsonStringer.toString());
        return executeClientCall(client, RequestMethod.POST);
    }

    public String getProfile(String userId) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {
        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_GET_PROFILE);
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.USER_ID).value(userId)

                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.setJSONString(jsonStringer.toString());
        Log.d("test", jsonStringer.toString());
        return executeClientCall(client, RequestMethod.POST);
    }


    public String getCountries() throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_GET_COUNTRIES);

        return executeClientCall(client, RequestMethod.POST);

    }


    public String getStates(String id) throws MissingBasicClientFunctionalityException, GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_GET_STATE_LIST);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.COUNTRY_ID).value(id)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);

    }


    public String getCities(String id) throws MissingBasicClientFunctionalityException, GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_GET_CITY_LIST);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.STATE_ID).value(id)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);

    }


    public String getAreas(String id) throws MissingBasicClientFunctionalityException, GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_GET_AREA_LIST);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.CITY_ID).value(id)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);

    }


    public String changePassword(String userId, String oldPassword, String newPassword) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_CHANGE_PASSWORD);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.USER_ID).value(userId)
                    .key(Constants.CURRENT_PASSWORD).value(oldPassword)
                    .key(Constants.NEW_PASSWORD).value(newPassword)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);

    }


    public String getOrderDetails(String userId, String orderId) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {
        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_ORDER_DETAILS);
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.USER_ID).value(userId)
                    .key(Constants.ORDER_ID).value(orderId)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.setJSONString(jsonStringer.toString());
        Log.d("test", jsonStringer.toString());
        return executeClientCall(client, RequestMethod.POST);
    }


    public String initOrder(InitOrderRequest request) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        JSONArray jsonArray = new JSONArray();
        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_INIT_ORDER);
        JSONStringer jsonStringer = null;

        for (int i = 0; i < request.getItems().size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Constants.ITEM_NAME, request.getItems().get(i).getItemName());
                jsonObject.put(Constants.ITEM_PHOTO, request.getItems().get(i).getItemPhoto());
                jsonObject.put(Constants.QUANTITY, request.getItems().get(i).getQuantity());
                jsonObject.put(Constants.PERIOD, request.getItems().get(i).getPeriod());
                jsonObject.put(Constants.SCHEDULER, request.getItems().get(i).getSchedular());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.ORDER_TIME).value(request.getOrderTime())
                    .key(Constants.ORDER_DATE).value(request.getOrderDate())
                    .key(Constants.ORDER_TYPE).value(request.getOrderType())
                    .key(Constants.USER_ID).value(request.getUserId())
                    .key(Constants.ITEMS).value(jsonArray)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.setJSONString(jsonStringer.toString());
        Log.d("test", jsonStringer.toString());
        return executeClientCall(client, RequestMethod.POST);
    }


    public String getOrders(String userId, String statusId) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {
        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_ORDERS);
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.USER_ID).value(userId)
                    .key(Constants.STATUS_ID).value(statusId)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.setJSONString(jsonStringer.toString());
        Log.d("test", jsonStringer.toString());
        return executeClientCall(client, RequestMethod.POST);
    }


    public String getOrdersAdmin(String userId, String statusId) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {
        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_ORDERS_ADMIN);
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.ADMIN_ID).value(userId)
                    .key(Constants.STATUS_ID).value(statusId)
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.setJSONString(jsonStringer.toString());
        Log.d("test", jsonStringer.toString());
        return executeClientCall(client, RequestMethod.POST);
    }


    public String updateOrder(UpdateOrderAdmin updateOrderAdmin) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {
        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_UPDATE_ORDER);
        JSONStringer jsonStringer = null;
        try {
            jsonStringer = new JSONStringer().object()
                    .key(Constants.ADMIN_ID).value(updateOrderAdmin.getAdminId())
                    .key(Constants.STATUS_ID).value(updateOrderAdmin.getStatusId())
                    .key(Constants.USER_ID).value(updateOrderAdmin.getUserId())
                    .key(Constants.ORDER_ID).value(updateOrderAdmin.getOrderId())
                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        client.setJSONString(jsonStringer.toString());
        Log.d("test", jsonStringer.toString());
        return executeClientCall(client, RequestMethod.POST);
    }


    public String updateProfile(Registration registration) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_UPDATE_USER_DETAIL);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()

                    .key(Constants.FIRST_NAME).value(registration.getFirstName())

                    .key(Constants.MIDDLE_NAME).value(registration.getMiddleName())

                    .key(Constants.LAST_NAME).value(registration.getLastName())

                    .key(Constants.DOB).value(registration.getDateOfBirth())

                    .key(Constants.GENDER).value(registration.getGender())

                    .key(Constants.PROFESSION).value(registration.getProfession())

                    .key(Constants.ADDRESS).value(registration.getAddress())

                    .key(Constants.ZIP).value(registration.getPinCode())

                    .key(Constants.COUNTRY_ID).value(registration.getCountry())

                    .key(Constants.STATE_ID).value(registration.getState())

                    .key(Constants.CITY_ID).value(registration.getCity())

                    .key(Constants.AREA_ID).value(registration.getArea())

                    .key(Constants.MOBILE).value(registration.getMobileNumber())

                    .key(Constants.EMAIL).value(registration.getEmail())

                    .key(Constants.USER_ID).value(registration.getId())

                    .endObject();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        Log.d("Update", jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);
    }


    public String registration(Registration registration) throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        RestClient client = RestClient.getInstance(context, ServiceNames.METHOD_REGISTRATION);

        JSONStringer jsonStringer = null;

        try {
            jsonStringer = new JSONStringer().object()

                    .key(Constants.FIRST_NAME).value(registration.getFirstName())

                    .key(Constants.MIDDLE_NAME).value(registration.getMiddleName())

                    .key(Constants.LAST_NAME).value(registration.getLastName())

                    .key(Constants.DOB).value(registration.getDateOfBirth())

                    .key(Constants.GENDER).value(registration.getGender())

                    .key(Constants.PROFESSION).value(registration.getProfession())

                    .key(Constants.ADDRESS).value(registration.getAddress())

                    .key(Constants.ZIP).value(registration.getPinCode())

                    .key(Constants.COUNTRY_ID).value(registration.getCountry())

                    .key(Constants.STATE_ID).value(registration.getState())

                    .key(Constants.CITY_ID).value(registration.getCity())

                    .key(Constants.AREA_ID).value(registration.getArea())

                    .key(Constants.MOBILE).value(registration.getMobileNumber())

                    .key(Constants.EMAIL).value(registration.getEmail())

                    .endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        client.setJSONString(jsonStringer.toString());

        Log.d("Registration", jsonStringer.toString());

        return executeClientCall(client, RequestMethod.POST);
    }

    private String executeClientCall(RestClient client, RequestMethod method)
            throws MissingBasicClientFunctionalityException,
            GatheredServerClientException {

        try {
            client.execute(method);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        if (client.getResponseCode() == 500 || client.getResponseCode() == 400)
            throw new ServerErrorException("Internal server error.");
        else if (client.getResponseCode() == EnumClasses.NetworkStatusClass.NetworkConnectTimeout)
            throw new NoInternetException("Server request timeout.");

        return client.getResponse();
    }

    public T parse(String response) {
        if (response.startsWith("[{") && response.endsWith("}]")) {
            try {
                JSONArray jsonArray = new JSONArray(response);
                return (T) jsonArray;
            } catch (JSONException e) {
                return (T) e;
            }
        } else if (response.startsWith("{") && response.endsWith("}")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                return (T) jsonObject;
            } catch (JSONException e) {
                return (T) e;
            }
        }
        return null;
    }


}
