package com.example.myfirstapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private String TAG = "StepsAdapter:";

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text_time;
        TextView text_steps;

        public ViewHolder (View itemView) {
            super(itemView);

            text_time = (TextView) itemView.findViewById(R.id.tv_time);
            text_steps = (TextView) itemView.findViewById(R.id.tv_steps);
        }
    }


    private ArrayList<StepsInstance> instanceList;
    public StepsAdapter(ArrayList<StepsInstance> steps) {instanceList = steps;}

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View stepItemView = inflater.inflate(R.layout.step_list_item, viewGroup, false);

        // Return a new holder instance
        final ViewHolder viewHolder = new ViewHolder(stepItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder viewHolder, int i) {
        // curr
        StepsInstance inst = instanceList.get(i);
        // time string
        TextView tvTime = viewHolder.text_time;
        tvTime.setText(inst.getTime());
        // steps
        TextView tvSteps = viewHolder.text_steps;
        tvSteps.setText(String.valueOf(inst.getSteps()));
    }

    @Override
    public int getItemCount() {
        return instanceList.size();
    }

}
