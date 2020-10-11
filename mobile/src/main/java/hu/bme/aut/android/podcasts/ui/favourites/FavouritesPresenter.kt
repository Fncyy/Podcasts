package hu.bme.aut.android.podcasts.ui.favourites

import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class FavouritesPresenter @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) {

    suspend fun getFavouritePodcasts(): List<Podcast> {
        return podcastInteractor.getFavouritePodcasts()
    }

    suspend fun updateStarred(uid: String, id: String, starred: Boolean) {
        podcastInteractor.updateFavourites(uid, id, starred)
    }

}
