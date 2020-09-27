package hu.bme.aut.android.podcasts.ui.home

import hu.bme.aut.android.podcasts.domain.SearchResult
import hu.bme.aut.android.podcasts.ui.home.HomePresenter.BestPodcasts
import hu.bme.aut.android.podcasts.ui.home.HomePresenter.Podcast


fun SearchResult.toBestPodcasts() = BestPodcasts(
    hasNext = hasNext,
    id = id,
    name = name,
    nextPageNumber = nextPageNumber,
    podcasts = podcasts.map { podcast ->
        Podcast(
            explicitContent = podcast.explicitContent,
            genres = podcast.genres,
            id = podcast.id,
            publisher = podcast.publisher,
            thumbnail = podcast.thumbnail,
            title = podcast.title,
            starred = podcast.starred
        )
    }
)