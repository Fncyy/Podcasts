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
        val string =
            "7f8f49e8e35346b5a86c907fd6e163ca, c226968c428c40ed87c25853a93d0a37, 100750a58b4e4269ad734d71f39dda3e, b4799cfd263b49cfa1c8358bf58516e4, 936b6d0fc9284041bfaa53508c460ab6, c58b00235fc14d4b8c38bde156f69327"
        return string.split(", ").toList()
//        return preferences.getString(FAVOURITES_KEY, "")?.split(", ")?.toList() ?: emptyList()
    }

    fun editFavourites(list: List<String>) {
        with(preferences.edit()) {
            putString(FAVOURITES_KEY, list.joinToString())
            commit()
        }
    }
}