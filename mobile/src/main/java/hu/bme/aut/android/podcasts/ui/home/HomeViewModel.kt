package hu.bme.aut.android.podcasts.ui.home

import android.util.Log
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homePresenter: HomePresenter
) : RainbowCakeViewModel<HomeViewState>(Initial) {

    fun load() = execute {
        val result = homePresenter.getBestPodcasts()
        Log.d("result", result.toString())
        viewState = HomeReady(result)
    }

}
