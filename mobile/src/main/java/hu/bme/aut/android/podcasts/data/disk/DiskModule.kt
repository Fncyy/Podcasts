package hu.bme.aut.android.podcasts.data.disk

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DiskModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): PodcastDatabase =
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

    @Provides
    @Singleton
    fun provideLanguageDao(podcastDatabase: PodcastDatabase): LanguageDao =
        podcastDatabase.languageDao()

    @Provides
    @Singleton
    fun provideRegionDao(podcastDatabase: PodcastDatabase): RegionDao =
        podcastDatabase.regionDao()
}