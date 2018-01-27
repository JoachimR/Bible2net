package de.reiss.bible2net.theword.database;

import android.app.Application;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

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