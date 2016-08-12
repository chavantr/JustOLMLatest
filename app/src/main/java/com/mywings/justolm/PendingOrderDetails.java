package com.mywings.justolm;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mywings.justolm.Binder.PendingOrderView;
import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.OnDeleteListener;

public class PendingOrderDetails extends JustOlmCompactActivity implements OnDeleteListener {


    public static Order order;
    //region UI Controls
    private RecyclerView lstPedingOrderDetails;
    private AppCompatTextView lblOrderDate;
    private AppCompatTextView lblOrderNo;
    private AppCompatTextView lblPreferTime;
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
        lblPreferTime = (AppCompatTextView) findViewById(R.id.lblPreferTime);
        lstPedingOrderDetails.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));
        final PendingOrderView pendingOrderView = new PendingOrderView(order.getItems());
        lstPedingOrderDetails.setAdapter(pendingOrderView);

        lblOrderNo.setText("   Order No \n" + order.getId() + "    ");
        lblOrderDate.setText("   Order Date \n" + order.getCreatedAt().split(" ")[0]);
        lblPreferTime.setText("Prefer time to accept delivery\n" + order.getOrderTime());
    }

    private LinearLayoutManager setLayout(int flow) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(flow);
        return linearLayoutManager;
    }

    /**
     * @param orderId
     */
    private void init(String orderId) {
        if (isConnected()) {
            show();
            DeleteOrder deleteOrder = initHelper.deleteOrder(serviceFunctions, String.valueOf(justOLMShared.getIntegerValue("userId")), orderId);
            deleteOrder.setOnDeleteListener(this, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ic_delete, menu);
        if (getIntent().hasExtra("isdelete")) {
            if (getIntent().getExtras().getString("isdelete").equalsIgnoreCase("Accepted")) {
                MenuItem menuItem = menu.findItem(R.id.action_delete);
                menuItem.setVisible(false);
                invalidateOptionsMenu();
            }
        }
        return true;
    }

    @Override
    public void onDeleteComplete(UserMessage userMessage, Exception exception) {
        hide();
        if (null != userMessage && exception == null) {
            finish();
        } else {
            show(exception.getMessage(), getGroup());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            init(PendingOrder.orderDetail.getId());
        }
        return super.onOptionsItemSelected(item);
    }


}
