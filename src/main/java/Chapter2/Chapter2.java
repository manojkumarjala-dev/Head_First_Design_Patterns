package Chapter2;

public class Chapter2 {
    public static void main(String[] args) {
        WeatherData weatherData = new WeatherData();
        Observer currentConditionsDisplay = new CurrentStats(weatherData);

        weatherData.registerObserver(currentConditionsDisplay);

        // Simulate new weather data
        weatherData.setMeasurements(30.0f, 65.0f, 1013.0f);

    }
}
