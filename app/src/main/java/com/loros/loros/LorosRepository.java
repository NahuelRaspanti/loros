package com.loros.loros;

import android.app.Application;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LorosRepository {

    private TaskDao taskDao;

    LorosRepository(Application application) {
        LorosDatabase db = LorosDatabase.getDatabase(application);
        taskDao = db.taskDao();
    }

    List<Task> GetTasksByType(TaskType taskType){
        List<Task> mList = null;
        try {
            mList = new GetAsyncTask(taskDao).execute(taskType).get();
        }
        catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return mList;
    }

    public void InsertTask(Task... task) {
        taskDao.InsertTasks(task);
    }

    public void CompleteTask(Task... task) {
        taskDao.CompleteTask(task);
    }

    private static class GetAsyncTask extends AsyncTask<TaskType, Void, List<Task>> {

        private TaskDao mAsyncTaskDao;

        GetAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Task> doInBackground(final TaskType... params) {
            return mAsyncTaskDao.GetTasksByType(params[0]);
        }
    }

}
