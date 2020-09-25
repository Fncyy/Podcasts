package hu.bme.aut.android.podcasts.ui.favourites

sealed class FavouritesViewState

object Initial : FavouritesViewState()

object Loading : FavouritesViewState()

data class FavouritesReady(val data: String = "") : FavouritesViewState()
