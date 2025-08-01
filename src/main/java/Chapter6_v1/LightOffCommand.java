package Chapter6_v1;

public class LightOffCommand implements Command{
    private final Light light;
    LightOffCommand(Light light){
        this.light = light;
    }
    @Override
    public void execute(){
        this.light.turnOff();
    }
}
