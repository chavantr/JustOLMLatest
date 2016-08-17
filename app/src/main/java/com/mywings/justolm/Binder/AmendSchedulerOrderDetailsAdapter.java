package com.mywings.justolm.Binder;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mywings.justolm.Model.Items;
import com.mywings.justolm.R;
import com.mywings.justolm.Utilities.OnItemSelectedListener;
import com.mywings.justolm.Utilities.OnTextChangeListener;

import java.util.List;

/**
 * Created by Admin on 5/27/2016.
 */
public class AmendSchedulerOrderDetailsAdapter extends RecyclerView.Adapter<AmendSchedulerOrderDetailsAdapter.ViewHolder> {

    //region Variable
    public List<Items> orderDetails;
    private OnTextChangeListener onTextChangeListener;
    private OnItemSelectedListener onItemSelectedListener;
    //endregion

    public AmendSchedulerOrderDetailsAdapter(List<Items> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amend_order_scheduler_detail_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.txtPeriod.setText("" + orderDetails.get(position).getPeriod());

        holder.lblPrescribeName.setText(orderDetails.get(position).getItemName());

        holder.txtQty.setText(orderDetails.get(position).getQuantity());

        holder.imgDeleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderDetails.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.spnSchedule.setSelection(orderDetails.get(position).getSchedular().equalsIgnoreCase("weekly") ? 0 : 1);

        handleEvent(holder, position);

    }

    /**
     * @param holder
     * @param position
     */
    private void handleEvent(ViewHolder holder, final int position) {
        holder.txtPeriod.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (null != onTextChangeListener) {
                    onTextChangeListener.onTextChanged(position, s.toString());
                }
            }
        });

        holder.spnSchedule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int index, long id) {
                if (null != onItemSelectedListener) {
                    onItemSelectedListener.onItemChanged(position, parent.getItemAtPosition(index).toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView lblPrescribeName;
        private AppCompatSpinner spnSchedule;
        private AppCompatEditText txtPeriod;
        private AppCompatTextView txtQty;
        private AppCompatImageView imgDeleteIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            lblPrescribeName = (AppCompatTextView) itemView.findViewById(R.id.lblPrescribeName);
            spnSchedule = (AppCompatSpinner) itemView.findViewById(R.id.spnScheduler);
            txtPeriod = (AppCompatEditText) itemView.findViewById(R.id.txtPeriod);
            txtQty = (AppCompatTextView) itemView.findViewById(R.id.txtQty);
            imgDeleteIcon = (AppCompatImageView) itemView.findViewById(R.id.imgDeleteIcon);
        }
    }
}
