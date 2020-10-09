package hu.bme.aut.android.podcasts.ui.favourites

import android.os.Bundle
import android.view.View
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Setup views
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: FavouritesViewState) {
        // TODO Render state
    }

}
