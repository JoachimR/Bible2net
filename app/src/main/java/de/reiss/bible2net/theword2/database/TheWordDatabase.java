package de.reiss.bible2net.theword2.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {TheWordItem.class, BibleItem.class, NoteItem.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class TheWordDatabase extends RoomDatabase {

    public static TheWordDatabase create(Application application) {
        return Room.databaseBuilder(application, TheWordDatabase.class, "TheWordDatabase.db")
                .allowMainThreadQueries() // for widgets
                .build();
    }

    public abstract TheWordItemDao theWordItemDao();

    public abstract BibleItemDao bibleItemDao();

    public abstract NoteItemDao noteItemDao();

}