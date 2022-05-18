package io.morphtuple.fictie.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.morphtuple.fictie.models.PartialFic

// TODO store full fic instead of partial
@Dao
interface BookmarkedFicDao {
    @Query("SELECT * FROM partial_fic")
    fun getAll(): List<PartialFic>

    @Query("SELECT id FROM partial_fic WHERE id = :id")
    fun exists(id: String): String?

    @Query("SELECT * FROM partial_fic")
    fun loadAllPartialFic(): LiveData<List<PartialFic>>

    @Insert
    fun insertAll(vararg users: PartialFic)

    @Delete
    fun delete(user: PartialFic)
}