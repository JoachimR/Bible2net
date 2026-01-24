package de.reiss.bible2net.theword.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BibleItemDao {

    @Query("SELECT * FROM BibleItem")
    fun all(): List<BibleItem>

    @Query("SELECT * FROM BibleItem WHERE bible = :bible")
    fun find(bible: String): BibleItem?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(item: BibleItem): Long

    @Insert
    fun insertAll(vararg items: BibleItem): LongArray

    @Delete
    fun delete(vararg item: BibleItem)

    @Query("DELETE FROM BibleItem")
    fun clear()
}
