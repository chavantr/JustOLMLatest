package com.mywings.justolm.Binder;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mywings.justolm.Model.Items;
import com.mywings.justolm.R;

import java.util.List;

/**
 * Created by Tatyabhau Chavan on 5/26/2016.
 */
public class UserOrderDetailAdapter extends RecyclerView.Adapter<UserOrderDetailAdapter.ViewHolder> {


    //region Variable
    public List<Items> orderDetails;
    //endregion

    public UserOrderDetailAdapter(List<Items> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amend_order_detail_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.lblPeriod.setText("Period : " + orderDetails.get(position).getPeriod());
        holder.lblPrescribeName.setText(orderDetails.get(position).getItemName());
        holder.lblSchedule.setText("Schedule : " + orderDetails.get(position).getSchedular());
        holder.txtQty.setText(orderDetails.get(position).getQuantity());

        holder.imgDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetails.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView lblPrescribeName;
        AppCompatTextView lblSchedule;
        AppCompatTextView lblPeriod;
        AppCompatEditText txtQty;
        private AppCompatImageView imgDeleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            lblPrescribeName = (AppCompatTextView) itemView.findViewById(R.id.lblPrescribeName);
            lblSchedule = (AppCompatTextView) itemView.findViewById(R.id.lblSchedule);
            lblPeriod = (AppCompatTextView) itemView.findViewById(R.id.lblPeriod);
            txtQty = (AppCompatEditText) itemView.findViewById(R.id.txtQty);
            imgDeleteIcon = (AppCompatImageView) itemView.findViewById(R.id.imgDeleteIcon);
        }
    }

}
