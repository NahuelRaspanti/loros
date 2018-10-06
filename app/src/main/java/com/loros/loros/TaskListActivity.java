package com.loros.loros;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskListActivity extends AppCompatActivity implements TaskRecyclerAdapter.OnItemClickListener {
    private TaskViewModel mTaskViewModel;
//    @BindView (R.id.my_toolbar) Toolbar mToolbar;
    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;
    private TaskRecyclerAdapter mTaskAdapter;
    private List<Task> mTaskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trabalenguas_fragment);
        ButterKnife.bind(this);
/*        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/


        int typeInt = getIntent().getExtras().getInt("Type");
        TaskType type = TaskTypeConverter.toType(typeInt);

        mTaskAdapter = new TaskRecyclerAdapter(mTaskList, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setAdapter(mTaskAdapter);

        mTaskAdapter.setOnItemClickListener(this);


        mTaskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        mTaskViewModel.GetTasksByType(type).observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable final List<Task> tasks) {
                mTaskList = tasks;
                mTaskAdapter.addItems(tasks);
            }
        });
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onYesClick(int position) {
        Task currentTask = mTaskList.get(position);
        int timesCompleted = currentTask.getTimesCompleted();
        int timesToComplete = currentTask.getTimesToComplete();
        if (timesCompleted != timesToComplete) {
            int taskId = currentTask.getTaskId();
            mTaskViewModel.UpdateTask(taskId);
        }
        else {
            Toast.makeText(this, "YA COMPLETASTE LA TAREA POR EL DÍA!!", Toast.LENGTH_SHORT).show();
        }

    }
}
