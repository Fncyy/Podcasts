package hu.bme.aut.android.podcasts.ui.home

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.domain.UserInteractor
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import hu.bme.aut.android.podcasts.util.FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val podcastInteractor: PodcastInteractor,
    private val userInteractor: UserInteractor
) {

    suspend fun updateStarred(uid: String, id: String, starred: Boolean) {
        podcastInteractor.updateFavourites(uid, id, starred)
    }

    suspend fun getUserData(id: String, listener: FirebaseDatabaseInsertionListener): UserData {
        return userInteractor.getUserData(id, listener = listener)
    }

    data class BestPodcasts(
        val hasNext: Boolean,
        val id: Int,
        val name: String,
        val nextPageNumber: Int,
        val podcasts: List<Podcast>
    )
}
