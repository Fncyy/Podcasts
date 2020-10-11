package hu.bme.aut.android.podcasts.ui.favourites

import hu.bme.aut.android.podcasts.domain.Podcast

sealed class FavouritesViewState

object Loading : FavouritesViewState()

data class FavouritesReady(val podcasts: List<Podcast>) : FavouritesViewState()
