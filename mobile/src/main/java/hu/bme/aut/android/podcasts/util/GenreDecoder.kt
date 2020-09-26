package hu.bme.aut.android.podcasts.util

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreDecoder @Inject constructor(
    private val podcastsInteractor: PodcastInteractor
) {
    private val genres = mutableMapOf<Int, String>()

    private var initialized = false

    private suspend fun initialize() {
        val response = podcastsInteractor.getGenres()
        genres.putAll(response.genres)
        initialized = true
    }

    suspend fun decodeKeys(keys: List<Int>): String {
        if (!initialized) initialize()
        // Removes the genre 'Podcast' which has the Id of '67' since they all are
        return keys.minus(67).map {
            genres[it]
        }.toList().joinToString()
    }
}