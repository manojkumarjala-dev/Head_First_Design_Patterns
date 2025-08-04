package Chapter6_v2;

public class LightOffCommand implements Command{
    Light light;
    LightOffCommand(Light light){
        this.light = light;
    }
    public void execute(){
        this.light.turnOff();
    }
}
