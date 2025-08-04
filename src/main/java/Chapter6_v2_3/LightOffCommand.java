package Chapter6_v2_3;

public class LightOffCommand implements Command{
    Light light;
    LightOffCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute(){
        this.light.turnOff();
    }

    @Override
    public void undo() {
        System.out.println("turning on light from off");
        this.light.turnOn();
    }
}
