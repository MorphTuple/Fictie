package io.morphtuple.fictie

import android.app.Application
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import io.morphtuple.fictie.services.AO3Service

@HiltAndroidApp
class FictieApplication : Application() {
}

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {
    @Provides
    fun provideAo3Service(): AO3Service {
        return AO3Service()
    }
}