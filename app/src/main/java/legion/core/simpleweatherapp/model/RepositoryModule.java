package legion.core.simpleweatherapp.model;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {

    @Binds
    abstract Repository cityManager(AppRepositoryManager cityManager);
}
