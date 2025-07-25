package Chapter2;

public interface Observer {
    void update(float temperature, float humidity, float pressure);
    void unsubscribe(Subject subject);
}
