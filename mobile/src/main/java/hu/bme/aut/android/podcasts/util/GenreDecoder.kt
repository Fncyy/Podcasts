package hu.bme.aut.android.podcasts.util

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GenreDecoder @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) {
    private val genres = mutableMapOf<Int, String>()

    private var initialized = false

    private suspend fun initialize() {
        val response = podcastInteractor.getGenres()
        genres.putAll(response.genres)
        initialized = true
    }

    suspend fun decodeKeys(keys: List<Int>): String {
        if (!initialized) initialize()
        // Removes the genre 'Podcast' which has the Id of '67' since they all are
        return keys.minusElement(67).map { genres[it] }.joinToString()
    }
}