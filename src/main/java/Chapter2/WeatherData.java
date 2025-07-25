package Chapter2;

import java.util.Set;

public class WeatherData implements Subject{
    Set<Observer> observers;
    float temperature;
    float humidity;
    float pressure;
    public WeatherData() {
        observers = new java.util.HashSet<>();
    }

    public void setMeasurements(float temperature, float humidity, float pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        notifyObservers();
    }

    @Override
    public void registerObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            try {
                observer.update(temperature, humidity, pressure);
            }
            catch (Exception e) {
                System.out.println("Error notifying observer: " + e.getMessage());
            }
        }
    }
}
