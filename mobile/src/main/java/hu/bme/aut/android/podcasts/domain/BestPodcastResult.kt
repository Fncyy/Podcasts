package hu.bme.aut.android.podcasts.domain


data class BestPodcastResult(
    val hasNext: Boolean,
    val id: Int,
    val name: String,
    val nextPageNumber: Int,
    val podcasts: List<FullPodcast>
)

data class FullPodcast(
    val country: String,
    val description: String,
    val explicitContent: Boolean,
    val genres: String,
    val id: String,
    val listenNotesUrl: String,
    val publisher: String,
    val thumbnail: String,
    val title: String,
    val totalEpisodes: Int,
    val type: String,
    val website: String,
    var starred: Boolean
)