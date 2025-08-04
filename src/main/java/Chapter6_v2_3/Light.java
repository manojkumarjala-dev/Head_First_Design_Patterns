package Chapter6_v2_3;

public class Light {
    public String onMessage;
    public String offMessage;
    private boolean status;

    Light(String onMessage, String offMessage, boolean status){
        this.offMessage = offMessage;
        this.onMessage = onMessage;
        this.status = status;
    }

    void turnOn(){
        System.out.println(onMessage);
        this.status = true;
    }
    void turnOff(){
        System.out.println(offMessage);
        this.status = false;
    }

    boolean getter(){
        return this.status;
    }

}
