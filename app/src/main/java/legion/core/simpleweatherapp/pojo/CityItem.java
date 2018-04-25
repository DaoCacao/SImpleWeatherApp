package legion.core.simpleweatherapp.pojo;

public class CityItem {
    private String name;

    private int id;
    private int currentTemp;
    private int morningTemp;
    private int dayTemp;
    private int eveningTemp;

    public CityItem(int id, String name, int currentTemp, int morningTemp, int dayTemp, int eveningTemp) {
        this.id = id;
        this.name = name;
        this.currentTemp = currentTemp;
        this.morningTemp = morningTemp;
        this.dayTemp = dayTemp;
        this.eveningTemp = eveningTemp;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentTemp() {
        return currentTemp;
    }

    public int getMorningTemp() {
        return morningTemp;
    }

    public int getDayTemp() {
        return dayTemp;
    }

    public int getEveningTemp() {
        return eveningTemp;
    }


}
