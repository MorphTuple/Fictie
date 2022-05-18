package io.morphtuple.fictie

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.morphtuple.fictie.dao.BookmarkedFicDao
import io.morphtuple.fictie.services.AO3Service
import javax.inject.Singleton

@HiltAndroidApp
class FictieApplication : Application() {
}

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {
    @Provides
    fun provideAo3Service(bookmarkedFicDao: BookmarkedFicDao): AO3Service {
        return AO3Service(bookmarkedFicDao)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "fictie"
        ).build()
    }

    @Provides
    fun provideBookmarkedFicDao(appDatabase: AppDatabase): BookmarkedFicDao {
        return appDatabase.bookmarkedFicDao()
    }
}