package com.mywings.justolm.Utilities;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Tatyabhau on 7/3/2016.
 */
public class JustOLMShared {

    private static JustOLMShared ourInstance;
    private static final String NAME = "justolmpreference";
    private Context context;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    public static synchronized JustOLMShared getInstance(Context context) {
        if (null == ourInstance) {
            ourInstance = new JustOLMShared(context);
        }
        return ourInstance;
    }

    private JustOLMShared(Context context) {
        this.context = context;
        settings = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
    }

    private void commitPreference() {
        editor.commit();
    }

    public void clearPreference() {
        editor.clear();
        editor.commit();
    }

    public void setStringValue(String key, String value) {
        try {
            editor.putString(key, value);
            commitPreference();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStringValue(String key) {
        try {
            return settings.getString(key, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setIntegerValue(String key, int value) {
        try {
            editor.putInt(key, value);
            commitPreference();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getIntegerValue(String key) {
        try {
            return settings.getInt(key, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
