package hu.bme.aut.android.podcasts.ui.details

import hu.bme.aut.android.podcasts.ui.details.DetailsPresenter.Podcast

sealed class DetailsViewState

object Initial : DetailsViewState()

object Loading : DetailsViewState()

data class DetailsReady(val podcast: Podcast) : DetailsViewState()
