package legion.core.simpleweatherapp.application;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import legion.core.simpleweatherapp.city_screen.CityActivity;
import legion.core.simpleweatherapp.city_screen.CityModule;

@Module
interface ActivityBindingModule {

    @ContributesAndroidInjector(modules = CityModule.class)
    CityActivity cityActivity();
}
