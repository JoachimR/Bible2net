package de.reiss.bible2net.theword.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteItemDao {

    @Query("SELECT * FROM NoteItem")
    List<NoteItem> all();

    @Query("SELECT * FROM NoteItem WHERE date = :date")
    NoteItem byDate(Date date);

    @Query("SELECT * FROM NoteItem WHERE date BETWEEN :from AND :to")
    List<NoteItem> range(Date from, Date to);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(NoteItem... items);

    @Delete
    void delete(NoteItem... item);

    @Query("DELETE FROM NoteItem")
    void clear();

}
