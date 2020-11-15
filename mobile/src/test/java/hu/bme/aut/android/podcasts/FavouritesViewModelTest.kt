package hu.bme.aut.android.podcasts

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import hu.bme.aut.android.podcasts.domain.model.Podcast
import hu.bme.aut.android.podcasts.ui.favourites.FavouritesPresenter
import hu.bme.aut.android.podcasts.ui.favourites.FavouritesReady
import hu.bme.aut.android.podcasts.ui.favourites.FavouritesViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class FavouritesViewModelTest : ViewModelTest() {

    companion object {
        private val MOCK_STARRED_PODCASTS = listOf(
            Podcast(
                explicitContent = false,
                genres = "Podcast, Show",
                id = "1",
                publisher = "Cool publishing",
                thumbnail = "",
                title = "Nice title",
                starred = true
            ),
            Podcast(
                true,
                "Podcast, Not a Show",
                id = "2",
                publisher = "Superb publishing",
                thumbnail = "",
                title = "Good title",
                starred = true
            )
        )
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `Podcasts are loaded correctly from presenter`() = runBlockingTest {
        val favouritesPresenter: FavouritesPresenter = mock()
        whenever(favouritesPresenter.getFavouritePodcasts()).doReturn(MOCK_STARRED_PODCASTS)

        val vm = FavouritesViewModel(favouritesPresenter)

        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(FavouritesReady(MOCK_STARRED_PODCASTS))
        }
    }
}