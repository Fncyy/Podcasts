package hu.bme.aut.android.podcasts.data.disk

import hu.bme.aut.android.podcasts.data.disk.entities.RoomBestPodcastItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomSearchPodcastItem
import hu.bme.aut.android.podcasts.domain.Podcast

fun RoomBestPodcastItem.toPodcast() = Podcast(
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

fun RoomSearchPodcastItem.toPodcast() = Podcast(
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

fun Podcast.toRoomBestPodcastItem() = RoomBestPodcastItem(
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

fun Podcast.toRoomSearchPodcastItem() = RoomSearchPodcastItem(
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