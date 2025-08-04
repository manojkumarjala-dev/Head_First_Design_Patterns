package Chapter6_v2;

public class Fan {
    public String onMessage;
    public String offMessage;
    private boolean status;

    Fan(String onMessage, String offMessage, boolean status){
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
