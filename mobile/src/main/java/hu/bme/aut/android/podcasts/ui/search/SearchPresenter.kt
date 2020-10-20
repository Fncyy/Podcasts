package hu.bme.aut.android.podcasts.ui.search

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.domain.UserInteractor
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val podcastInteractor: PodcastInteractor,
    private val userInteractor: UserInteractor
) {

    suspend fun updateStarred(uid: String, id: String, starred: Boolean) {
        podcastInteractor.updateFavourites(uid, id, starred)
    }

    suspend fun loadUserData(): UserData {
        return userInteractor.getUserData()
    }

    suspend fun getAvailableRegions(): List<Region> {
        return podcastInteractor.getAvailableRegions()
    }

    suspend fun getAvailableLanguages(): List<Language> {
        return podcastInteractor.getAvailableLanguages()
    }

}
