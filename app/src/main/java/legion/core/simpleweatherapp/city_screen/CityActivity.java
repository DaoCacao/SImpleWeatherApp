package legion.core.simpleweatherapp.city_screen;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import legion.core.simpleweatherapp.R;
import legion.core.simpleweatherapp.base.BaseActivity;
import legion.core.simpleweatherapp.pojo.CityItem;

public class CityActivity extends BaseActivity implements CityMvp.View {

    public static final int LOCATION_REQUEST_CODE = 1;

    @BindView(R.id.info_layout) ConstraintLayout infoLayout;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_current_temp) TextView tvCurrentTemp;
    @BindView(R.id.tv_morning_temp) TextView tvMorningTemp;
    @BindView(R.id.tv_day_temp) TextView tvDayTemp;
    @BindView(R.id.tv_evening_temp) TextView tvEveningTemp;
    @BindView(R.id.tv_allow_permission) TextView tvAllowPermission;
    @BindView(R.id.tv_enable_location) TextView tvEnableLocation;
    @BindView(R.id.tv_try_again) TextView tvTryAgainNetwork;

    @BindView(R.id.pb_loading) ProgressBar pbLoading;

    @Inject CityMvp.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tvAllowPermission.setOnClickListener(view -> presenter.onAllowPermissionClick());
        tvEnableLocation.setOnClickListener(view -> presenter.onEnableLocationClick());
        tvTryAgainNetwork.setOnClickListener(view -> presenter.onTryAgainClick());
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onViewPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.city_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_change_city) presenter.onChangeCityClick();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            presenter.onPermissionAllow();
        } else {
            presenter.onPermissionDenied();
        }
        //TODO --> check if deny forever
    }

    @Override
    public void navigateToSettingsScreen() {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    @Override
    public void showLoadingState() {
        pbLoading.setVisibility(View.VISIBLE);
        infoLayout.setVisibility(View.GONE);
        tvAllowPermission.setVisibility(View.GONE);
        tvEnableLocation.setVisibility(View.GONE);
        tvTryAgainNetwork.setVisibility(View.GONE);
    }

    @Override
    public void showCity(CityItem cityItem) {
        pbLoading.setVisibility(View.GONE);
        infoLayout.setVisibility(View.VISIBLE);
        tvAllowPermission.setVisibility(View.GONE);
        tvEnableLocation.setVisibility(View.GONE);
        tvTryAgainNetwork.setVisibility(View.GONE);

        tvName.setText(cityItem.getName());
        tvCurrentTemp.setText(String.format(Locale.getDefault(), "%d C°", cityItem.getCurrentTemp()));
        tvMorningTemp.setText(String.format(Locale.getDefault(), "%d C°", cityItem.getMorningTemp()));
        tvDayTemp.setText(String.format(Locale.getDefault(), "%d C°", cityItem.getDayTemp()));
        tvEveningTemp.setText(String.format(Locale.getDefault(), "%d C°", cityItem.getEveningTemp()));
    }

    @Override
    public void showNoPermissionState() {
        pbLoading.setVisibility(View.GONE);
        infoLayout.setVisibility(View.GONE);
        tvAllowPermission.setVisibility(View.VISIBLE);
        tvEnableLocation.setVisibility(View.GONE);
        tvTryAgainNetwork.setVisibility(View.GONE);
    }

    @Override
    public void showNeedLocationState() {
        tvAllowPermission.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        infoLayout.setVisibility(View.GONE);
        tvEnableLocation.setVisibility(View.VISIBLE);
        tvTryAgainNetwork.setVisibility(View.GONE);
    }

    @Override
    public void showErrorState() {
        tvAllowPermission.setVisibility(View.GONE);
        pbLoading.setVisibility(View.GONE);
        infoLayout.setVisibility(View.GONE);
        tvEnableLocation.setVisibility(View.GONE);
        tvTryAgainNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    @Override
    public boolean isLocationAvailable() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gpsEnabled = false;
        boolean networkEnabled = false;

        try {
            gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return gpsEnabled || networkEnabled;
    }

    @Override
    public void showChangeCityDialog() {
        new AlertDialog.Builder(this)
                .setItems(new String[]{
                        "Your location",
                        "London",
                        "Paris",
                        "Tokyo",
                        "New York"}, (dialogInterface, i) -> {
                    switch (i) {
                        case 0:
                            presenter.onYourLocationClick();
                            break;
                        case 1:
                            presenter.onCityClick(getResources().getInteger(R.integer.london_id));
                            break;
                        case 2:
                            presenter.onCityClick(getResources().getInteger(R.integer.paris_id));
                            break;
                        case 3:
                            presenter.onCityClick(getResources().getInteger(R.integer.tokyo_id));
                            break;
                        case 4:
                            presenter.onCityClick(getResources().getInteger(R.integer.new_york_id));
                            break;
                    }
                })
                .show();
    }

    @Override
    public boolean hasLocationPermission() {
        return hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    @Override
    public void requestLocationPermission() {
        requestPermission(LOCATION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
    }
}
