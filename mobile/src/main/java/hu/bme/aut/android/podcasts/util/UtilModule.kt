package hu.bme.aut.android.podcasts.util

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilModule {

    @Provides
    @Singleton
    fun provideFirebaseDatabaseAccessor() = FirebaseDatabaseAccessor()
}