package com.mywings.justolm.Process;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mywings.justolm.Model.Items;
import com.mywings.justolm.Model.Order;
import com.mywings.justolm.NetworkUtils.GatheredServerClientException;
import com.mywings.justolm.NetworkUtils.MissingBasicClientFunctionalityException;
import com.mywings.justolm.NetworkUtils.ServiceFunctions;
import com.mywings.justolm.Utilities.Constants;
import com.mywings.justolm.Utilities.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau on 7/6/2016.
 */
public class GetOrders extends AsyncTask<Context, Void, List<Order>> {


    private Exception exception = null;
    private String userId;
    private String statusId;
    private boolean isadmin;
    private OnOrderListener onOrderListener;
    private ServiceFunctions serviceFunctions;

    public GetOrders(ServiceFunctions serviceFunctions, String userId, String statusId, boolean isadmin) {
        this.serviceFunctions = serviceFunctions;
        this.userId = userId;
        this.isadmin = isadmin;
        this.statusId = statusId;
    }

    public void setOnOrderListener(OnOrderListener onOrderListener, Context context) {
        this.onOrderListener = onOrderListener;
        this.doLoadInbackground(context);
    }

    private void doLoadInbackground(Context context) {
        super.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
    }

    @Override
    protected List<Order> doInBackground(Context... params) {
        String response = null;
        try {
            if (isadmin) {
                response = serviceFunctions.getOrdersAdmin(userId, statusId);
            } else {
                response = serviceFunctions.getOrders(userId, statusId);
            }
            Log.d("test", response);
        } catch (MissingBasicClientFunctionalityException e) {
            e.printStackTrace();
            exception = e;
        } catch (GatheredServerClientException e) {
            e.printStackTrace();
            exception = e;
        }
        return process(response);
    }

    @Nullable
    private List<Order> process(String response) {
        List<Order> orders = new ArrayList<Order>();
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getInt(Constants.STATUS) == 200) {
                JSONArray jsonArray = jsonObject.getJSONArray(Constants.DATA);
                for (int i = 0; i < jsonArray.length(); i++) {
                    Order order = new Order();
                    order.setActionDelete(false);
                    order.setConfirmDeleted(false);
                    order.setCreatedAt(DateUtils.shuffle(DateUtils.parse(jsonArray.getJSONObject(i).getString("created_at").split(" ")[0])) + " " + jsonArray.getJSONObject(i).getString(Constants.ORDER_TIME));
                    order.setId(jsonArray.getJSONObject(i).getString(Constants.ORDER_ID));
                    order.setTypeId(jsonArray.getJSONObject(i).getString(Constants.ORDER_TYPE));
                    order.setOrderStatusId(jsonArray.getJSONObject(i).getString(Constants.STATUS_ID));
                    if (jsonArray.getJSONObject(i).has(Constants.AREA_ID)) {
                        order.setAreaId(jsonArray.getJSONObject(i).getString(Constants.AREA_ID));
                    } else {
                        order.setAreaId("0");
                    }
                    if (jsonArray.getJSONObject(i).has(Constants.USER_ID)) {
                        order.setUserId(jsonArray.getJSONObject(i).getString(Constants.USER_ID));
                    } else {
                        order.setUserId("");
                    }
                    order.setOrderStatusName(jsonArray.getJSONObject(i).getString(Constants.STATUS_NAME));
                    order.setOrderTime(jsonArray.getJSONObject(i).getString(Constants.ORDER_TIME));
                    JSONArray jItems = jsonArray.getJSONObject(i).getJSONArray(Constants.ITEMS);
                    List<Items> items = new ArrayList<>();
                    for (int j = 0; j < jItems.length(); j++) {
                        Items item = new Items();
                        item.setId(jItems.getJSONObject(j).getString(Constants.ITEM_ID));
                        item.setItemName(jItems.getJSONObject(j).getString(Constants.ITEM_NAME));
                        item.setPeriod(jItems.getJSONObject(j).getString(Constants.PERIOD));
                        item.setQuantity(jItems.getJSONObject(j).getString(Constants.QUANTITY));
                        item.setSchedular(jItems.getJSONObject(j).getString(Constants.SCHEDULER));
                        items.add(item);
                    }
                    order.setItems(items);
                    orders.add(order);
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    protected void onPostExecute(List<Order> orders) {
        super.onPostExecute(orders);
        onOrderListener.onOrderComplete(orders, isadmin, exception);
    }
}
