package com.mywings.justolm.NetworkUtils;


import android.content.Context;
import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/27/2016.
 */
public class RestClient extends DefaultHttpClient {

    //region Variables
    private static RestClient ourInstance;
    private String url;
    private String baseUrl = "http://savorsys.com/justolm/auth/v1/";
    private Context context;
    private List<NameValuePair> header;
    private List<NameValuePair> param;
    private String json;
    private String message;
    private String response;
    private int responseCode;
    private String userName;
    private String password;
    private boolean authentication;
    //endregion

    public RestClient(Context context, String url) {
        this.context = context;
        this.url = baseUrl + url;
        header = new ArrayList<>();
        param = new ArrayList<>();
        header.add(new BasicNameValuePair("Cache-Control", "no-store"));
    }

    public static synchronized RestClient getInstance(Context context, String url) {
        ourInstance = new RestClient(context, url);
        return ourInstance;
    }

    // Be warned that this is sent in clear text, don't use basic auth unless
    // you have to.


    public void addBasicAuthentication(String user, String pass) {
        authentication = true;
        userName = user;
        password = pass;
    }

    public void addHeader(String HeaderName, String userName, String password) {
        String value = "Basic "
                + Base64.encodeToString(
                (userName.trim() + ":" + password.trim()).getBytes(),
                Base64.NO_WRAP);
        header.add(new BasicNameValuePair(HeaderName, value));
    }

    public void addHeaderWithHMAC(String HeaderName, String userName,
                                  String password, String HAMC) {
        String value = "Basic "
                + (Base64.encodeToString(
                (userName.trim() + ":" + password.trim()).getBytes(),
                Base64.NO_WRAP)) + "#" + HAMC;
        header.add(new BasicNameValuePair(HeaderName, value));
    }

    public void addHeader(String HeaderName, String value) {

        header.add(new BasicNameValuePair(HeaderName, value));
    }

    public void addParam(String name, String value) {
        param.add(new BasicNameValuePair(name, value));
    }

    public void execute(RequestMethod method)
            throws MissingBasicClientFunctionalityException,
            KeyManagementException {
        response = "";
        switch (method) {
            case GET: {
                HttpGet request = new HttpGet(url + addGetParams());
                request = (HttpGet) addHeaderParams(request);
                // printGet(request);
                executeRequest(request, url);

                break;
            }

            case POST: {
                HttpPost request = new HttpPost(url);
                request = (HttpPost) addHeaderParams(request);
                request = (HttpPost) addBodyParams(request);
                //printPost(request);
                executeRequest(request, url);
                break;
            }

            case PUT: {
                HttpPut request = new HttpPut(url);
                request = (HttpPut) addHeaderParams(request);
                request = (HttpPut) addBodyParams(request);
                executeRequest(request, url);
                break;
            }

            case DELETE: {
                HttpDelete request = new HttpDelete(url);
                request = (HttpDelete) addHeaderParams(request);
                executeRequest(request, url);
            }

        }

    }


    private HttpUriRequest addHeaderParams(HttpUriRequest request)
            throws MissingBasicClientFunctionalityException {
        for (NameValuePair h : header) {
            request.addHeader(h.getName(), h.getValue());
        }

        if (authentication) {
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(
                    userName, password);
            try {
                request.addHeader(new BasicScheme()
                        .authenticate(creds, request));
            } catch (AuthenticationException e) {
                throw new MissingBasicClientFunctionalityException(
                        e.getMessage());
            }
        }
        return request;
    }

    private HttpUriRequest addBodyParams(HttpUriRequest request)
            throws MissingBasicClientFunctionalityException {
        try {
            if (json != null) {
                request.addHeader("Content-Type", "application/json");
                if (request instanceof HttpPost) {
                    ((HttpPost) request).setEntity(new StringEntity(json,
                            "UTF-8"));
                } else if (request instanceof HttpPut)
                    ((HttpPut) request).setEntity(new StringEntity(json,
                            "UTF-8"));

            } else if (!param.isEmpty()) {
                if (request instanceof HttpPost)
                    ((HttpPost) request).setEntity(new UrlEncodedFormEntity(
                            param, HTTP.UTF_8));
                else if (request instanceof HttpPut)
                    ((HttpPut) request).setEntity(new UrlEncodedFormEntity(
                            param, HTTP.UTF_8));
            }
        } catch (UnsupportedEncodingException e) {
            throw new MissingBasicClientFunctionalityException(e.getMessage());
        }
        return request;
    }

    private String addGetParams() {
        StringBuffer combinedParams = new StringBuffer();
        if (!param.isEmpty()) {
            combinedParams.append("?");
            for (NameValuePair p : param) {
                String val = p.getValue();
                try {
                    val = URLEncoder.encode(p.getValue(), "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }
                combinedParams.append((combinedParams.length() > 1 ? "&" : "")
                        + p.getName() + "=" + val);
            }
        }
        return combinedParams.toString();
    }

    public String getErrorMessage() {
        return message;
    }

    public String getResponse() {
        return response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setContext(Context ctx) {
        context = ctx;
    }

    public void setJSONString(String data) {
        json = data;
    }

    private void executeRequest(HttpUriRequest request, String url)
            throws KeyManagementException {
        response = "";
        DefaultHttpClient client = null;

        // // // client = new HttpSLLClient(context);

        client = new DefaultHttpClient();

        HttpParams httpParameters = client.getParams();
        // Set the timeout in milliseconds until a connection is
        // established.
        // The default value is zero, that means the timeout is not used.
        int timeoutConnection = 30000;

        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);

        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.

        int timeoutSocket = 30000;

        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        client.setParams(httpParameters);

        HttpResponse httpResponse;

        try {

            httpResponse = client.execute(request);

            responseCode = httpResponse.getStatusLine().getStatusCode();
            message = httpResponse.getStatusLine().getReasonPhrase();

            if (responseCode == EnumClasses.NetworkStatusClass.GatewayTimeout) {
                response = "";
                Thread.currentThread().interrupt();
            } else if (responseCode == EnumClasses.NetworkStatusClass.NetworkConnectTimeout) {
                response = "";
                Thread.currentThread().interrupt();
            } else if (responseCode == EnumClasses.NetworkStatusClass.NetworkReadTimeout) {
                response = "";
                Thread.currentThread().interrupt();
            } else if (responseCode == EnumClasses.NetworkStatusClass.ConnectTimeout) {
                response = "";
                Thread.currentThread().interrupt();
            }

            HttpEntity entity = httpResponse.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                response = convertStreamToString(instream);
                // Closing the input stream will trigger connection release
                instream.close();
            }

        } catch (ClientProtocolException e) {
            client.getConnectionManager().shutdown();
            responseCode = EnumClasses.NetworkStatusClass.NetworkConnectTimeout;
        } catch (IOException e) {
            e.printStackTrace();
            responseCode = EnumClasses.NetworkStatusClass.NetworkConnectTimeout;

        }

    }

    private static String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, HTTP.UTF_8));
            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
