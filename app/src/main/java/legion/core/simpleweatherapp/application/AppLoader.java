package legion.core.simpleweatherapp.application;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class AppLoader extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}
