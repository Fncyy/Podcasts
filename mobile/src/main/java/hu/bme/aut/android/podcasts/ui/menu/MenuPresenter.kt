package hu.bme.aut.android.podcasts.ui.menu

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.domain.UserInteractor
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import hu.bme.aut.android.podcasts.util.FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener
import javax.inject.Inject

class MenuPresenter @Inject constructor(
    private val userInteractor: UserInteractor,
    private val podcastInteractor: PodcastInteractor
) {

    suspend fun getUserData(
        id: String,
        displayName: String,
        listener: FirebaseDatabaseInsertionListener
    ): UserData {
        return userInteractor.getUserData(id, displayName, listener)
    }

    suspend fun updateSettings(id: String, data: UserData) {
        userInteractor.updateUserData(id, data)
    }

    suspend fun getAvailableRegions(): List<Region> {
        return podcastInteractor.getAvailableRegions()
    }

    suspend fun getAvailableLanguages(): List<Language> {
        return podcastInteractor.getAvailableLanguages()
    }
}
