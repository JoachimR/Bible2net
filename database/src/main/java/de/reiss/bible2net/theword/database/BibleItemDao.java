package de.reiss.bible2net.theword.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

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
