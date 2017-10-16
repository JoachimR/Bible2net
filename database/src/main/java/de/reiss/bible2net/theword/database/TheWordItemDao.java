package de.reiss.bible2net.theword.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TheWordItemDao {

    @Query("SELECT * FROM TheWordItem")
    List<TheWordItem> all();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM TheWordItem" +
            " INNER JOIN BibleItem ON TheWordItem.bibleId = BibleItem.id" +
            " WHERE BibleItem.id = :bibleId AND TheWordItem.date = :date")
    TheWordItem byDate(int bibleId, Date date);

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM TheWordItem" +
            " INNER JOIN BibleItem ON TheWordItem.bibleId = BibleItem.id" +
            " WHERE BibleItem.id = :bibleId AND TheWordItem.date BETWEEN :from AND :to")
    List<TheWordItem> range(int bibleId, Date from, Date to);

    @Insert(onConflict = REPLACE)
    List<Long> insertOrReplace(TheWordItem... items);

    @Delete
    void delete(TheWordItem... item);

    @Query("DELETE FROM TheWordItem")
    void clear();

}
