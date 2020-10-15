package hu.bme.aut.android.podcasts.shared.library

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import hu.bme.aut.android.podcasts.shared.extensions.*
import javax.inject.Inject

/**
 * Represents a tree of media that's used by [MusicService.onLoadChildren].
 *
 * [BrowseTree] maps a media id (see: [MediaMetadataCompat.METADATA_KEY_MEDIA_ID]) to one (or
 * more) [MediaMetadataCompat] objects, which are children of that media id.
 */
class BrowseTree @Inject constructor(
    podcastSource: PodcastSource
) {
    private val mediaIdToChildren = mutableMapOf<String, MutableList<MediaMetadataCompat>>()

    init {
        val rootList = mediaIdToChildren[PODCASTS_BROWSABLE_ROOT] ?: mutableListOf()

        val favouritesMetadata = MediaMetadataCompat.Builder().apply {
            id = PODCASTS_FAVOURITES_ROOT
            title = "Favourites"
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        rootList += favouritesMetadata
        mediaIdToChildren[PODCASTS_BROWSABLE_ROOT] = rootList

        podcastSource.whenReady { successfullyInitialized ->
            if (successfullyInitialized) {
                podcastSource.sortedBy { it.album }.forEach { mediaItem ->
                    val albumMediaId = mediaItem.album
                    val albumChildren = mediaIdToChildren[albumMediaId] ?: buildAlbumRoot(mediaItem)
                    albumChildren += mediaItem
                }
            }
        }
    }

    operator fun get(mediaId: String) = mediaIdToChildren[mediaId]

    /**
     * Builds a node, under the root, that represents an album, given
     * a [MediaMetadataCompat] object that's one of the songs on that album,
     * marking the item as [MediaItem.FLAG_BROWSABLE], since it will have child
     * node(s) AKA at least 1 podcast.
     */
    private fun buildAlbumRoot(mediaItem: MediaMetadataCompat): MutableList<MediaMetadataCompat> {
        val albumMetadata = MediaMetadataCompat.Builder().apply {
            id = mediaItem.album.toString()
            title = mediaItem.displaySubtitle
            albumArtUri = mediaItem.albumArtUri.toString()
            flag = MediaBrowserCompat.MediaItem.FLAG_BROWSABLE
        }.build()

        val rootList = mediaIdToChildren[PODCASTS_FAVOURITES_ROOT] ?: mutableListOf()
        rootList += albumMetadata
        mediaIdToChildren[PODCASTS_FAVOURITES_ROOT] = rootList

        return mutableListOf<MediaMetadataCompat>().also {
            mediaIdToChildren[albumMetadata.id] = it
        }
    }
}

const val PODCASTS_BROWSABLE_ROOT = "/"
const val PODCASTS_FAVOURITES_ROOT = "favourites"
