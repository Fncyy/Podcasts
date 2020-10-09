package hu.bme.aut.android.podcasts.ui.menu

import android.view.View
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.navigation.fragment.findNavController
import co.zsmb.rainbowcake.base.OneShotEvent
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.extensions.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.domain.Language
import hu.bme.aut.android.podcasts.domain.Region
import hu.bme.aut.android.podcasts.domain.UserData
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

    override fun onStart() {
        super.onStart()

        viewModel.load((activity as MainActivity).auth.currentUser)
    }

    override fun onStop() {
        val user = (activity as MainActivity).auth.currentUser
        val data = UserData(
            displayName = user?.displayName ?: "",
            explicitContent = explicitContentButton.isChecked,
            regions = regionsText.allChips.map {
                Region(
                    regions[it.text.toString()] ?: "",
                    it.text.toString()
                )
            }.toMutableList(),
            languages = languagesText.allChips.map {
                Language(it.text.toString())
            }.toMutableList()
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
        explicitContentButton.setOnCheckedChangeListener { _, explicit ->
            viewModel.updateExplicitContent(
                (activity as MainActivity).auth.currentUser?.uid ?: "", explicit
            )
        }

        regionsText.setText(viewState.userData.regions!!.map { it.name })
        regionsText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableRegions.map { it.name }
            )
        )

        languagesText.setText(viewState.userData.languages!!.map { it.name })
        languagesText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableLanguages.map { it.name }
            )
        )
        // TODO update the nacho text views so they become multi line

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
        explicitContentButton.setOnCheckedChangeListener { _, explicit ->
            viewModel.updateExplicitContent(
                (activity as MainActivity).auth.currentUser?.uid ?: "", explicit
            )
        }

        if (viewState.userData.regions?.isNotEmpty() == true)
            regionsText.setText(viewState.userData.regions.map { it.name })
        regionsText.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableRegions.map { it.name }
            )
        )

        if (viewState.userData.languages?.isNotEmpty() == true)
            languagesText.setText(viewState.userData.languages.map { it.name })
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
                val current = regionsText.allChips
                val list = current.map { it.text.toString() }.toMutableList()
                list.add(event.region.name)
                regionsText.setText(list)
            }
            is NewLanguageEvent -> {
                val current = languagesText.allChips
                val list = current.map { it.text.toString() }.toMutableList()
                list.add(event.language.name)
                languagesText.setText(list)
            }
            is NewExplicitEvent ->
                explicitContentButton.isChecked = event.explicit
        }
    }

}
