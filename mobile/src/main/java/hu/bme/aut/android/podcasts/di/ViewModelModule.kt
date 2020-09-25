package hu.bme.aut.android.podcasts.di

import androidx.lifecycle.ViewModel
import co.zsmb.rainbowcake.dagger.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import hu.bme.aut.android.podcasts.ui.favourites.FavouritesViewModel
import hu.bme.aut.android.podcasts.ui.home.HomeViewModel
import hu.bme.aut.android.podcasts.ui.search.SearchViewModel

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun bindSearchViewModel(searchViewModel: SearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavouritesViewModel::class)
    abstract fun bindFavouritesViewModel(favouritesViewModel: FavouritesViewModel): ViewModel
}