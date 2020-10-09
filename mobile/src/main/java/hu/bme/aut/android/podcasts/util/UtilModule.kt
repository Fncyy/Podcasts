package hu.bme.aut.android.podcasts.util

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class UtilModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabaseAccessor() = FirebaseDatabaseAccessor()
}