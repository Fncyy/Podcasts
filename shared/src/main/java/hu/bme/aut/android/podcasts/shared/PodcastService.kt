package hu.bme.aut.android.podcasts.shared

import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat.MediaItem
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.shared.util.extensions.flag
import hu.bme.aut.android.podcasts.shared.util.library.BrowseTree
import hu.bme.aut.android.podcasts.shared.util.library.PODCASTS_BROWSABLE_ROOT
import hu.bme.aut.android.podcasts.shared.util.library.PodcastSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import javax.inject.Inject

/**
 * This class provides a MediaBrowser through a service. It exposes the media library to a browsing
 * client, through the onGetRoot and onLoadChildren methods. It also creates a MediaSession and
 * exposes it through its MediaSession.Token, which allows the client to create a MediaController
 * that connects to and send control commands to the MediaSession remotely.
 */
@AndroidEntryPoint
class PodcastService : MediaBrowserServiceCompat() {

    private var state: Int = PlaybackStateCompat.STATE_STOPPED

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var mediaController: MediaControllerCompat
    private lateinit var mediaSessionConnector: MediaSessionConnector

    @Inject
    lateinit var browseTree: BrowseTree

    @Inject
    lateinit var podcastSource: PodcastSource

    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayerFactory.newSimpleInstance(this).apply {
            setAudioAttributes(podcastsAudioArrays, false)
        }
    }

    private val podcastsAudioArrays = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_SPEECH)
        .setUsage(C.USAGE_MEDIA)
        .build()


    override fun onCreate() {
        super.onCreate()

        mediaSession = MediaSessionCompat(this, "PodcastService")
        sessionToken = mediaSession.sessionToken
        mediaController = MediaControllerCompat(this, mediaSession)

        CoroutineScope(Dispatchers.IO).async {
            podcastSource.load()
        }

        mediaSessionConnector = MediaSessionConnector(mediaSession).also { connector ->
            // Produces DataSource instances through which media data is loaded.
            val dataSourceFactory = DefaultDataSourceFactory(
                this, Util.getUserAgent(this, PODCASTS_USER_AGENT), null
            )

            // Create the PlaybackPreparer of the media session connector.
            val playbackPreparer = PodcastsPlaybackPreparer(
                podcastSource,
                exoPlayer,
                dataSourceFactory
            )

            connector.setPlayer(exoPlayer)
            connector.setPlaybackPreparer(playbackPreparer)
            connector.setQueueNavigator(PodcastsQueueNavigator(mediaSession))
        }
    }

    override fun onDestroy() {
        mediaSession.release()
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        val extras = Bundle()
        extras.putBoolean(CONTENT_STYLE_SUPPORTED, true)
        extras.putInt(CONTENT_STYLE_BROWSABLE_HINT, CONTENT_STYLE_GRID_ITEM_HINT_VALUE)
        extras.putInt(CONTENT_STYLE_PLAYABLE_HINT, CONTENT_STYLE_LIST_ITEM_HINT_VALUE)
        return BrowserRoot(PODCASTS_BROWSABLE_ROOT, extras)
    }

    override fun onLoadChildren(parentId: String, result: Result<MutableList<MediaItem>>) {
        val resultSent = podcastSource.whenReady { successfullyInitialized ->
            if (successfullyInitialized) {
                val children = browseTree[parentId]?.map {
                    MediaItem(it.description, it.flag)
                }
                result.sendResult(children?.toMutableList())
            } else {
                mediaSession.sendSessionEvent(NETWORK_FAILURE, null)
                result.sendResult(null)
            }
        }

        if (!resultSent)
            result.detach()
    }

}


/**
 * Helper class to retrieve the the Metadata necessary for the ExoPlayer MediaSession connection
 * extension to call [MediaSessionCompat.setMetadata].
 */
private class PodcastsQueueNavigator(
    mediaSession: MediaSessionCompat
) : TimelineQueueNavigator(mediaSession) {
    private val window = Timeline.Window()
    override fun getMediaDescription(player: Player, windowIndex: Int): MediaDescriptionCompat =
        player.currentTimeline
            .getWindow(windowIndex, window, true).tag as MediaDescriptionCompat
}

const val NETWORK_FAILURE = "hu.bme.aut.android.podcasts.NETWORK_FAILURE"

const val PODCASTS_USER_AGENT = "podcasts.next"
