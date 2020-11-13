package hu.bme.aut.android.podcasts.shared.util.extensions.library;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import hu.bme.aut.android.podcasts.shared.data.disk.SharedPreferencesProvider;
import hu.bme.aut.android.podcasts.shared.data.network.ListenNotesAPI;
import javax.annotation.Generated;
import javax.inject.Provider;

@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes"
})
public final class LibraryModule_ProvidePodcastSourceFactory implements Factory<PodcastSource> {
  private final LibraryModule module;

  private final Provider<Context> contextProvider;

  private final Provider<SharedPreferencesProvider> sharedPreferencesProvider;

  private final Provider<ListenNotesAPI> listenNotesApiProvider;

  public LibraryModule_ProvidePodcastSourceFactory(LibraryModule module,
      Provider<Context> contextProvider,
      Provider<SharedPreferencesProvider> sharedPreferencesProvider,
      Provider<ListenNotesAPI> listenNotesApiProvider) {
    this.module = module;
    this.contextProvider = contextProvider;
    this.sharedPreferencesProvider = sharedPreferencesProvider;
    this.listenNotesApiProvider = listenNotesApiProvider;
  }

  @Override
  public PodcastSource get() {
    return providePodcastSource(module, contextProvider.get(), sharedPreferencesProvider.get(), listenNotesApiProvider.get());
  }

  public static LibraryModule_ProvidePodcastSourceFactory create(LibraryModule module,
      Provider<Context> contextProvider,
      Provider<SharedPreferencesProvider> sharedPreferencesProvider,
      Provider<ListenNotesAPI> listenNotesApiProvider) {
    return new LibraryModule_ProvidePodcastSourceFactory(module, contextProvider, sharedPreferencesProvider, listenNotesApiProvider);
  }

  public static PodcastSource providePodcastSource(LibraryModule instance, Context context,
      SharedPreferencesProvider sharedPreferencesProvider, ListenNotesAPI listenNotesApi) {
    return Preconditions.checkNotNull(instance.providePodcastSource(context, sharedPreferencesProvider, listenNotesApi), "Cannot return null from a non-@Nullable @Provides method");
  }
}
