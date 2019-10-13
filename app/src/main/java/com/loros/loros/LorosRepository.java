package com.loros.loros;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class LorosRepository {

    private TaskDao taskDao;

    LorosRepository(Application application) {
        LorosDatabase db = LorosDatabase.getDatabase(application);
        taskDao = db.taskDao();
    }

    LiveData<List<Task>> GetTasksByType(TaskType taskType){
        LiveData<List<Task>> mList = null;
        mList =  taskDao.GetTasksByType(taskType);
        return mList;
    }

    void UpdateTask(int taskId){
        new UpdateAsyncTask(taskDao).execute(taskId);
    }

    public void InsertTask(Task... task) {
        taskDao.InsertTasks(task);
    }

    public void CompleteTask(Task... task) {
        taskDao.CompleteTask(task);
    }

    private static class GetAsyncTask extends AsyncTask<TaskType, Void, LiveData<List<Task>>> {

        private TaskDao mAsyncTaskDao;

        GetAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected LiveData<List<Task>> doInBackground(final TaskType... params) {
            return mAsyncTaskDao.GetTasksByType(params[0]);
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Integer, Void, Void> {

        private TaskDao mAsyncTaskDao;

        UpdateAsyncTask(TaskDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.TaskCompleted(params[0]);
            return null;
        }
    }

}
