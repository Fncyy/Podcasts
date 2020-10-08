package hu.bme.aut.android.podcasts.di

import co.zsmb.rainbowcake.dagger.RainbowCakeComponent
import co.zsmb.rainbowcake.dagger.RainbowCakeModule
import dagger.Component
import hu.bme.aut.android.podcasts.data.disk.DiskModule
import hu.bme.aut.android.podcasts.data.network.NetworkModule
import hu.bme.aut.android.podcasts.util.UtilModule
import hu.bme.aut.android.podcasts.util.paging.BestPodcastDataSource
import hu.bme.aut.android.podcasts.util.paging.PodcastAdapter
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        DiskModule::class,
        NetworkModule::class,
        RainbowCakeModule::class,
        UtilModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : RainbowCakeComponent {
    fun inject(podcastAdapter: PodcastAdapter)
    fun inject(bestPodcastDataSource: BestPodcastDataSource)
}