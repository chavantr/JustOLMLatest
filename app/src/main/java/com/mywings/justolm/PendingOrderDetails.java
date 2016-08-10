package com.mywings.justolm;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mywings.justolm.Binder.PendingOrderView;
import com.mywings.justolm.Model.Order;

public class PendingOrderDetails extends JustOlmCompactActivity {


    public static Order order;
    //region UI Controls
    private RecyclerView lstPedingOrderDetails;
    private AppCompatTextView lblOrderDate;
    private AppCompatTextView lblOrderNo;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        lstPedingOrderDetails = (RecyclerView) findViewById(R.id.lstPendingOrdersItems);
        lblOrderDate = (AppCompatTextView) findViewById(R.id.lblOrderDate);
        lblOrderNo = (AppCompatTextView) findViewById(R.id.lblOrderNo);
        lstPedingOrderDetails.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));
        final PendingOrderView pendingOrderView = new PendingOrderView(order.getItems());
        lstPedingOrderDetails.setAdapter(pendingOrderView);

        lblOrderNo.setText("   Order No \n" + order.getId());
        lblOrderDate.setText("   Order Date \n" + order.getCreatedAt());
    }

    private LinearLayoutManager setLayout(int flow) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(flow);
        return linearLayoutManager;
    }
}
