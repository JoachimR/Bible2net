package de.reiss.bible2net.theword.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BibleItemDao {

    @Query("SELECT * FROM BibleItem")
    List<BibleItem> all();

    @Query("SELECT * FROM BibleItem WHERE bible = :bible")
    BibleItem find(String bible);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    long insert(BibleItem item);

    @Insert
    long[] insertAll(BibleItem... items);

    @Delete
    void delete(BibleItem... item);

    @Query("DELETE FROM BibleItem")
    void clear();

}
