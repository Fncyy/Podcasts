package hu.bme.aut.android.podcasts.ui.menu

import hu.bme.aut.android.podcasts.domain.*
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

    suspend fun updateExplicitContent(id: String, explicit: Boolean) {
        userInteractor.updateExplicitContent(id, explicit)
    }

    suspend fun getRegions(id: String, listener: FirebaseDatabaseInsertionListener): List<Region> {
        return userInteractor.getRegions(id, listener)
    }

    suspend fun getAvailableRegions(): List<Region> {
        return podcastInteractor.getAvailableRegions()
    }

    suspend fun getLanguages(
        id: String,
        listener: FirebaseDatabaseInsertionListener
    ): List<Language> {
        return userInteractor.getLanguages(id, listener)
    }

    suspend fun getAvailableLanguages(): List<Language> {
        return podcastInteractor.getAvailableLanguages()
    }
}
