package hu.bme.aut.android.podcasts.ui.home

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.google.android.material.transition.MaterialElevationScale
import hu.bme.aut.android.podcasts.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : RainbowCakeFragment<HomeViewState, HomeViewModel>(),
    PodcastsAdapter.PodcastSelectionListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_home

    private lateinit var podcastsAdapter: PodcastsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        // TODO Setup views
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        podcastsAdapter = PodcastsAdapter(requireContext())
        podcastsAdapter.podcastSelectionListener = this
        bestPodcastsList.apply {
            val itemTouchHelper = ItemTouchHelper(ReboundingSwipeActionCallback())
            itemTouchHelper.attachToRecyclerView(this)
            adapter = podcastsAdapter
        }
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

    override fun onPodcastSelected(id: String, extras: FragmentNavigator.Extras) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300
        }
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToNavigationDetails(id),
            extras
        )
    }

}
