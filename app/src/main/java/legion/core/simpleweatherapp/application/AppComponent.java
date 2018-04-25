package legion.core.simpleweatherapp.application;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;
import legion.core.simpleweatherapp.model.RepositoryModule;

@Singleton
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        ActivityBindingModule.class,
        RepositoryModule.class})
interface AppComponent extends AndroidInjector<DaggerApplication> {
}
