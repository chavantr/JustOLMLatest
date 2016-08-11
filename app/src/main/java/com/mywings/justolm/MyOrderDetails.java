package com.mywings.justolm;

import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mywings.justolm.Binder.UserOrderDetailAdapter;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.OnDeleteListener;

public class MyOrderDetails extends JustOlmCompactActivity implements OnDeleteListener {

    //region
    private AppCompatTextView lblOrderDate;
    private AppCompatTextView lblOrderNumber;
    private RecyclerView lstAmendOrderDetails;
    private UserOrderDetailAdapter amendOrderDetailAdapter;
    private AppCompatTextView lblPreferTime;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lblOrderDate = (AppCompatTextView) findViewById(R.id.lblOrderDate);
        lblOrderNumber = (AppCompatTextView) findViewById(R.id.lblOrderNo);
        lblPreferTime = (AppCompatTextView) findViewById(R.id.lblPreferTime);
        lstAmendOrderDetails = (RecyclerView) findViewById(R.id.lstAmendOrderDetails);
        lblOrderDate.setText("   Order Date\n" + MyOrder.orderDetail.getCreatedAt().split(" ")[0]);
        lblOrderNumber.setText("  Order No \n" + MyOrder.orderDetail.getId() + "   ");
        lblPreferTime.setText("Prefer time to accept delivery\\n" + MyOrder.orderDetail.getOrderTime());
        lstAmendOrderDetails.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));
        amendOrderDetailAdapter = new UserOrderDetailAdapter(MyOrder.orderDetail.getItems());
        lstAmendOrderDetails.setAdapter(amendOrderDetailAdapter);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            init(MyOrder.orderDetail.getId());
        }
        return super.onOptionsItemSelected(item);
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
}
