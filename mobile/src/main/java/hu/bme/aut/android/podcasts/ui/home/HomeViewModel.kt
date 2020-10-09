package hu.bme.aut.android.podcasts.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.switchMap
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.podcasts.domain.Podcast
import hu.bme.aut.android.podcasts.util.paging.BestPodcastsRepository
import hu.bme.aut.android.podcasts.util.paging.Listing
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homePresenter: HomePresenter,
    bestPodcastsRepository: BestPodcastsRepository
) : RainbowCakeViewModel<HomeViewState>(Loading) {

    private val podcastsList = MutableLiveData<Listing<Podcast>>()
    val podcasts = switchMap(podcastsList) { it.pagedList }
    var networkState = switchMap(podcastsList) { it.networkState }

    init {
        podcastsList.value = bestPodcastsRepository.getPagedPodcasts()
        viewState = HomeReady
    }

    fun updateStarred(user: FirebaseUser?, id: String, starred: Boolean) = execute {
        val uid = user?.uid ?: ""
        homePresenter.updateStarred(uid, id, starred)
    }

    fun retry() {
        val listing = podcastsList.value
        listing?.retry?.invoke()
    }
}
