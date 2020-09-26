package hu.bme.aut.android.podcasts.ui.details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import co.zsmb.rainbowcake.base.RainbowCakeFragment
import co.zsmb.rainbowcake.dagger.getViewModelFromFactory
import com.google.android.material.transition.MaterialContainerTransform
import hu.bme.aut.android.podcasts.R
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : RainbowCakeFragment<DetailsViewState, DetailsViewModel>() {

    override fun provideViewModel() = getViewModelFromFactory()
    override fun getViewResource() = R.layout.fragment_details

    private val args: DetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = 300
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(Color.WHITE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detailsFragmentRoot.transitionName = args.podcastId

        // TODO Setup views
    }

    override fun onStart() {
        super.onStart()

        viewModel.load()
    }

    override fun render(viewState: DetailsViewState) {
        // TODO Render state
    }

}
