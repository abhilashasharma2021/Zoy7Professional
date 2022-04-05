package com.zoyo7professional.adaper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zoyo7professional.databinding.RvCancelPolicyLayoutBinding;
import com.zoyo7professional.model.CancelPolicyData;

import java.util.ArrayList;

public class CancelPolicyAdapter extends RecyclerView.Adapter<CancelPolicyAdapter.Viewholder> {

    private final Context mContext;
    private final ArrayList<CancelPolicyData> policyList;

    public CancelPolicyAdapter(Context mContext, ArrayList<CancelPolicyData> policyList) {
        this.mContext = mContext;
        this.policyList = policyList;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(RvCancelPolicyLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        CancelPolicyData modelObject = policyList.get(position);

        if (modelObject!=null) {

            Log.e("bbjkjkj",modelObject.getPolicy());
            holder.rvCancelPolicyLayoutBinding.txPolicy.setText(modelObject.getPolicy());
        }
    }

    @Override
    public int getItemCount() {
        return policyList==null?0:policyList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private RvCancelPolicyLayoutBinding rvCancelPolicyLayoutBinding ;
        public Viewholder(RvCancelPolicyLayoutBinding rvCancelPolicyLayoutBinding ) {
            super(rvCancelPolicyLayoutBinding.getRoot());
            this.rvCancelPolicyLayoutBinding = rvCancelPolicyLayoutBinding;
        }
        }

}