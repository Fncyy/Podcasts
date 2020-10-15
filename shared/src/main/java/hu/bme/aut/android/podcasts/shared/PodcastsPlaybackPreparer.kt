package hu.bme.aut.android.podcasts.shared

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.upstream.DataSource
import hu.bme.aut.android.podcasts.shared.extensions.album
import hu.bme.aut.android.podcasts.shared.extensions.id
import hu.bme.aut.android.podcasts.shared.extensions.toMediaSource
import hu.bme.aut.android.podcasts.shared.extensions.trackNumber
import hu.bme.aut.android.podcasts.shared.library.PodcastSource

class PodcastsPlaybackPreparer(
    private val podcastSource: PodcastSource,
    private val exoPlayer: ExoPlayer,
    private val dataSourceFactory: DataSource.Factory
) : MediaSessionConnector.PlaybackPreparer {

    override fun getSupportedPrepareActions(): Long =
        PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID

    override fun onPrepare() = Unit

    override fun onPrepareFromMediaId(mediaId: String?, extras: Bundle?) {
        podcastSource.whenReady {
            val itemToPlay: MediaMetadataCompat? = podcastSource.find { item ->
                item.id == mediaId
            }
            if (itemToPlay == null) {
                Log.w(TAG, "Content not found: MediaID=$mediaId")

                // TODO: Notify caller of the error.
            } else {
                val metadataList = buildPlaylist(itemToPlay)
                val mediaSource = metadataList.toMediaSource(dataSourceFactory)

                // Since the playlist was probably based on some ordering (such as tracks
                // on an album), find which window index to play first so that the song the
                // user actually wants to hear plays first.
                val initialWindowIndex = metadataList.indexOf(itemToPlay)

                exoPlayer.prepare(mediaSource)
                exoPlayer.seekTo(initialWindowIndex, 0)
            }
        }
    }

    override fun onPrepareFromSearch(query: String?, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri?, extras: Bundle?) = Unit

    override fun onCommand(
        player: Player?,
        controlDispatcher: ControlDispatcher?,
        command: String?,
        extras: Bundle?,
        cb: ResultReceiver?
    ) = false

    private fun buildPlaylist(item: MediaMetadataCompat): List<MediaMetadataCompat> =
        podcastSource.filter { it.album == item.album }.sortedByDescending { it.trackNumber }
}

private const val TAG = "MediaSessionHelper"