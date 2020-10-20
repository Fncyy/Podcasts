package hu.bme.aut.android.podcasts.ui.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.view.children
import androidx.core.view.doOnPreDraw
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.ItemTouchHelper
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.util.FavouriteDecoder
import hu.bme.aut.android.podcasts.util.animation.ReboundingSwipeActionCallback
import hu.bme.aut.android.podcasts.util.extensions.DATE
import hu.bme.aut.android.podcasts.util.extensions.RELEVANCE
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
    private val regionMap = mutableMapOf<String, String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        viewModel.loadUserData()
        setupViews()
        setupRecyclerView()
    }

    private fun setupViews() {
        sortByRadioGroup.check(dateButton.id)
        dateButton.text = DATE
        relevanceButton.text = RELEVANCE
        searchInput.doOnTextChanged { text, _, _, _ ->
            searchButton.isEnabled = !text.isNullOrBlank()
        }
        searchButton.setOnClickListener {
            val region = regionInput.text.toString().let {
                if (it.isBlank()) null
                else {
                    val regionKey = regionMap[it]
                    if (regionKey == null) {
                        regionInput.error = "Not supported region!"
                        return@setOnClickListener
                    } else Region(regionKey, it)

                }
            }

            val language = languageInput.text.toString().let {
                if (it.isBlank()) null
                else Language(it)
            }

            val sortBy = when (sortByRadioGroup.checkedRadioButtonId) {
                dateButton.id -> DATE
                relevanceButton.id -> RELEVANCE
                else -> null
            }

            viewModel.getPagedPodcasts(
                searchInput.text.toString(),
                language,
                region,
                sortBy
            )

            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(searchFragmentRoot.windowToken, 0)
            searchInput.clearFocus()
        }
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
        searchFragmentRoot.children.forEach {
            it.visibility = View.INVISIBLE
        }
        when (viewState) {
            is Loading -> searchProgress.visibility = View.VISIBLE
            is Initialized -> stateInitialized(viewState)
        }
    }

    private fun stateInitialized(viewState: Initialized) {
        searchFragmentRoot.children.forEach {
            if (it.id != searchProgress.id)
                it.visibility = View.VISIBLE
        }

        updateRegionsMap(viewState.availableRegions)

        languageInput.setText(viewState.userData.language?.name)
        languageInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableLanguages.map { it.name }
            )
        )

        regionInput.setText(viewState.userData.region?.name)
        regionInput.setAdapter(
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                viewState.availableRegions.map { it.name }
            )
        )
    }

    private fun updateRegionsMap(availableRegions: List<Region>) {
        regionMap.let {
            it.clear()
            availableRegions.forEach { region ->
                it[region.name] = region.key
            }
        }
    }

    override fun onPodcastSelected(id: String, extras: FragmentNavigator.Extras) {}

    override fun onPodcastStarred(id: String, starred: Boolean) {
        viewModel.updateStarred((activity as MainActivity).auth.currentUser, id, starred)
    }
}
