package com.zoyo7professional.adaper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zoyo7professional.activity.AddDetailsActivity;
import com.zoyo7professional.databinding.RowChooseSkillsLayoutBinding;

import com.zoyo7professional.model.ShowSkillsData;

import java.util.ArrayList;

public class ShowSkillsAdapter extends RecyclerView.Adapter<ShowSkillsAdapter.Viewholder> {

    private final Context mContext;
    private final ArrayList<ShowSkillsData> skillList;

    public ShowSkillsAdapter(Context mContext, ArrayList<ShowSkillsData> skillList) {
        this.mContext = mContext;
        this.skillList = skillList;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Viewholder(RowChooseSkillsLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent, false));    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ShowSkillsData modelObject = skillList.get(position);

        if (modelObject!=null) {

            Log.e("bbjkjkj",modelObject.getSkillName());
            holder.rowChooseSkillsLayoutBinding.txSkills.setText(modelObject.getSkillName());


            holder.rowChooseSkillsLayoutBinding.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                           @Override
                                                           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                                                               if(isChecked){

                                                                   AddDetailsActivity.Arr_skillsId.add(modelObject.getSkillId());
                                                                   AddDetailsActivity.Arr_skillsName.add(modelObject.getSkillName());

                                                               }else {
                                                                   try {
                                                                       AddDetailsActivity.Arr_skillsId.remove(modelObject.getSkillId());
                                                                       AddDetailsActivity.Arr_skillsName.remove(modelObject.getSkillName());
                                                                   } catch (Exception e) {
                                                                       e.printStackTrace();
                                                                   }
                                                               }
                                                           }
                                                       }
            );

            AddDetailsActivity.btSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddDetailsActivity.dialog.dismiss();


                    if (mContext instanceof AddDetailsActivity) {
                        ((AddDetailsActivity)mContext).GetSkillId();
                        ((AddDetailsActivity)mContext).GetSkillName();


                    }
                }
            });








        }
    }

    @Override
    public int getItemCount() {
        return skillList==null?0:skillList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private RowChooseSkillsLayoutBinding rowChooseSkillsLayoutBinding ;
        public Viewholder(RowChooseSkillsLayoutBinding rowChooseSkillsLayoutBinding ) {
            super(rowChooseSkillsLayoutBinding.getRoot());
            this.rowChooseSkillsLayoutBinding = rowChooseSkillsLayoutBinding;
        }
        }

}