package Chapter7_v2_facade;

public class TheaterLights {
    public void on() { System.out.println("Lights on"); }
    public void dim(int level) { System.out.println("Lights dim to " + level + "%"); }
}
