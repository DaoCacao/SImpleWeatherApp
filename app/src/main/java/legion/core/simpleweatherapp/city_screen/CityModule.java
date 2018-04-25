package legion.core.simpleweatherapp.city_screen;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class CityModule {

    @Binds
    abstract CityMvp.View view(CityActivity activity);

    @Binds
    abstract CityMvp.Presenter presenter(CityPresenter presenter);

}
