package com.example.myfirstapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.ViewHolder> {
    private String TAG = "StepsAdapter:";

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_date;
        TextView text_seconds;

        public ViewHolder(View itemView) {
            super(itemView);

            text_date = (TextView) itemView.findViewById(R.id.tv_time);
            text_seconds = (TextView) itemView.findViewById(R.id.tv_steps);
        }
    }


    private ArrayList<WatchInstance> instanceList;

    public WatchAdapter(ArrayList<WatchInstance> times) {
        instanceList = times;
    }

    @NonNull
    @Override
    public WatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View stepItemView = inflater.inflate(R.layout.step_list_item, viewGroup, false);

        // Return a new holder instance
        final WatchAdapter.ViewHolder viewHolder = new WatchAdapter.ViewHolder(stepItemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WatchAdapter.ViewHolder viewHolder, int i) {
        // curr
        WatchInstance inst = instanceList.get(i);
        // time string
        TextView tvTime = viewHolder.text_date;
        tvTime.setText(inst.getDate());
        // steps
        TextView tvSteps = viewHolder.text_seconds;
        long seconds = inst.getSeconds();
        long milli = seconds % 100;
        long sec = (seconds / 100) % 60;
        long min = (seconds / 6000) % 60 ;
        String timeString = (min<10 ? "0" + min : min)  + ":" + (sec<10 ? "0" + sec : sec) + ":" + (milli<10 ? "0" + milli : milli);
        tvSteps.setText(timeString);
    }

    @Override
    public int getItemCount() {
        return instanceList.size();
    }
}
