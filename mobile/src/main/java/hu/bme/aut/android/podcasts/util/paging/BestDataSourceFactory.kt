package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class BestDataSourceFactory @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) : DataSource.Factory<Int, Podcast>() {
    val sourceLiveData = MutableLiveData<BestDataSource>()
    override fun create(): DataSource<Int, Podcast> {
        val source = BestDataSource(podcastInteractor)
        sourceLiveData.postValue(source)
        return source
    }
}