package io.morphtuple.fictie

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.morphtuple.fictie.common.JsonTypeConverter
import io.morphtuple.fictie.dao.BookmarkedFicDao
import io.morphtuple.fictie.models.PartialFic

@Database(entities = [PartialFic::class], version = 1)
@TypeConverters(JsonTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookmarkedFicDao(): BookmarkedFicDao
}
