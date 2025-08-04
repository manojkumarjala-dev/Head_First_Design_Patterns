package Chapter6_v2;

public class FanOnCommand implements Command{
    Fan fan;
    FanOnCommand(Fan fan){
        this.fan = fan;
    }
    public void execute(){
        this.fan.turnOn();
    }
}
