package hu.bme.aut.android.podcasts

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import hu.bme.aut.android.podcasts.ui.details.DetailsPresenter
import hu.bme.aut.android.podcasts.ui.details.DetailsPresenter.Podcast
import hu.bme.aut.android.podcasts.ui.details.DetailsReady
import hu.bme.aut.android.podcasts.ui.details.DetailsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest : ViewModelTest() {

    companion object {
        private val MOCK_PODCAST = Podcast(
            country = "country",
            description = "desc",
            explicitContent = true,
            genres = "genre",
            id = "1",
            listenNotesUrl = "",
            publisher = "publisher",
            thumbnail = "",
            title = "title",
            totalEpisodes = 2,
            type = "podcast",
            website = "",
            starred = true
        )
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Podcast is loaded correctly from presenter`() = runBlockingTest {
        val detailsPresenter: DetailsPresenter = mock()
        whenever(detailsPresenter.getPodcast("1")).doReturn(MOCK_PODCAST)

        val vm = DetailsViewModel(detailsPresenter)

        vm.load("1")

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(DetailsReady(MOCK_PODCAST))
        }
    }
}