package com.mywings.justolm.Binder;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mywings.justolm.Model.Order;
import com.mywings.justolm.R;

import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/25/2016.
 */
public class PendingOrdersAdapter extends RecyclerView.Adapter<PendingOrdersAdapter.ViewHolder> {


    //region Variables
    public List<Order> orders;
    private OnViewItemClickListener onViewItemClickListener;
    //endregion

    public PendingOrdersAdapter(List<Order> orders) {
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

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.remove(position);
                notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return orders.size();
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

