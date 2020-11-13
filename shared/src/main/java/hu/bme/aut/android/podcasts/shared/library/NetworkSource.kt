package hu.bme.aut.android.podcasts.shared.library

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import hu.bme.aut.android.podcasts.shared.R
import hu.bme.aut.android.podcasts.shared.data.disk.SharedPreferencesProvider
import hu.bme.aut.android.podcasts.shared.data.network.ListenNotesAPI
import hu.bme.aut.android.podcasts.shared.util.extensions.*
import java.io.File
import java.util.concurrent.TimeUnit

class NetworkSource(
    context: Context,
    private val sharedPreferencesProvider: SharedPreferencesProvider,
    private val listenNotesAPI: ListenNotesAPI
) : AbstractPodcastSource() {
    private var catalog: List<MediaMetadataCompat> = emptyList()
    private val glide: RequestManager

    init {
        state = STATE_INITIALIZING
        glide = Glide.with(context)
    }

    override fun iterator(): Iterator<MediaMetadataCompat> = catalog.iterator()

    override suspend fun load() {
        updateCatalog()?.let { updatedCatalog ->
            catalog = updatedCatalog
            state = STATE_INITIALIZED
        } ?: run {
            catalog = emptyList()
            state = STATE_ERROR
        }
    }

    private suspend fun updateCatalog(): List<MediaMetadataCompat>? {
        val favourites = sharedPreferencesProvider.getFavourites()
        val updatedCatalog = mutableListOf<MediaMetadataCompat>()
        favourites.forEach { favourite ->
            val podcast = listenNotesAPI.getPodcast(favourite)
            podcast.episodes.forEach { episode ->
                val artFile = glide.applyDefaultRequestOptions(glideOptions)
                    .downloadOnly()
                    .load(episode.image)
                    .submit(144, 144)
                    .get()

                val artUri = artFile.asAlbumArtContentUri()

                Log.d("DisplayIcon", Uri.decode(episode.image))

                val media = MediaMetadataCompat.Builder()
                    .apply {
                        val durationMs =
                            TimeUnit.SECONDS.toMillis(episode.audio_length_sec.toLong())

                        id = episode.id
                        title = episode.title
                        artist = podcast.publisher
                        album = podcast.id
                        duration = durationMs
                        genre = podcast.genre_ids.toString()
                        mediaUri = episode.audio
                        albumArtUri = Uri.decode(episode.image)
                        trackNumber = episode.pub_date_ms
                        flag = MediaBrowserCompat.MediaItem.FLAG_PLAYABLE

                        displayTitle = episode.title
                        displaySubtitle = podcast.title
                        displayDescription = episode.description
                        displayIconUri = Uri.decode(episode.image)
                    }
                    .build()

                updatedCatalog.add(media)
            }
        }
        updatedCatalog.forEach { it.description.extras?.putAll(it.bundle) }
        return updatedCatalog
    }

    override fun search(query: String, extras: Bundle): List<MediaMetadataCompat> {
        TODO("not implemented")
    }
}

private fun File.asAlbumArtContentUri(): Uri {
    return Uri.Builder()
        .scheme(ContentResolver.SCHEME_CONTENT)
        .appendPath(this.path)
        .build()
}

private val glideOptions = RequestOptions()
    .fallback(R.mipmap.ic_launcher)
    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)