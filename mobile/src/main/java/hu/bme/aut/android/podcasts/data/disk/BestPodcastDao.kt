package hu.bme.aut.android.podcasts.data.disk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.Query
import androidx.room.Transaction
import hu.bme.aut.android.podcasts.data.disk.entities.BEST_PODCAST_TABLE_NAME
import hu.bme.aut.android.podcasts.data.disk.entities.RoomBestPodcastItem
import kotlinx.coroutines.flow.Flow

@Dao
interface BestPodcastDao {

    @Query("SELECT * FROM $BEST_PODCAST_TABLE_NAME")
    fun getPodcasts(): Flow<List<RoomBestPodcastItem>>

    @Query("SELECT * FROM $BEST_PODCAST_TABLE_NAME WHERE id = :id")
    suspend fun getPodcastById(id: String): RoomBestPodcastItem?

    @Insert(onConflict = IGNORE)
    suspend fun insertPodcast(roomBestPodcastItem: RoomBestPodcastItem)

    @Transaction
    suspend fun insertAllPodcasts(bestPodcasts: List<RoomBestPodcastItem>) {
        bestPodcasts.forEach {
            insertPodcast(it)
        }
    }

    @Query("DELETE FROM $BEST_PODCAST_TABLE_NAME")
    suspend fun removeAllPodcasts()

    @Query("UPDATE $BEST_PODCAST_TABLE_NAME SET starred = :starred WHERE id = :id")
    suspend fun updateFavourite(id: String, starred: Boolean)
}