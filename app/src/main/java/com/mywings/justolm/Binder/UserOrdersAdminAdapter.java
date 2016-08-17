package com.mywings.justolm.Binder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Model.UpdateOrderAdmin;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.MyOrder;
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.OnDeleteListener;
import com.mywings.justolm.Process.OnUpdateOrderListener;
import com.mywings.justolm.Process.UpdateOrder;
import com.mywings.justolm.R;

import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/25/2016.
 */
public class UserOrdersAdminAdapter extends RecyclerView.Adapter<UserOrdersAdminAdapter.ViewHolder> implements OnDeleteListener, OnUpdateOrderListener {

    //region Variables
    public List<Order> orders;
    private MyOrder pendingOrder;
    private int clickPosition;
    private int selectedPosition;
    private Context context;
    private OnItemClickListener onItemClickListener;
    //endregion

    public UserOrdersAdminAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        pendingOrder = (MyOrder) context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_item_admin, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.lblOrderNo.setText("Order No : " + orders.get(position).getId());

        holder.lblOrderDate.setText("Order Date : " + orders.get(position).getCreatedAt());

        holder.spnStatus.setSelection(0/*getSelectedPosition(orders.get(position).getOrderStatusId())*/, false);

        if (orders.get(position).getOrderStatusId().equalsIgnoreCase("1")) {
            ((TextView) holder.spnStatus.getSelectedView()).setTextColor(context.getResources().getColor(R.color.pending_orange_1));
        } else if (orders.get(position).getOrderStatusId().equalsIgnoreCase("2")) {
            ((TextView) holder.spnStatus.getSelectedView()).setTextColor(context.getResources().getColor(R.color.accepted_green_2));
        } else if (orders.get(position).getOrderStatusId().equalsIgnoreCase("3")) {
            ((TextView) holder.spnStatus.getSelectedView()).setTextColor(context.getResources().getColor(R.color.rejected_red_3));
        } else if (orders.get(position).getOrderStatusId().equalsIgnoreCase("4")) {
            ((TextView) holder.spnStatus.getSelectedView()).setTextColor(context.getResources().getColor(R.color.delivered_majenta_4));
        }

        if (orders.get(position).getTypeId().equalsIgnoreCase("1")) {
            holder.lblOrderType.setText("Order Type : " + "Prescribed");
        } else {
            holder.lblOrderType.setText("Order Type : " + "Non-prescribed");
        }

        if (orders.get(position).isActionDelete()) {
            holder.imgDeleteIcon.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.lnrStatus.setVisibility(View.GONE);
        } else {
            holder.imgDeleteIcon.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
            holder.lnrStatus.setVisibility(View.VISIBLE);
        }

        if (orders.get(position).isConfirmDeleted()) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.lnrStatus.setVisibility(View.GONE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.lnrStatus.setVisibility(View.VISIBLE);
        }

        holder.imgDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.get(position).setConfirmDeleted(true);
                notifyDataSetChanged();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition = position;
                init(orders.get(position).getId());
            }
        });

        holder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onItemClickListener) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        holder.spnStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {

                selectedPosition = position;

                UpdateOrderAdmin updateOrderAdmin = new UpdateOrderAdmin();
                updateOrderAdmin.setUserId(orders.get(position).getUserId());
                updateOrderAdmin.setAdminId(String.valueOf(pendingOrder.justOLMShared.getIntegerValue("userId")));
                updateOrderAdmin.setOrderId(orders.get(position).getId());
                updateOrderAdmin.setStatusId(orders.get(position).getOrderStatusId());

                if (pendingOrder.isConnected()) {
                    initUpdateOrder(updateOrderAdmin);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onDeleteComplete(UserMessage userMessage, Exception exception) {
        pendingOrder.hide();
        orders.remove(clickPosition);
        notifyDataSetChanged();
    }

    private void init(String orderId) {
        pendingOrder.show();
        DeleteOrder deleteOrder = pendingOrder.initHelper.deleteOrder(pendingOrder.serviceFunctions, String.valueOf(pendingOrder.justOLMShared.getIntegerValue("userId")), orderId);
        deleteOrder.setOnDeleteListener(this, context);
    }

    private void initUpdateOrder(UpdateOrderAdmin updateOrderAdmin) {
        pendingOrder.show();
        UpdateOrder updateOrder = pendingOrder.initHelper.updateOrder(pendingOrder.serviceFunctions, updateOrderAdmin);
        updateOrder.setOnUpdateOrderListener(this, context);
    }

    @Override
    public void onUpdateComplete(UserMessage userMessage, Exception exception) {
        pendingOrder.hide();
        orders.remove(selectedPosition);
        notifyDataSetChanged();
    }

    /**
     * @param id
     * @return
     */
    private int getSelectedPosition(String id) {
        if (id.equalsIgnoreCase("1")) {
            return 0;
        } else if (id.equalsIgnoreCase("2")) {
            return 1;
        } else if (id.equalsIgnoreCase("3")) {
            return 2;
        } else if (id.equalsIgnoreCase("4")) {
            return 3;
        }
        return 0;
    }


    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView lblOrderNo;
        AppCompatTextView lblOrderDate;
        AppCompatTextView lblOrderType;
        AppCompatSpinner spnStatus;
        LinearLayout lnrStatus;
        Button btnDelete;
        AppCompatImageView imgDeleteIcon;
        CardView panel;

        public ViewHolder(View itemView) {
            super(itemView);
            lblOrderNo = (AppCompatTextView) itemView.findViewById(R.id.lblOrderNo);
            lblOrderDate = (AppCompatTextView) itemView.findViewById(R.id.lblOrderDate);
            lblOrderType = (AppCompatTextView) itemView.findViewById(R.id.lblOrderType);
            spnStatus = (AppCompatSpinner) itemView.findViewById(R.id.spnStatus);
            lnrStatus = (LinearLayout) itemView.findViewById(R.id.lnrStatus);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            imgDeleteIcon = (AppCompatImageView) itemView.findViewById(R.id.imgDeleteIcon);
            panel = (CardView) itemView.findViewById(R.id.panel);
        }
    }
}
