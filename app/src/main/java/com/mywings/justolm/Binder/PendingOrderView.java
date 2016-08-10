package com.mywings.justolm.Binder;

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
public class PendingOrderView extends RecyclerView.Adapter<PendingOrderView.ViewHolder> {

    //region Variable
    List<Items> items;
    //endregion

    public PendingOrderView(List<Items> items) {
        this.items = items;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.uneditableview, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.lblIndex.setText(String.valueOf(position + 1));

        holder.lblPeriod.setText(items.get(position).getPeriod());

        holder.lblScheduler.setText(items.get(position).getSchedular());

        holder.lblPreName.setText(items.get(position).getItemName());

        holder.lblQty.setText(items.get(position).getQuantity());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView lblIndex;
        AppCompatTextView lblPreName;
        AppCompatTextView lblQty;
        AppCompatTextView lblScheduler;
        AppCompatTextView lblPeriod;

        public ViewHolder(View itemView) {
            super(itemView);
            lblIndex = (AppCompatTextView) itemView.findViewById(R.id.lblIndex);
            lblPreName = (AppCompatTextView) itemView.findViewById(R.id.txtName);
            lblQty = (AppCompatTextView) itemView.findViewById(R.id.txtQty);
            lblScheduler = (AppCompatTextView) itemView.findViewById(R.id.txtScheduler);
            lblPeriod = (AppCompatTextView) itemView.findViewById(R.id.txtPeriod);
        }
    }
}
