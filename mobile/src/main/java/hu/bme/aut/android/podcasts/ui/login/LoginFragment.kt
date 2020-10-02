package hu.bme.aut.android.podcasts.ui.login

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import co.zsmb.rainbowcake.extensions.exhaustive
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.UserProfileChangeRequest
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : RainbowCakeFragment<LoginViewState, LoginViewModel>(),
    LoginViewModel.MigrationListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_login

    private val args: LoginFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Setup views
    }

    override fun onStart() {
        super.onStart()

        viewModel.load(args.logType)
    }

    override fun render(viewState: LoginViewState) {
        loginFragmentRoot.children.forEach { it.visibility = View.GONE }
        when (viewState) {
            Initial -> {
            }
            Login -> {
                stateLogin()
            }
            Register -> {
                stateRegister()
            }
        }.exhaustive
    }

    private fun stateLogin() {
        emailInputLayout.visibility = View.VISIBLE
        passwordInputLayout.visibility = View.VISIBLE
        loginRegisterButton.visibility = View.VISIBLE

        loginRegisterButton.text = getString(R.string.menu_button_login)
        loginRegisterButton.setOnClickListener {
            loginRegisterButton.visibility = View.INVISIBLE
            statusProgressBar.visibility = View.VISIBLE
            (activity as MainActivity).auth.signInWithEmailAndPassword(
                emailInput.text.toString(),
                passwordInput.text.toString()
            ).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().popBackStack()
                } else {
                    Snackbar.make(
                        loginFragmentRoot,
                        "Login was not successful!",
                        Snackbar.LENGTH_SHORT
                    )
                    loginRegisterButton.visibility = View.VISIBLE
                    statusProgressBar.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun stateRegister() {
        userNameInputLayout.visibility = View.VISIBLE
        emailInputLayout.visibility = View.VISIBLE
        passwordInputLayout.visibility = View.VISIBLE
        passwordAgainInputLayout.visibility = View.VISIBLE
        loginRegisterButton.visibility = View.VISIBLE

        loginRegisterButton.text = getString(R.string.menu_button_register)
        loginRegisterButton.setOnClickListener {
            if (registerValidity()) {
                loginRegisterButton.visibility = View.INVISIBLE
                statusProgressBar.visibility = View.VISIBLE
                (activity as MainActivity).auth.createUserWithEmailAndPassword(
                    emailInput.text.toString(),
                    passwordInput.text.toString()
                ).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val request = UserProfileChangeRequest.Builder().apply {
                            displayName = userNameInput.text.toString()
                        }.build()
                        task.result?.user?.let {
                            it.updateProfile(request)
                            Snackbar.make(
                                loginFragmentRoot,
                                "Your settings are currently being uploaded to the cloud...",
                                Snackbar.LENGTH_INDEFINITE
                            )
                            viewModel.migrateData(it.uid, this)
                        }
                    } else {
                        Snackbar.make(
                            loginFragmentRoot,
                            "Registration was not successful!",
                            Snackbar.LENGTH_SHORT
                        )
                        loginRegisterButton.visibility = View.VISIBLE
                        statusProgressBar.visibility = View.INVISIBLE
                    }
                }
            }

        }
    }

    private fun registerValidity(): Boolean {
        return userNameInput.validate() &&
                emailInput.validate() &&
                passwordInput.validate() &&
                passwordAgainInput.validate() &&
                passwordMatchValidity()
    }

    private fun passwordMatchValidity(): Boolean {
        return if (passwordInput.text.toString() != passwordAgainInput.text.toString()) {
            passwordAgainInput.error = "Passwords don't match!"
            false
        } else true
    }

    private fun TextInputEditText.validate(): Boolean {
        return if (text.isNullOrEmpty()) {
            error = "This field is required!"
            false
        } else true
    }

    override fun onMigrationComplete() {
        findNavController().popBackStack()
    }
}


