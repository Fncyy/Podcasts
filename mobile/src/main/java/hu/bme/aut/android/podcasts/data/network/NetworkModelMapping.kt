package hu.bme.aut.android.podcasts.data.network

import dagger.Lazy
import hu.bme.aut.android.podcasts.data.network.model.BestPodcastsResponse
import hu.bme.aut.android.podcasts.data.network.model.GenresResponse
import hu.bme.aut.android.podcasts.data.network.model.IndividualPodcast
import hu.bme.aut.android.podcasts.data.network.model.Podcast
import hu.bme.aut.android.podcasts.domain.FullPodcast
import hu.bme.aut.android.podcasts.domain.GenresResult
import hu.bme.aut.android.podcasts.domain.SearchResult
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.GenreDecoder

suspend fun BestPodcastsResponse.toSearchResult(
    genreDecoder: Lazy<GenreDecoder>,
    favouriteDecoder: Lazy<FavouriteDecoder>
) = SearchResult(
    hasNext = has_next ?: false,
    id = id ?: 0,
    name = name ?: "",
    nextPageNumber = next_page_number ?: 0,
    podcasts = podcasts.map { it.toFullPodcast(genreDecoder, favouriteDecoder) }
)

fun GenresResponse.toGenresResult() = GenresResult(
    genres = genres.map { genre ->
        genre.id to genre.name
    }.toMap()
)

suspend fun Podcast.toFullPodcast(
    genreDecoder: Lazy<GenreDecoder>,
    favouriteDecoder: Lazy<FavouriteDecoder>
) = FullPodcast(
    country = country ?: "",
    description = description ?: "",
    explicitContent = explicit_content ?: false,
    genres = genreDecoder.get().decodeKeys(genre_ids),
    id = id,
    listenNotesUrl = listennotes_url ?: "",
    publisher = publisher ?: "",
    starred = favouriteDecoder.get().checkStarred(id),
    thumbnail = thumbnail ?: "",
    title = title ?: "",
    totalEpisodes = total_episodes ?: 0,
    type = type ?: "",
    website = website ?: ""
)

suspend fun IndividualPodcast.toFullPodcast(
    genreDecoder: Lazy<GenreDecoder>,
    favouriteDecoder: Lazy<FavouriteDecoder>
) = FullPodcast(
    country = country ?: "",
    description = description ?: "",
    explicitContent = explicit_content ?: false,
    genres = genreDecoder.get().decodeKeys(genre_ids),
    id = id ?: "",
    listenNotesUrl = listennotes_url ?: "",
    publisher = publisher ?: "",
    starred = favouriteDecoder.get().checkStarred(id ?: ""),
    thumbnail = thumbnail ?: "",
    title = title ?: "",
    totalEpisodes = total_episodes ?: 0,
    type = type ?: "",
    website = website ?: ""
)