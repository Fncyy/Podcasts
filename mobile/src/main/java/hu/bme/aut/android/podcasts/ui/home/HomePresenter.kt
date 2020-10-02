package hu.bme.aut.android.podcasts.ui.home

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) {

    suspend fun getBestPodcasts(): BestPodcasts =
        podcastInteractor.getBestPodcasts(null, null, null).toBestPodcasts()

    suspend fun updateStarred(uid: String, id: String, starred: Boolean) {
        podcastInteractor.updateFavourites(uid, id, starred)
    }

    data class BestPodcasts(
        val hasNext: Boolean,
        val id: Int,
        val name: String,
        val nextPageNumber: Int,
        val podcasts: List<Podcast>
    )

    data class Podcast(
        val explicitContent: Boolean,
        val genres: String,
        val id: String,
        val publisher: String,
        val thumbnail: String,
        val title: String,
        var starred: Boolean
    )
}
