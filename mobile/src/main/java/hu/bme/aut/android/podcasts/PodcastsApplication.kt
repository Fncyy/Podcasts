package hu.bme.aut.android.podcasts

import co.zsmb.rainbowcake.dagger.RainbowCakeApplication
import hu.bme.aut.android.podcasts.di.AppComponent
import hu.bme.aut.android.podcasts.di.ApplicationModule
import hu.bme.aut.android.podcasts.di.DaggerAppComponent

class PodcastsApplication : RainbowCakeApplication() {

    override lateinit var injector: AppComponent

    override fun setupInjector() {
        injector = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }
}