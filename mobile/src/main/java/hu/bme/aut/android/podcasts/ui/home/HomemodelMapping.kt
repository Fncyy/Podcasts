package hu.bme.aut.android.podcasts.ui.home

import hu.bme.aut.android.podcasts.domain.SearchResult


fun SearchResult.toBestPodcasts() = HomePresenter.BestPodcasts(
    hasNext = hasNext,
    id = id,
    name = name,
    nextPageNumber = nextPageNumber,
    podcasts = podcasts.map { podcast ->
        HomePresenter.Podcast(
            explicitContent = podcast.explicitContent,
            genres = podcast.genres,
            id = podcast.id,
            publisher = podcast.publisher,
            thumbnail = podcast.thumbnail,
            title = podcast.title
        )
    }
)