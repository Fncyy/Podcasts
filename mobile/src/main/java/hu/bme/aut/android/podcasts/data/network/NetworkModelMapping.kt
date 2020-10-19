package hu.bme.aut.android.podcasts.data.network

import dagger.Lazy
import hu.bme.aut.android.podcasts.domain.model.BestPodcastResult
import hu.bme.aut.android.podcasts.domain.model.FullPodcast
import hu.bme.aut.android.podcasts.domain.model.GenresResult
import hu.bme.aut.android.podcasts.domain.model.SearchResult
import hu.bme.aut.android.podcasts.shared.data.network.model.*
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.GenreDecoder
import hu.bme.aut.android.podcasts.domain.model.Podcast as DomainPodcast

suspend fun BestPodcastsResponse.toBestPodcastResult(
    genreDecoder: Lazy<GenreDecoder>,
    favouriteDecoder: Lazy<FavouriteDecoder>
) = BestPodcastResult(
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
): FullPodcast {
    return FullPodcast(
        country = country ?: "",
        description = description ?: "",
        explicitContent = explicit_content ?: false,
        genres = genre_ids?.let { genreDecoder.get().decodeKeys(it) } ?: "Genre not specified",
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
}

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

suspend fun SearchResponse.toSearchResult(
    genreDecoder: Lazy<GenreDecoder>,
    favouriteDecoder: Lazy<FavouriteDecoder>
) = SearchResult(
    nextOffset = next_offset ?: 0,
    podcasts = results.map { it.toPodcast(genreDecoder, favouriteDecoder) },
    total = total ?: 0
)

suspend fun hu.bme.aut.android.podcasts.shared.data.network.model.Result.toPodcast(
    genreDecoder: Lazy<GenreDecoder>,
    favouriteDecoder: Lazy<FavouriteDecoder>
) = DomainPodcast(
    explicitContent = explicit_content ?: false,
    genres = genreDecoder.get().decodeKeys(genre_ids),
    id = id ?: "",
    publisher = publisher_original ?: "",
    thumbnail = thumbnail ?: "",
    title = title_original ?: "",
    starred = favouriteDecoder.get().checkStarred(id ?: "")
)