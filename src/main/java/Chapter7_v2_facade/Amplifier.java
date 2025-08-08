package Chapter7_v2_facade;

public class Amplifier {
    public void on() { System.out.println("Amp on"); }
    public void off() { System.out.println("Amp off"); }
    public void setStreamingPlayer(StreamingPlayer p) { System.out.println("Amp set player"); }
    public void setSurroundSound() { System.out.println("Amp surround sound"); }
    public void setVolume(int v) { System.out.println("Amp volume " + v); }
}