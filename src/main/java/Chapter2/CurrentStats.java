package Chapter2;

public class CurrentStats implements Display, Observer {
    private float temperature;
    private float humidity;
    private float pressure;
    private final Subject weatherData;

    public CurrentStats(Subject weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    @Override
    public void update(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        display();
    }

    @Override
    public void unsubscribe(Subject subject) {
        subject.removeObserver(this);
    }

    @Override
    public void display() {
        System.out.println("Current conditions: " + temperature + "°C and " + humidity + "% humidity"
                + " with pressure " + pressure + " hPa");
    }
}
