package hu.bme.aut.android.podcasts.data.disk

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DiskModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): PodcastDatabase =
        Room.databaseBuilder(
            context,
            PodcastDatabase::class.java,
            "podcasts.db"
        ).build()

    @Provides
    @Singleton
    fun provideBestPodcastDao(podcastDatabase: PodcastDatabase): BestPodcastDao =
        podcastDatabase.bestPodcastDao()

    @Provides
    @Singleton
    fun provideSearchPodcastDao(podcastDatabase: PodcastDatabase): SearchPodcastDao =
        podcastDatabase.searchPodcastDao()
}