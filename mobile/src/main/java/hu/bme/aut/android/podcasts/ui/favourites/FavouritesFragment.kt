package hu.bme.aut.android.podcasts.ui.favourites

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.animation.ReboundingSwipeActionCallback
import kotlinx.android.synthetic.main.fragment_favourites.*
import javax.inject.Inject

@AndroidEntryPoint
class FavouritesFragment : RainbowCakeFragment<FavouritesViewState, FavouritesViewModel>(),
    PodcastAdapter.PodcastUpdateListener {

    @Inject
    lateinit var injectedViewModel: FavouritesViewModel
    override fun provideViewModel() = injectedViewModel
    override fun getViewResource() = R.layout.fragment_favourites

    @Inject
    lateinit var favouriteDecoder: FavouriteDecoder

    private lateinit var podcastAdapter: PodcastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        podcastAdapter = PodcastAdapter(requireContext(), favouriteDecoder)
        podcastAdapter.podcastUpdateListener = this
        favouritesPodcastsList.apply {
            val itemTouchHelper = ItemTouchHelper(ReboundingSwipeActionCallback())
            itemTouchHelper.attachToRecyclerView(this)
            adapter = podcastAdapter
        }
    }

    override fun render(viewState: FavouritesViewState) {
        favouritesPodcastsList.visibility = View.GONE
        favouritesErrorText.visibility = View.GONE
        favouritesProgress.visibility = View.GONE
        when (viewState) {
            is Loading -> favouritesProgress.visibility = View.VISIBLE
            is FavouritesReady -> {
                favouritesPodcastsList.visibility = View.VISIBLE
                podcastAdapter.addElements(viewState.podcasts)
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
            FavouritesFragmentDirections.actionNavigationFavouritesToNavigationDetails(id),
            extras
        )
    }

    override fun onPodcastStarred(id: String, starred: Boolean) {
        viewModel.updateStarred((activity as MainActivity).auth.currentUser, id, starred)
    }

}
