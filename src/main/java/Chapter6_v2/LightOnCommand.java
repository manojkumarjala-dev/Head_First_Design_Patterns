package Chapter6_v2;

public class LightOnCommand implements Command{
    Light light;
    LightOnCommand(Light light){
        this.light = light;
    }
    public void execute(){
        this.light.turnOn();
    }
}
