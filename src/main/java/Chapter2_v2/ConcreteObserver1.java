package Chapter2_v2;
// Observer contains reference to Observable because it needs to get updates from it like pull model. If we don't want
// to have reference to Observable in Observer, we can use push model where Observable pushes updates to Observer.
public class ConcreteObserver1 implements IObserver{
    ConcreteObservable observable;

    private float temperature;
    private float humidity;

    public ConcreteObserver1(ConcreteObservable observable) {
        this.observable = observable;
        this.observable.addObserver(this);
        this.temperature = this.observable.getTemperature();
        this.humidity = this.observable.getHumidity();
    }

    @Override
    public void unsubscribe() {
        this.observable.removeObserver(this);
        this.observable = null; // Clear reference to avoid memory leaks
        System.out.println("Observer 1 unsubscribed.");
    }

    @Override
    public void update() {
        this.temperature = observable.getTemperature();
        this.humidity = observable.getHumidity();
        System.out.println("Observer 1 updated: Temperature = " + temperature + ", Humidity = " + humidity);
    }
}
