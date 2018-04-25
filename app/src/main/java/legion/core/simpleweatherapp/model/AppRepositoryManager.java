package legion.core.simpleweatherapp.model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import io.reactivex.Single;
import legion.core.simpleweatherapp.R;
import legion.core.simpleweatherapp.pojo.CityItem;

import static android.content.Context.LOCATION_SERVICE;


public class AppRepositoryManager implements Repository {

    private Context context;
    private LocationManager locationManager;
    private String apiKey;

    @Inject
    public AppRepositoryManager(Context context, LocationManager locationManager) {
        this.context = context;
        this.locationManager = locationManager;
        apiKey = context.getString(R.string.open_weather_app_api_key);
    }

    @Override
    public Single<CityItem> getCity(int id) {
        return Single.create(emitter -> {
            String url = String.format("http://api.openweathermap.org/data/2.5/forecast?id=%s&units=metric&appid=%s", id, apiKey);
//            String url = String.format("http://api.openweathermap.org/data/2.5/forecast/daily?id=%s&units=metric&appid=%s", id, apiKey);
            System.out.println(url);

            Volley.newRequestQueue(context).add(new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> emitter.onSuccess(parseSingleJson(response)),
                    emitter::onError));
        });
    }

    @Override
    public Single<CityItem> getCity(double lat, double lon) {
        return Single.create(emitter -> {
            String url = String.format("http://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&units=metric&APPID=%s", lat, lon, apiKey);
//            String url = String.format("http://api.openweathermap.org/data/2.5/forecast/daily?lat=%s&lon=%s&units=metric&APPID=%s", lat, lon, apiKey);
            System.out.println(url);

            Volley.newRequestQueue(context).add(new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    response -> emitter.onSuccess(parseSingleJson(response)),
                    emitter::onError));
        });
    }

    @Override
    public void removeLocationListener(LocationListener locationListener) {
        locationManager.removeUpdates(locationListener);
    }

    @Override
    @SuppressLint("MissingPermission")
    public void requestLocation(LocationListener locationListener) {
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            Location location = locationManager.getLastKnownLocation(provider);
            if (location == null) {
                locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
            } else {
                locationListener.onLocationChanged(location);
                break;
            }
        }
    }

    private CityItem parseSingleJson(JSONObject response) {
        try {
            String id = response.getJSONObject("city").getString("id");
            String name = response.getJSONObject("city").getString("name");
            String currentTemp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getString("temp");

            return new CityItem(Integer.parseInt(id), name, (int) Double.parseDouble(currentTemp), 0, 0, 0);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private CityItem parseDailyJson(JSONObject response) {
        try {
            String id = response.getJSONArray("list").getJSONObject(0).getString("id");
            String name = response.getJSONArray("list").getJSONObject(0).getString("name");
            String currentTemp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getJSONObject("temp").getString("day");
            String morningTemp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getJSONObject("temp").getString("morn");
            String dayTemp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getJSONObject("temp").getString("day");
            String eveningTemp = response.getJSONArray("list").getJSONObject(0).getJSONObject("main").getJSONObject("temp").getString("eve");

            return new CityItem(
                    Integer.parseInt(id),
                    name,
                    (int) Double.parseDouble(currentTemp),
                    (int) Double.parseDouble(morningTemp),
                    (int) Double.parseDouble(dayTemp),
                    (int) Double.parseDouble(eveningTemp));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
