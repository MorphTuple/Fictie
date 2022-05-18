package io.morphtuple.fictie.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.morphtuple.fictie.models.PartialFic

@Dao
interface BookmarkedFicDao {
    @Query("SELECT * FROM partial_fic")
    fun getAll(): List<PartialFic>

    @Query("SELECT id FROM partial_fic WHERE id = :id")
    fun exists(id: String): String?

    @Insert
    fun insertAll(vararg users: PartialFic)

    @Delete
    fun delete(user: PartialFic)
}