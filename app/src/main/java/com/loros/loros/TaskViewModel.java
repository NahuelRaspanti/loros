package com.loros.loros;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private LorosRepository mRepository;

    public TaskViewModel (Application application) {
        super(application);
        mRepository = new LorosRepository(application);
    }

    public LiveData<List<Task>> GetTasksByType(TaskType taskType){
        return mRepository.GetTasksByType(taskType);
    }

    public void UpdateTask (int taskId) {
        mRepository.UpdateTask(taskId);
    }

    public void InsertTask(Task... task) {
        mRepository.InsertTask(task);
    }

    public void CompleteTask(Task... task) {
        mRepository.CompleteTask(task);
    }



}
