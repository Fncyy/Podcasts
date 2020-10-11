package hu.bme.aut.android.podcasts.data.disk

import androidx.room.*
import hu.bme.aut.android.podcasts.data.disk.entities.FAVOURITE_PODCAST_TABLE_NAME
import hu.bme.aut.android.podcasts.data.disk.entities.RoomFavouritePodcastItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouritePodcastDao {

    @Query("SELECT * FROM $FAVOURITE_PODCAST_TABLE_NAME")
    fun getPodcasts(): Flow<List<RoomFavouritePodcastItem>>

    @Query("SELECT * FROM $FAVOURITE_PODCAST_TABLE_NAME WHERE id = :id")
    suspend fun getPodcastById(id: String): RoomFavouritePodcastItem?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPodcast(roomFavouritePodcastItem: RoomFavouritePodcastItem)

    @Transaction
    suspend fun insertAllPodcasts(bestPodcasts: List<RoomFavouritePodcastItem>) {
        bestPodcasts.forEach {
            insertPodcast(it)
        }
    }

    @Query("DELETE FROM $FAVOURITE_PODCAST_TABLE_NAME")
    suspend fun removeAllPodcasts()

    @Query("UPDATE $FAVOURITE_PODCAST_TABLE_NAME SET starred = :starred WHERE id = :id")
    suspend fun updateFavourite(id: String, starred: Boolean)
}