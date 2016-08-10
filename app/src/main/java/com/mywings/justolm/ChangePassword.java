package com.mywings.justolm;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.OnChangePasswordListener;

public class ChangePassword extends JustOlmCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnChangePasswordListener {


    //region UI Controls

    private AppCompatEditText txtOldPassword;
    private AppCompatEditText txtNewPassword;
    private AppCompatEditText txtConfirmPassword;
    private Button btnSubmit;

    //endregion


    //region Varibales
    private DrawerLayout drawer;
    private Dialog dialog;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialization(toolbar);

        events();

    }

    private void events() {
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
    }

    /**
     *
     */
    private void validate() {
        if (txtOldPassword.getText().toString().isEmpty() || txtNewPassword.getText().toString().isEmpty() || txtConfirmPassword.getText().toString().isEmpty()) {
            show(getString(R.string.action_all_fields_mandetory), btnSubmit);
        } else if (!txtNewPassword.getText().toString().trim().equalsIgnoreCase(txtConfirmPassword.getText().toString().trim())) {
            show(getString(R.string.password_doesnot_match), btnSubmit);
        } else if (!validationHelper.isStrongPassword(txtNewPassword.getText().toString())) {
            show("Please enter valid password", btnSubmit);
        } else {
            initChangePassword();
        }
    }

    /**
     *
     */
    private void initChangePassword() {
        if (isConnected()) {
            show();
            com.mywings.justolm.Process.ChangePassword changePassword = initHelper.changePassword(serviceFunctions, "1", txtOldPassword.getText().toString().trim(), txtNewPassword.getText().toString().trim());
            changePassword.setOnUpdateListener(this);
        }
    }


    private void initialization(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (Boolean.parseBoolean(justOLMShared.getStringValue("isadmin"))) {
            if (null != navigationView) {
                navigationView.getMenu().clear();
                navigationView.inflateMenu(R.menu.activity_just_olm_admin_drawer);
            }
        }
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        txtOldPassword = (AppCompatEditText) findViewById(R.id.txtOldPassword);
        txtNewPassword = (AppCompatEditText) findViewById(R.id.txtNewPassword);
        txtConfirmPassword = (AppCompatEditText) findViewById(R.id.txtConfirmPassword);


        btnSubmit = (Button) findViewById(R.id.btnSubmit);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startHomeScreen();
                finish();
                break;

            case R.id.profile:
                startProfile();
                finish();
                break;

            case R.id.contactus:
                contactus();
                finish();
                break;

            case R.id.userorder:
                userorder();
                break;

            case R.id.neworder:
                neworder();
                finish();
                break;


            case R.id.amendorder:
                startamendorder();
                finish();
                break;

            case R.id.amendschedulerorder:
                startamendscheduler();
                finish();
                break;

            case R.id.abountus:
                startAboutUs();
                break;

            case R.id.logout:
                dialog = logout();
                dialog.show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void contactus() {
        Intent intent = new Intent(ChangePassword.this, ContactUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startamendscheduler() {
        Intent intent = new Intent(ChangePassword.this, AmendScheduler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startProfile() {
        Intent intent = new Intent(ChangePassword.this, MyProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startHomeScreen() {
        Intent intent = new Intent(ChangePassword.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void neworder() {
        Intent intent = new Intent(ChangePassword.this, NewOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void userorder() {
        Intent intent = new Intent(ChangePassword.this, MyOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startpendingscreen() {
        Intent intent = new Intent(ChangePassword.this, PendingOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startamendorder() {
        Intent intent = new Intent(ChangePassword.this, AmendOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startAboutUs() {
        Intent intent = new Intent(ChangePassword.this, AboutUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    @Override
    public void onChangePasswordComplete(UserMessage result, Exception exception) {
        hide();
        if (null != result && exception == null) {
            show(result.getMessage(), btnSubmit);
        } else {
            show(exception.getMessage(), btnSubmit);
        }
    }
}
