package hu.bme.aut.android.podcasts.util

import android.util.Log
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

    private val updateListeners: MutableList<FavouriteListener> = mutableListOf()

    private val favourites: MutableList<String> = mutableListOf()

    private fun initialize() {
        val response =
            userInteractor.getFavourites(FirebaseAuth.getInstance().currentUser?.uid ?: "", this)
        favourites.apply {
            clear()
            addAll(response)
        }
    }

    fun subscribe(listener: FavouriteListener) {
        updateListeners.add(listener)
    }

    fun checkStarred(id: String): Boolean {
        initialize()
        return favourites.contains(id)
    }

    fun updateStarred(userId: String, podcastId: String, starred: Boolean) {
        if (starred) {
            if (!favourites.contains(podcastId))
                favourites.add(podcastId)
        } else
            favourites.remove(podcastId)
        Log.d("Firebase", "Decoder uploading: $favourites")
        sharedPreferencesProvider.editFavourites(favourites)
        if (userId.isNotEmpty())
            firebaseDatabaseAccessor.updateFavourites(userId, favourites)

        updateListeners.forEach {
            it.onFavouriteUpdated(podcastId, starred)
        }
    }

    override fun onFavouriteAdded(id: String) {
        if (!favourites.contains(id))
            favourites.add(id)
        updateListeners.forEach { it.onFavouriteUpdated(id, true) }
    }

    interface FavouriteListener {
        fun onFavouriteUpdated(id: String, starred: Boolean)
    }
}