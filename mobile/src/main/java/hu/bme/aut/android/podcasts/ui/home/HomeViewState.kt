package hu.bme.aut.android.podcasts.ui.home

sealed class HomeViewState

object Initial : HomeViewState()

object Loading : HomeViewState()

data class HomeReady(val bestPodcasts: HomePresenter.BestPodcasts) : HomeViewState()
