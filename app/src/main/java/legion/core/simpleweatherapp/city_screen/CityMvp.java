package legion.core.simpleweatherapp.city_screen;

import legion.core.simpleweatherapp.base.BaseMvp;
import legion.core.simpleweatherapp.pojo.CityItem;

public interface CityMvp {

    interface View extends BaseMvp.View {
        void navigateToSettingsScreen();

        void showLoadingState();
        void showCity(CityItem cityItem);
        void showNoPermissionState();
        void showNeedLocationState();
        void showErrorState();

        boolean hasLocationPermission();
        void requestLocationPermission();

        boolean isNetworkAvailable();
        boolean isLocationAvailable();

        void showChangeCityDialog();
    }

    interface Presenter extends BaseMvp.Presenter {
        void onAllowPermissionClick();
        void onEnableLocationClick();
        void onTryAgainClick();
        void onChangeCityClick();

        void onPermissionAllow();
        void onPermissionDenied();

        void onYourLocationClick();
        void onCityClick(int cityId);
    }
}
