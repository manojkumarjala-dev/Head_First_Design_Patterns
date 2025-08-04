package Chapter6_v2_3;

public class FanOnCommand implements Command{
    Fan fan;
    FanOnCommand(Fan fan){
        this.fan = fan;
    }
    @Override
    public void execute(){
        this.fan.turnOn();
    }
    @Override
    public void undo(){
        System.out.println("turning off fan from on");
        this.fan.turnOff();
    }
}
