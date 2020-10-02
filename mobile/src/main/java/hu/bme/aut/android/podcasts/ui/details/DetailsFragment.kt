package hu.bme.aut.android.podcasts.ui.details

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.navigation.fragment.navArgs
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.transition.MaterialContainerTransform
import hu.bme.aut.android.podcasts.MainActivity
import hu.bme.aut.android.podcasts.R
import hu.bme.aut.android.podcasts.ui.details.DetailsPresenter.Podcast
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : RainbowCakeFragment<DetailsViewState, DetailsViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_details

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.navHostFragment
            duration = 500
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(Color.WHITE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsFragmentRoot.transitionName = args.podcastId + "root"

        // TODO Setup views
    }

    override fun onStart() {
        super.onStart()

        viewModel.load(args.podcastId)
    }

    override fun render(viewState: DetailsViewState) {
        when (viewState) {
            is DetailsReady -> {
                fillData(viewState.podcast)
            }
        }
    }

    private fun fillData(podcast: Podcast) {
        starButton.setImageDrawable(PodcastStarActionDrawable(requireContext()))
        starButton.setOnClickListener {
            it.isActivated = !it.isActivated
            podcast.starred = it.isActivated
            viewModel.updateStarred(
                (activity as MainActivity).auth.currentUser,
                podcast.id,
                it.isActivated
            )
        }
        starButton.isActivated = podcast.starred
        titleText.text = podcast.title
        explicitImage.visibility = if (podcast.explicitContent) View.VISIBLE else View.GONE
        publisherText.text = podcast.publisher
        miscText.text =
            "${podcast.country}, ${podcast.totalEpisodes} episodes, ${podcast.type} type"
        categoriesText.text = podcast.genres
        descriptionText.text = Html.fromHtml(podcast.description)

        Glide.with(thumbnailImage)
            .load(podcast.thumbnail)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(thumbnailImage)

        val chromeIntent = CustomTabsIntent.Builder()
            .setToolbarColor(resources.getColor(R.color.colorPrimary, null))
            .build()

        podcastWebsiteButton.setOnClickListener {
            chromeIntent.launchUrl(requireContext(), Uri.parse(podcast.website))
        }

        listenNotesWebsiteButton.setOnClickListener {
            chromeIntent.launchUrl(requireContext(), Uri.parse(podcast.listenNotesUrl))
        }
    }

}
