package hu.bme.aut.android.podcasts.di

import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import dagger.Component
import hu.bme.aut.android.podcasts.data.disk.DiskModule
import hu.bme.aut.android.podcasts.data.network.NetworkModule
import hu.bme.aut.android.podcasts.ui.home.PodcastsAdapter
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        DiskModule::class,
        NetworkModule::class,
        RainbowCakeModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : RainbowCakeComponent {
    fun inject(podcastsAdapter: PodcastsAdapter)
}