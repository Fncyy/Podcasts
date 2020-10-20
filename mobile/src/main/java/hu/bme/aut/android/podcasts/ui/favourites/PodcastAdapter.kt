package hu.bme.aut.android.podcasts.ui.favourites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.animation.PodcastSwipeActionDrawable
import hu.bme.aut.android.podcasts.util.animation.ReboundingSwipeActionCallback
import kotlinx.android.synthetic.main.item_podcast.view.*
import kotlin.math.abs

class PodcastAdapter(
    private val context: Context,
    favouriteDecoder: FavouriteDecoder
) :
    RecyclerView.Adapter<PodcastAdapter.ViewHolder>(),
    FavouriteDecoder.FavouriteListener {

    init {
        favouriteDecoder.subscribe(this)
    }

    private val podcasts: MutableList<Podcast> = mutableListOf()

    var podcastUpdateListener: PodcastUpdateListener? = null

    fun addElements(list: List<Podcast>) {
        val size = podcasts.size
        podcasts.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_podcast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(podcasts[position], position)
    }

    override fun getItemCount(): Int = podcasts.size

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        ReboundingSwipeActionCallback.ReboundableViewHolder {

        private val cardView = itemView.itemCardView
        private val frame = itemView.itemRoot
        private val title = itemView.titleText
        private val publisher = itemView.publisherText
        private val explicit = itemView.explicitImage
        private val categories = itemView.categoriesText
        private val thumbnail = itemView.thumbnailImage

        private var podcast: Podcast? = null

        private var pos = 0

        private val starredCornerSize =
            itemView.resources.getDimension(R.dimen.reply_small_component_corner_radius)

        init {
            frame.background = PodcastSwipeActionDrawable(context)
        }

        fun bind(item: Podcast, position: Int) {
            podcast = item

            pos = position

            ViewCompat.setTransitionName(cardView, item.id + "root")

            title.text = item.title
            explicit.visibility = if (item.explicitContent) View.VISIBLE else View.GONE
            publisher.text = item.publisher
            categories.text = item.genres
            Glide.with(thumbnail)
                .load(item.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(thumbnail)

            cardView.setOnClickListener {
                podcast?.let {
                    val extras = FragmentNavigatorExtras(
                        cardView to it.id + "root"
                    )
                    podcastUpdateListener?.onPodcastSelected(it.id, extras)
                }
            }
            updateCardViewTopLeftCornerSize(if (item.starred) 1F else 0F)
            frame.isActivated = item.starred
        }

        override val reboundableView: View = cardView

        override fun onReboundOffsetChanged(
            currentSwipePercentage: Float,
            swipeThreshold: Float,
            currentTargetHasMetThresholdOnce: Boolean
        ) {
            if (currentTargetHasMetThresholdOnce) return

            val isStarred = podcast?.starred ?: false

            // Animate the top left corner radius of the email card as swipe happens.
            val interpolation = (currentSwipePercentage / swipeThreshold).coerceIn(0F, 1F)
            val adjustedInterpolation = abs((if (isStarred) 1F else 0F) - interpolation)
            updateCardViewTopLeftCornerSize(adjustedInterpolation)

            // Start the background animation once the threshold is met.
            val thresholdMet = currentSwipePercentage >= swipeThreshold
            val shouldStar = when {
                thresholdMet && isStarred -> false
                thresholdMet && !isStarred -> true
                else -> return
            }
            frame.isActivated = shouldStar
        }

        override fun onRebounded() {
            podcast?.let {
                podcasts[pos].starred = !(it.starred)
                podcastUpdateListener?.onPodcastStarred(it.id, it.starred)
            }
        }

        // We have to update the shape appearance itself to have the MaterialContainerTransform pick up
        // the correct shape appearance, since it doesn't have access to the MaterialShapeDrawable
        // interpolation. If you don't need this work around, prefer using MaterialShapeDrawable's
        // interpolation property, or in the case of MaterialCardView, the progress property.
        private fun updateCardViewTopLeftCornerSize(interpolation: Float) {
            cardView.apply {
                shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                    .setTopLeftCornerSize(interpolation * starredCornerSize)
                    .build()
            }
        }
    }


    interface PodcastUpdateListener {
        fun onPodcastSelected(id: String, extras: FragmentNavigator.Extras)
        fun onPodcastStarred(id: String, starred: Boolean)
    }

    override fun onFavouriteUpdated(id: String, starred: Boolean) {
        podcasts.filter { podcast ->
            podcast.id == id
        }.run {
            forEach {
                it.starred = starred
            }
        }
        notifyDataSetChanged()
    }
}