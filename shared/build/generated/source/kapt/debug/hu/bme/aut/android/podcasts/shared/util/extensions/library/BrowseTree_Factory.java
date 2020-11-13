package hu.bme.aut.android.podcasts.shared.util.extensions.library;

import dagger.internal.Factory;
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
public final class BrowseTree_Factory implements Factory<BrowseTree> {
  private final Provider<PodcastSource> podcastSourceProvider;

  public BrowseTree_Factory(Provider<PodcastSource> podcastSourceProvider) {
    this.podcastSourceProvider = podcastSourceProvider;
  }

  @Override
  public BrowseTree get() {
    return newInstance(podcastSourceProvider.get());
  }

  public static BrowseTree_Factory create(Provider<PodcastSource> podcastSourceProvider) {
    return new BrowseTree_Factory(podcastSourceProvider);
  }

  public static BrowseTree newInstance(PodcastSource podcastSource) {
    return new BrowseTree(podcastSource);
  }
}
