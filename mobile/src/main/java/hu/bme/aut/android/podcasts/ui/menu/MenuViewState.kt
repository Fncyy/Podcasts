package hu.bme.aut.android.podcasts.ui.menu

import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData


sealed class MenuViewState

object Loading : MenuViewState()

data class LoggedIn(
    val userData: UserData,
    val availableRegions: List<Region>,
    val availableLanguages: List<Language>
) : MenuViewState()

data class LoggedOut(
    val userData: UserData,
    val availableRegions: List<Region>,
    val availableLanguages: List<Language>
) : MenuViewState()
