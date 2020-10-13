package hu.bme.aut.android.podcasts.util.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.domain.PodcastInteractor
import javax.inject.Inject

class SearchDataSourceFactory @Inject constructor(
    private val podcastInteractor: PodcastInteractor
) : DataSource.Factory<Int, Podcast>() {

    val sourceLiveData = MutableLiveData<SearchDataSource>()
    private lateinit var query: String

    override fun create(): DataSource<Int, Podcast> {
        val source = SearchDataSource(podcastInteractor, query)
        sourceLiveData.postValue(source)
        return source
    }

    fun init(query: String) {
        this.query = query
    }
}