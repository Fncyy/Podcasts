package hu.bme.aut.android.podcasts.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.android.podcasts.data.disk.entities.*

@Database(
    entities = [
        RoomBestPodcastItem::class,
        RoomFavouritePodcastItem::class,
        RoomLanguageItem::class,
        RoomRegionItem::class,
        RoomSearchPodcastItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PodcastDatabase : RoomDatabase() {
    abstract fun bestPodcastDao(): BestPodcastDao
    abstract fun favouritePodcastDao(): FavouritePodcastDao
    abstract fun languageDao(): LanguageDao
    abstract fun regionDao(): RegionDao
    abstract fun searchPodcastDao(): SearchPodcastDao
}