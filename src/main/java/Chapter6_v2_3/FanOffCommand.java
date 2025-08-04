package Chapter6_v2_3;

public class FanOffCommand implements Command{
    Fan fan;
    FanOffCommand(Fan fan){
        this.fan = fan;
    }
    @Override
    public void execute(){
        this.fan.turnOff();
    }

    @Override
    public void undo() {
        System.out.println("turning on fan from off");
        this.fan.turnOn();
    }
}
