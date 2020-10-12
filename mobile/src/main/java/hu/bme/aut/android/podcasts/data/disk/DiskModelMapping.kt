package hu.bme.aut.android.podcasts.data.disk

import hu.bme.aut.android.podcasts.data.disk.entities.RoomBestPodcastItem
import hu.bme.aut.android.podcasts.data.disk.entities.RoomFavouritePodcastItem
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
    starred = starred,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website
)

fun RoomSearchPodcastItem.toPodcast() = FullPodcast(
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    starred = starred,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website
)

fun RoomFavouritePodcastItem.toPodcast() = FullPodcast(
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    starred = starred,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website
)

fun FullPodcast.toRoomBestPodcastItem() = RoomBestPodcastItem(
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    starred = starred,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website
)

fun FullPodcast.toRoomSearchPodcastItem() = RoomSearchPodcastItem(
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    starred = starred,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website
)

fun FullPodcast.toRoomFavouritePodcastItem() = RoomFavouritePodcastItem(
    country = country,
    description = description,
    explicitContent = explicitContent,
    genres = genres,
    id = id,
    listenNotesUrl = listenNotesUrl,
    publisher = publisher,
    starred = starred,
    thumbnail = thumbnail,
    title = title,
    totalEpisodes = totalEpisodes,
    type = type,
    website = website
)

