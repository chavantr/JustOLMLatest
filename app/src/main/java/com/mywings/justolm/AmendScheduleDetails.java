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

import com.mywings.justolm.Binder.AmendSchedulerOrderDetailsAdapter;
import com.mywings.justolm.Model.InitOrderRequest;
import com.mywings.justolm.Model.Items;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.InitOrder;
import com.mywings.justolm.Process.OnDeleteListener;
import com.mywings.justolm.Process.OnInitOrderListener;
import com.mywings.justolm.Utilities.OnItemSelectedListener;
import com.mywings.justolm.Utilities.OnTextChangeListener;

import java.util.List;

public class AmendScheduleDetails extends JustOlmCompactActivity implements OnInitOrderListener, OnDeleteListener {

    private AppCompatTextView lblOrderDate;
    private AppCompatTextView lblOrderNumber;
    private AppCompatTextView lblPreferTime;
    private RecyclerView lstAmendOrderDetails;
    private AmendSchedulerOrderDetailsAdapter amendOrderDetailAdapter;
    private AppCompatButton btnUpdateOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amend_schedule_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lblOrderDate = (AppCompatTextView) findViewById(R.id.lblOrderDate);
        lblOrderNumber = (AppCompatTextView) findViewById(R.id.lblOrderNo);
        lblPreferTime = (AppCompatTextView) findViewById(R.id.lblPreferTime);
        lstAmendOrderDetails = (RecyclerView) findViewById(R.id.lstAmendOrderDetails);
        btnUpdateOrder = (AppCompatButton) findViewById(R.id.btnUpdateOrder);
        lblOrderDate.setText("  Order Date\n" + AmendScheduler.orderDetail.getCreatedAt().split(" ")[0]);
        lblOrderNumber.setText("Order No \n" + AmendScheduler.orderDetail.getId() + "   ");
        lblPreferTime.setText("Prefer time to accept delivery\n" + AmendScheduler.orderDetail.getOrderTime());

        lstAmendOrderDetails.setLayoutManager(setLayout(LinearLayoutManager.VERTICAL));
        amendOrderDetailAdapter = new AmendSchedulerOrderDetailsAdapter(AmendScheduler.orderDetail.getItems());

        amendOrderDetailAdapter.setOnTextChangeListener(new OnTextChangeListener() {
            @Override
            public void onTextChanged(int position, String input) {

                if (!TextUtils.isEmpty(input)) {
                    amendOrderDetailAdapter.orderDetails.get(position).setPeriod(input);
                }

            }
        });

        amendOrderDetailAdapter.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemChanged(int position, String input) {
                if (!TextUtils.isEmpty(input)) {
                    amendOrderDetailAdapter.orderDetails.get(position).setSchedular(input);
                }
            }
        });

        lstAmendOrderDetails.setAdapter(amendOrderDetailAdapter);

        events();

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
                init(AmendScheduler.orderDetail.getId());
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

    private void events() {
        btnUpdateOrder.setOnClickListener(new View.OnClickListener() {
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
        request.setOrderDate(AmendScheduler.orderDetail.getCreatedAt());
        request.setOrderTime(AmendScheduler.orderDetail.getOrderTime());
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
            show(exception.getMessage(), btnUpdateOrder);
        }
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
