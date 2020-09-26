package hu.bme.aut.android.podcasts.ui.home

import android.os.Bundle
import android.view.View
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import hu.bme.aut.android.podcasts.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : RainbowCakeFragment<HomeViewState, HomeViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_home

    private lateinit var podcastsAdapter: PodcastsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO Setup views
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        podcastsAdapter = PodcastsAdapter(requireContext())
        // TODO setup listener as this
        bestPodcastsList.adapter = podcastsAdapter
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: HomeViewState) {
        when (viewState) {
            is HomeReady -> {
                podcastsAdapter.addElements(viewState.bestPodcasts.podcasts)
            }
        }
    }

}
