package hu.bme.aut.android.podcasts.ui.details

import hu.bme.aut.android.podcasts.domain.FullPodcast
import hu.bme.aut.android.podcasts.ui.details.DetailsPresenter.Podcast

fun FullPodcast.toPodcast() = Podcast(
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