package com.zoyo7professional.adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zoyo7professional.databinding.RowbookinghistorylayoutBinding;
import com.zoyo7professional.databinding.RowwallethistorylayoutBinding;
import com.zoyo7professional.model.ShowOrderData;
import com.zoyo7professional.model.WalletHistoryData;

import java.util.ArrayList;

public class MyWalletHistoryAdapter extends RecyclerView.Adapter<MyWalletHistoryAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<WalletHistoryData>walletList;


    public MyWalletHistoryAdapter(Context mcontext, ArrayList<WalletHistoryData>walletList){
        this.mcontext=mcontext;
        this.walletList=walletList;
    }

    @NonNull
    @Override
    public MyWalletHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(RowwallethistorylayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyWalletHistoryAdapter.ViewHolder holder, int position) {
        WalletHistoryData modelObject = walletList.get(position);

        if (modelObject!=null) {

            holder.rowwallethistorylayoutBinding.tvName.setText(modelObject.getName());

            holder.rowwallethistorylayoutBinding.txDate.setText(modelObject.getTrnDate());
            holder.rowwallethistorylayoutBinding.txTime.setText(modelObject.getTrnTime());

            holder.rowwallethistorylayoutBinding.txAmount.setText(modelObject.getAmount());


        }
    }

    @Override
    public int getItemCount() {
        return walletList==null?0: walletList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RowwallethistorylayoutBinding rowwallethistorylayoutBinding;
        public ViewHolder(RowwallethistorylayoutBinding rowwallethistorylayoutBinding) {
            super(rowwallethistorylayoutBinding.getRoot());
            this.rowwallethistorylayoutBinding=rowwallethistorylayoutBinding;
        }
    }
}
