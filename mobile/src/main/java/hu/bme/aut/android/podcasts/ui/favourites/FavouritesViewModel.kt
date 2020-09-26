package hu.bme.aut.android.podcasts.ui.favourites

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class FavouritesViewModel @Inject constructor(
    private val favouritesPresenter: FavouritesPresenter
) : RainbowCakeViewModel<FavouritesViewState>(Initial) {

    fun load() = execute {
        viewState = FavouritesReady(favouritesPresenter.getData())
    }

}
