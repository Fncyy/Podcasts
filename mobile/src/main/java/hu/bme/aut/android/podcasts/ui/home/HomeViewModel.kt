package hu.bme.aut.android.podcasts.ui.home

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val homePresenter: HomePresenter
) : RainbowCakeViewModel<HomeViewState>(Initial) {

    fun load() = execute {
        viewState = HomeReady(homePresenter.getData())
    }

}
