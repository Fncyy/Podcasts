package hu.bme.aut.android.podcasts.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.ui.home.HomePresenter.Podcast
import hu.bme.aut.android.podcasts.ui.home.PodcastsAdapter.ViewHolder
import kotlinx.android.synthetic.main.item_podcast_home.view.*

class PodcastsAdapter(val context: Context) : RecyclerView.Adapter<ViewHolder>() {

    private val podcasts: MutableList<Podcast> = mutableListOf()

    fun addElements(list: List<Podcast>) {
        val size = podcasts.size
        podcasts.addAll(list)
        notifyItemRangeChanged(size, list.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_podcast_home, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(podcasts[position])
    }

    override fun getItemCount(): Int = podcasts.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView = itemView.item_card_view
        private val title = itemView.title_text
        private val publisher = itemView.publisher_text
        private val explicit = itemView.explicit_image
        private val categories = itemView.categories_text
        private val thumbnail = itemView.thumbnail_image

        private var podcast: Podcast? = null

        fun bind(item: Podcast) {
            podcast = item

            title.text = item.title
            explicit.visibility = if (item.explicitContent) View.VISIBLE else View.GONE
            publisher.text = item.publisher
            categories.text = item.genres
            Glide.with(thumbnail)
                .load(item.thumbnail)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(thumbnail)
        }
    }
}