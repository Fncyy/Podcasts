package hu.bme.aut.android.podcasts.ui.home

sealed class HomeViewState

object Initial : HomeViewState()

object Loading : HomeViewState()

object HomeReady : HomeViewState()
