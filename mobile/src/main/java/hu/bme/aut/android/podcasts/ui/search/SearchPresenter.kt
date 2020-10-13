package hu.bme.aut.android.podcasts.ui.search

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) {

    suspend fun updateStarred(uid: String, id: String, starred: Boolean) {
        podcastInteractor.updateFavourites(uid, id, starred)
    }

}
