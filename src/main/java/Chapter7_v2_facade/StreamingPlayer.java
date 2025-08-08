package Chapter7_v2_facade;

public class StreamingPlayer {
    public void on() { System.out.println("Player on"); }
    public void off() { System.out.println("Player off"); }
    public void play(String movie) { System.out.println("Playing \"" + movie + "\""); }
    public void stop() { System.out.println("Player stopped"); }
}
