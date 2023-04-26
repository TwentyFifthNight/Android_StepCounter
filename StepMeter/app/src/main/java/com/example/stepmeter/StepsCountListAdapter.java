package com.example.stepmeter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stepmeter.dbclasses.StepsCount;

import java.util.List;

public class StepsCountListAdapter extends RecyclerView.Adapter<StepsCountListAdapter.StepsCountViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private List<StepsCount> mStepsCountList;


    public StepsCountListAdapter(Context context) {
        mLayoutInflater = LayoutInflater.from(context);
        this.mStepsCountList = null;
    }

    @NonNull
    @Override
    public StepsCountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = mLayoutInflater.inflate(R.layout.list_row,parent,false);
        return new StepsCountViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsCountViewHolder holder, int position) {
        StepsCount stepsCount = mStepsCountList.get(position);
        holder.date.setText(stepsCount.getDate());
        holder.steps.setText(stepsCount.getSteps());
    }

    @Override
    public int getItemCount() {
        if (mStepsCountList != null)
            return mStepsCountList.size();
        return 0;
    }

    public StepsCount getStepsCount(int position){
        return mStepsCountList.get(position);
    }

    public void setElementList(List<StepsCount> list){
        this.mStepsCountList = list;
        notifyDataSetChanged();
    }

    public class StepsCountViewHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView steps;

        public StepsCountViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateRow);
            steps = itemView.findViewById(R.id.stepsRow);
        }
    }
}
