package hu.bme.aut.android.podcasts.ui.home

sealed class HomeViewState

object Loading : HomeViewState()

object HomeReady : HomeViewState()
