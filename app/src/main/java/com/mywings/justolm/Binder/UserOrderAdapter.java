package com.mywings.justolm.Binder;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import com.mywings.justolm.Model.Order;
import com.mywings.justolm.Model.UserMessage;
import com.mywings.justolm.MyOrder;
import com.mywings.justolm.Process.DeleteOrder;
import com.mywings.justolm.Process.OnDeleteListener;
import com.mywings.justolm.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/25/2016.
 */
public class UserOrderAdapter extends RecyclerView.Adapter<UserOrderAdapter.ViewHolder> implements OnDeleteListener, Filterable {


    //region Variables
    public List<Order> orders;
    private List<Order> ordersList;
    private MyOrder pendingOrder;
    private int clickPosition;
    private Context context;

    private OnItemClickListener onItemClickListener;
    //endregion

    public UserOrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.ordersList = orders;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_order_item_spinner, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.lblOrderNo.setText("Order No : " + orders.get(position).getId());
        holder.lblOrderDate.setText("Order Date : " + orders.get(position).getCreatedAt());
        holder.lblOrderStatus.setText(orders.get(position).getOrderStatusName());

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
        pendingOrder = (MyOrder) context;
        pendingOrder.show();
        DeleteOrder deleteOrder = pendingOrder.initHelper.deleteOrder(pendingOrder.serviceFunctions, String.valueOf(pendingOrder.justOLMShared.getIntegerValue("userId")), orderId);
        deleteOrder.setOnDeleteListener(this, context);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                List<Order> nlist = new ArrayList<>();
                FilterResults filterResults = new FilterResults();

                switch (constraint.toString()) {

                    case "all":
                        nlist.addAll(ordersList);
                        break;
                    case "1"://pending
                        filterUsingId(nlist, "1");
                        break;

                    case "2"://accepted
                        filterUsingId(nlist, "2");
                        break;

                    case "3"://rejected
                        filterUsingId(nlist, "3");
                        break;


                    case "4"://delivered
                        filterUsingId(nlist, "4");
                        break;
                }

                filterResults.values = nlist;
                filterResults.count = nlist.size();
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                orders = (List<Order>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private void filterUsingId(List<Order> nlist, String id) {
        for (Order order : ordersList) {
            if (order.getOrderStatusId().equalsIgnoreCase(id)) {
                nlist.add(order);
            }
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int id);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView lblOrderNo;
        AppCompatTextView lblOrderDate;
        AppCompatTextView lblOrderType;
        AppCompatTextView lblOrderStatus;
        LinearLayout lnrStatus;

        Button btnDelete;
        AppCompatImageView imgDeleteIcon;
        CardView panel;

        public ViewHolder(View itemView) {
            super(itemView);
            lblOrderNo = (AppCompatTextView) itemView.findViewById(R.id.lblOrderNo);
            lblOrderDate = (AppCompatTextView) itemView.findViewById(R.id.lblOrderDate);
            lblOrderType = (AppCompatTextView) itemView.findViewById(R.id.lblOrderType);
            lblOrderStatus = (AppCompatTextView) itemView.findViewById(R.id.lblOrderStatus);
            lnrStatus = (LinearLayout) itemView.findViewById(R.id.lnrStatus);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
            imgDeleteIcon = (AppCompatImageView) itemView.findViewById(R.id.imgDeleteIcon);
            panel = (CardView) itemView.findViewById(R.id.panel);
        }
    }
}
