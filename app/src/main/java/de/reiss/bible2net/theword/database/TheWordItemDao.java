package de.reiss.bible2net.theword.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.Date;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

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
