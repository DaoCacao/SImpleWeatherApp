package legion.core.simpleweatherapp.model;

import android.content.Context;
import android.location.LocationManager;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class RepositoryModule {

    @Binds
    abstract Repository cityManager(AppRepositoryManager cityManager);

    @Provides
    static LocationManager locationManager(Context context) {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
