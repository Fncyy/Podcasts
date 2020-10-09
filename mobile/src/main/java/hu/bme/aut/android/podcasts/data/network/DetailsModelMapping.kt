package hu.bme.aut.android.podcasts.data.network

import hu.bme.aut.android.podcasts.domain.FullPodcast
import hu.bme.aut.android.podcasts.ui.details.DetailsPresenter

fun FullPodcast.toPodcast() = DetailsPresenter.Podcast(
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website,
    starred = starred
)