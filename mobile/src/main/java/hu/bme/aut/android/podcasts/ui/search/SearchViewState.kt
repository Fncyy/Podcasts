package hu.bme.aut.android.podcasts.ui.search

sealed class SearchViewState

object Initial : SearchViewState()

object Loading : SearchViewState()

object SearchReady : SearchViewState()
