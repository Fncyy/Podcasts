package hu.bme.aut.android.podcasts.ui.favourites

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class FavouritesPresenter @Inject constructor() {

    suspend fun getData(): String = withIOContext {
        ""
    }

}
