package com.mywings.justolm.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Tatyabhau Chavan on 5/18/2016.
 */
public class NetworkUtil {

    private static NetworkUtil ourInstance;
    private static Context context;

    public static synchronized NetworkUtil getInstance() {
        if (null == ourInstance) {
            ourInstance = new NetworkUtil(context);
        }
        return ourInstance;
    }

    public NetworkUtil(Context context) {
        this.context = context;
    }


    public boolean isConnected() {
        boolean isConnectedToInternet = false;

        NetworkInfo networkInfoWiFi = null;
        NetworkInfo networkInfoGSM = null;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfoWiFi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        networkInfoGSM = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isWifiConn = false;
        if (networkInfoWiFi != null) {
            isWifiConn = networkInfoWiFi.isConnected();
        }
        boolean isMobileConn = false;
        if (networkInfoGSM != null) {
            isMobileConn = networkInfoGSM.isConnected();
        }
        if (isWifiConn)
            isConnectedToInternet = true;
        else if (isMobileConn)
            isConnectedToInternet = true;
        else
            isConnectedToInternet = false;
        return isConnectedToInternet;
    }
}
