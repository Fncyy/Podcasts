package hu.bme.aut.android.podcasts.ui.login

sealed class LoginViewState

object Initial : LoginViewState()

object Login : LoginViewState()

object Register : LoginViewState()
