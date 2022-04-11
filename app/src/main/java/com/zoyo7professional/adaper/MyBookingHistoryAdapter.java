package com.zoyo7professional.adaper;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zoyo7professional.databinding.RowbookinghistorylayoutBinding;
import com.zoyo7professional.databinding.RowshoworderlayoutBinding;
import com.zoyo7professional.model.ShowOrderData;

import java.util.ArrayList;

public class MyBookingHistoryAdapter extends RecyclerView.Adapter<MyBookingHistoryAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<ShowOrderData>orderList;


    public MyBookingHistoryAdapter(Context mcontext, ArrayList<ShowOrderData>orderList){
        this.mcontext=mcontext;
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public MyBookingHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowbookinghistorylayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookingHistoryAdapter.ViewHolder holder, int position) {
        ShowOrderData modelObject = orderList.get(position);

        if (modelObject!=null) {

            holder.rowbookinghistorylayoutBinding.tvName.setText(modelObject.getServiceName());

            holder.rowbookinghistorylayoutBinding.tvDate.setText(modelObject.getOrderDate());
            holder.rowbookinghistorylayoutBinding.tvTime.setText(modelObject.getOrderTime());

            holder.rowbookinghistorylayoutBinding.ratingBar.setRating(Float.parseFloat(modelObject.getRating()));

            try {
                Picasso.get().load(modelObject.getServicePath()+modelObject.getServiceImage()).into(holder.rowbookinghistorylayoutBinding.ivBooking);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList==null?0: orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowbookinghistorylayoutBinding rowbookinghistorylayoutBinding;
        public ViewHolder(RowbookinghistorylayoutBinding rowbookinghistorylayoutBinding) {
            super(rowbookinghistorylayoutBinding.getRoot());
            this.rowbookinghistorylayoutBinding=rowbookinghistorylayoutBinding;
        }
    }
}
