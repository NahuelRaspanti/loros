package com.loros.loros;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private List<Task> taskList;
    private Context mContext;


    public TaskRecyclerAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.mContext = context;
    }

    @Override
    public TaskRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = R.layout.task_list_fragment;
        View v = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new TaskRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TaskRecyclerAdapter.ViewHolder holder, int position) {
        Task currentTask = taskList.get(position);
        holder.mTaskDesc.setText(currentTask.getTaskDesc().toUpperCase());
        holder.mTaskCompleted.setText(currentTask.getTimesCompleted());
        holder.mTaskTimesToComplete.setText(currentTask.getTimesToComplete());
    }

    public void addItems(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTaskDesc;
        public TextView mTaskCompleted;
        public TextView mTaskTimesToComplete;

        public ViewHolder(View itemView) {
            super(itemView);
            mTaskTimesToComplete = itemView.findViewById(R.id.TimesToComplete);
            mTaskCompleted = itemView.findViewById(R.id.TimesCompleted);
            mTaskDesc = itemView.findViewById(R.id.TaskName);
        }
    }

}
