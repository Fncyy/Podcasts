package hu.bme.aut.android.podcasts.data.disk

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.android.podcasts.data.disk.entities.RoomBestPodcastItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomFavouritePodcastItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomSearchPodcastItem

@Database(
    entities = [
        RoomBestPodcastItem::class,
        RoomFavouritePodcastItem::class,
        RoomSearchPodcastItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PodcastDatabase : RoomDatabase() {
    abstract fun bestPodcastDao(): BestPodcastDao
    abstract fun favouritePodcastDao(): FavouritePodcastDao
    abstract fun searchPodcastDao(): SearchPodcastDao
}