package hu.bme.aut.android.podcasts.util.paging

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import hu.bme.aut.android.podcasts.util.paging.Status.FAILED
import hu.bme.aut.android.podcasts.util.paging.Status.RUNNING
import kotlinx.android.synthetic.main.item_network_state.view.*

class NetworkStateItemViewHolder(view: View, private val retryCallback: () -> Unit) :
    RecyclerView.ViewHolder(view) {
    private val progressBar = view.progress
    private val retry = view.retryButton
    private val errorMsg = view.errorText

    init {
        retry.setOnClickListener {
            retryCallback()
        }
    }

    fun bindTo(networkState: NetworkState?) {
        progressBar.visibility = toVisibility(networkState?.status == RUNNING)
        retry.visibility = toVisibility(networkState?.status == FAILED)
        errorMsg.visibility = toVisibility(networkState?.msg != null)
        errorMsg.text = networkState?.msg
    }

    companion object {
        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }
}