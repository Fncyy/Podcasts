package hu.bme.aut.android.podcasts.ui.home

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val podcastsInteractor: PodcastInteractor
) {

    suspend fun getBestPodcasts(): BestPodcasts =
        podcastsInteractor.getBestPodcasts(null, null, null).toBestPodcasts()


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
        val title: String
    )
}
