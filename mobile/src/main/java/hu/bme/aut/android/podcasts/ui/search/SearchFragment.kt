package hu.bme.aut.android.podcasts.ui.search

import co.zsmb.rainbowcake.base.RainbowCakeFragment
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.R
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : RainbowCakeFragment<SearchViewState, SearchViewModel>() {

    @Inject
    lateinit var injectedViewModel: SearchViewModel
    override fun provideViewModel() = injectedViewModel
    override fun getViewResource() = R.layout.fragment_search

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: SearchViewState) {
        // TODO Render state
    }

}
