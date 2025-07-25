package Chapter2_v2;

public class Chapter2_v2 {
    public static void main(String[] args) {
        ConcreteObservable observable = new ConcreteObservable();
        ConcreteObserver1 observer1 = new ConcreteObserver1(observable);
        ConcreteObserver2 observer2 = new ConcreteObserver2(observable);

        observable.setTemperature(25.0f);
        observable.setPressure(1013.0f);
        observable.setHumidity(60.0f);

        observer1.unsubscribe();
        observable.setTemperature(30.0f);
        observable.setPressure(1020.0f);
        observable.setHumidity(62.0f);

    }
}
