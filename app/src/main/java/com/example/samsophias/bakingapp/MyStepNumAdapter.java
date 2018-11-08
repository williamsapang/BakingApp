package com.example.samsophias.bakingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;

public class MyStepNumAdapter extends RecyclerView.Adapter<MyStepNumAdapter.StepNumberHolder>{

    private final Context mContext;
    private final ArrayList<Step> mStepArrayList;
    public OnStepClick mOnStepClick;
    private int rowNo = 0;

    public MyStepNumAdapter(Context context, ArrayList<Step> stepArrayList, OnStepClick onStepClick, int rowNo) {
        this.mContext = context;
        this.mStepArrayList = stepArrayList;
        this.mOnStepClick = onStepClick;
        this.rowNo = rowNo;
    }

    public interface OnStepClick {
        void onStepClick(int position);
    }

    @Override
    public int getItemCount() {
        return mStepArrayList.size();
    }


    @Override
    public void onBindViewHolder(@NonNull final StepNumberHolder holder, int position) {


        holder.stepTitle.setText(mStepArrayList.get(position).getShortDescription());

        holder.stepNumber.setText(String.valueOf(position+1));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnStepClick.onStepClick(holder.getAdapterPosition());
                rowNo = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        if(rowNo == position){
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryLight));
        } else
        {
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.backgroundIngredients));
        }
    }


    @NonNull
    @Override
    public StepNumberHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.my_number_step, parent, false);

        return new StepNumberHolder(view);
    }

    public class StepNumberHolder extends RecyclerView.ViewHolder {

        TextView stepNumber;
        TextView stepTitle;

        public StepNumberHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);
            stepNumber = v.findViewById(R.id.step_number_for_tablet);
            stepTitle = v.findViewById(R.id.step_title_for_tablet);
        }
    }
}