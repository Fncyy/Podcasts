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

