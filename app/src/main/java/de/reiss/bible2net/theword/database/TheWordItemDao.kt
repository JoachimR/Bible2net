package de.reiss.bible2net.theword.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomWarnings
import java.util.Date

@Dao
interface TheWordItemDao {

    @Query("SELECT * FROM TheWordItem")
    fun all(): List<TheWordItem>

    @SuppressWarnings(RoomWarnings.QUERY_MISMATCH)
    @Query(
        "SELECT * FROM TheWordItem" +
            " INNER JOIN BibleItem ON TheWordItem.bibleId = BibleItem.id" +
            " WHERE BibleItem.id = :bibleId AND TheWordItem.date = :date"
    )
    fun byDate(bibleId: Int, date: Date): TheWordItem?

    @SuppressWarnings(RoomWarnings.QUERY_MISMATCH)
    @Query(
        "SELECT * FROM TheWordItem" +
            " INNER JOIN BibleItem ON TheWordItem.bibleId = BibleItem.id" +
            " WHERE BibleItem.id = :bibleId AND TheWordItem.date BETWEEN :from AND :to"
    )
    fun range(bibleId: Int, from: Date, to: Date): List<TheWordItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(vararg items: TheWordItem): List<Long>

    @Delete
    fun delete(vararg item: TheWordItem)

    @Query("DELETE FROM TheWordItem")
    fun clear()
}
