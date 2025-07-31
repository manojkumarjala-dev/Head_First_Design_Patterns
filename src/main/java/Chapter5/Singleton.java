package Chapter5;
public final class Singleton {

    private static volatile Singleton instance;   // private + volatile

    private Singleton() {                         // private
    }

    public static Singleton getInstance() {
        if (instance == null) {                   // first check (no lock)
            synchronized (Singleton.class) {      // lock on the class object
                if (instance == null) {           // second check (with lock)
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
