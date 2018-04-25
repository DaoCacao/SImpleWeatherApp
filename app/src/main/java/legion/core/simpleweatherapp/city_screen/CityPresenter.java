package legion.core.simpleweatherapp.city_screen;


import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import legion.core.simpleweatherapp.base.BasePresenter;
import legion.core.simpleweatherapp.model.Repository;

public class CityPresenter extends BasePresenter<CityMvp.View> implements CityMvp.Presenter, LocationListener {

    private enum State {
        OK,
        NO_PERMISSION,
        GPS_DISABLED,
        NO_CONNECTION
    }

    private final Repository repository;
    private int currentCityId;

    @Inject
    CityPresenter(CityMvp.View view, Repository repository) {
        super(view);
        this.repository = repository;
    }

    @Override
    public void onViewResume() {
        tryToLoadCity();
    }

    @Override
    public void onViewPause() {
        repository.removeLocationListener(this);
    }

    @Override
    public void onAllowPermissionClick() {
        getView().requestLocationPermission();
    }

    @Override
    public void onEnableLocationClick() {
        getView().navigateToSettingsScreen();
    }

    @Override
    public void onTryAgainClick() {
        tryToLoadCity();
    }

    @Override
    public void onChangeCityClick() {
        getView().showChangeCityDialog();
    }

    @Override
    public void onPermissionAllow() {
        tryToLoadCity();
    }

    @Override
    public void onPermissionDenied() {
        getView().showNoPermissionState();
    }

    @Override
    public void onYourLocationClick() {
        currentCityId = 0;
        tryToLoadCity();
    }

    @Override
    public void onCityClick(int cityId) {
        repository.removeLocationListener(this);
        currentCityId = cityId;
        tryToLoadCity();
    }


    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        loadCity(lat, lon);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private State checkState() {
        if (!getView().hasLocationPermission()) return State.NO_PERMISSION;
        if (!getView().isLocationAvailable()) return State.GPS_DISABLED;
        if (!getView().isNetworkAvailable()) return State.NO_CONNECTION;
        return State.OK;
    }

    private void tryToLoadCity() {
        getView().showLoadingState();

        switch (checkState()) {
            case OK:
                getCityData();
                break;
            case NO_PERMISSION:
                getView().requestLocationPermission();
                break;
            case GPS_DISABLED:
                getView().showNeedLocationState();
                break;
            case NO_CONNECTION:
                getView().showErrorState();
                break;
        }
    }
    private void getCityData() {
        if (currentCityId == 0) {
            repository.requestLocation(this);
        } else {
            loadCity(currentCityId);
        }
    }
    private void loadCity(int cityId) {
        repository.getCity(cityId)
                .doOnSuccess(cityItem -> {
                    currentCityId = cityItem.getId();
                    getView().showCity(cityItem);
                })
                .doOnError(throwable -> getView().showErrorState())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
    private void loadCity(double lat, double lon) {
        repository.getCity(lat, lon)
                .doOnSuccess(cityItem -> {
                    currentCityId = cityItem.getId();
                    getView().showCity(cityItem);
                })
                .doOnError(throwable -> getView().showErrorState())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
