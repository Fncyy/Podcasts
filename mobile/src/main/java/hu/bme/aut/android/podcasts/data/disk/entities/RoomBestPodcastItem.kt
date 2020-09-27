package hu.bme.aut.android.podcasts.data.disk.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val BEST_PODCAST_TABLE_NAME = "BestPodcastItems"

@Entity(tableName = BEST_PODCAST_TABLE_NAME)
class RoomBestPodcastItem(
    @PrimaryKey
    val id: String,
    val country: String,
    val description: String,
    val explicitContent: Boolean,
    val genres: String,
    val listenNotesUrl: String,
    val publisher: String,
    val thumbnail: String,
    val title: String,
    val totalEpisodes: Int,
    val type: String,
    val website: String,
    val starred: Boolean
)