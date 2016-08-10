package com.mywings.justolm;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.mywings.justolm.Binder.AmendOrderDetailAdapter;
import com.mywings.justolm.Model.InitOrderRequest;
import com.mywings.justolm.Model.Items;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.InitOrder;
import com.mywings.justolm.Process.OnInitOrderListener;

import java.util.List;

public class AmendOrderDetails extends JustOlmCompactActivity implements OnInitOrderListener {

    private AppCompatTextView lblOrderDate;
    private AppCompatTextView lblOrderNumber;
    private RecyclerView lstAmendOrderDetails;
    private AmendOrderDetailAdapter amendOrderDetailAdapter;
    private AppCompatButton btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amend_order_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lblOrderDate = (AppCompatTextView) findViewById(R.id.lblOrderDate);
        lblOrderNumber = (AppCompatTextView) findViewById(R.id.lblOrderNo);
        lstAmendOrderDetails = (RecyclerView) findViewById(R.id.lstAmendOrderDetails);
        btnUpdate = (AppCompatButton) findViewById(R.id.btnUpdateOrder);
        lblOrderDate.setText("Order Date\n" + AmendOrder.orderDetail.getCreatedAt());
        lblOrderNumber.setText("Order No \n" + AmendOrder.orderDetail.getId());
        lstAmendOrderDetails.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));
        amendOrderDetailAdapter = new AmendOrderDetailAdapter(AmendOrder.orderDetail.getItems());
        amendOrderDetailAdapter.setOnTextChangeListener(new AmendOrderDetailAdapter.OnTextChangeListener() {
            @Override
            public void onTextChange(int position, String input) {
                if (!TextUtils.isEmpty(input)) {
                    if (Integer.parseInt(input) <= 16) {
                        amendOrderDetailAdapter.orderDetails.get(position).setQuantity(input);
                    } else {
                        show(getString(R.string.lbl_more_than_16_qty), btnUpdate);
                    }
                }
            }
        });
        lstAmendOrderDetails.setAdapter(amendOrderDetailAdapter);
        events();
    }

    private void events() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initOrder(generate(amendOrderDetailAdapter.orderDetails));
            }
        });
    }

    private LinearLayoutManager setLayout(int flow) {
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(flow);
        return linearLayoutManager;
    }

    /**
     * @param request
     */
    private void initOrder(InitOrderRequest request) {
        if (isConnected()) {
            show();
            InitOrder initOrder = initHelper.initOrder(serviceFunctions);
            initOrder.setOnInitOrderListener(this, request);
        }
    }

    private InitOrderRequest generate(List<Items> orderDetails) {
        InitOrderRequest request = InitOrderRequest.getInstance();
        request.setOrderDate(AmendOrder.orderDetail.getCreatedAt());
        request.setOrderTime(AmendOrder.orderDetail.getOrderTime());
        request.setOrderType("2");
        request.setUserId(String.valueOf(justOLMShared.getIntegerValue("userId")));
        request.setItems(orderDetails);
        return request;
    }

    @Override
    public void onInitOrderComplete(UserMessage result, Exception exception) {
        hide();
        if (null != result && exception == null) {
            finish();
        } else {
            show(exception.getMessage(), btnUpdate);
        }
    }
}
