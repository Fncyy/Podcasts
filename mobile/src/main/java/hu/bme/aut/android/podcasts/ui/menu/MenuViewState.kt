package hu.bme.aut.android.podcasts.ui.menu

sealed class MenuViewState

object Initial : MenuViewState()

object Loading : MenuViewState()

data class MenuReady(val data: String = "") : MenuViewState()
