package com.mywings.justolm;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mywings.justolm.Binder.PendingOrdersAdapter;
import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Process.GetOrders;
import com.mywings.justolm.Process.OnOrderListener;

import java.util.Collections;
import java.util.List;

public class AmendOrder extends JustOlmCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnOrderListener {

    //region UI Controls
    public static Order orderDetail;
    private DrawerLayout drawer;
    private Dialog dialog;
    private Button btnEdit;
    private RecyclerView lstPendingOrders;
    private PendingOrdersAdapter pendingOrdersAdapter;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amend_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        btnEdit = (Button) findViewById(R.id.btnEdit);
        lstPendingOrders = (RecyclerView) findViewById(R.id.lstPendingOrders);
        lstPendingOrders.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnEdit.getText().toString().trim().equalsIgnoreCase("Edit")) {
                    for (int i = 0; i < pendingOrdersAdapter.orders.size(); i++) {
                        pendingOrdersAdapter.orders.get(i).setActionDelete(true);
                    }
                    pendingOrdersAdapter.notifyDataSetChanged();
                    btnEdit.setText(R.string.cancel);
                } else {
                    for (int i = 0; i < pendingOrdersAdapter.orders.size(); i++) {
                        pendingOrdersAdapter.orders.get(i).setActionDelete(false);
                        pendingOrdersAdapter.orders.get(i).setConfirmDeleted(false);
                    }
                    pendingOrdersAdapter.notifyDataSetChanged();
                    btnEdit.setText(R.string.edit);
                }
            }
        });

        initGetOrders(String.valueOf(justOLMShared.getIntegerValue("userId")), "1", Boolean.parseBoolean(justOLMShared.getStringValue("isadmin")));
    }

    private LinearLayoutManager setLayout(int flow) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(flow);
        return linearLayoutManager;
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

                break;

            case R.id.profile:
                startProfile();

                break;

            case R.id.userorder:
                userorder();
                break;


            case R.id.contactus:
                contactus();

                break;

            case R.id.neworder:
                neworder();
                break;

            case R.id.pendingorder:
                startpendingscreen();
                break;

            case R.id.abountus:
                startAboutUs();
                break;

            case R.id.amendschedulerorder:
                startamendscheduler();
                break;

            case R.id.changepassword:
                startchangepassword();
                break;

            case R.id.logout:
                dialog = logout();
                dialog.show();
                break;
        }


        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void startamendscheduler() {
        Intent intent = new Intent(AmendOrder.this, AmendScheduler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    private void contactus() {
        Intent intent = new Intent(AmendOrder.this, ContactUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startProfile() {
        Intent intent = new Intent(AmendOrder.this, MyProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startHomeScreen() {
        Intent intent = new Intent(AmendOrder.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void neworder() {
        Intent intent = new Intent(AmendOrder.this, NewOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void userorder() {
        Intent intent = new Intent(AmendOrder.this, MyOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startAboutUs() {
        Intent intent = new Intent(AmendOrder.this, AboutUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startpendingscreen() {
        Intent intent = new Intent(AmendOrder.this, PendingOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startchangepassword() {
        Intent intent = new Intent(AmendOrder.this, ChangePassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    @Override
    public void onOrderComplete(List<Order> result, boolean isadmin, Exception exception) {
        hide();
        if (null != result && exception == null) {
            Collections.sort(result, new IdComparator());
            pendingOrdersAdapter = new PendingOrdersAdapter(result);
            pendingOrdersAdapter.setOnViewItemClickListener(new PendingOrdersAdapter.OnViewItemClickListener() {
                @Override
                public void onVIewItemClickListener(int position) {
                    orderDetail = pendingOrdersAdapter.orders.get(position);
                    Intent intent = new Intent(AmendOrder.this, AmendOrderDetails.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            lstPendingOrders.setAdapter(pendingOrdersAdapter);
        } else {
            show(exception.getMessage(), lstPendingOrders);
        }
    }

    /**
     * @param userId
     * @param statusId
     */
    private void initGetOrders(String userId, String statusId, boolean isadmin) {
        if (isConnected()) {
            show();
            GetOrders getOrders = initHelper.getOrders(serviceFunctions, userId, statusId, isadmin);
            getOrders.setOnOrderListener(this, this);
        }
    }
}
