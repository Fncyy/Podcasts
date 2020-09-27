package hu.bme.aut.android.podcasts.data.disk

import androidx.room.*
import hu.bme.aut.android.podcasts.data.disk.entities.RoomSearchPodcastItem
import hu.bme.aut.android.podcasts.data.disk.entities.SEARCH_PODCAST_TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchPodcastDao {

    @Query("SELECT * FROM $SEARCH_PODCAST_TABLE_NAME")
    fun getPodcasts(): Flow<List<RoomSearchPodcastItem>>

    @Query("SELECT * FROM $SEARCH_PODCAST_TABLE_NAME WHERE id = :id")
    suspend fun getPodcastById(id: String): RoomSearchPodcastItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPodcast(roomBestPodcastItem: RoomSearchPodcastItem)

    @Transaction
    suspend fun insertAllPodcasts(bestPodcasts: List<RoomSearchPodcastItem>) {
        bestPodcasts.forEach {
            insertPodcast(it)
        }
    }

    @Query("DELETE FROM $SEARCH_PODCAST_TABLE_NAME")
    suspend fun removeAllPodcasts()

    @Query("UPDATE $SEARCH_PODCAST_TABLE_NAME SET starred = :starred WHERE id = :id")
    suspend fun updateFavourite(id: String, starred: Boolean)
}