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

import com.mywings.justolm.Binder.PendingOrdersAdminAdapter;
import com.mywings.justolm.Binder.UserOrderAdapter;
import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Process.GetOrders;
import com.mywings.justolm.Process.OnOrderListener;

import java.util.Collections;
import java.util.List;

public class MyOrder extends JustOlmCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnOrderListener {


    //region
    public static Order orderDetail;
    private DrawerLayout drawer;
    private RecyclerView lstPendingOrders;
    private UserOrderAdapter pendingOrdersAdapter;
    private PendingOrdersAdminAdapter pendingOrdersAdminAdapter;
    private Dialog dialog;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Boolean.parseBoolean(justOLMShared.getStringValue("isadmin"))) {
            setTitle(R.string.all_orders);
        }
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
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        lstPendingOrders = (RecyclerView) findViewById(R.id.lstPendingOrders);
        lstPendingOrders.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));
        initGetOrders(String.valueOf(justOLMShared.getIntegerValue("userId")), "all");
    }

    /**
     * @param flow
     * @return
     */
    private LinearLayoutManager setLayout(int flow) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(flow);
        return linearLayoutManager;
    }

    /**
     * @param userId
     * @param statusId
     */
    private void initGetOrders(String userId, String statusId) {
        if (isConnected()) {
            show();
            GetOrders getOrders = initHelper.getOrders(serviceFunctions, userId, statusId, Boolean.parseBoolean(justOLMShared.getStringValue("isadmin")));
            getOrders.setOnOrderListener(this, this);
        }
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
        getMenuInflater().inflate(R.menu.my_order, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_all) {
            pendingOrdersAdapter.getFilter().filter("all");
            return true;
        } else if (id == R.id.action_pending) {
            pendingOrdersAdapter.getFilter().filter("1");
            return true;
        } else if (id == R.id.action_accepted) {
            pendingOrdersAdapter.getFilter().filter("2");
            return true;
        } else if (id == R.id.action_rejected) {
            pendingOrdersAdapter.getFilter().filter("3");
            return true;
        } else if (id == R.id.action_delivered) {
            pendingOrdersAdapter.getFilter().filter("4");
            return true;
        }
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

            case R.id.abountus:

                aboutus();
                finish();

                break;

            case R.id.contactus:
                contactus();
                finish();
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

    private void aboutus() {
        Intent intent = new Intent(MyOrder.this, AboutUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void contactus() {
        Intent intent = new Intent(MyOrder.this, ContactUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startamendscheduler() {
        Intent intent = new Intent(MyOrder.this, AmendScheduler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startProfile() {
        Intent intent = new Intent(MyOrder.this, MyProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startHomeScreen() {
        Intent intent = new Intent(MyOrder.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void neworder() {
        Intent intent = new Intent(MyOrder.this, NewOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startpendingscreen() {
        Intent intent = new Intent(MyOrder.this, PendingOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startchangepassword() {
        Intent intent = new Intent(MyOrder.this, ChangePassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    private void startamendorder() {
        Intent intent = new Intent(MyOrder.this, AmendOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    @Override
    public void onOrderComplete(List<Order> result, boolean isadmin, Exception exception) {
        hide();
        if (null != result && exception == null) {
            Collections.sort(result, new IdComparator());
            setData(result, isadmin);
        } else {
            show(exception.getMessage(), lstPendingOrders);
        }

    }

    private void setData(final List<Order> orders, boolean isadmin) {
        if (isadmin) {
            pendingOrdersAdminAdapter = new PendingOrdersAdminAdapter(this, orders);
            //pendingOrdersAdapter = new UserOrderAdapter(this, orders);
            pendingOrdersAdminAdapter.setOnItemClickListener(new PendingOrdersAdminAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    orderDetail = orders.get(id);
                    Intent intent = new Intent(MyOrder.this, MyOrderDetails.class);
                    intent.putExtra("isdelete", orders.get(id).getOrderStatusName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            lstPendingOrders.setAdapter(pendingOrdersAdminAdapter);
        } else {
            pendingOrdersAdapter = new UserOrderAdapter(this, orders);
            pendingOrdersAdapter.setOnItemClickListener(new UserOrderAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    orderDetail = orders.get(id);
                    Intent intent = new Intent(MyOrder.this, MyOrderDetails.class);
                    intent.putExtra("isdelete", orders.get(id).getOrderStatusName());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            });
            lstPendingOrders.setAdapter(pendingOrdersAdapter);
        }
    }
}
