package hu.bme.aut.android.podcasts

import co.zsmb.rainbowcake.test.assertObserved
import co.zsmb.rainbowcake.test.base.ViewModelTest
import co.zsmb.rainbowcake.test.observeStateAndEvents
import com.google.firebase.auth.FirebaseUser
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import hu.bme.aut.android.podcasts.shared.domain.model.Language
import hu.bme.aut.android.podcasts.shared.domain.model.Region
import hu.bme.aut.android.podcasts.shared.domain.model.UserData
import hu.bme.aut.android.podcasts.ui.menu.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

@ExperimentalCoroutinesApi
class MenuViewModelTest : ViewModelTest() {
    companion object {
        private val MOCK_REGION = Region("hu", "Hungary")
        private val MOCK_LANGUAGE = Language("hungarian")
        private val MOCK_USER_DATA =
            UserData(
                displayName = "name",
                explicitContent = true,
                region = MOCK_REGION,
                language = MOCK_LANGUAGE
            )
    }

    @Test
    @ExperimentalCoroutinesApi
    fun `User data is loaded correctly from presenter`() = runBlockingTest {
        val menuPresenter: MenuPresenter = mock()
        val vm = MenuViewModel(menuPresenter)
        whenever(menuPresenter.getUserData("", "Podcasts", vm)).doReturn(MOCK_USER_DATA)
        whenever(menuPresenter.getUserData("123456", "Podcasts", vm)).doReturn(MOCK_USER_DATA)
        whenever((menuPresenter.getAvailableRegions())).doReturn(listOf(MOCK_REGION))
        whenever(menuPresenter.getAvailableLanguages()).doReturn(listOf(MOCK_LANGUAGE))

        val user: FirebaseUser = mock()
        whenever(user.uid).doReturn("123456")



        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(Loading)
        }
        vm.load(user)
        verify(menuPresenter).getUserData("123456", "Podcasts", vm)
        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                LoggedIn(
                    MOCK_USER_DATA,
                    listOf(MOCK_REGION),
                    listOf(MOCK_LANGUAGE)
                )
            )
        }
        vm.load(null)
        verify(menuPresenter).getUserData("", "Podcasts", vm)
        vm.observeStateAndEvents { stateObserver, _ ->
            stateObserver.assertObserved(
                LoggedOut(
                    MOCK_USER_DATA,
                    listOf(MOCK_REGION),
                    listOf(MOCK_LANGUAGE)
                )
            )
        }
    }
}