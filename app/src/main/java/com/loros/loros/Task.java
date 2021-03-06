package com.loros.loros;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "Task")
@TypeConverters({TaskSerializer.class})
public class Task {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TaskId")
    private int TaskId;

    @ColumnInfo(name = "TaskDesc")
    private String TaskDesc;

    /** Status of the given task.
     * Enumerated Values: 0 (Higiene), 1 (Trabajo), 2 (Social), 3 (Hogar)
     */
    @ColumnInfo(name = "TaskType")
    @TypeConverters({TaskTypeConverter.class})
    private TaskType TaskType;

    @ColumnInfo(name = "TimesCompleted")
    private int timesCompleted;

    @ColumnInfo(name = "TimesToComplete")
    private int timesToComplete;

    @ColumnInfo(name = "DaysToReset")
    private int daysToReset;

    @ColumnInfo(name = "UpdateDttm")
    @TypeConverters({DateTypeConverter.class})
    private Date lastUpdateDttm;

    public Task() {
    }

    public Date getLastUpdateDttm() {
        return lastUpdateDttm;
    }

    public int getTimesCompleted() {
        return timesCompleted;
    }

    public int getTimesToComplete() {
        return timesToComplete;
    }

    public int getTaskId() {
        return TaskId;
    }

    public String getTaskDesc() {
        return TaskDesc;
    }

    public com.loros.loros.TaskType getTaskType() {
        return TaskType;
    }

    public int getDaysToReset() {
        return daysToReset;
    }

    public void setLastUpdateDttm(Date lastUpdateDttm) {
        this.lastUpdateDttm = lastUpdateDttm;
    }

    public void setTaskType(com.loros.loros.TaskType taskType) {
        TaskType = taskType;
    }

    public void setTimesCompleted(int timesCompleted) {
        this.timesCompleted = timesCompleted;
    }

    public void setTimesToComplete(int timesToComplete) {
        this.timesToComplete = timesToComplete;
    }

    public void setTaskId(int taskId) {
        TaskId = taskId;
    }

    public void setTaskDesc(String taskDesc) {
        TaskDesc = taskDesc;
    }

    public void setDaysToReset(int daysToReset) {
        this.daysToReset = daysToReset;
    }
}
