package hu.bme.aut.android.podcasts.ui.home

import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.SearchResult
import hu.bme.aut.android.podcasts.ui.home.HomePresenter.BestPodcasts


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

fun hu.bme.aut.android.podcasts.domain.FullPodcast.toPodcast() = Podcast(
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    publisher = publisher,
    thumbnail = thumbnail,
    title = title,
    starred = starred
)