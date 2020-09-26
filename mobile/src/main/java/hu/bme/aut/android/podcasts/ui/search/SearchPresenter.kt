package hu.bme.aut.android.podcasts.ui.search

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class SearchPresenter @Inject constructor() {

    suspend fun getData(): String = withIOContext {
        ""
    }

}
