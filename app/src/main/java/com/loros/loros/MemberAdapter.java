package com.loros.loros;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder>{
    private ArrayList<User> mMemberList;
    private Context mContext;
    private MemberAdapter.onMemberClick mListener;


    public MemberAdapter (Context context, ArrayList<User> list) {
        mContext = context;
        mMemberList = list;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int layout = R.layout.trabalenguas_list;
        View v = LayoutInflater.from(mContext).inflate(layout, parent, false);
        return new MemberAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MemberAdapter.ViewHolder holder, int position) {
        User currentItem = mMemberList.get(position);
        String className = currentItem.name;
        holder.mTextView.setText(className);
    }

    @Override
    public int getItemCount() {
        return mMemberList.size();
    }

    public interface onMemberClick {
        public void onItemClick(int position);
    }

    public void setOnItemClickListener(MemberAdapter.onMemberClick listener) {
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