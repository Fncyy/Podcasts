package hu.bme.aut.android.podcasts.domain

import hu.bme.aut.android.podcasts.domain.model.FullPodcast
import hu.bme.aut.android.podcasts.domain.model.Podcast

fun FullPodcast.toPodcast() = Podcast(
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    publisher = publisher,
    thumbnail = thumbnail,
    title = title,
    starred = starred
)

fun Podcast.toFullPodcast() = FullPodcast(
    country = "",
    description = "",
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    listenNotesUrl = "",
    publisher = publisher,
    starred = starred,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = 0,
    type = "",
    website = ""
)