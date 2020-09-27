package hu.bme.aut.android.podcasts.data.network

import dagger.Lazy
import hu.bme.aut.android.podcasts.data.network.model.BestPodcastsResponse
import hu.bme.aut.android.podcasts.data.network.model.GenresResponse
import hu.bme.aut.android.podcasts.domain.GenresResult
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.SearchResult
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.GenreDecoder

suspend fun BestPodcastsResponse.toSearchResult(
    genreDecoder: Lazy<GenreDecoder>,
    favouriteDecoder: Lazy<FavouriteDecoder>
) = SearchResult(
    id = id ?: 0,
    hasNext = has_next ?: false,
    name = name ?: "",
    nextPageNumber = next_page_number ?: 0,
    podcasts = podcasts.map { podcast ->
        Podcast(
            country = podcast.country ?: "",
            description = podcast.description ?: "",
            explicitContent = podcast.explicit_content ?: false,
            genres = genreDecoder.get().decodeKeys(podcast.genre_ids),
            id = podcast.id,
            listenNotesUrl = podcast.listennotes_url ?: "",
            publisher = podcast.publisher ?: "",
            thumbnail = podcast.thumbnail ?: "",
            title = podcast.title ?: "",
            totalEpisodes = podcast.total_episodes ?: 0,
            type = podcast.type ?: "",
            website = podcast.website ?: "",
            starred = favouriteDecoder.get().checkStarred(podcast.id)
        )
    }
)

fun GenresResponse.toGenresResult() = GenresResult(
    genres = genres.map { genre ->
        genre.id to genre.name
    }.toMap()
)