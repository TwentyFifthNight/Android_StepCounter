package com.example.stepmeter;

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
    //abstrakcyjna metoda zwracająca DAO
    public abstract DaoElement elementDao();

    //implementacja singletona
    private static volatile StepsCountRoomDatabase INSTANCE;

    static StepsCountRoomDatabase getDatabase(final Context context) {
        //tworzymy nowy obiekt tylko gdy żaden nie istnieje
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
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    private static final RoomDatabase.Callback
            sRoomDatabaseCallback = new RoomDatabase.Callback() {
        //uruchamiane przy tworzeniu bazy (pierwsze
        //uruchomienie aplikacji, gdy baza nie istnieje)

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //wykonanie operacji w osobnym wątku. Parametrem
            // metody execute() jest obiekt implementujący
            // interfejs Runnable, może być zastąpiony
            // wyrażeniem lambda
            databaseWriteExecutor.execute(() -> {
                DaoElement dao = INSTANCE.elementDao();
                //tworzenie elementów (obiektów klasy Element)
                //i dodawanie ich do bazy
                //za pomocą metody insert() z obiektu dao
                //tutaj możemy określić początkową zawartość
                //bazy danych
                //...
                dao.insert(new StepsCount("18-04-2023","6780"));
                dao.insert(new StepsCount("19-04-2023", "8676"));
            });
        }
    };
}
