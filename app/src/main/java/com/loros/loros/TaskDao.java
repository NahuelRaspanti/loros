package com.loros.loros;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task WHERE TaskType = :taskType")
    @TypeConverters({TaskTypeConverter.class})
    LiveData<List<Task>> GetTasksByType(TaskType taskType);

    @Query("UPDATE Task SET TimesCompleted = TimesCompleted + 1, UpdateDttm = " +
            "date('now') WHERE TaskId = :taskId")
    void TaskCompleted(int taskId);

    @Insert
    void InsertTasks(Task... tasks);

    @Update
    void CompleteTask(Task... tasks);

}
