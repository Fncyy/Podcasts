package hu.bme.aut.android.podcasts.ui.login

import co.zsmb.rainbowcake.base.RainbowCakeViewModel
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginPresenter: LoginPresenter
) : RainbowCakeViewModel<LoginViewState>(Initial) {

    companion object {
        const val STATE_LOGIN = "login"
        const val STATE_REGISTER = "register"
    }

    fun load(logType: String) = execute {
        viewState = when (logType) {
            STATE_LOGIN -> Login
            STATE_REGISTER -> Register
            else -> Initial
        }
    }

    fun migrateData(uid: String, listener: MigrationListener) = execute {
        loginPresenter.migrateData(uid)
        listener.onMigrationComplete()
    }

    interface MigrationListener {
        fun onMigrationComplete()
    }
}
