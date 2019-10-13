package com.loros.loros;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskListActivity extends AppCompatActivity implements TaskRecyclerAdapter.OnItemClickListener {
    private TaskViewModel mTaskViewModel;
    @BindView (R.id.my_toolbar) Toolbar mToolbar;
    @BindView(R.id.my_recycler_view) RecyclerView mRecyclerView;
    private TaskRecyclerAdapter mTaskAdapter;
    private List<Task> mTaskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_list_activity);
        ButterKnife.bind(this);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
