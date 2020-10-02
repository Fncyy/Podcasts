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
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : RainbowCakeFragment<HomeViewState, HomeViewModel>(),
    PodcastsAdapter.PodcastUpdateListener {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_home

    private lateinit var podcastsAdapter: PodcastsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.load()
    }

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
        podcastsAdapter.podcastUpdateListener = this
        bestPodcastsList.apply {
            val itemTouchHelper = ItemTouchHelper(ReboundingSwipeActionCallback())
            itemTouchHelper.attachToRecyclerView(this)
            adapter = podcastsAdapter
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun render(viewState: HomeViewState) {
        homeProgress.visibility = View.INVISIBLE
        bestPodcastsList.visibility = View.INVISIBLE
        when (viewState) {
            is HomeReady -> {
                bestPodcastsList.visibility = View.VISIBLE
                podcastsAdapter.addElements(viewState.bestPodcasts.podcasts)
            }
            is Loading -> {
                homeProgress.visibility = View.VISIBLE
            }
        }
    }

    override fun onPodcastSelected(id: String, extras: FragmentNavigator.Extras) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 500
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 500
        }
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToNavigationDetails(id),
            extras
        )
    }

    override fun onPodcastStarred(id: String, starred: Boolean) {
        viewModel.updateStarred((activity as MainActivity).auth.currentUser, id, starred)
    }

}
