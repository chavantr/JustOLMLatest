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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.mywings.justolm.Binder.PendingOrdersAdminAdapter;
import com.mywings.justolm.Binder.PendingOrdersSpinnerAdapter;
import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Process.GetOrders;
import com.mywings.justolm.Process.OnOrderListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PendingOrder extends JustOlmCompactActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnOrderListener {

    //region UI Controls
    private DrawerLayout drawer;
    private Dialog dialog;
    private RecyclerView lstPendingOrders;
    private PendingOrdersSpinnerAdapter pendingOrdersAdapter;
    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            show("Item has been removed.", lstPendingOrders);
            pendingOrdersAdapter.orders.remove(viewHolder.getAdapterPosition());
            pendingOrdersAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
            pendingOrdersAdapter.notifyItemRangeChanged(viewHolder.getAdapterPosition(), pendingOrdersAdapter.getItemCount());
        }
    };
    private PendingOrdersAdminAdapter pendingOrdersAdminAdapter;
    //endregion
    private Button btnEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialization(toolbar);
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
        btnEdit = (Button) findViewById(R.id.btnEdit);
        lstPendingOrders = (RecyclerView) findViewById(R.id.lstPendingOrders);
        lstPendingOrders.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));
        initGetOrders(String.valueOf(justOLMShared.getIntegerValue("userId")), "1");
    }

    private void setData(final List<Order> orders, boolean isadmin) {

        if (isadmin) {

            pendingOrdersAdminAdapter = new PendingOrdersAdminAdapter(this, orders);

            pendingOrdersAdminAdapter.setOnItemClickListener(new PendingOrdersAdminAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    PendingOrderDetails.order = orders.get(id);
                    startpendingorderdetails();
                }
            });
            lstPendingOrders.setAdapter(pendingOrdersAdminAdapter);


        } else {
            pendingOrdersAdapter = new PendingOrdersSpinnerAdapter(this, orders);
            pendingOrdersAdapter.setOnItemClickListener(new PendingOrdersSpinnerAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    PendingOrderDetails.order = orders.get(id);
                    startpendingorderdetails();
                }
            });
            lstPendingOrders.setAdapter(pendingOrdersAdapter);
        }
    }

    private void startpendingorderdetails() {
        Intent intent = new Intent(PendingOrder.this, PendingOrderDetails.class);
        startActivity(intent);
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

    /**
     * @param flow
     * @return
     */
    private LinearLayoutManager setLayout(int flow) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(flow);
        return linearLayoutManager;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

            case R.id.abountus:
                startAboutUs();
                finish();
                break;
            case R.id.neworder:
                neworder();
                finish();
                break;

            case R.id.changepassword:
                startchangepassword();
                break;
            case R.id.amendorder:
                startamendorder();
                finish();
                break;

            case R.id.amendschedulerorder:
                startamendscheduler();
                finish();
                break;
            case R.id.logout:
                dialog = logout();
                dialog.show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startchangepassword() {
        Intent intent = new Intent(PendingOrder.this, ChangePassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }


    private void startamendscheduler() {
        Intent intent = new Intent(PendingOrder.this, AmendScheduler.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void userorder() {
        Intent intent = new Intent(PendingOrder.this, MyOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startamendorder() {
        Intent intent = new Intent(PendingOrder.this, AmendOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void contactus() {
        Intent intent = new Intent(PendingOrder.this, ContactUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startAboutUs() {
        Intent intent = new Intent(PendingOrder.this, AboutUs.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startProfile() {
        Intent intent = new Intent(PendingOrder.this, MyProfile.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void startHomeScreen() {
        Intent intent = new Intent(PendingOrder.this, JustOLM.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void neworder() {
        Intent intent = new Intent(PendingOrder.this, NewOrder.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
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




}
