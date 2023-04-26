package com.example.stepmeter.dbclasses;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {StepsCount.class}, version = 1, exportSchema = false)
public abstract class StepsCountRoomDatabase extends RoomDatabase {
    public abstract DaoElement elementDao();

    private static volatile StepsCountRoomDatabase INSTANCE;

    public static StepsCountRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StepsCountRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), StepsCountRoomDatabase.class, "localDB")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback
            sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                DaoElement dao = INSTANCE.elementDao();
                dao.insert(new StepsCount("18-04-2023","6780"));
                dao.insert(new StepsCount("19-04-2023", "8676"));
            });
        }
    };
}
