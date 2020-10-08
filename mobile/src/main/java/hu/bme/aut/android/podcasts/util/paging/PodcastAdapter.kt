package hu.bme.aut.android.podcasts.util.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hu.bme.aut.android.podcasts.PodcastsApplication
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.animation.PodcastSwipeActionDrawable
import hu.bme.aut.android.podcasts.util.animation.ReboundingSwipeActionCallback
import kotlinx.android.synthetic.main.item_podcast_home.view.*
import javax.inject.Inject
import kotlin.math.abs

class PodcastAdapter(private val context: Context) :
    PagedListAdapter<Podcast, RecyclerView.ViewHolder>(POST_COMPARATOR),
    FavouriteDecoder.FavouriteListener {

    @Inject
    lateinit var favouriteDecoder: FavouriteDecoder

    @Inject
    lateinit var bestPodcastsRepository: BestPodcastsRepository
    var podcastUpdateListener: PodcastUpdateListener? = null
    private var networkState: NetworkState? = null

    private val podcasts = mutableListOf<Podcast>()

    init {
        (context.applicationContext as PodcastsApplication).injector.inject(this)
        favouriteDecoder.subscribe(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_podcast_home -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_podcast_home, parent, false)
                PodcastViewHolder(view)
            }
            R.layout.item_network_state -> {
                val view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_network_state, parent, false)
                NetworkStateItemViewHolder(view)
            }
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_podcast_home -> (holder as PodcastViewHolder).bind(
                getItem(position),
                position
            )
            R.layout.item_network_state -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState
            )
        }
    }

    private fun hasExtraRow() =
        networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            R.layout.item_network_state
        } else {
            R.layout.item_podcast_home
        }
    }

    override fun getItemCount(): Int = super.getItemCount() + if (hasExtraRow()) 1 else 0

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    inner class PodcastViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView),
        ReboundingSwipeActionCallback.ReboundableViewHolder {
        private val cardView = itemView.item_card_view
        private val frame = itemView.item_root
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

        fun bind(item: Podcast?, position: Int) {
            podcast = item

            pos = position

            ViewCompat.setTransitionName(cardView, item?.id + "root")

            title.text = item?.title
            explicit.visibility = if (item?.explicitContent == true) View.VISIBLE else View.GONE
            publisher.text = item?.publisher
            categories.text = item?.genres
            Glide.with(thumbnail)
                .load(item?.thumbnail)
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
            updateCardViewTopLeftCornerSize(if (item?.starred == true) 1F else 0F)
            frame.isActivated = item?.starred == true
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
                currentList?.get(pos)?.starred = !(it.starred)
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

    companion object {
        val POST_COMPARATOR = object : DiffUtil.ItemCallback<Podcast>() {
            override fun areContentsTheSame(oldItem: Podcast, newItem: Podcast): Boolean =
                oldItem.id == newItem.id

            override fun areItemsTheSame(oldItem: Podcast, newItem: Podcast): Boolean =
                oldItem.id == newItem.id

            override fun getChangePayload(oldItem: Podcast, newItem: Podcast): Any? {
                return null
            }
        }
    }


    interface PodcastUpdateListener {
        fun onPodcastSelected(id: String, extras: FragmentNavigator.Extras)
        fun onPodcastStarred(id: String, starred: Boolean)
    }

    override fun onFavouriteUpdated(id: String, starred: Boolean) {
        currentList?.filter { podcast ->
            podcast.id == id
        }?.run {
            forEach {
                it.starred = starred
            }
        }
        notifyDataSetChanged()
    }
}