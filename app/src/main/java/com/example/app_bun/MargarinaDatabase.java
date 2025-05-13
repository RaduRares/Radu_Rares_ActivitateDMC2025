package com.example.app_bun;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.Database;

@Database(entities = {MargarinaEntity.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MargarinaDatabase extends RoomDatabase {
    private static MargarinaDatabase INSTANCE;

    public abstract MargarinaDao margarinaDao();

    public static synchronized MargarinaDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MargarinaDatabase.class, "margarina_db")
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
