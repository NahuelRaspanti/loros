package com.loros.loros;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder>{

    private ArrayList<Classroom> mClassroomList;
    private Context mContext;
    private onClassroomClick mListener;


    public ClassroomAdapter (Context context, ArrayList<Classroom> list) {
        mContext = context;
        mClassroomList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = R.layout.classroom_list;
        View v = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Classroom currentItem = mClassroomList.get(position);
        String className = currentItem.getClass_name();
        String studentNames = currentItem.getStudentNames().toString().replace("[", "").replace("]", "");
        holder.mTextView.setText(className.toUpperCase());
        if(studentNames.isEmpty()){
            holder.mStudentLayout.setVisibility(View.GONE);
            holder.mEmptyLayout.setVisibility(View.VISIBLE);
        }
        else {
            holder.mStudentLayout.setVisibility(View.VISIBLE);
            holder.mEmptyLayout.setVisibility(View.GONE);
            holder.mStudentView.setText(studentNames.toUpperCase());
        }

    }

    @Override
    public int getItemCount() {
        return mClassroomList.size();
    }

    public interface onClassroomClick {
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(onClassroomClick listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mStudentView;
        public LinearLayout mEmptyLayout;
        public LinearLayout mStudentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.class_name);
            mStudentView = itemView.findViewById(R.id.student_names);
            mEmptyLayout = itemView.findViewById(R.id.student_empty);
            mStudentLayout = itemView.findViewById(R.id.student_data);

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
        }
    }


}
