package com.loros.loros;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment {
    private TaskViewModel mTaskViewModel;
    private RecyclerView mRecyclerView;
    private TaskRecyclerAdapter mTaskAdapter;
    private List<Task> mTaskList;

    public TaskListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trabalenguas_fragment, null);
        Bundle bundle = this.getArguments();
        int typeInt = bundle.getInt("Type");
        TaskType type = TaskTypeConverter.toType(typeInt);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mTaskAdapter = new TaskRecyclerAdapter(mTaskList, getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerView.setAdapter(mTaskAdapter);

        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

       mTaskAdapter.addItems(mTaskViewModel.GetTasksByType(type));

        return view;
    }

}
