package com.loros.loros;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import java.util.List;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private List<Task> taskList;
    private Context mContext;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onYesClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

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
        String timesCompleted = String.valueOf(currentTask.getTimesCompleted());
        String timesToComplete = String.valueOf(currentTask.getTimesToComplete());
        String completion = timesCompleted  + "/" + timesToComplete;
        holder.mCompletion.setText(completion);
    }

    public void addItems(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return taskList == null ? 0 : taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTaskDesc;
        public TextView mCompletion;
        public MaterialButton mYesButton;

        public ViewHolder(View itemView) {
            super(itemView);
            mCompletion = itemView.findViewById(R.id.Completion);
            mTaskDesc = itemView.findViewById(R.id.TaskName);
            mYesButton = itemView.findViewById(R.id.yesBtn);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
            mYesButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(mListener != null) {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION) {
                            mListener.onYesClick(position);
                        }
                    }
                }
            });
        }


    }



}
