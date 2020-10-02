package hu.bme.aut.android.podcasts.util

import com.google.firebase.auth.FirebaseAuth
import hu.bme.aut.android.podcasts.domain.UserInteractor
import hu.bme.aut.android.podcasts.util.FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteDecoder @Inject constructor(
    private val userInteractor: UserInteractor,
    private val sharedPreferencesProvider: SharedPreferencesProvider,
    private val firebaseDatabaseAccessor: FirebaseDatabaseAccessor
) : FirebaseDatabaseInsertionListener {
    private var initialized = false

    private val updateListeners: MutableList<FavouriteListener> = mutableListOf()

    private val favourites: MutableList<String> = mutableListOf()

    private suspend fun initialize() {
        val response =
            userInteractor.getFavourites(FirebaseAuth.getInstance().currentUser?.uid ?: "", this)
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

    fun updateStarred(userId: String, podcastId: String, starred: Boolean) {
        if (starred)
            favourites.add(podcastId)
        else
            favourites.remove(podcastId)

        sharedPreferencesProvider.editFavourites(favourites)
        if (userId.isNotEmpty())
            firebaseDatabaseAccessor.updateFavourites(userId, favourites)

        updateListeners.forEach {
            it.onUpdated(podcastId, starred)
        }
    }

    override fun onFavouriteAdded(id: String) {
        favourites.add(id)
        updateListeners.forEach { it.onUpdated(id, true) }
    }

    interface FavouriteListener {
        fun onUpdated(id: String, starred: Boolean)
    }
}