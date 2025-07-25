package Chapter2_v2;

public class ConcreteObserver2 implements IObserver{
    private final ConcreteObservable observable;
    private float temperature;
    private float pressure;

    public ConcreteObserver2(ConcreteObservable observable) {
        this.observable = observable;
        observable.addObserver(this);
        this.temperature = observable.getTemperature();
        this.pressure = observable.getPressure();
    }

    @Override
    public void update() {
        this.temperature = observable.getTemperature();
        this.pressure = observable.getPressure();
        System.out.println("Observer 2 updated: Temperature = " + temperature + ", Pressure = " + pressure);
    }

    @Override
    public void unsubscribe() {
        observable.removeObserver(this);
    }
}
