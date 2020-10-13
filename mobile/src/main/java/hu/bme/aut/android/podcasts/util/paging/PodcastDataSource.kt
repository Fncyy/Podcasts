package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import hu.bme.aut.android.podcasts.domain.Podcast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

abstract class PodcastDataSource : PageKeyedDataSource<Int, Podcast>() {

    protected var retry: (() -> Any)? = null
    val networkState = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            CoroutineScope(Dispatchers.IO).async {
                it.invoke()
            }
        }
    }
}