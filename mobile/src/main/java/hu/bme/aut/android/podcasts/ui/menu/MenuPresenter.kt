package hu.bme.aut.android.podcasts.ui.menu

import co.zsmb.rainbowcake.withIOContext
import javax.inject.Inject

class MenuPresenter @Inject constructor() {

    suspend fun getData(): String = withIOContext {
        ""
    }

}
