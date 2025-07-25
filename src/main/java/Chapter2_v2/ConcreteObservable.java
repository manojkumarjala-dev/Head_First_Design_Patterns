package Chapter2_v2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConcreteObservable implements IObservable {
    private final List<IObserver> observers = new ArrayList<>();

    private Float temperature;
    private Float humidity;
    private Float pressure;

    public ConcreteObservable() {
        this.temperature = 0.0f;
        this.humidity = 0.0f;
        this.pressure = 0.0f;
    }

    public Float getTemperature() {
        return temperature;
    }

    public Float getHumidity() {
        return humidity;
    }

    public Float getPressure() {
        return pressure;
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        int idx  = observers.indexOf(observer);
        if (idx != -1) {
            observers.remove(idx);
        } else {
            System.out.println("Observer not found in the list.");
        }

    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            observer.update();
        }
    }

    public void setTemperature(float v) {
        this.temperature = v;
        notifyObservers();
    }

    public void setPressure(float v) {
        this.pressure = v;
        notifyObservers();
    }

    public void setHumidity(float v) {
        this.humidity = v;
        notifyObservers();
    }
}
