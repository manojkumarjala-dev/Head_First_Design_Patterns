package Chapter6_v2_3;

public class LightOnCommand implements Command{
    Light light;
    LightOnCommand(Light light){
        this.light = light;
    }

    @Override
    public void execute(){
        this.light.turnOn();
    }

    @Override
    public void undo() {
        System.out.println("turning off light from on");
        this.light.turnOff();
    }
}
