package hu.bme.aut.android.podcasts.util.paging

import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.ui.home.toBestPodcasts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.HttpException

class BestDataSource(
    private val podcastInteractor: PodcastInteractor
) : PodcastDataSource() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Podcast>
    ) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            try {
                podcastInteractor.removeAllBestPodcasts()
                val result = podcastInteractor.getBestPodcasts(null, null, null).toBestPodcasts()
                val next = if (result.hasNext) result.nextPageNumber else null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(result.podcasts, null, next)
            } catch (e: HttpException) {
                val error = NetworkState.error(
                    when (e.code()) {
                        429 -> "Monthly quota reached."
                        500 -> "The server cannot be reached at the moment."
                        else -> "OOPSIE WOOPSIE!! Uwu We made a fucky wucky!! A wittle fucko boingo! The code monkeys at our headquarters are working VEWY HAWD to fix this!"
                    }
                )
                networkState.postValue(error)
            } catch (e: Exception) {
                val error = NetworkState.error(e.message)
                networkState.postValue(error)
            } finally {
                retry = { loadInitial(params, callback) }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
        // Won't be loading data backwards
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Podcast>) {
        networkState.postValue(NetworkState.LOADING)

        CoroutineScope(Dispatchers.IO).async {
            try {
                val result =
                    podcastInteractor.getBestPodcasts(null, params.key, null).toBestPodcasts()
                val next = if (result.hasNext) result.nextPageNumber else null
                networkState.postValue(NetworkState.LOADED)
                callback.onResult(result.podcasts, next)
            } catch (e: Exception) {
                retry = { loadAfter(params, callback) }
                networkState.postValue(NetworkState.error("The server could not be reached."))
            }
        }
    }
}