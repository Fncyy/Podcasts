package hu.bme.aut.android.podcasts.ui.favourites

import co.zsmb.rainbowcake.base.RainbowCakeFragment
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.R
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : RainbowCakeFragment<FavouritesViewState, FavouritesViewModel>() {

    @Inject
    lateinit var injectedViewModel: FavouritesViewModel
    override fun provideViewModel() = injectedViewModel
    override fun getViewResource() = R.layout.fragment_favourites

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: FavouritesViewState) {
        // TODO Render state
    }

}
