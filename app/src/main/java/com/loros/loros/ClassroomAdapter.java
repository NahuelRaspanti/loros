package com.loros.loros;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.ViewHolder>{

    private ArrayList<Classroom> mClassroomList;
    private Context mContext;
    private onClasroomClick mListener;


    public ClassroomAdapter (Context context, ArrayList<Classroom> list) {
        mContext = context;
        mClassroomList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = R.layout.trabalenguas_list;
        View v = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Classroom currentItem = mClassroomList.get(position);
        String className = currentItem.getClass_name();
        holder.mTextView.setText(className);
    }

    @Override
    public int getItemCount() {
        return mClassroomList.size();
    }

    public interface onClasroomClick {
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(onClasroomClick listener) {
        mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public View mView;
        public ViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.my_text_view);
            mView = (View) itemView.findViewById(R.id.selected_overlay);

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
