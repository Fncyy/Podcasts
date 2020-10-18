package hu.bme.aut.android.podcasts.shared.util

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
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
        private const val REGION_KEY = "region"
        private const val LANGUAGE_KEY = "language"
    }

    private val preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE)
    }

    fun getUserData(displayName: String) = UserData(
        displayName = displayName,
        explicitContent = getExplicitContent(),
        region = getRegion(),
        language = getLanguage()
    )

    fun updateUserData(userData: UserData) {
        editExplicitContent(userData.explicitContent!!)
        editRegion(userData.region!!)
        editLanguage(userData.language!!)
    }

    private fun getExplicitContent(): Boolean {
        return preferences.getBoolean(EXPLICIT_KEY, true)
    }

    private fun editExplicitContent(explicitContent: Boolean) {
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

    private fun getRegion(): Region {
        val region = preferences.getString(REGION_KEY, ",")?.split(",") ?: return Region()
        return Region(region.first(), region.last())
    }

    private fun editRegion(region: Region) {
        with(preferences.edit()) {
            putString(REGION_KEY, "${region.key},${region.name}")
            commit()
        }
    }

    private fun getLanguage(): Language {
        return Language(preferences.getString(LANGUAGE_KEY, "") ?: "")
    }

    private fun editLanguage(language: Language) {
        with(preferences.edit()) {
            putString(LANGUAGE_KEY, language.name)
            commit()
        }
    }
}