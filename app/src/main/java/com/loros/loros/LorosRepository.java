package com.loros.loros;

import android.app.Application;

import java.util.List;

public class LorosRepository {

    private TaskDao taskDao;

    LorosRepository(Application application) {
        LorosDatabase db = LorosDatabase.getDatabase(application);
    }

    List<Task> GetTasksByType(TaskType taskType){
        return taskDao.GetTasksByType(taskType);
    }

    public void InsertTask(Task... task) {
        taskDao.InsertTasks(task);
    }

    public void CompleteTask(Task... task) {
        taskDao.CompleteTask(task);
    }

}
