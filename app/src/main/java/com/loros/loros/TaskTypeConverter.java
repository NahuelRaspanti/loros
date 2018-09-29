package com.loros.loros;

import android.arch.persistence.room.TypeConverter;

public class TaskTypeConverter {

    @TypeConverter
    public static TaskType toType(int type) {
        switch (type) {
            case 0 :
                return TaskType.HIGIENE;
            case 1:
                return TaskType.TRABAJO;
            case 2:
                return TaskType.SOCIAL;
            case 3:
                return TaskType.HOGAR;
            default:
                throw new IllegalArgumentException("Could not recognize status");
        }


    }

    @TypeConverter
    public static int toInteger(TaskType taskType) {
        return taskType.getCode();
    }
}

