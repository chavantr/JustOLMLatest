package com.mywings.justolm;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.mywings.justolm.Model.LoginResponse;

public class SplashScreen extends JustOlmCompactActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (null != justOLMShared.getStringValue("username") && null != justOLMShared.getStringValue("password")) {
                    init();
                } else {
                    initlogin();
                }

                finish();

            }
        }.execute();
    }

    private void init() {
        getCookies();
        LoginResponse loginResponse = LoginResponse.getInstance();
        loginResponse.setUserInfo(getCookies());
        getRegCookies();

        Intent intent = new Intent(SplashScreen.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void initlogin() {
        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }


}
