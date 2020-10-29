package hu.bme.aut.android.podcasts.ui.menu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import hu.bme.aut.android.podcasts.ui.login.LoginViewModel.Companion.STATE_LOGIN
import hu.bme.aut.android.podcasts.ui.login.LoginViewModel.Companion.STATE_REGISTER
import hu.bme.aut.android.podcasts.ui.menu.MenuViewModel.*
import kotlinx.android.synthetic.main.fragment_menu.*
import javax.inject.Inject

@AndroidEntryPoint
class MenuFragment : RainbowCakeFragment<MenuViewState, MenuViewModel>() {

    @Inject
    lateinit var injectedViewModel: MenuViewModel
    override fun provideViewModel() = injectedViewModel
    override fun getViewResource() = R.layout.fragment_menu

    private val regions: MutableMap<String, String> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }

    override fun onStart() {
        super.onStart()

        viewModel.load((activity as MainActivity).auth.currentUser)
    }

    override fun onStop() {
        val user = (activity as MainActivity).auth.currentUser
        val data = UserData(
            displayName = user?.displayName ?: "",
            explicitContent = explicitContentButton.isChecked,
            region = Region(
                regions[regionsText.text.toString()] ?: "",
                regionsText.text.toString()
            ),
            language = Language(languagesText.text.toString())
        )
        viewModel.updateSettings(user, data)
        super.onStop()
    }

    override fun render(viewState: MenuViewState) {
        menuFragmentRoot.children.forEach {
            it.visibility = View.GONE
        }
        when (viewState) {
            is Loading -> {
                menuProgress.visibility = View.VISIBLE
                poweredByImage.visibility = View.VISIBLE
            }
            is LoggedOut -> {
                stateLoggedOut(viewState)
            }
            is LoggedIn -> {
                stateLoggedIn(viewState)
            }
        }.exhaustive
    }

    private fun stateLoggedOut(viewState: LoggedOut) {
        profileImage.visibility = View.VISIBLE
        userNameText.visibility = View.VISIBLE
        menuScrollView.visibility = View.VISIBLE
        logButton.visibility = View.VISIBLE
        registerButton.visibility = View.VISIBLE
        poweredByImage.visibility = View.VISIBLE

        logButton.text = getString(R.string.menu_button_login)

        updateRegionsMap(viewState.availableRegions)

        viewState.userData.explicitContent?.let {
            explicitContentButton.isChecked = it
        }

        regionsText.setText(viewState.userData.region?.name)
        regionsText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableRegions.map { it.name }
            )
        )

        languagesText.setText(viewState.userData.language?.name)
        languagesText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableLanguages.map { it.name }
            )
        )

        registerButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionNavigationMenuToNavigationLoginOutRegister(
                    STATE_REGISTER
                )
            )
        }

        logButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionNavigationMenuToNavigationLoginOutRegister(
                    STATE_LOGIN
                )
            )
        }
    }

    private fun stateLoggedIn(viewState: LoggedIn) {
        profileImage.visibility = View.VISIBLE
        userNameText.visibility = View.VISIBLE
        menuScrollView.visibility = View.VISIBLE
        logButton.visibility = View.VISIBLE
        poweredByImage.visibility = View.VISIBLE

        logButton.text = getString(R.string.menu_button_logout)
        userNameText.text = viewState.userData.displayName

        updateRegionsMap(viewState.availableRegions)

        viewState.userData.explicitContent?.let {
            explicitContentButton.isChecked = it
        }

        if (viewState.userData.region != null)
            regionsText.setText(viewState.userData.region?.name)
        regionsText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableRegions.map { it.name }
            )
        )

        if (viewState.userData.language != null)
            languagesText.setText(viewState.userData.language?.name)
        languagesText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableLanguages.map { it.name }
            )
        )

        logButton.setOnClickListener {
            (activity as MainActivity).auth.let {
                it.signOut()
                viewModel.load(it.currentUser)
            }
        }
    }

    private fun updateRegionsMap(availableRegions: List<Region>) {
        regions.let {
            it.clear()
            availableRegions.forEach { region ->
                it[region.name] = region.key
            }
        }
    }

    override fun onEvent(event: OneShotEvent) {
        when (event) {
            is NewRegionEvent -> {
                regionsText.setText(event.region.name, false)
                regionsText.setSelection(regionsText.text.length)
            }
            is NewLanguageEvent -> {
                languagesText.setText(event.language.name, false)
                languagesText.setSelection(languagesText.text.length)
            }
            is NewExplicitEvent ->
                explicitContentButton.isChecked = event.explicit
        }
    }

}
