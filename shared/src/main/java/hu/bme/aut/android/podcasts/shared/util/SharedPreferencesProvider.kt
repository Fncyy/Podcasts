package hu.bme.aut.android.podcasts.shared.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesProvider @Inject constructor(
    @ApplicationContext val context: Context
) {

    companion object {
        private const val PREFERENCE_KEY = "preferences"
        private const val EXPLICIT_KEY = "explicit"
        private const val FAVOURITES_KEY = "favourites"
    }

    private val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    fun getExplicitContent(): Boolean {
        return preferences.getBoolean(EXPLICIT_KEY, true)
    }

    fun editExplicitContent(explicitContent: Boolean) {
        with(preferences.edit()) {
            putBoolean(EXPLICIT_KEY, explicitContent)
            commit()
        }
    }

    fun getFavourites(): List<String> {
        return preferences.getString(FAVOURITES_KEY, "")?.split(", ")?.toList() ?: emptyList()
    }

    fun editFavourites(list: List<String>) {
        with(preferences.edit()) {
            putString(FAVOURITES_KEY, list.joinToString())
            commit()
        }
    }
}