package legion.core.simpleweatherapp.model;

import io.reactivex.Single;
import legion.core.simpleweatherapp.pojo.CityItem;

public interface Repository {
    Single<CityItem> getCity(int id);
    Single<CityItem> getCity(double lat, double lon);
    void requestLocation(AppRepositoryManager.OnLocationChanged onLocationChanged);
}
