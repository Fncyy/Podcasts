package hu.bme.aut.android.podcasts.ui.search

import android.os.Bundle
import android.view.View
import android.widget.SearchView.OnQueryTextListener
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.ItemTouchHelper
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.animation.ReboundingSwipeActionCallback
import hu.bme.aut.android.podcasts.util.paging.PodcastAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment :
    RainbowCakeFragment<SearchViewState, SearchViewModel>(),
    PodcastAdapter.PodcastUpdateListener {

    @Inject
    lateinit var injectedViewModel: SearchViewModel
    override fun provideViewModel() = injectedViewModel
    override fun getViewResource() = R.layout.fragment_search

    @Inject
    lateinit var favouriteDecoder: FavouriteDecoder
    private lateinit var podcastAdapter: PodcastAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        setupViews()

        setupRecyclerView()
    }

    private fun setupViews() {
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty())
                    viewModel.getPagedPodcasts(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun setupRecyclerView() {
        podcastAdapter = PodcastAdapter(requireContext(), favouriteDecoder) {
            viewModel.retry()
        }
        podcastAdapter.podcastUpdateListener = this
        searchPodcastsList.apply {
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

    override fun render(viewState: SearchViewState) {
        searchProgress.visibility = View.INVISIBLE
        searchPodcastsList.visibility = View.INVISIBLE
        when (viewState) {
            is SearchReady -> searchPodcastsList.visibility = View.VISIBLE
            is Loading -> searchProgress.visibility = View.VISIBLE
        }
    }

    override fun onPodcastSelected(id: String, extras: FragmentNavigator.Extras) {}

    override fun onPodcastStarred(id: String, starred: Boolean) {
        viewModel.updateStarred((activity as MainActivity).auth.currentUser, id, starred)
    }
}
