package hu.bme.aut.android.podcasts.ui.menu

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val menuPresenter: MenuPresenter
) : RainbowCakeViewModel<MenuViewState>(Initial) {

    fun load() = execute {
        viewState = MenuReady(menuPresenter.getData())
    }

}
