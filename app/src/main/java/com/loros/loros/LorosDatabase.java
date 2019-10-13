package com.loros.loros;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.concurrent.Executors;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class LorosDatabase extends RoomDatabase {

    private static LorosDatabase INSTANCE;

    public abstract TaskDao taskDao();

    static LorosDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (LorosDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            LorosDatabase.class, "loros_database")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            getDatabase(context).taskDao().InsertTasks(getJsonToTaskArray(context));
                                        }
                                    });
                                }})
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static Task[] getJsonToTaskArray (Context context) {
        InputStream raw = context.getResources().openRawResource(R.raw.dbinit);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(raw, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        }
        catch(IOException ie) {
            ie.printStackTrace();
        }


        String taskString = writer.toString();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm").create();
        Type type = new TypeToken<Task[]>() {
        }.getType();
        Task[] taskList = gson.fromJson(taskString, type);
        return taskList;
    }


}
