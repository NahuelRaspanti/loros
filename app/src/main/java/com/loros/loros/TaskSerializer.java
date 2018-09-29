package com.loros.loros;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TaskSerializer {

    @TypeConverter
    public String fromTask(List<Task> task) {
        if (task == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        String json = gson.toJson(task, type);
        return json;
    }

    @TypeConverter
    public List<Task> toTask(String taskString) {
        if (taskString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Task>>() {
        }.getType();
        List<Task> taskList = gson.fromJson(taskString, type);
        return taskList;
    }
}
