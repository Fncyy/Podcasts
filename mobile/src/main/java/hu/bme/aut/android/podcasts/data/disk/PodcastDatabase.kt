package hu.bme.aut.android.podcasts.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.android.podcasts.data.disk.entities.RoomBestPodcastItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomLanguageItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomRegionItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomSearchPodcastItem

@Database(
    entities = [
        RoomBestPodcastItem::class,
        RoomLanguageItem::class,
        RoomRegionItem::class,
        RoomSearchPodcastItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PodcastDatabase : RoomDatabase() {
    abstract fun bestPodcastDao(): BestPodcastDao
    abstract fun searchPodcastDao(): SearchPodcastDao
    abstract fun languageDao(): LanguageDao
    abstract fun regionDao(): RegionDao
}