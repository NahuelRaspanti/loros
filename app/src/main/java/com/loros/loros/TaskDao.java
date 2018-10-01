package com.loros.loros;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task WHERE TaskType = :taskType")
    @TypeConverters({TaskTypeConverter.class})
    List<Task> GetTasksByType(TaskType taskType);

    @Insert
    void InsertTasks(Task... tasks);

    @Update
    void CompleteTask(Task... tasks);

}
