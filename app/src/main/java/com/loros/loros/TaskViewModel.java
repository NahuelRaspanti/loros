package com.loros.loros;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private LorosRepository mRepository;

    public TaskViewModel (Application application) {
        super(application);
        mRepository = new LorosRepository(application);
    }

    public List<Task> GetTasksByType(TaskType taskType){
        return mRepository.GetTasksByType(taskType);
    }

    public void InsertTask(Task... task) {
        mRepository.InsertTask(task);
    }

    public void CompleteTask(Task... task) {
        mRepository.CompleteTask(task);
    }



}
