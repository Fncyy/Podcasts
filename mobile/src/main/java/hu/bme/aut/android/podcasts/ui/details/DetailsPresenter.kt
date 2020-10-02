package hu.bme.aut.android.podcasts.ui.details

import hu.bme.aut.android.podcasts.data.network.toPodcast
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class DetailsPresenter @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) {
    suspend fun getPodcast(id: String): Podcast? {
        return podcastInteractor.getBestPodcastById(id)?.toPodcast()
    }

    suspend fun updateStarred(uid: String, id: String, starred: Boolean) {
        podcastInteractor.updateFavourites(uid, id, starred)
    }

    data class Podcast(
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
}
