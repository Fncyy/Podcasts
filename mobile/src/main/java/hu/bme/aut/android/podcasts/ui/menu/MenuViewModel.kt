package hu.bme.aut.android.podcasts.ui.menu

import android.util.Log
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.android.podcasts.data.network.FirebaseDatabaseAccessor
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import javax.inject.Inject

class MenuViewModel @Inject constructor(
    private val menuPresenter: MenuPresenter
) : RainbowCakeViewModel<MenuViewState>(Loading),
    FirebaseDatabaseAccessor.FirebaseDatabaseInsertionListener {

    class NewRegionEvent(val region: Region) : OneShotEvent
    class NewLanguageEvent(val language: Language) : OneShotEvent
    class NewExplicitEvent(val explicit: Boolean) : OneShotEvent

    fun load(user: FirebaseUser?) = execute {
        viewState = if (user == null) {
            val userId = ""
            LoggedOut(
                menuPresenter.getUserData(userId, "Podcasts", this),
                menuPresenter.getAvailableRegions(),
                menuPresenter.getAvailableLanguages()
            )
        } else {
            val userId = user.uid
            LoggedIn(
                menuPresenter.getUserData(userId, user.displayName ?: "Podcasts", this),
                menuPresenter.getAvailableRegions(),
                menuPresenter.getAvailableLanguages()
            )
        }
    }

    fun updateSettings(user: FirebaseUser?, data: UserData) = execute {
        Log.d("upload", user?.uid ?: "empty")
        menuPresenter.updateSettings(user?.uid ?: "", data)
    }

    override fun onRegionChanged(region: Region) {
        postEvent(NewRegionEvent(region))
    }

    override fun onLanguageChanged(language: Language) {
        postEvent(NewLanguageEvent(language))
    }

    override fun onExplicitChanged(explicit: Boolean) {
        postEvent((NewExplicitEvent(explicit)))
    }
}
