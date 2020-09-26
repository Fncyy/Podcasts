package hu.bme.aut.android.podcasts.ui.details

sealed class DetailsViewState

object Initial : DetailsViewState()

object Loading : DetailsViewState()

data class DetailsReady(val data: String = "") : DetailsViewState()
