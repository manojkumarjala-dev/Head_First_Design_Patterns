package Chapter6_v2;

public class FanOffCommand implements Command{
    Fan fan;
    FanOffCommand(Fan fan){
        this.fan = fan;
    }
    public void execute(){
        this.fan.turnOff();
    }
}
