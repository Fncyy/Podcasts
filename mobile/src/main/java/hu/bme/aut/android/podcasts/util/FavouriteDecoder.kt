package hu.bme.aut.android.podcasts.util

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteDecoder @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) {
    private var initialized = false

    private val updateListeners: MutableList<FavouriteListener> = mutableListOf()

    private val favourites: MutableList<String> = mutableListOf()

    private suspend fun initialize() {
        val response = podcastInteractor.getFavourites()
        favourites.apply {
            clear()
            addAll(response)
        }
        initialized = true
    }

    fun subscribe(listener: FavouriteListener) {
        updateListeners.add(listener)
    }

    suspend fun checkStarred(id: String): Boolean {
        if (!initialized) initialize()
        return favourites.contains(id)
    }

    fun updateStarred(id: String, starred: Boolean) {
        if (starred)
            favourites.add(id)
        else
            favourites.remove(id)
        updateListeners.forEach {
            it.onUpdated(id, starred)
        }
    }

    interface FavouriteListener {
        fun onUpdated(id: String, starred: Boolean)
    }
}