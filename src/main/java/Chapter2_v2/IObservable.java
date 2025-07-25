package Chapter2_v2;

public interface IObservable {
    public void addObserver(IObserver observer);
    public void removeObserver(IObserver observer);
    public void notifyObservers();
}
