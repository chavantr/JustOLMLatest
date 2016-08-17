package com.mywings.justolm.Binder;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mywings.justolm.AmendOrder;
import com.mywings.justolm.AmendScheduler;
import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.OnDeleteListener;
import com.mywings.justolm.R;

import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/25/2016.
 */
public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> implements OnDeleteListener {


    public List<Order> orders;
    //region Variables
    private Context context;
    private OnViewItemClickListener onViewItemClickListener;
    private int clickPosition;
    private AmendOrder amendOrder;
    private AmendScheduler amendScheduler;
    private boolean flag;


    //endregion

    public PendingOrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    public void setOnViewItemClickListener(OnViewItemClickListener onViewItemClickListener) {
        this.onViewItemClickListener = onViewItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.lblOrderNo.setText("Order No : " + orders.get(position).getId());
        holder.lblOrderDate.setText("Order Date : " + orders.get(position).getCreatedAt());

        if (orders.get(position).getTypeId().equalsIgnoreCase("1")) {
            holder.lblOrderType.setText("Order Type : " + "Prescribed");
        } else {
            holder.lblOrderType.setText("Order Type : " + "Non-prescribed");
        }

        if (orders.get(position).isActionDelete()) {
            holder.imgDeleteIcon.setVisibility(View.VISIBLE);
        } else {
            holder.imgDeleteIcon.setVisibility(View.GONE);
        }

        if (orders.get(position).isConfirmDeleted()) {
            holder.btnDelete.setVisibility(View.VISIBLE);
            holder.btnView.setVisibility(View.GONE);
        } else {
            holder.btnDelete.setVisibility(View.GONE);
            holder.btnView.setVisibility(View.VISIBLE);
        }

        holder.imgDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.get(position).setConfirmDeleted(true);
                notifyDataSetChanged();
            }
        });
        holder.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickPosition = position;
                Dialog dialog = confirmation(position);
                dialog.show();
            }
        });
        holder.panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != onViewItemClickListener) {
                    onViewItemClickListener.onVIewItemClickListener(position);
                }
            }
        });
    }


    /**
     *
     */
    public Dialog confirmation(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(context.getString(R.string.delete_confirmation));
        builder.setPositiveButton(context.getString(R.string.action_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                init(orders.get(position).getId());
            }
        });
        builder.setNegativeButton(context.getString(R.string.action_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        return builder.create();
    }

    private void init(String orderId) {
        if ((AmendOrder) context instanceof AmendOrder) {
            amendOrder = (AmendOrder) context;
            amendOrder.show();
            flag = true;
            DeleteOrder deleteOrder = amendOrder.initHelper.deleteOrder(amendOrder.serviceFunctions, String.valueOf(amendOrder.justOLMShared.getIntegerValue("userId")), orderId);
            deleteOrder.setOnDeleteListener(this, context);
        } else {
            amendScheduler = (AmendScheduler) context;
            amendScheduler.show();
            flag = false;
            DeleteOrder deleteOrder = amendScheduler.initHelper.deleteOrder(amendScheduler.serviceFunctions, String.valueOf(amendScheduler.justOLMShared.getIntegerValue("userId")), orderId);
            deleteOrder.setOnDeleteListener(this, context);
        }

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    @Override
    public void onDeleteComplete(UserMessage userMessage, Exception exception) {
        if (flag) {
            amendOrder.hide();
        } else {
            amendScheduler.hide();
        }
        orders.remove(clickPosition);
        notifyDataSetChanged();
    }

    public interface OnViewItemClickListener {
        void onVIewItemClickListener(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView lblOrderNo;
        AppCompatTextView lblOrderDate;
        AppCompatTextView lblOrderType;
        Button btnView;
        Button btnDelete;
        AppCompatImageView imgDeleteIcon;
        CardView panel;

        public ViewHolder(View itemView) {
            super(itemView);
            lblOrderNo = (AppCompatTextView) itemView.findViewById(R.id.lblOrderNo);
            lblOrderDate = (AppCompatTextView) itemView.findViewById(R.id.lblOrderDate);
            lblOrderType = (AppCompatTextView) itemView.findViewById(R.id.lblOrderType);
            btnView = (Button) itemView.findViewById(R.id.btnView);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            imgDeleteIcon = (AppCompatImageView) itemView.findViewById(R.id.imgDeleteIcon);
            panel = (CardView) itemView.findViewById(R.id.panel);
        }
    }

}

