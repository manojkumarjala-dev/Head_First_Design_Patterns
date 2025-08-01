package Chapter6_v1;

public class Light {
    private final String onMessage;
    private final String offMessage;

    public Light(String onMessage, String offMessage) {
        this.onMessage = onMessage;
        this.offMessage = offMessage;
    }

    public void turnOn() {
        System.out.println(onMessage);
    }

    public void turnOff() {
        System.out.println(offMessage);
    }
}
