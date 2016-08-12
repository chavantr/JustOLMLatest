package com.mywings.justolm;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.InitOrder;
import com.mywings.justolm.Process.OnDeleteListener;
import com.mywings.justolm.Process.OnInitOrderListener;

import java.util.List;

public class AmendOrderDetails extends JustOlmCompactActivity implements OnInitOrderListener, OnDeleteListener {

    private AppCompatTextView lblOrderDate;
    private AppCompatTextView lblOrderNumber;
    private AppCompatTextView lblPreferTime;
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
        lblPreferTime = (AppCompatTextView) findViewById(R.id.lblPreferTime);
        lstAmendOrderDetails = (RecyclerView) findViewById(R.id.lstAmendOrderDetails);
        btnUpdate = (AppCompatButton) findViewById(R.id.btnUpdateOrder);
        lblOrderDate.setText("  Order Date\n" + AmendOrder.orderDetail.getCreatedAt().split(" ")[0]);
        lblOrderNumber.setText("Order No \n" + AmendOrder.orderDetail.getId() + "  ");
        lblPreferTime.setText("Prefer time to accept delivery\n" + AmendOrder.orderDetail.getOrderTime());
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
            if (request.getItems().size() <= 0) {
                Dialog dialog = confirmation();
                dialog.show();
            } else {
                show();
                InitOrder initOrder = initHelper.initOrder(serviceFunctions);
                initOrder.setOnInitOrderListener(this, request);
            }
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


    /**
     *
     */
    public Dialog confirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.confirmation_));
        builder.setPositiveButton(getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                init(AmendOrder.orderDetail.getId());
            }
        });
        builder.setNegativeButton(getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        return builder.create();
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
}
