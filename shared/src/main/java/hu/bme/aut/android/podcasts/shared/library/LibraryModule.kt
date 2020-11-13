package hu.bme.aut.android.podcasts.shared.library

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.bme.aut.android.podcasts.shared.data.disk.SharedPreferencesProvider
import hu.bme.aut.android.podcasts.shared.data.network.ListenNotesAPI
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class LibraryModule {

    @Provides
    @Singleton
    fun providePodcastSource(
        @ApplicationContext context: Context,
        sharedPreferencesProvider: SharedPreferencesProvider,
        listenNotesApi: ListenNotesAPI
    ): PodcastSource = NetworkSource(context, sharedPreferencesProvider, listenNotesApi)
}