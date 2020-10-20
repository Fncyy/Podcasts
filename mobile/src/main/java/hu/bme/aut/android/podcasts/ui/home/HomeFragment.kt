package hu.bme.aut.android.podcasts.ui.home

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.animation.ReboundingSwipeActionCallback
import hu.bme.aut.android.podcasts.util.paging.PodcastAdapter
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment :
    RainbowCakeFragment<HomeViewState, HomeViewModel>(),
    PodcastAdapter.PodcastUpdateListener {

    @Inject
    lateinit var injectedViewModel: HomeViewModel
    override fun provideViewModel() = injectedViewModel
    override fun getViewResource() = R.layout.fragment_home

    @Inject
    lateinit var favouriteDecoder: FavouriteDecoder
    private lateinit var podcastAdapter: PodcastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
            enterTransition = MaterialFadeThrough()
            exitTransition = MaterialFadeThrough()
        }

        // TODO Setup views
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        podcastAdapter = PodcastAdapter(requireContext(), favouriteDecoder) {
            viewModel.retry()
        }
        podcastAdapter.podcastUpdateListener = this
        bestPodcastsList.apply {
            val itemTouchHelper = ItemTouchHelper(ReboundingSwipeActionCallback())
            itemTouchHelper.attachToRecyclerView(this)
            adapter = podcastAdapter
        }
        viewModel.podcasts.observe(requireActivity()) {
            podcastAdapter.submitList(it)
        }
        viewModel.networkState.observe(requireActivity()) {
            podcastAdapter.setNetworkState(it)
        }
    }

    override fun render(viewState: HomeViewState) {
        homeProgress.visibility = View.INVISIBLE
        bestPodcastsList.visibility = View.INVISIBLE
        when (viewState) {
            is HomeReady -> bestPodcastsList.visibility = View.VISIBLE
            is Loading -> homeProgress.visibility = View.VISIBLE
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

inline fun <T : View> T.afterMeasure(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}
