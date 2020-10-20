package hu.bme.aut.android.podcasts.ui.home

import hu.bme.aut.android.podcasts.domain.model.BestPodcastResult
import hu.bme.aut.android.podcasts.domain.toPodcast
import hu.bme.aut.android.podcasts.ui.home.HomePresenter.BestPodcasts


fun BestPodcastResult.toBestPodcasts() = BestPodcasts(
    hasNext = hasNext,
    id = id,
    name = name,
    nextPageNumber = nextPageNumber,
    podcasts = podcasts.map { it.toPodcast() }
)