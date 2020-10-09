package hu.bme.aut.android.podcasts.data.disk

import hu.bme.aut.android.podcasts.data.disk.entities.RoomBestPodcastItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomSearchPodcastItem
import hu.bme.aut.android.podcasts.domain.FullPodcast

fun RoomBestPodcastItem.toPodcast() = FullPodcast(
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

fun RoomSearchPodcastItem.toPodcast() = FullPodcast(
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

fun FullPodcast.toRoomBestPodcastItem() = RoomBestPodcastItem(
    id = id,
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website,
    starred = starred
)

fun FullPodcast.toRoomSearchPodcastItem() = RoomSearchPodcastItem(
    id = id,
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website,
    starred = starred
)