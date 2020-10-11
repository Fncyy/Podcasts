package hu.bme.aut.android.podcasts.domain

fun FullPodcast.toPodcast() = Podcast(
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    publisher = publisher,
    thumbnail = thumbnail,
    title = title,
    starred = starred
)