package hu.bme.aut.android.podcasts.ui.search

import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData

sealed class SearchViewState

object Loading : SearchViewState()

data class Initialized(
    val userData: UserData,
    val availableRegions: List<Region>,
    val availableLanguages: List<Language>
) : SearchViewState()
