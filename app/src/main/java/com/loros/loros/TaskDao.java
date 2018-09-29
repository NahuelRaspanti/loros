package com.loros.loros;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task WHERE TaskType = :taskType")
    List<Task> GetTasksByType(TaskType taskType);

    @Insert
    @TypeConverters({TaskTypeConverter.class})
    void InsertTasks(Task... tasks);

    @Update
    void CompleteTask(Task... tasks);




}
