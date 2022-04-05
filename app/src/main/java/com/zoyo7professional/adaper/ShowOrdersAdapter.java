package com.zoyo7professional.adaper;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zoyo7professional.databinding.RowshoworderlayoutBinding;

import com.zoyo7professional.model.ShowOrderData;

import java.util.ArrayList;

public class ShowOrdersAdapter extends RecyclerView.Adapter<ShowOrdersAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<ShowOrderData>orderList;


    public ShowOrdersAdapter( Context mcontext,ArrayList<ShowOrderData>orderList){
        this.mcontext=mcontext;
        this.orderList=orderList;
    }

    @NonNull
    @Override
    public ShowOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowshoworderlayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ShowOrdersAdapter.ViewHolder holder, int position) {
        ShowOrderData modelObject = orderList.get(position);

        if (modelObject!=null) {

            holder.rowshoworderlayoutBinding.txName.setText(modelObject.getServiceName());

            holder.rowshoworderlayoutBinding.txDesp.setText(Html.fromHtml(modelObject.getServiceDescription().toString()));
            holder.rowshoworderlayoutBinding.txDate.setText(modelObject.getOrderDate());
            holder.rowshoworderlayoutBinding.txTime.setText(modelObject.getOrderTime());

            holder.rowshoworderlayoutBinding.txRating.setText(modelObject.getRating()+"Rating");

            try {
                Picasso.get().load(modelObject.getServicePath()+modelObject.getServiceImage()).into(holder.rowshoworderlayoutBinding.ivService);
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
        private RowshoworderlayoutBinding rowshoworderlayoutBinding;
        public ViewHolder(RowshoworderlayoutBinding rowshoworderlayoutBinding) {
            super(rowshoworderlayoutBinding.getRoot());
            this.rowshoworderlayoutBinding=rowshoworderlayoutBinding;
        }
    }
}
